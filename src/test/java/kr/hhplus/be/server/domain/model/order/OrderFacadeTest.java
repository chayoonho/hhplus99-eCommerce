package kr.hhplus.be.server.domain.model.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // Mockito 어노테이션(@Mock, @InjectMocks)을 사용하기 위한 필수 설정
public class OrderFacadeTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks //  mockProductRepository와 mockOrderRepository를 자동으로 주입
    private OrderFacade orderFacade;

    private OrderItemRequest createOrderItemRequest(Long productId, int quantity) {
        return new OrderItemRequest(productId, quantity);
    }

    private Product createProduct(Long id, String name, BigDecimal price, int stock) {
        Product product = new Product(name, price, stock);
        product.setId(id); // ID 설정 (실제 DB에서 할당되는 것을 모방)
        return product;
    }

    @Test
    void 유효한_주문_요청_시_주문이_성공적으로_생성되어야_함() {
        // Given (테스트를 위한 준비 단계)
        Long userId = 1L;
        List<OrderItemRequest> itemRequests = Arrays.asList(
                createOrderItemRequest(1L, 2), // 상품 ID 1, 수량 2
                createOrderItemRequest(2L, 1)  // 상품 ID 2, 수량 1
        );
        OrderRequest orderRequest = new OrderRequest(userId, itemRequests);

        // Mocking 설정: ProductRepository의 동작을 가상으로 정의합니다.
        // productRepository.findById(1L)이 호출되면 mockProduct1을 반환하도록 설정
        Product mockProduct1 = createProduct(1L, "Laptop", BigDecimal.valueOf(1000.00), 10); // 초기 재고 10
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct1));

        // productRepository.findById(2L)이 호출되면 mockProduct2를 반환하도록 설정
        Product mockProduct2 = createProduct(2L, "Mouse", BigDecimal.valueOf(50.00), 5); // 초기 재고 5
        given(productRepository.findById(2L)).willReturn(Optional.of(mockProduct2));

        // Mocking 설정: OrderRepository의 동작을 가상으로 정의합니다.
        // orderRepository.save(어떤 Order 객체든)가 호출되면,
        // save 메서드에 전달된 Order 객체에 가상의 ID(10L)를 설정한 후 그 객체를 반환하도록 설정
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> {
            Order order = invocation.getArgument(0); // save 메서드의 첫 번째 인자(Order 객체)를 가져옴
            order.setId(10L); // 가상의 주문 ID 설정
            return order; // ID가 설정된 Order 객체 반환
        });

        // When (테스트 대상 메서드 실행 단계)
        // orderFacade의 placeOrder 메서드를 호출합니다.
        OrderResult result = orderFacade.placeOrder(orderRequest);

        // Then (결과 검증 단계)
        assertThat(result).isNotNull(); // 결과 객체가 null이 아닌지 확인
        assertThat(result.getOrderId()).isEqualTo(10L); // 가상으로 설정한 주문 ID와 일치하는지 확인
        assertThat(result.getUserId()).isEqualTo(userId); // 요청한 사용자 ID와 일치하는지 확인
        assertThat(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(2050.00)); // (1000*2) + (50*1) = 2050
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PAID.name()); // Facade에서 바로 PAID로 변경된다고 가정했으므로 PAID인지 확인

        // Mockito verify: ProductRepository의 findById 메서드가 각 상품 ID에 대해 '정확히 한 번' 호출되었는지 검증
        // 이는 OrderFacade가 ProductRepository를 올바르게 사용하여 상품 정보를 조회하는지 확인합니다.
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);

        // Mockito verify: OrderRepository의 save 메서드가 '정확히 한 번' 호출되었는지 검증
        // 이는 OrderFacade가 최종 Order 객체를 OrderRepository에 저장하는지 확인합니다.
        verify(orderRepository, times(1)).save(any(Order.class));

        // 각 상품의 재고가 올바르게 감소했는지 확인 (Product 엔티티의 내부 로직이므로 간접적으로 확인)
        assertThat(mockProduct1.getStock()).isEqualTo(8); // 초기 10 - 주문 수량 2 = 8
        assertThat(mockProduct2.getStock()).isEqualTo(4); // 초기 5 - 주문 수량 1 = 4
    }

    @Test
    @DisplayName("주문 요청 시 상품을 찾을 수 없으면 ProductNotFoundException을 발생시켜야 한다")
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        // Given
        Long userId = 1L;
        List<OrderItemRequest> itemRequests = Arrays.asList(
                createOrderItemRequest(1L, 2),
                createOrderItemRequest(999L, 1) // 존재하지 않는 상품 ID (999L)
        );
        OrderRequest orderRequest = new OrderRequest(userId, itemRequests);

        // Mocking: 상품 1L은 찾을 수 있지만, 상품 999L은 찾을 수 없도록 설정합니다.
        Product mockProduct1 = createProduct(1L, "Laptop", BigDecimal.valueOf(1000.00), 10);
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct1));
        given(productRepository.findById(999L)).willReturn(Optional.empty()); // Optional.empty()로 상품이 없음을 표현

        // When & Then (특정 예외가 발생하는지 검증)
        // orderFacade.placeOrder(orderRequest) 호출 시 ProductNotFoundException이 발생하는지 확인합니다.
        assertThrows(ProductNotFoundException.class, () -> orderFacade.placeOrder(orderRequest));

        // Mockito verify: findById가 두 번 호출되었는지 검증
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(999L);
        // 중요한 검증: 주문 실패했으므로 OrderRepository.save는 '호출되지 않아야' 합니다.
        verify(orderRepository, times(0)).save(any(Order.class));

        // 상품의 재고가 변경되지 않았는지 확인 (예외 발생으로 인해 트랜잭션 롤백 가정)
        assertThat(mockProduct1.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("주문 요청 시 재고가 부족하면 OutOfStockException을 발생시켜야 한다")
    void shouldThrowOutOfStockExceptionWhenStockIsInsufficient() {
        // Given
        Long userId = 1L;
        List<OrderItemRequest> itemRequests = Arrays.asList(
                createOrderItemRequest(1L, 2),
                createOrderItemRequest(2L, 10) // 상품 ID 2는 재고(5개)보다 많은 수량(10개)을 요청
        );
        OrderRequest orderRequest = new OrderRequest(userId, itemRequests);

        // Mocking: 상품 1L은 재고 충분, 상품 2L은 재고 부족 상황을 설정합니다.
        Product mockProduct1 = createProduct(1L, "Laptop", BigDecimal.valueOf(1000.00), 10);
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct1));

        Product mockProduct2 = createProduct(2L, "Mouse", BigDecimal.valueOf(50.00), 5); // 재고 5개
        given(productRepository.findById(2L)).willReturn(Optional.of(mockProduct2));

        // When & Then
        // orderFacade.placeOrder(orderRequest) 호출 시 OutOfStockException이 발생하는지 확인합니다.
        assertThrows(OutOfStockException.class, () -> orderFacade.placeOrder(orderRequest));

        // Mockito verify: findById는 두 번 호출
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
        // 중요한 검증: 주문 실패했으므로 OrderRepository.save는 '호출되지 않아야' 합니다.
        verify(orderRepository, times(0)).save(any(Order.class));

        // 재고는 원상태로 유지되어야 함 (트랜잭션 롤백 가정)
        assertThat(mockProduct1.getStock()).isEqualTo(10); // 변경되지 않아야 함
        assertThat(mockProduct2.getStock()).isEqualTo(5); // 변경되지 않아야 함
    }
}


package kr.hhplus.be.server.domain.model.order;

import kr.hhplus.be.server.domain.exception.InvalidOrderStateException;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    private OrderItem createOrderItem(Long productId, String productName, BigDecimal price, int quantity) {
        return new OrderItem(productId, productName, price, quantity);
    }

    @Test
    @DisplayName("주문 생성 시, 주문자 ID가 null이면 IllegalArgumentException 발생")
    void 주문자ID가_null_이면_주문실패(){
        // Given
        Long userId = null; // userId로 변수명 변경 (Order 생성자에 맞춤)
        List<OrderItem> items = Collections.singletonList(
                createOrderItem(101L, "Laptop", BigDecimal.valueOf(1200.00), 1)
        );

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Order(userId, items));
    }

    @Test
    @DisplayName("주문 생성 시, 주문 상품 목록이 null이면 IllegalArgumentException 발생")
    void 주문상품이_null_이면_주문실패(){
        // Given
        Long userId = 1L; // userId로 변수명 변경
        List<OrderItem> items = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Order(userId, items));
    }

    @Test
    @DisplayName("주문 생성 시, 주문 상품 목록이 비어있으면 IllegalArgumentException 발생")
    void 주문상품이_비어있으면_주문실패(){
        // Given
        Long userId = 1L; // userId로 변수명 변경
        List<OrderItem> items = Collections.emptyList(); // 수정: List<OrderItem>으로 변경

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Order(userId, items));
    }

    @Test
    @DisplayName("주문 상태가 PENDING에서 PAID로 변경 가능")
    void 주문상태가_PENDING에서_PAID로_변경가능(){
        // Given
        Long userId = 1L; // 주문자 ID 추가
        List<OrderItem> items = Collections.singletonList(
                createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1) // OrderItem 생성자 인자 수정
        );
        Order order = new Order(userId, items); // 수정: order 객체 선언 및 초기화
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

        // When
        order.markAsPaid();

        // Then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("주문 상태가 PAID에서 CANCELED로 변경 가능")
    void 주문상태가_PAID에서_CANCELED로_변경가능(){ // 오타 수정: CANCELLED -> CANCELED
        // Given
        Long userId = 1L; // 주문자 ID 추가
        Order order = new Order(userId, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
        order.markAsPaid(); // PAID 상태로 변경
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID); // 현재 상태 확인

        // When
        order.cancel(); // 취소 메서드 호출

        // Then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("주문 상태가 PAID에서 REFUNDED로 변경 가능")
    void 주문상태가_PAID에서_REFUNDED로_변경가능(){
        // Given
        Long userId = 1L; // 주문자 ID 추가
        Order order = new Order(userId, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
        order.markAsPaid(); // PAID 상태로 변경
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID); // 현재 상태 확인

        // When
        order.markAsRefunded(); // 수정: 환불 메서드 호출

        // Then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.REFUNDED); // 수정: REFUNDED 상태 확인
    }

    @Test
    @DisplayName("PAID가 아닌 경우 REFUNDED로 변경 시 예외 발생") // 테스트 이름 변경 및 오타 수정
    void PAID가_아닌_경우_REFUNDED으로_변경시_예외발생(){ // 오타 수정: REFUNED -> REFUNDED
        // Given
        // PENDING 상태의 주문
        Long userId = 1L;
        Order pendingOrder = new Order(userId, Collections.singletonList(createOrderItem(101L, "ItemA", BigDecimal.valueOf(100.00), 1)));
        assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.PENDING);

        // CANCELED 상태의 주문
        Order canceledOrder = new Order(userId, Collections.singletonList(createOrderItem(102L, "ItemB", BigDecimal.valueOf(200.00), 1)));
        canceledOrder.cancel(); // 취소 상태로 변경
        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCELED);

        // When & Then
        // PENDING 상태에서 환불 시도 시 예외 발생
        assertThrows(InvalidOrderStateException.class, () -> pendingOrder.markAsRefunded());
        // CANCELED 상태에서 환불 시도 시 예외 발생
        assertThrows(InvalidOrderStateException.class, () -> canceledOrder.markAsRefunded());
    }

    // 추가적으로, 필요하다면 Order 생성 성공 테스트 케이스도 추가하는 것이 좋습니다.
    @Test
    @DisplayName("주문 생성 성공 및 초기 상태 확인")
    void 주문생성_성공_및_초기상태_확인(){
        // Given
        Long userId = 1L;
        List<OrderItem> items = Collections.singletonList(
                createOrderItem(101L, "Test Product", BigDecimal.valueOf(50.00), 2)
        );

        // When
        Order order = new Order(userId, items);

        // Then
        assertThat(order).isNotNull();
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getItems().size()).isEqualTo(1);
        assertThat(order.getTotalAmount()).isEqualTo(BigDecimal.valueOf(100.00)); // 50 * 2
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getOrderedAt()).isNotNull();
    }
}
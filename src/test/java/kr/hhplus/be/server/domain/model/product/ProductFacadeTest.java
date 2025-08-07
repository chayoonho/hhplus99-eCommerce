package kr.hhplus.be.server.domain.model.product;

import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.ProductResult;
import kr.hhplus.be.server.domain.exception.ProductNotFoundException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductFacade productFacade;

    private Product createProduct(Long id, String name, BigDecimal price, int stock, ProductStatus status) {
        Product product = new Product(name, price, stock);
        product.setId(id);
        // 테스트용이라 LocalDateTime.now() 대신 특정 시간으로 고정 (optional)
        // 실제 Product 생성자에서 현재 시간을 넣고 있다면 이 부분은 제거하거나 생성자 매개변수로 추가해야 함.
        // 현재 Product 생성자는 ID가 없으므로 DB 로드용 생성자를 활용하거나 별도의 setId를 통해 ID만 설정
        // 지금은 생성자로 생성 후 setId로 ID만 설정하는 방식 사용
        try {
            // 리플렉션 등으로 createdAt을 설정할 수 있지만, 여기서는 일단 Product의 기본 동작에 의존합니다.
            // 만약 createdAt을 생성자에 넣어야 한다면 Product 엔티티를 수정해야 합니다.
            // 현재 Product 엔티티는 createdAt을 직접 받지 않고 내부적으로 생성합니다.
            // 테스트의 편의를 위해 임시로 public setter를 사용하거나 DB 로드용 생성자를 사용할 수 있습니다.
            // 여기서는 Product 생성자가 내부적으로 LocalDateTime.now()를 사용하므로, 테스트에서는 대략적인 시간만 검증합니다.
            // 더 정확한 시간 비교를 원한다면 Product 엔티티에 LocalDateTime createdAt을 받는 생성자를 추가해야 합니다.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    @Test
    @DisplayName("새로운_상품이_성공적으로_생성되어야_한다")
    void 새로운_상품이_성공적으로_생성() {
        // Given
        ProductCommand.CreateProduct command = new ProductCommand.CreateProduct("새로운 상품", BigDecimal.valueOf(1500.00), 50);

        // Mocking: productRepository.save(어떤 Product 객체든)가 호출되면,
        // save 메서드에 전달된 Product 객체에 가상의 ID(1L)와 생성 시간(현재)을 설정한 후 그 객체를 반환하도록 설정
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> {
            Product product = invocation.getArgument(0); // save 메서드의 첫 번째 인자(Product 객체)를 가져옴
            product.setId(1L); // 가상의 상품 ID 설정
            // product.setCreatedAt(LocalDateTime.now()); // Product 생성자에서 설정되므로 불필요
            return product; // ID가 설정된 Product 객체 반환
        });

        // When
        ProductResult result = productFacade.createProduct(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("새로운 상품");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(1500.00));
        assertThat(result.getStock()).isEqualTo(50);
        assertThat(result.getCreatedAt()).isNotNull(); // 생성 시간이 설정되었는지 확인
        // verify: productRepository.save 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("ID로_상품_상세_정보를_조회할_수_있어야_한다")
    void ID로_상품_상세_정보를_조회() {
        // Given
        Long productId = 1L;
        Product mockProduct = createProduct(productId, "테스트 상품", BigDecimal.valueOf(100.00), 10, ProductStatus.AVAILABLE);
        // Mocking: productRepository.findById(productId)가 호출되면 mockProduct를 반환하도록 설정
        given(productRepository.findById(productId)).willReturn(Optional.of(mockProduct));

        // When
        ProductResult result = productFacade.getProduct(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("테스트 상품");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(result.getStock()).isEqualTo(10);
        assertThat(result.getCreatedAt()).isNotNull();
        // verify: productRepository.findById 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("존재하지_않는_상품_ID_조회_시_ProductNotFoundException을_발생시켜야_한다")
    void 존재하지_않는_상품_ID_조회_시_ProductNotFoundException이_발생함() {
        // Given
        Long nonExistentProductId = 99L;
        // Mocking: productRepository.findById(nonExistentProductId)가 호출되면 Optional.empty()를 반환하도록 설정
        given(productRepository.findById(nonExistentProductId)).willReturn(Optional.empty());

        // When & Then
        // productFacade.getProduct(nonExistentProductId) 호출 시 ProductNotFoundException이 발생하는지 확인
        assertThrows(ProductNotFoundException.class, () -> productFacade.getProduct(nonExistentProductId));
        // verify: productRepository.findById 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productRepository, times(1)).findById(nonExistentProductId);
    }

    @Test
    @DisplayName("모든_상품_목록을_조회할_수_있어야_한다")
    void 모든_상품_목록을_조회할_수_있음() {
        // Given
        Product mockProduct1 = createProduct(1L, "상품1", BigDecimal.valueOf(100.00), 5, ProductStatus.AVAILABLE);
        Product mockProduct2 = createProduct(2L, "상품2", BigDecimal.valueOf(200.00), 0, ProductStatus.OUT_OF_STOCK);
        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);

        // Mocking: productRepository.findAll()이 호출되면 mockProducts 리스트를 반환하도록 설정
        given(productRepository.findAll()).willReturn(mockProducts);

        // When
        List<ProductResult> results = productFacade.getAllProducts();

        // Then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2); // 두 개의 상품이 반환되었는지 확인

        // 첫 번째 상품 검증
        ProductResult result1 = results.get(0);
        assertThat(result1.getId()).isEqualTo(1L);
        assertThat(result1.getName()).isEqualTo("상품1");
        assertThat(result1.getPrice()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(result1.getStock()).isEqualTo(5);

        // 두 번째 상품 검증
        ProductResult result2 = results.get(1);
        assertThat(result2.getId()).isEqualTo(2L);
        assertThat(result2.getName()).isEqualTo("상품2");
        assertThat(result2.getPrice()).isEqualTo(BigDecimal.valueOf(200.00));
        assertThat(result2.getStock()).isEqualTo(0);

        // verify: productRepository.findAll 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productRepository, times(1)).findAll();
    }
}
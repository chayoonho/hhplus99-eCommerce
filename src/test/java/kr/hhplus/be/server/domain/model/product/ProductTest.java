package kr.hhplus.be.server.domain.model.product;

import kr.hhplus.be.server.domain.exception.OutOfStockException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    @DisplayName("상품은_이름, 가격, 재고를_가지고_정상적으로_생성되어야_하며_초기_상태는_AVAILABLE이다")
    void 상품은_가격과_금액이_항상_있어야함(){
        // Give
        String name = "Laptop";
        BigDecimal price = BigDecimal.valueOf(1000.00);
        int stock = 10;

        // When
        Product product = new Product(name, price, stock);

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getStock()).isEqualTo(stock);
        assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE); // 초기 상태 검증
        assertThat(product.getCreatedAt()).isNotNull(); // 생성 시간 자동 설정 검증
    }

    @Test
    @DisplayName("상품의_이름은_null일_수_없으며_예외를_발생시켜야_한다")
    void 상품의_이름은_null일_수_없음(){
        // Given
        String name = null;
        BigDecimal price = BigDecimal.valueOf(100.00);
        int stock = 10;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Product(name, price, stock));
    }

    @Test
    @DisplayName("상품의_이름이_공백_또는_빈_문자열일_수_없으며_예외를_발생시켜야_한다")
    void 상품의_이름이_비워있을_수_없음(){
        // Given
        String name1 = ""; // 빈 문자열
        String name2 = "   "; // 공백 문자열

        BigDecimal price = BigDecimal.valueOf(100.00);
        int stock = 10;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Product(name1, price, stock));
        assertThrows(IllegalArgumentException.class, () -> new Product(name2, price, stock)); // 공백 문자열 테스트 추가
    }

    @Test
    @DisplayName("상품의_가격이_음수일_수_없으며_예외를_발생시켜야_한다")
    void 상품의_가격이_음수일_수_없음(){
        // Given
        String name = "Test Product";
        BigDecimal price = BigDecimal.valueOf(-10.00);
        int stock = 10;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Product(name, price, stock));
    }

    @Test
    @DisplayName("상품의_재고가_음수일_수_없으며_예외를_발생시켜야_한다")
    void 상품의_재고가_음수일_수_없음(){
        // Given
        String name = "Test Product";
        BigDecimal price = BigDecimal.valueOf(100.00);
        int stock = -5;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Product(name, price, stock));
    }

    @Test
    @DisplayName("재고_감소시_요청_수량만큼_정상적으로_재고가_줄어야_하며_상태는_AVAILABLE_유지")
    void 재고_감소시_정상적으로_재고가_줄어야_함(){
        // Given
        Product product = new Product("Test Product", BigDecimal.valueOf(100.00), 10);
        int quantityToDecrease = 3;

        // When
        product.decreaseStock(quantityToDecrease);

        // Then
        assertThat(product.getStock()).isEqualTo(7);
        assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE); // 아직 재고 남음
    }

    @Test
    @DisplayName("재고_감소시_재고가_0이_되면_상태가_OUT_OF_STOCK으로_변경되어야_한다") // 테스트 이름 구체화
    void 재고_감소시_재고가_0이_되면_상태가_OUT_OF_STOCK으로_변경되어야_한다(){
        // Given
        Product product = new Product("Test Product", BigDecimal.valueOf(100.00), 5);
        int quantityToDecrease = 5; // 재고를 0으로 만들 수량

        // When
        product.decreaseStock(quantityToDecrease);

        // Then
        assertThat(product.getStock()).isEqualTo(0);
        assertThat(product.getStatus()).isEqualTo(ProductStatus.OUT_OF_STOCK); // 재고 0이 되면 OUT_OF_STOCK 상태로 변경
    }


    @Test
    @DisplayName("재고보다_많은_수량을_감소_시킬_수_없으며_OutOfStockException을_발생시켜야_한다")
    void 재고보다_많은_수량을_감소_시킬_수_없음(){
        // Given
        Product product = new Product("Test Product", BigDecimal.valueOf(100.00), 5);
        int quantityToDecrease = 10; // 현재 재고보다 많은 수량

        // When & Then
        assertThrows(OutOfStockException.class, () -> product.decreaseStock(quantityToDecrease));
        assertThat(product.getStock()).isEqualTo(5); // 재고는 변경되지 않아야 함
        assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE); // 상태도 변경되지 않아야 함
    }
}
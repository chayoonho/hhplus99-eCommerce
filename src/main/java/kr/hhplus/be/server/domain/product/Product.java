// src/main/java/kr/hhplus.be.server.domain.product/Product.java
package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.exception.OutOfStockException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Product {
    private Long id; // 상품 ID (DB에서 할당)
    private String name; // 상품 이름
    private BigDecimal price; // 상품 가격
    private int stock; // 상품 재고
    private ProductStatus status; // 상품 상태
    private LocalDateTime createdAt; // 생성 시각

    // 새로운 상품 생성 시 사용되는 생성자
    public Product(String name, BigDecimal price, int stock) {
        // 유효성 검사 (테스트에서 확인하는 부분)
        if (name == null || name.trim().isEmpty()) { // 이름이 null이거나 비어있거나 공백만 있는 경우
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) { // 가격이 null이거나 음수인 경우
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
        if (stock < 0) { // 재고가 음수인 경우
            throw new IllegalArgumentException("상품 재고는 음수일 수 없습니다.");
        }

        this.name = name.trim(); // 이름 앞뒤 공백 제거
        this.price = price;
        this.stock = stock;
        // 초기 상태 설정: 재고가 0보다 크면 AVAILABLE, 아니면 OUT_OF_STOCK
        this.status = (stock > 0) ? ProductStatus.AVAILABLE : ProductStatus.OUT_OF_STOCK;
        this.createdAt = LocalDateTime.now(); // 생성 시각 설정
    }

    // DB 로드용 생성자 (프레임워크가 사용)
    public Product(Long id, String name, BigDecimal price, int stock, ProductStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 비즈니스 행위: 재고 감소
    public void decreaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("감소시킬 수량은 음수일 수 없습니다.");
        }
        if (this.stock < quantity) { // 현재 재고보다 감소시킬 수량이 많으면
            throw new OutOfStockException("재고가 부족합니다. 현재 재고: " + this.stock + ", 요청 수량: " + quantity);
        }
        this.stock -= quantity; // 재고 감소
        if (this.stock == 0) { // 재고가 0이 되면 상태를 OUT_OF_STOCK으로 변경
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    // (선택 사항) 비즈니스 행위: 재고 증가
    public void increaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("증가시킬 수량은 음수일 수 없습니다.");
        }
        this.stock += quantity;
        if (this.stock > 0 && this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.AVAILABLE; // 재고가 생기고 품절 상태였다면 AVAILABLE로 변경
        }
    }

    // (선택 사항) 비즈니스 행위: 상품 상태 변경 (관리자 기능)
    public void markAsUnavailable() {
        this.status = ProductStatus.UNAVAILABLE;
    }

    public void markAsAvailable() {
        if (this.stock > 0) { // 재고가 있어야 AVAILABLE로 변경 가능하도록 규칙 정의
            this.status = ProductStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("재고가 없는 상품은 AVAILABLE 상태로 변경할 수 없습니다.");
        }
    }


    // Setter (주로 영속성 계층에서 ID 할당 시 사용)
    public void setId(Long id) {
        this.id = id;
    }

    // Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public ProductStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
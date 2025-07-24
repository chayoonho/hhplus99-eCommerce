package kr.hhplus.be.server.application.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResult{
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    private LocalDateTime createdAt; // 생성 시간도 결과에 포함

    // 모든 필드를 인자로 받는 생성자
    public ProductResult(Long id, String name, BigDecimal price, int stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // Getter 메서드
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

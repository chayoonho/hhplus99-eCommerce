package kr.hhplus.be.server.interfaces.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    private LocalDateTime createdAt;

    public ProductResponse(Long id, String name, BigDecimal price, int stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // 기본 생성자
    public ProductResponse() {}

    // Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setter (선택적: 직렬화/역직렬화 라이브러리에 따라 필요할 수 있음)
//    public void setId(Long id) { this.id = id; }
//    public void setName(String name) { this.name = name; }
//    public void setPrice(BigDecimal price) { this.price = price; }
//    public void setStock(int stock) { this.stock = stock; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
package kr.hhplus.be.server.interfaces.product;

import java.math.BigDecimal;

public class ProductRequest {

    public static class CreateProductRequest {
        private String name;
        private BigDecimal price;
        private int stock;

        // 생성자
        public CreateProductRequest(String name, BigDecimal price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        // Getter
        public String getName() { return name; }
        public BigDecimal getPrice() { return price; }
        public int getStock() { return stock; }

        // Setter
        public void setName(String name) { this.name = name; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public void setStock(int stock) { this.stock = stock; }
    }
}
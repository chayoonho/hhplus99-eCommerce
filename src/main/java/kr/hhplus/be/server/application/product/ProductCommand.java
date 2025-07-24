package kr.hhplus.be.server.application.product;

import java.math.BigDecimal;

public class ProductCommand {
    public static class CreateProduct { // static inner class로 정의
        private String name;
        private BigDecimal price;
        private int stock;

        public CreateProduct(String name, BigDecimal price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        // Getter
        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }
    }
}


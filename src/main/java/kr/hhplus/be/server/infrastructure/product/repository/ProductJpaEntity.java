package kr.hhplus.be.server.infrastructure.product.repository;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Entity // JPA 엔티티임을 명시
    @Table(name = "products") // 매핑될 테이블 이름
    public class ProductJpaEntity {

        @Id // 기본 키
        @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 (DB에 위임)
        private Long id;

        @Column(nullable = false) // NULL을 허용하지 않는 컬럼
        private String name;

        @Column(nullable = false, precision = 10, scale = 2) // 소수점 자릿수 지정
        private BigDecimal price;

        @Column(nullable = false)
        private int stock;

        @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
        @Column(nullable = false)
        private ProductStatus status; // 도메인 ProductStatus Enum 사용

        @Column(nullable = false, updatable = false) // 생성 시각, 업데이트 불가
        private LocalDateTime createdAt;

        protected ProductJpaEntity() {}

        // 생성자
        public ProductJpaEntity(String name, BigDecimal price, int stock, ProductStatus status, LocalDateTime createdAt) {
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.status = status;
            this.createdAt = createdAt;
        }

        // Getter
        public Long getId() { return id; }
        public String getName() { return name; }
        public BigDecimal getPrice() { return price; }
        public int getStock() { return stock; }
        public ProductStatus getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        // Setter
        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public void setStock(int stock) { this.stock = stock; }
        public void setStatus(ProductStatus status) { this.status = status; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

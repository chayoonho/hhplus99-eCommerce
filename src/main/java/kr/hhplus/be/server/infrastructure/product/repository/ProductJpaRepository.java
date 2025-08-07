package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository  extends JpaRepository<Product, Long> {
}

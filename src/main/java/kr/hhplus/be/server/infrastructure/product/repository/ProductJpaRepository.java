package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository {
    Optional<Product> findById(Long id);
    Product save(Product product);
    List<Product> findAll();
}

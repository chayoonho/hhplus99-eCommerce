package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    // 인메모리
    private final Map<Long, Product> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L); // ID 생성을 위한 시퀀스

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            // 새 Product인 경우 ID 할당
            Long newId = sequence.incrementAndGet();
            product.setId(newId);
            // createdAt이 Product 생성자에서 설정되므로 별도로 설정할 필요 없음
        }
        storage.put(product.getId(), product); // Map에 저장
        return product;
    }

    @Override
    public List<Product> findAll() {
        // ID 순서대로 정렬하여 반환 (Optional)
        return storage.values().stream()
                .sorted(Comparator.comparing(Product::getId))
                .collect(Collectors.toList());
    }
}

package kr.hhplus.be.server.application.product;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.exception.ProductNotFoundException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductFacade {

    private final ProductRepository productRepository; // 인터페이스에 의존

    public ProductFacade(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 새로운 상품을 생성하는 유스케이스를 처리합니다.
     *
     * @param command 상품 생성 요청 데이터
     * @return 생성된 상품의 결과 DTO
     */
    @Transactional // 트랜잭션 관리
    public ProductResult createProduct(ProductCommand.CreateProduct command) {
        // 1. Product 도메인 엔티티를 생성합니다. (도메인 규칙에 따라 유효성 검사 수행)
        Product newProduct = new Product(
                command.getName(),
                command.getPrice(),
                command.getStock()
        );

        // 2. 생성된 Product 엔티티를 저장소에 저장합니다. (Repository 인터페이스 사용)
        Product savedProduct = productRepository.save(newProduct);

        // 3. 저장된 Product 엔티티를 ProductResult DTO로 변환하여 반환합니다.
        return new ProductResult(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getCreatedAt()
        );
    }

    /**
     * 특정 ID의 상품을 조회하는 유스케이스를 처리합니다.
     *
     * @param productId 조회할 상품 ID
     * @return 조회된 상품의 결과 DTO
     */
    @Transactional
    public ProductResult getProduct(Long productId) {
        // ProductRepository를 통해 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt()
        );
    }

    /**
     * 모든 상품을 조회하는 유스케이스를 처리합니다.
     *
     * @return 모든 상품의 결과 DTO 리스트
     */
    @Transactional
    public List<ProductResult> getAllProducts() {
        // ProductRepository를 통해 모든 상품 조회
        List<Product> products = productRepository.findAll();

        // 도메인 엔티티 리스트를 ProductResult DTO 리스트로 변환하여 반환
        return products.stream()
                .map(product -> new ProductResult(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}

package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.ProductResult;
import kr.hhplus.be.server.domain.exception.ProductNotFoundException; // ProductNotFoundException 임포트
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    /**
     * 새로운 상품을 생성하는 HTTP POST 요청을 처리
     * RequestBody로 ProductRequest.CreateProductRequest를 받아서 ProductFacade에 전달
     * @param request 상품 생성 요청 DTO
     * @return 생성된 상품 정보와 함께 HTTP 201 Created 응답
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest.CreateProductRequest request) {
        // 1. ProductRequest를 ProductCommand로 변환
        ProductCommand.CreateProduct command = new ProductCommand.CreateProduct(
                request.getName(),
                request.getPrice(),
                request.getStock()
        );

        // 2. ProductFacade를 호출하여 비즈니스 로직 수행
        ProductResult result = productFacade.createProduct(command);

        // 3. ProductResult를 ProductResponse로 변환 (애플리케이션 계층 -> 인터페이스 계층)
        ProductResponse response = new ProductResponse(
                result.getId(),
                result.getName(),
                result.getPrice(),
                result.getStock(),
                result.getCreatedAt()
        );

        // 4. HTTP 201 Created 상태 코드와 함께 응답 반환
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 ID의 상품을 조회하는 HTTP GET 요청을 처리
     * PathVariable로 상품 ID를 받아서 ProductFacade에 전달
     * @param productId 조회할 상품 ID
     * @return 조회된 상품 정보와 함께 HTTP 200 OK 응답
     */
    @GetMapping("/{productId}") // "/products/{productId}" 경로로 GET 요청이 오면 이 메서드가 처리
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        // 1. ProductFacade를 호출하여 상품 정보 조회
        ProductResult result = productFacade.getProduct(productId);

        // 2. ProductResult를 ProductResponse로 변환
        ProductResponse response = new ProductResponse(
                result.getId(),
                result.getName(),
                result.getPrice(),
                result.getStock(),
                result.getCreatedAt()
        );

        // 3. HTTP 200 OK 상태 코드와 함께 응답 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모든 상품 목록을 조회하는 HTTP GET 요청을 처리
     * @return 모든 상품 정보 리스트와 함께 HTTP 200 OK 응답
     */
    @GetMapping // "/products" 경로로 GET 요청이 오면 이 메서드가 처리 (ID가 없는 경우)
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        // 1. ProductFacade를 호출하여 모든 상품 정보 조회
        List<ProductResult> results = productFacade.getAllProducts();

        // 2. ProductResult 리스트를 ProductResponse 리스트로 변환
        List<ProductResponse> responses = results.stream()
                .map(result -> new ProductResponse(
                        result.getId(),
                        result.getName(),
                        result.getPrice(),
                        result.getStock(),
                        result.getCreatedAt()
                ))
                .collect(Collectors.toList());

        // 3. HTTP 200 OK 상태 코드와 함께 응답 반환
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * ProductNotFoundException 발생 시 처리하는 예외 핸들러
     * @param ex 발생한 ProductNotFoundException
     * @return HTTP 404 Not Found 상태 코드와 함께 오류 메시지 응답
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
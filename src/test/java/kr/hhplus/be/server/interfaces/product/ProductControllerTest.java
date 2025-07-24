package kr.hhplus.be.server.interfaces.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.ProductResult;
import kr.hhplus.be.server.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest 어노테이션은 Spring MVC 컴포넌트만 스캔하여 테스트합니다.
// 이 테스트는 ProductController만 로드하고 ProductFacade는 MockBean으로 대체합니다.
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductFacade productFacade;

    @Test
    @DisplayName("새로운_상품을_생성하는_요청이_성공적으로_처리됨으로")
    void 새로운_상품을_생성하는_요청이_성공적으로_처리됨으로() throws Exception {
        // Given
        ProductRequest.CreateProductRequest request = new ProductRequest.CreateProductRequest(
                "새로운 상품", BigDecimal.valueOf(1500.00), 50
        );
        // ProductFacade가 반환할 예상 결과 DTO 생성
        ProductResult expectedResult = new ProductResult(
                1L, "새로운 상품", BigDecimal.valueOf(1500.00), 50, LocalDateTime.now()
        );

        // Mocking: productFacade.createProduct(어떤 ProductCommand.CreateProduct든) 호출 시 expectedResult 반환
        given(productFacade.createProduct(any(ProductCommand.CreateProduct.class)))
                .willReturn(expectedResult);

        // When & Then
        mockMvc.perform(post("/products") // POST 요청
                        .contentType(MediaType.APPLICATION_JSON) // JSON 타입
                        .content(objectMapper.writeValueAsString(request))) // 요청 바디를 JSON 문자열로 변환
                .andDo(print()) // 요청/응답 상세 내용 출력 (디버깅용)
                .andExpect(status().isCreated()) // HTTP 상태 코드 201 Created 확인
                .andExpect(jsonPath("$.id").value(1L)) // 응답 JSON의 id 필드 검증
                .andExpect(jsonPath("$.name").value("새로운 상품")) // 응답 JSON의 name 필드 검증
                .andExpect(jsonPath("$.price").value(1500.00)) // 응답 JSON의 price 필드 검증
                .andExpect(jsonPath("$.stock").value(50)); // 응답 JSON의 stock 필드 검증

        // verify: productFacade.createProduct 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productFacade, times(1)).createProduct(any(ProductCommand.CreateProduct.class));
    }

    @Test
    @DisplayName("ID로_상품_상세_정보를_조회하는_요청이_성공적으로_처리됨으로")
    void ID로_상품_상세_정보를_조회하는_요청이_성공적으로_처리됨으로() throws Exception {
        // Given
        Long productId = 1L;
        // ProductFacade가 반환할 예상 결과 DTO 생성
        ProductResult expectedResult = new ProductResult(
                productId, "테스트 상품", BigDecimal.valueOf(100.00), 10, LocalDateTime.now()
        );
        // Mocking: productFacade.getProduct(productId) 호출 시 expectedResult 반환
        given(productFacade.getProduct(productId)).willReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/products/{productId}", productId) // GET 요청
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 OK 확인
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("테스트 상품"))
                .andExpect(jsonPath("$.price").value(100.00))
                .andExpect(jsonPath("$.stock").value(10));

        // verify: productFacade.getProduct 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productFacade, times(1)).getProduct(productId);
    }

    @Test
    @DisplayName("존재하지_않는_상품_ID_조회_시_ProductNotFoundException이_발생하여_적절히_처리됨으로")
    void 존재하지_않는_상품_ID_조회_시_ProductNotFoundException이_발생하여_적절히_처리됨으로() throws Exception {
        // Given
        Long nonExistentProductId = 99L;
        // Mocking: productFacade.getProduct(nonExistentProductId) 호출 시 ProductNotFoundException 발생하도록 설정
        given(productFacade.getProduct(nonExistentProductId))
                .willThrow(new ProductNotFoundException("상품을 찾을 수 없습니다."));

        // When & Then
        mockMvc.perform(get("/products/{productId}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()) // HTTP 상태 코드 404 Not Found 확인
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다.")); // 예외 메시지 확인

        // verify: productFacade.getProduct 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productFacade, times(1)).getProduct(nonExistentProductId);
    }

    @Test
    @DisplayName("모든_상품_목록을_조회하는_요청이_성공적으로_처리됨으로")
    void 모든_상품_목록을_조회하는_요청이_성공적으로_처리됨으로() throws Exception {
        // Given
        // ProductFacade가 반환할 예상 결과 DTO 리스트 생성
        List<ProductResult> expectedResults = Arrays.asList(
                new ProductResult(1L, "상품1", BigDecimal.valueOf(100.00), 5, LocalDateTime.now()),
                new ProductResult(2L, "상품2", BigDecimal.valueOf(200.00), 0, LocalDateTime.now())
        );
        // Mocking: productFacade.getAllProducts() 호출 시 expectedResults 반환
        given(productFacade.getAllProducts()).willReturn(expectedResults);

        // When & Then
        mockMvc.perform(get("/products") // GET 요청
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 OK 확인
                .andExpect(jsonPath("$").isArray()) // 응답이 배열인지 확인
                .andExpect(jsonPath("$.length()").value(2)) // 배열의 크기 확인
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("상품1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("상품2"));

        // verify: productFacade.getAllProducts 메서드가 '정확히 한 번' 호출되었는지 검증
        verify(productFacade, times(1)).getAllProducts();
    }
}
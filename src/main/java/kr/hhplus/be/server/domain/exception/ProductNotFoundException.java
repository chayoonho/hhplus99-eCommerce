package kr.hhplus.be.server.domain.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }

    // 필요하다면 예외 메시지와 함께 원인(cause)을 받는 생성자도 추가할 수 있습니다.
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

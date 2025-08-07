package kr.hhplus.be.server.domain.exception;

public class BusinessException extends RuntimeException {

    // 예외 코드 (선택 사항, 상세한 에러 코드 관리가 필요할 때 사용)
    private final String errorCode;

    /**
     * 기본 생성자
     * @param message 예외 메시지
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = null; // 기본적으로 errorCode 없음
    }

    /**
     * 예외 코드와 함께 생성하는 생성자
     * @param message 예외 메시지
     * @param errorCode 예외 코드
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 메시지와 원인 예외와 함께 생성하는 생성자
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * 메시지, 원인 예외, 예외 코드와 함께 생성하는 생성자
     * @param message 예외 메시지
     * @param cause 원인 예외
     * @param errorCode 예외 코드
     */
    public BusinessException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 예외 코드를 반환
     * @return 예외 코드
     */
    public String getErrorCode() {
        return errorCode;
    }
}
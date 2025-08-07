package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    PENDING,    // 주문 접수 (결제 대기)
    PAID,       // 결제 완료 (결제 완료 및 처리 완료)
    CANCELED,   // 주문 취소됨
    REFUNDED    // 환불됨
}

package kr.hhplus.be.server.application.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderCommand {

    // 새로운 주문을 생성하기 위한 요청 DTO
    public record CreateOrder(
            Long userId,
            List<CreateOrderItem> items
    ) {}

    // 주문 생성 시 각 상품 항목에 대한 요청 DTO
    public record CreateOrderItem(
            Long productId,
            // productName, price는 Facade에서 조회하여 OrderItem에 설정 (클라이언트에서 받지 않음)
            int quantity
    ) {}

    // 테스트 코드에서 사용된 OrderRequest에 매핑되는 DTO
    public record OrderRequest( // placeOrder 메서드에 사용
                                Long userId,
                                List<OrderItemRequest> itemRequests // OrderItemRequest 리스트
    ) {}

    // 테스트 코드에서 사용된 OrderItemRequest에 매핑되는 DTO
    public record OrderItemRequest( // OrderRequest 내부에서 사용
                                    Long productId,
                                    int quantity
    ) {}


    // 주문 조회 요청을 위한 Command (기존 유지)
    public record GetOrder(
            Long orderId
    ) {}

    // 주문 취소 요청을 위한 Command (기존 유지)
    public record CancelOrder(
            Long orderId,
            Long userId
    ) {}

    // 주문 환불 요청을 위한 Command (기존 유지)
    public record RefundOrder(
            Long orderId,
            Long userId
    ) {}
}
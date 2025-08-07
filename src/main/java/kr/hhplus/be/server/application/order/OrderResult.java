package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResult {

    // 주문 정보를 위한 Result (기존 OrderInfo 유지)
    public record OrderInfo(
            Long orderId,
            Long userId,
            List<OrderItemInfo> items,
            OrderStatus status,
            BigDecimal totalPrice,
            LocalDateTime orderDate,
            LocalDateTime lastUpdatedAt
    ) {}

    // OrderItem 정보를 위한 Result (기존 OrderItemInfo 유지)
    public record OrderItemInfo(
            Long productId,
            String productName,
            BigDecimal price,
            int quantity
    ) {}



    // 주문 생성 성공 응답 (간단한 정보만 반환할 때 - 기존 유지)
    public record CreateOrderResponse(
            Long orderId,
            OrderStatus status,
            BigDecimal totalPrice
    ) {}
}
package kr.hhplus.be.server.domain.model.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
//    @Test
//    void 주문자ID가_null_이면_주문실패(){
//        // Given
//        Long id = null;
//        List<OrderItem> items = Collections.singletonList(createOrderItem(101L, "Laptop"), BigDecimal.valueOf(1200.00), 1));
//
//        // When & Then
//        assertThrows(IllegalArgumentException.class, () -> new Order(id, items));
//    }
//
//    @Test
//    void 주문상품이_null_이면_주문실패(){
//        // Given
//        Long id = 1L;
//        List<OrderItem> items = null;
//
//        // When & Then
//        assertThrows(IllegalArgumentException.class, () -> new Order(id, items));
//    }
//
//    @Test
//    void 주문상품이_비어있으면_주문실패(){
//        // Given
//        Long id = 1L;
//        List<OrderItem> items = Collections.emptyList();
//
//        // When & Then
//        assertThrows(IllegalArgumentException.class, () -> new Order(id, items));
//    }
//
//    @Test
//    void 주문상태가_PENDING에서_PAID로_변경가능(){
//        // Given
//        Order order = new Order(1L, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
//
//        // When
//        order.markAsPaid();
//
//        // Then
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
//    }
//
//    @Test
//    void 주문상태가_PAID에서_CANCELD로_변경가능(){
//        // Given
//        Order order = new Order(1L, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
//        order.markAsPaid(); // PAID 상태로 변경
//
//        // When
//        order.cancel(); // 취소 메서드 호출
//
//        // Then
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
//    }
//
//    @Test
//    void 주문상태가_PAID에서_REFUNDED로_변경가능(){
//        // Given
//        Order order = new Order(1L, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
//        order.markAsPaid(); // PAID 상태로 변경
//
//        // When
//        order.cancel(); // 취소 메서드 호출
//
//        // Then
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
//    }
//
//    @Test
//    void PAID가_아닌_경우_REFUNED으로_변경시_예외발생(){
//        // Given
//        Order order = new Order(1L, Collections.singletonList(createOrderItem(101L, "Laptop", BigDecimal.valueOf(100.00), 1)));
//        order.markAsPaid(); // PAID 상태로 변경
//
//        // When & Then
//        assertThrows(InvalidOrderStateException.class, () -> order.markAsPaid()); // 다시 PAID 시도
//    }

}

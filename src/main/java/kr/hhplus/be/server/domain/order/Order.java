package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
//    private Long id;
//    private Long userId;
//    private List<OrderItem> items = new ArrayList<>();
//    private BigDecimal totalAmount;
//    private OrderStatus status;
//    private LocalDateTime orderedAt;
//
//    public Order(Long userId, List<OrderItem> items){
//        if(userId == null || userId <= 0L){
//            throw new IllegalArgumentException("아아디가 음수거나 0일수 없습니다.");
//        }
//
//        if(items == null ){
//            throw new IllegalArgumentException("주문 아이템이 null일 수 없습니다.");
//        }
//
//        this.userId = userId;
//        this.items = items;
//        this.totalAmount = BigDecimal.ZERO; // 초기화
//        this.status = OrderStatus.PENDING;
//        this.orderedAt = LocalDateTime.now();
//
//        calculateTotalAmount();
//    }
//
//    // 총액 계산
//    private void calculateTotalAmount(){
//        this.totalAmount = items.stream()
//                .map(OrderItem::getTotalPrice) // 값세팅
//                .reduce(BigDecimal.ZERO, BigDecimal::add); // 연산
//    }
//
//    // 상태변경(결제완료)
//    public void markAsPaid(){
//        if(this.status != OrderStatus.PENDING){
//            throw new InvalidOrderStateException("PAID로 상태변경 할 수 없습니다. 오직 PENDING 단계에서만 가능합니다.");
//        }
//
//        this.status = OrderStatus.PAID;
//    }
//
//    // 상태변경(취소)
//    public void cancel() {
//        // 이미 CANCELED 또는 REFUNDED 상태이면 취소 불가능
//        if (this.status == OrderStatus.CANCELED || this.status == OrderStatus.REFUNDED) {
//            throw new InvalidOrderStateException("현재 상태(" + this.status + ")에서는 주문을 취소할 수 없습니다.");
//        }
//        this.status = OrderStatus.CANCELED; // 상태를 CANCELED로 변경
//    }
//
//    // 상태변경(환불완료)
//    public void markAsRefunded() {
//        // PAID 또는 CANCELED 상태에서만 환불 가능하도록 가정
//        // 만약 CANCELED가 아닌 PAID에서만 환불 가능하도록 할 경우 조건 변경 필요
//        if (this.status != OrderStatus.PAID && this.status != OrderStatus.CANCELED) {
//            throw new InvalidOrderStateException("현재 상태(" + this.status + ")에서는 주문을 환불할 수 없습니다. PAID 또는 CANCELED 상태의 주문만 환불 가능합니다.");
//        }
//        // 이미 환불된 경우 중복 환불 방지
//        if (this.status == OrderStatus.REFUNDED) {
//            throw new InvalidOrderStateException("주문이 이미 환불된 상태입니다.");
//        }
//        this.status = OrderStatus.REFUNDED; // 상태를 REFUNDED로 변경
//    }
//
//    // Getter 메서드들 (외부에서 주문 정보 조회 시 사용)
//    public Long getId() { return id; }
//    // setId는 DB 어댑터에서 ID 설정 시 사용될 수 있으며, 도메인 외부에서만 사용 권장
//    public void setId(Long id) { this.id = id; }
//
//    public Long getUserId() { return userId; }
//    // OrderItem 리스트는 불변 리스트로 반환하여 외부에서의 직접적인 변경 방지
//    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
//    public BigDecimal getTotalAmount() { return totalAmount; }
//    public OrderStatus getStatus() { return status; }
//    public LocalDateTime getOrderedAt() { return orderedAt; }

}


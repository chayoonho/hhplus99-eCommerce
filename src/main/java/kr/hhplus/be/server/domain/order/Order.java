package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.exception.InvalidOrderStateException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList; // ArrayList 임포트 추가
import java.util.Collections;
import java.util.List;
import java.util.Objects; // Objects.equals, hashCode 사용을 위해 추가

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>(); // 초기화하여 null 방지

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false) // updatable = false: 이 필드는 생성 이후 변경되지 않음 (DB에서)
    private LocalDateTime orderedAt;

    @Column(nullable = false) // 마지막 업데이트 시간을 추적하기 위한 필드 추가
    private LocalDateTime lastUpdatedAt;

    protected Order() {}

    // 비즈니스 로직을 포함하는 생성자
    public Order(Long userId, List<OrderItem> items) {

        if(userId == null || userId <= 0L){
            throw new IllegalArgumentException("주문자 ID는 null이거나 0 이하일 수 없습니다.");
        }
        if(items == null || items.isEmpty()){ // items가 null이거나 비어있는 경우 모두 거부
            throw new IllegalArgumentException("주문 상품 목록은 필수이며, 비어있을 수 없습니다.");
        }

        this.userId = userId;
        for (OrderItem item : items) {
            addOrderItem(item);
        }

        this.totalAmount = BigDecimal.ZERO; // 초기화
        this.status = OrderStatus.PENDING; // 초기 상태는 PENDING
        this.orderedAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now(); // 생성 시점도 업데이트 시점으로 간주

        // 모든 초기화가 끝난 후 총액 계산
        calculateTotalAmount();
    }

    // OrderItem과 양방향 연관관계를 설정하는 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        this.items.add(orderItem);
        if (orderItem.getOrder() != this) {
            orderItem.setOrder(this);
        }
    }

    private void calculateTotalAmount(){
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice) // 각 OrderItem의 총 가격을 가져옴
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 모든 OrderItem의 총 가격을 합산
    }

    // 상태변경(결제완료)
    public void markAsPaid(){
        if(this.status != OrderStatus.PENDING){
            throw new InvalidOrderStateException("결제는 PENDING 상태의 주문만 가능합니다. 현재 상태: " + this.status);
        }
        this.status = OrderStatus.PAID;
        this.lastUpdatedAt = LocalDateTime.now(); // 상태 변경 시 업데이트 시간 기록
    }

    // 상태변경(취소)
    public void cancel() {
        // 이미 취소되었거나 환불된 주문은 취소 불가능
        if (this.status == OrderStatus.CANCELED || this.status == OrderStatus.REFUNDED) {
            throw new InvalidOrderStateException("현재 상태(" + this.status + ")에서는 주문을 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELED;
        this.lastUpdatedAt = LocalDateTime.now(); // 상태 변경 시 업데이트 시간 기록
    }

    // 상태변경(환불완료)
    public void markAsRefunded() {
        // 환불은 PAID 상태의 주문만 가능하도록 가정 (정책에 따라 CANCELED도 가능하게 할 수 있음)
        if (this.status != OrderStatus.PAID) {
            throw new InvalidOrderStateException("현재 상태(" + this.status + ")에서는 주문을 환불할 수 없습니다. PAID 상태의 주문만 환불 가능합니다.");
        }
        // 이미 환불된 경우 중복 환불 방지
        if (this.status == OrderStatus.REFUNDED) {
            throw new InvalidOrderStateException("주문이 이미 환불된 상태입니다.");
        }
        this.status = OrderStatus.REFUNDED;
        this.lastUpdatedAt = LocalDateTime.now(); // 상태 변경 시 업데이트 시간 기록
    }

    // Getter 메서드들
    public Long getId() { return id; }
    private void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; } // 추가된 Getter

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id); // ID가 같으면 같은 Order로 간주
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderedAt=" + orderedAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
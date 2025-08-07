package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*; // JPA 관련 어노테이션 임포트
import java.math.BigDecimal;
import java.util.Objects;

@Entity // 이 클래스가 JPA 엔티티임을 선언
@Table(name = "order_items") // 데이터베이스 테이블 이름 지정
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order와 다대일(ManyToOne) 관계 설정
    // fetch = FetchType.LAZY: OrderItem을 조회할 때 Order는 바로 로딩하지 않고 필요할 때 로딩 (성능 최적화)
    // @JoinColumn(name = "order_id", nullable = false): 외래 키 컬럼 이름과 NULL 허용 여부 지정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Order 엔티티 참조 (연관관계의 주인)

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 255) // 상품 이름 길이 지정
    private String productName;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    // JPA가 엔티티를 생성할 때 사용하는 기본 생성자 (필수)
    protected OrderItem() {}

    // 비즈니스 로직을 포함하는 생성자
    public OrderItem(Long productId, String productName, BigDecimal price, int quantity) {
        // OrderItem 자체의 유효성 검증 (상품 ID, 이름, 가격, 수량)
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("상품 ID는 필수이며, 0 이하일 수 없습니다.");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // 총 가격 계산 (도메인 로직)
    public BigDecimal getTotalPrice() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    // Getter 메서드들
    public Long getId() { return id; }
    public Order getOrder() { return order; } // 연관된 Order 객체 가져오기
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Order 엔티티에서만 호출될 Setter (양방향 연관관계 편의 메서드용)
    // 일반적으로는 private 또는 package-private으로 설정하여 외부에서의 직접적인 변경 방지
    void setOrder(Order order) {
        this.order = order;
    }

    // JPA 내부용 Setter (ID 설정 등)
    private void setId(Long id) {
        this.id = id;
    }

    // equals와 hashCode 오버라이딩 (Value Object의 특성을 고려하여 모든 필드로 비교)
    // 다만, 엔티티로서 ID가 있다면 ID로 비교하는 것이 일반적일 수 있습니다.
    // 여기서는 OrderItem이 주문 내에서 특정 상품의 고유한 항목을 나타내므로 ID로 비교하는 것이 더 적합할 수 있습니다.
    // 만약 ID가 아닌 값으로만 동등성을 판단하고 싶다면 이전처럼 모든 필드를 비교하는 로직을 사용합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        // ID가 있는 엔티티라면 ID로만 비교하는 것이 일반적
        return Objects.equals(id, orderItem.id);
        // ID가 없는 값 객체라면 모든 필드로 비교:
        // return quantity == orderItem.quantity &&
        //        Objects.equals(productId, orderItem.productId) &&
        //        Objects.equals(productName, orderItem.productName) &&
        //        Objects.equals(price, orderItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // ID가 있다면 ID로 해시 코드 생성
        // ID가 없다면 모든 필드로 해시 코드 생성:
        // return Objects.hash(productId, productName, price, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
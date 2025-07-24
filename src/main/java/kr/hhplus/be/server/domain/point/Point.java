package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.exception.BusinessException;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    protected Point() {
    }

    public Point(Long userId, Long amount) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("사용자 ID는 0보다 커야 합니다.");
        }
        if (amount < 0) { // 초기 amount는 0일 수 있으므로 0 이상으로 설정
            throw new IllegalArgumentException("초기 포인트는 음수일 수 없습니다.");
        }
        this.userId = userId;
        this.amount = amount;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAmount() {
        return amount;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    // 포인트 충전
    public void charge(Long chargeAmount) {
        if (chargeAmount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.amount += chargeAmount;
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    // 포인트 사용
    public void use(Long useAmount) {
        if (useAmount <= 0) {
            throw new BusinessException("사용 금액은 0보다 커야 합니다.");
        }
        if (this.amount < useAmount) {
            throw new BusinessException("포인트 잔액이 부족합니다.");
        }
        this.amount -= useAmount;
        this.lastUpdatedAt = LocalDateTime.now();
    }    
}
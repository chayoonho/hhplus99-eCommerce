package kr.hhplus.be.server.application.point;

import java.time.LocalDateTime;

// 포인트 작업 결과
public class PointResult {
    public static class PointInfo {
        private final Long userId;
        private final Long amount;
        private final LocalDateTime lastUpdatedAt;

        public PointInfo(Long userId, Long amount, LocalDateTime lastUpdatedAt) {
            this.userId = userId;
            this.amount = amount;
            this.lastUpdatedAt = lastUpdatedAt;
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
    }
}
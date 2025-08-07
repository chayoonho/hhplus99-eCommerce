package kr.hhplus.be.server.interfaces.point;

import java.time.LocalDateTime;

public class PointResponse {

    public static class PointInfoResponse {
        private final Long userId;
        private final Long amount;
        private final LocalDateTime lastUpdatedAt;

        public PointInfoResponse(Long userId, Long amount, LocalDateTime lastUpdatedAt) {
            this.userId = userId;
            this.amount = amount;
            this.lastUpdatedAt = lastUpdatedAt;
        }

        // Getter
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
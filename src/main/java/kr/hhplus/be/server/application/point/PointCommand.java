package kr.hhplus.be.server.application.point;

public class PointCommand {
    // 포인트 충전 요청
    public static class ChargePoint {
        private final Long userId;
        private final Long amount;

        public ChargePoint(Long userId, Long amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getAmount() {
            return amount;
        }
    }

    // 포인트 조회 요청
    public static class GetPoint {
        private final Long userId;

        public GetPoint(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }
    }

    // 포인트 사용 요청
    public static class UsePoint {
        private final Long userId;
        private final Long amount;

        public UsePoint(Long userId, Long amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getAmount() {
            return amount;
        }
    }
}
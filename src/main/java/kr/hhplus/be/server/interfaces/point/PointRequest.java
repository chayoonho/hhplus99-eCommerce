package kr.hhplus.be.server.interfaces.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PointRequest {

    // 포인트 충전 요청
    public static class ChargePointRequest {
        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @NotNull(message = "충전 금액은 필수입니다.")
        @Min(value = 1, message = "충전 금액은 1 이상이어야 합니다.")
        private Long amount;

        public ChargePointRequest() {}

        public ChargePointRequest(Long userId, Long amount) {
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

    // 포인트 사용 요청
    public static class UsePointRequest {
        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @NotNull(message = "사용 금액은 필수입니다.")
        @Min(value = 1, message = "사용 금액은 1 이상이어야 합니다.")
        private Long amount;

        public UsePointRequest() {}

        public UsePointRequest(Long userId, Long amount) {
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
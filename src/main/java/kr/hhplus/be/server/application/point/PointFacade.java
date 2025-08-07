package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import org.springframework.stereotype.Service;

@Service
public class PointFacade {

    private final PointService pointService;

    public PointFacade(PointService pointService) {
        this.pointService = pointService;
    }

    // 포인트 충전
    public PointResult.PointInfo chargePoint(PointCommand.ChargePoint command) {
        Point point = pointService.chargePoint(command.getUserId(), command.getAmount());
        return new PointResult.PointInfo(point.getUserId(), point.getAmount(), point.getLastUpdatedAt());
    }

    // 포인트 조회
    public PointResult.PointInfo getPoint(PointCommand.GetPoint command) {
        Point point = pointService.getPointByUserId(command.getUserId());
        return new PointResult.PointInfo(point.getUserId(), point.getAmount(), point.getLastUpdatedAt());
    }

    // 포인트 사용
    public PointResult.PointInfo usePoint(PointCommand.UsePoint command) {
        Point point = pointService.usePoint(command.getUserId(), command.getAmount());
        return new PointResult.PointInfo(point.getUserId(), point.getAmount(), point.getLastUpdatedAt());
    }
}
package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional
    public Point chargePoint(Long userId, Long amount) {
        // 1. 충전 금액 유효성 검사 (음수 또는 0 방지)
        if (amount <= 0) {
            throw new BusinessException("충전 금액은 0보다 커야 합니다.");
        }

        // 2. 해당 userId의 포인트 정보 조회
        Optional<Point> optionalPoint = pointRepository.findByUserId(userId);
        Point point;

        if (optionalPoint.isPresent()) {
            // 3. 기존 사용자의 경우: 기존 포인트에 충전 금액 추가
            point = optionalPoint.get();
            point.charge(amount); // Point 엔티티의 charge 메서드 호출 (amount 유효성 검사 포함)
        } else {
            // 4. 새로운 사용자의 경우: 새로운 Point 객체 생성
            point = new Point(userId, amount);
        }

        // 5. 변경된 또는 새로 생성된 Point 객체 저장
        return pointRepository.save(point);
    }

    @Transactional(readOnly = true)
    public Point getPointByUserId(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("사용자 ID " + userId + "에 해당하는 포인트를 찾을 수 없습니다."));
    }

    @Transactional
    public Point usePoint(Long userId, Long amount) {
        // 1. 사용 금액 유효성 검사 (음수 또는 0 방지)
        if (amount <= 0) {
            throw new BusinessException("사용 금액은 0보다 커야 합니다.");
        }

        // 2. 해당 userId의 포인트 정보 조회 (없으면 예외 발생)
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("사용자 ID " + userId + "에 해당하는 포인트를 찾을 수 없습니다."));

        // 3. Point 엔티티의 use 메서드 호출 (잔액 부족 검사 및 차감 로직 포함)
        point.use(amount);

        // 4. 변경된 Point 객체 저장
        return pointRepository.save(point);
    }
}
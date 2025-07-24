package kr.hhplus.be.server.infrastructure.point.repository; // 현재 경로

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl(PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public Point save(Point point) {
        return pointJpaRepository.save(point);
    }
}
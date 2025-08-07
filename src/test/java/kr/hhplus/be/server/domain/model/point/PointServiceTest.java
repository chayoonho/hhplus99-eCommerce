package kr.hhplus.be.server.domain.model.point;

import kr.hhplus.be.server.domain.exception.BusinessException; // 이 예외 클래스가 존재한다고 가정
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
    }

    @Test
    @DisplayName("새로운 사용자의 포인트를 충전한다.")
    void 새로운_사용자의_포이트를_충전() {
        // Given
        Long chargeAmount = 1000L;

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        when(pointRepository.save(any(Point.class)))
                .thenAnswer(invocation -> {
                    Point savedPoint = (Point) invocation.getArgument(0);
                    Point newPointWithId = new Point(savedPoint.getUserId(), savedPoint.getAmount());

                    return newPointWithId;
                });

        // When
        Point resultPoint = pointService.chargePoint(userId, chargeAmount);

        // Then
        assertThat(resultPoint).isNotNull();
        assertThat(resultPoint.getUserId()).isEqualTo(userId);
        assertThat(resultPoint.getAmount()).isEqualTo(chargeAmount);
        assertThat(resultPoint.getLastUpdatedAt()).isAfterOrEqualTo(LocalDateTime.now().minusSeconds(1));
    }

    @Test
    @DisplayName("기존 사용자의 포인트를 충전한다.")
    void 기존_사용자의_포인트를_충전() {
        // Given
        Long initialAmount = 500L;
        Long chargeAmount = 1000L;
        Point existingPoint = new Point(userId, initialAmount);

        // findByUserId 호출 시 기존 Point 객체 반환
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(existingPoint));
        // save 호출 시 어떤 Point 객체가 들어오든 그대로 반환
        when(pointRepository.save(any(Point.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Point resultPoint = pointService.chargePoint(userId, chargeAmount);

        // Then
        assertThat(resultPoint).isNotNull();
        assertThat(resultPoint.getUserId()).isEqualTo(userId);
        assertThat(resultPoint.getAmount()).isEqualTo(initialAmount + chargeAmount); // 잔액이 증가해야 함
        // lastUpdatedAt이 현재 시간과 가까운지 확인
        assertThat(resultPoint.getLastUpdatedAt()).isAfterOrEqualTo(LocalDateTime.now().minusSeconds(1));
    }

    @Test
    @DisplayName("음수 금액으로 포인트를 충전하면 예외가 발생한다.")
    void 음수_금액으로_포인트_충전_시_예외발생() {
        // Given
        Long chargeAmount = -100L;

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.chargePoint(userId, chargeAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("충전 금액은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("0 금액으로 포인트를 충전하면 예외가 발생한다.")
    void 충전금액이_0일_경우_예외_발생() {
        // Given
        Long chargeAmount = 0L;

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.chargePoint(userId, chargeAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("충전 금액은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 포인트를 조회하면 예외가 발생한다.")
    void 존재하지_않는_사용자가_조회하면_예외발생() {
        // Given
        // pointRepository.findByUserId 호출 시 Optional.empty() 반환하도록 Mock 설정
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.getPointByUserId(userId);
        });

        assertThat(exception.getMessage()).isEqualTo("사용자 ID " + userId + "에 해당하는 포인트를 찾을 수 없습니다.");
    }
    
    //** 포인트 사용

    @Test
    @DisplayName("사용자가 포인트를 성공적으로 사용한다.")
    void 포인트를_성공적으로_사용() {
        // Given
        Long initialAmount = 10000L;
        Long useAmount = 3000L;
        Point existingPoint = new Point(userId, initialAmount);

        // findByUserId 호출 시 기존 Point 객체 반환
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(existingPoint));
        // save 호출 시 어떤 Point 객체가 들어오든 그대로 반환
        when(pointRepository.save(any(Point.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Point resultPoint = pointService.usePoint(userId, useAmount);

        // Then
        assertThat(resultPoint).isNotNull();
        assertThat(resultPoint.getUserId()).isEqualTo(userId);
        assertThat(resultPoint.getAmount()).isEqualTo(initialAmount - useAmount); // 잔액이 차감되어야 함
        assertThat(resultPoint.getLastUpdatedAt()).isAfterOrEqualTo(LocalDateTime.now().minusSeconds(1));
    }

    @Test
    @DisplayName("포인트 잔액이 부족하면 사용에 실패하고 예외가 발생한다.")
    void 포인트_부족시_예외발생() {
        // Given
        Long initialAmount = 1000L;
        Long useAmount = 3000L; // 잔액보다 많은 금액
        Point existingPoint = new Point(userId, initialAmount);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(existingPoint));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.usePoint(userId, useAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("포인트 잔액이 부족합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 포인트를 사용하려고 하면 예외가 발생한다.")
    void 존재하지_않는_사용자가_포인트_사용_시_예외발생() {
        // Given
        Long useAmount = 1000L;

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.usePoint(userId, useAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("사용자 ID " + userId + "에 해당하는 포인트를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("음수 금액으로 포인트를 사용하려고 하면 예외가 발생한다.")
    void 음수_금액으로_포인트_사용() {
        // Given
        Long initialAmount = 5000L;
        Long useAmount = -100L; // 음수 금액
        Point existingPoint = new Point(userId, initialAmount);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(existingPoint));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.usePoint(userId, useAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("사용 금액은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("0 금액으로 포인트를 사용하려고 하면 예외가 발생한다.")
    void 사용_금액이_0일경우_포인트_사용() {
        // Given
        Long initialAmount = 5000L;
        Long useAmount = 0L; // 0 금액
        Point existingPoint = new Point(userId, initialAmount);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(existingPoint));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pointService.usePoint(userId, useAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("사용 금액은 0보다 커야 합니다.");
    }    
}
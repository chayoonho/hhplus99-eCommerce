package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // @Valid 어노테이션 사용을 위해 import

@RestController
@RequestMapping("/points") // 기본 경로 설정
public class PointController {

    private final PointFacade pointFacade;

    public PointController(PointFacade pointFacade) {
        this.pointFacade = pointFacade;
    }

    /**
     * 특정 사용자의 포인트 정보를 조회
     * GET /points/{userId}
     * @param userId 조회할 사용자 ID
     * @return 조회된 포인트 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<PointResponse.PointInfoResponse> getPoint(@PathVariable Long userId) {
        PointCommand.GetPoint command = new PointCommand.GetPoint(userId);
        PointResult.PointInfo result = pointFacade.getPoint(command);

        PointResponse.PointInfoResponse response = new PointResponse.PointInfoResponse(
                result.getUserId(),
                result.getAmount(),
                result.getLastUpdatedAt()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 사용자에게 포인트를 충전
     * POST /points/charge
     * @param request 충전 요청 본문 (userId, amount)
     * @return 충전 후 포인트 정보
     */
    @PostMapping("/charge")
    public ResponseEntity<PointResponse.PointInfoResponse> chargePoint(@Valid @RequestBody PointRequest.ChargePointRequest request) {
        // 1. PointRequest를 PointCommand로 변환
        PointCommand.ChargePoint command = new PointCommand.ChargePoint(
                request.getUserId(),
                request.getAmount()
        );

        // 2. PointFacade를 호출하여 비즈니스 로직 수행
        PointResult.PointInfo result = pointFacade.chargePoint(command);

        // 3. PointResult를 PointResponse로 변환 (애플리케이션 계층 -> 인터페이스 계층)
        PointResponse.PointInfoResponse response = new PointResponse.PointInfoResponse(
                result.getUserId(),
                result.getAmount(),
                result.getLastUpdatedAt()
        );

        // 4. HTTP 200 OK 상태 코드와 함께 응답 반환 (충전은 보통 OK로 처리)
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 특정 사용자가 포인트를 사용
     * POST /points/use
     * @param request 사용 요청 본문 (userId, amount)
     * @return 사용 후 포인트 정보
     */
    @PostMapping("/use")
    public ResponseEntity<PointResponse.PointInfoResponse> usePoint(@Valid @RequestBody PointRequest.UsePointRequest request) {
        // 1. PointRequest를 PointCommand로 변환
        PointCommand.UsePoint command = new PointCommand.UsePoint(
                request.getUserId(),
                request.getAmount()
        );

        // 2. PointFacade를 호출하여 비즈니스 로직 수행
        PointResult.PointInfo result = pointFacade.usePoint(command);

        // 3. PointResult를 PointResponse로 변환
        PointResponse.PointInfoResponse response = new PointResponse.PointInfoResponse(
                result.getUserId(),
                result.getAmount(),
                result.getLastUpdatedAt()
        );

        // 4. HTTP 200 OK 상태 코드와 함께 응답 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
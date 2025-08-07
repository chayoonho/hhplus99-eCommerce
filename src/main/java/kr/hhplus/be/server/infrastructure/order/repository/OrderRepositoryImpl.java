package kr.hhplus.be.server.infrastructure.order.repository;

import groovyjarjarantlr.collections.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    // Spring Data JPA가 자동으로 구현해주는 OrderJpaRepository 인터페이스를 주입받습니다.
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id);
    }

//    @Override
//    public List<Order> findByUserId(Long userId) {
//        return orderJpaRepository.findByUserId(userId);
//    }
}
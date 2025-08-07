package kr.hhplus.be.server.domain.order;

import groovyjarjarantlr.collections.List;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    //List<Order> findByUserId(Long userId);
}

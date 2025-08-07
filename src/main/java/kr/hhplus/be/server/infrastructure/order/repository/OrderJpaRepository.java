
package kr.hhplus.be.server.infrastructure.order.repository;

import groovyjarjarantlr.collections.List;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
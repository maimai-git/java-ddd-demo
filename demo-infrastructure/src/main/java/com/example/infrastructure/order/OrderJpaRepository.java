package com.example.infrastructure.order;

import com.example.infrastructure.order.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderPO, Long> {

    Optional<OrderPO> findByOrderNo(String orderNo);
}

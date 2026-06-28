package com.example.infrastructure.order;

import com.example.domain.order.model.Order;
import com.example.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        var po = OrderConverter.toPO(order);
        var saved = jpaRepository.save(po);
        return OrderConverter.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id).map(OrderConverter::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        return jpaRepository.findByOrderNo(orderNo).map(OrderConverter::toDomain);
    }

    @Override
    public java.util.List<Order> findAll() {
        return jpaRepository.findAll().stream().map(OrderConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

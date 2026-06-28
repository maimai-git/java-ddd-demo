package com.example.domain.order.repository;

import com.example.domain.order.model.Order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderNo(String orderNo);

    java.util.List<Order> findAll();

    void deleteById(Long id);
}

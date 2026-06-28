package com.example.domain.order.service;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.order.model.Order;
import com.example.domain.order.repository.OrderRepository;
import com.example.domain.shared.model.Address;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public OrderDomainService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(String orderNo, Address address) {
        Order order = Order.place(orderNo, address);
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public boolean isOrderNoDuplicate(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).isPresent();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));
        orderRepository.deleteById(id);
    }
}

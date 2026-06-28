package com.example.application.converter;

import com.example.application.dto.co.OrderCO;
import com.example.domain.order.model.Order;

public class OrderAppConverter {

    private OrderAppConverter() {}

    public static OrderCO toCO(Order order) {
        OrderCO co = new OrderCO();
        co.setId(order.getId());
        co.setOrderNo(order.getOrderNo());
        co.setStatus(order.getStatus().name());
        co.setCreatedAt(order.getCreatedAt());
        var total = order.totalAmount();
        if (total != null) {
            co.setAmount(total.amount().toPlainString());
            co.setCurrency(total.currency().getCurrencyCode());
        }
        return co;
    }
}

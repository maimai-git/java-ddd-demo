package com.example.infrastructure.order;

import com.example.domain.order.model.Order;
import com.example.domain.order.model.OrderItem;
import com.example.domain.shared.model.Address;
import com.example.domain.shared.model.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class OrderConverter {

    public static OrderPO toPO(Order order) {
        OrderPO po = new OrderPO();
        po.setId(order.getId());
        po.setOrderNo(order.getOrderNo());
        if (order.getAddress() != null) {
            po.setProvince(order.getAddress().province());
            po.setCity(order.getAddress().city());
            po.setDistrict(order.getAddress().district());
            po.setDetail(order.getAddress().detail());
        }
        Money total = order.totalAmount();
        po.setTotalAmount(total.amount());
        po.setCurrency(total.currency().getCurrencyCode());
        po.setStatus(OrderPO.OrderStatus.valueOf(order.getStatus().name()));
        po.setCreatedAt(order.getCreatedAt());
        po.setUpdatedAt(order.getUpdatedAt());

        for (OrderItem item : order.getItems()) {
            OrderItemPO itemPO = new OrderItemPO();
            itemPO.setId(item.getId());
            itemPO.setProductId(item.getProductId());
            itemPO.setProductName(item.getProductName());
            itemPO.setPrice(item.getPrice().amount());
            itemPO.setCurrency(item.getPrice().currency().getCurrencyCode());
            itemPO.setQuantity(item.getQuantity());
            po.addItem(itemPO);
        }
        return po;
    }

    public static Order toDomain(OrderPO po) {
        Address address = null;
        if (po.getProvince() != null || po.getCity() != null || po.getDistrict() != null || po.getDetail() != null) {
            address = new Address(po.getProvince(), po.getCity(), po.getDistrict(), po.getDetail());
        }
        Order order = Order.reconstitute(po.getId(), po.getOrderNo(), address,
                Order.OrderStatus.valueOf(po.getStatus().name()),
                po.getCreatedAt(), po.getUpdatedAt());

        for (OrderItemPO itemPO : po.getItems()) {
            Currency currency = itemPO.getCurrency() != null
                    ? Currency.getInstance(itemPO.getCurrency()) : Currency.getInstance("CNY");
            order.addItem(OrderItem.reconstitute(itemPO.getId(), itemPO.getProductId(),
                    itemPO.getProductName(), new Money(itemPO.getPrice(), currency), itemPO.getQuantity()));
        }
        return order;
    }
}

package com.example.domain.order.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.order.event.OrderPaidEvent;
import com.example.domain.shared.model.Address;
import com.example.domain.shared.model.BaseEntity;
import com.example.domain.shared.model.Money;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

public class Order extends BaseEntity {

    private String orderNo;
    private List<OrderItem> items = new ArrayList<>();
    private Address address;
    private OrderStatus status;

    public static Order place(String orderNo, Address address) {
        return new Order(orderNo, address);
    }

    /** 从持久层重建 */
    public static Order reconstitute(Long id, String orderNo, Address address, OrderStatus status,
                                     Instant createdAt, Instant updatedAt) {
        Order order = new Order(orderNo, address);
        order.setId(id);
        order.status = status;
        order.setCreatedAt(createdAt);
        order.setUpdatedAt(updatedAt);
        return order;
    }

    private Order(String orderNo, Address address) {
        if (orderNo == null || orderNo.isBlank()) {
            throw new BizException(ErrorCode.ORDER_NO_REQUIRED);
        }
        this.orderNo = orderNo;
        this.address = address;
        this.status = OrderStatus.NEW;
        var now = Instant.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    public void addItem(OrderItem item) {
        if (item != null) this.items.add(item);
    }

    public Money totalAmount() {
        if (items.isEmpty()) return Money.zero(Currency.getInstance("CNY"));
        return items.stream().map(OrderItem::totalPrice)
                .reduce(Money.zero(items.get(0).getPrice().currency()), Money::add);
    }

    public void pay() {
        if (this.status != OrderStatus.NEW) {
            throw new BizException(ErrorCode.ORDER_STATUS_INVALID, "当前状态: " + this.status);
        }
        this.status = OrderStatus.PAID;
        setUpdatedAt(Instant.now());
        registerEvent(new OrderPaidEvent(this.orderNo, this.totalAmount(), Instant.now()));
    }

    public String getOrderNo() { return orderNo; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public Address getAddress() { return address; }
    public OrderStatus getStatus() { return status; }

    public enum OrderStatus { NEW, PAID, CANCELLED }
}

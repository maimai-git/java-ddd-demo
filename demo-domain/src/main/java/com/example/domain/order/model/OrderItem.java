package com.example.domain.order.model;

import com.example.domain.shared.model.Money;

public class OrderItem {

    private Long id;
    private Long productId;
    private String productName;
    private Money price;
    private int quantity;

    public static OrderItem of(Long productId, String productName, Money price, int quantity) {
        return new OrderItem(productId, productName, price, quantity);
    }

    /** 从持久层重建 */
    public static OrderItem reconstitute(Long id, Long productId, String productName, Money price, int quantity) {
        OrderItem item = new OrderItem(productId, productName, price, quantity);
        item.id = id;
        return item;
    }

    private OrderItem(Long productId, String productName, Money price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public Money totalPrice() { return price.multiply(quantity); }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Money getPrice() { return price; }
    public int getQuantity() { return quantity; }
}

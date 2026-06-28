package com.example.interfaces.controller;

import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.dto.co.OrderCO;
import com.example.application.dto.command.CreateOrderRequest;
import com.example.application.service.OrderAppService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderAppService orderAppService;

    public OrderController(OrderAppService orderAppService) {
        this.orderAppService = orderAppService;
    }

    @PostMapping
    public SingleResponse<OrderCO> create(@RequestBody @Valid CreateOrderRequest request) {
        return orderAppService.createOrder(request);
    }

    @GetMapping("/{id}")
    public SingleResponse<OrderCO> get(@PathVariable Long id) {
        return orderAppService.getById(id);
    }

    @PostMapping("/{id}/pay")
    public Response pay(@PathVariable Long id) {
        return orderAppService.payOrder(id);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return orderAppService.delete(id);
    }
}

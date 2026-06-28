package com.example.application.service;

import com.example.application.common.DomainEvents;
import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.converter.OrderAppConverter;
import com.example.application.dto.co.OrderCO;
import com.example.application.dto.command.CreateOrderRequest;
import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.common.log.BizLog;
import com.example.domain.order.model.Order;
import com.example.domain.order.service.OrderDomainService;
import com.example.domain.shared.model.Address;
import com.example.domain.shared.external.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderAppService {

    private final OrderDomainService orderDomainService;
    private final EventPublisher eventPublisher;

    public OrderAppService(OrderDomainService orderDomainService, EventPublisher eventPublisher) {
        this.orderDomainService = orderDomainService;
        this.eventPublisher = eventPublisher;
    }

    @BizLog("创建订单")
    @Transactional
    public SingleResponse<OrderCO> createOrder(CreateOrderRequest request) {
        if (orderDomainService.isOrderNoDuplicate(request.getOrderNo())) {
            throw new BizException(ErrorCode.ORDER_NO_DUPLICATE);
        }
        Address address = new Address(request.getProvince(), request.getCity(),
                request.getDistrict(), request.getDetail());
        Order order = orderDomainService.create(request.getOrderNo(), address);
        return SingleResponse.of(OrderAppConverter.toCO(order));
    }

    @BizLog("支付订单")
    @Transactional
    public Response payOrder(Long orderId) {
        Order order = orderDomainService.findById(orderId).orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));
        order.pay();
        DomainEvents.saveAndPublish(order, orderDomainService::save, eventPublisher);
        return Response.buildSuccess();
    }

    /* ===== 读操作 ===== */

    @Transactional(readOnly = true)
    public SingleResponse<OrderCO> getById(Long id) {
        Order order = orderDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));
        return SingleResponse.of(OrderAppConverter.toCO(order));
    }

    @BizLog("删除订单")
    @Transactional
    public Response delete(Long id) {
        orderDomainService.delete(id);
        return Response.buildSuccess();
    }
}

package org.isd.shop.services.orderDetail;

import org.isd.shop.entities.Order;

import java.util.List;

public interface IOrderDetailService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Long userId);

    List<Order> getOrderByUserId(Long userId);

}

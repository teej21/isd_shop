package org.isd.shop.services.order;

import org.isd.shop.entities.Order;
import org.isd.shop.enums.Enums;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Long userId);

    List<Order> getOrderByUserId(Long userId);

    List<Order> getOrderByStatus(String status);


    Order findInitOrderByUserId(Long userId);
}


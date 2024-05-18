package org.isd.shop.services.order;

import org.isd.shop.entities.Order;
import org.isd.shop.enums.Enums;

import java.util.List;

public interface IOrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Long userId);

    List<Order> getOrderByUserId(Long userId);

    List<Order> getOrderByStatusByAdmin(String status);

    List<Order> getOrderByUserIdAndStatus(Long userId, Enums.OrderStatus status);

    Order confirmOrder(String token, Long userId, String name, String address, String phone, String note);

    Order updateEmployee(Long employeeId, Long orderId);

    List<Order> getAssignedOrders(String token, Long employeeId);

    Order updateOrders( Long orderId, String name, String address, String phone, String note, String status);
}


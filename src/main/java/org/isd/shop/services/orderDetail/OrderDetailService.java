package org.isd.shop.services.orderDetail;

import lombok.RequiredArgsConstructor;
import org.isd.shop.entities.Order;
import org.isd.shop.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        Optional<List<Order>> orders = Optional.of(orderRepository.findAll());
        return orders.get();
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
        }
        return order.get();
    }


}

package org.isd.shop.services.orderDetail;

import lombok.RequiredArgsConstructor;
import org.isd.shop.entities.Order;
import org.isd.shop.entities.User;
import org.isd.shop.repositories.OrderRepository;
import org.isd.shop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

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

    @Override
    public Order createOrder(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Order order = Order.builder().user(user.get()).build();
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Optional<List<Order>> orders = orderRepository.findByUser(user.get());
        return orders.get();
    }


}

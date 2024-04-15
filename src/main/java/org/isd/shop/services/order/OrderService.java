package org.isd.shop.services.order;

import lombok.RequiredArgsConstructor;
import org.isd.shop.entities.Order;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.OrderRepository;
import org.isd.shop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        Optional<List<Order>> orders = Optional.of(orderRepository.findAll());
        return orders.get();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Không Tìm Thấy Đơn Hàng"));
    }

    @Override
    public Order createOrder(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Order order = Order.builder()
                .user(user.get())
                .status(Enums.OrderStatus.INIT)
                .build();
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

    @Override
    public List<Order> getOrderByStatus(String status) {
        Optional<List<Order>> orders = orderRepository.findByStatus(status);
        if (orders.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
        }
        return orders.get();
    }

    @Override
    public Order findInitOrderByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Optional<Order> order = orderRepository.findByUserAndStatus(user.get(), Enums.OrderStatus.valueOf("INIT"));
        return order.orElseGet(() -> createOrder(userId));
    }


}

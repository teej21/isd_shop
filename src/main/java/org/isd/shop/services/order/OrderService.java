package org.isd.shop.services.order;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
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
    private final JwtTokenUtil jwtTokenUtil;
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
        Optional<List<Order>> orders = orderRepository.findByUserAndStatus(user.get(), Enums.OrderStatus.INIT);
        if (orders.isPresent() & orders.get().size() != 0) {
            throw new RuntimeException("Người Dùng Có Đơn Hàng Chưa Xử Lý");
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
    public List<Order> getOrderByStatusByAdmin(String status) {
        Optional<List<Order>> orders = orderRepository.findByStatus(Enums.OrderStatus.valueOf(status));
        if (orders.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
        }
        return orders.get();
    }

    @Override
    public List<Order> getOrderByUserIdAndStatus(Long userId, Enums.OrderStatus status) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Optional<List<Order>> orders = orderRepository.findByUserAndStatus(user.get(), status);
        if (orders.isEmpty() || orders.get().size() == 0) {
            throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
        }
        return orders.get();
    }

    @Override
    public Order confirmOrder(
        String token,
        Long userId,
        String name,
        String address,
        String phone,
        String note) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Optional<List<Order>> orders = orderRepository.findByUserAndStatus(user.get(), Enums.OrderStatus.INIT);
        if (orders.isEmpty() || orders.get().isEmpty()) {
            throw new RuntimeException("Giỏ Hàng Trống");
        }

        Order order = orders.get().get(0);
        order.setStatus(Enums.OrderStatus.PENDING);
        order.setName(name);
        order.setAddress(address);
        order.setPhoneNumber(phone);
        order.setNote(note);
        return orderRepository.save(order);
    }

    @Override
    public Order updateEmployee(Long employeeId, Long orderId) {
        Optional<User> employee = userRepository.findById(employeeId);

        if (employee.isEmpty()) {
            throw new RuntimeException("ID Nhân Viên Không Tồn Tại");
        }


        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new RuntimeException("ID Đơn Hàng Không Tồn Tại");
        }
        order.get().setEmployee(employee.get());
        order.get().setStatus(Enums.OrderStatus.ASSIGNED);
        return orderRepository.save(order.get());

    }

    @Override
    public List<Order> getAssignedOrder(String token, Long employeeId) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new RuntimeException("Token Không Hợp Lệ");
            }
            String extractedToken = token.substring(7);
            if (!jwtTokenUtil.isValidUserIdByToken(extractedToken, employeeId)) {
                throw new RuntimeException("Bạn Chỉ Có Thể Xem Đơn Hàng Được Giao");
            }
            Optional<User> employee = userRepository.findById(employeeId);
            if (employee.isEmpty()) {
                throw new RuntimeException("ID Nhân Viên Không Tồn Tại");
            }
            Optional<List<Order>> orders = orderRepository.findByEmployee(employee.get());
            if (orders.isEmpty() || orders.get().isEmpty()) {
                throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
            }
            return orders.get();
        } catch (Exception e) {
            throw new RuntimeException("Không Tìm Thấy Đơn Hàng");
        }
    }


}

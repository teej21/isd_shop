package org.isd.shop.services.order;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.entities.Order;
import org.isd.shop.entities.OrderDetail;
import org.isd.shop.entities.Product;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.OrderDetailReposity;
import org.isd.shop.repositories.OrderRepository;
import org.isd.shop.repositories.ProductRepository;
import org.isd.shop.repositories.UserRepository;
import org.isd.shop.responses.common.ResultResponse;
import org.isd.shop.services.orderDetail.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final JwtTokenUtil jwtTokenUtil;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailReposity orderDetailRepository;
    private final ProductRepository productRepository;

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
        Optional<List<Order>> orders = orderRepository.findByUserAndStatus(user.get(), Enums.OrderStatus.INIT);
        //return order has status INIT
        return orders.orElseGet(List::of);

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
        if (orders.isEmpty() || orders.get().size() == 0) {
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
    public List<Order> getAssignedOrders(String token, Long employeeId) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new RuntimeException("Token Không Hợp Lệ");
            }
            String extractedToken = token.substring(7);
            if (!isValidUserIdByToken(extractedToken, employeeId) && !isAdminOrManager(extractedToken)) {
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
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResultResponse updateOrders(Long orderId, String name, String address, String phone, String note, String status) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isEmpty()) {
                throw new RuntimeException("ID Đơn Hàng Không Tồn Tại");
            }
            Order order = orderOptional.get();
            order.setName(name);
            order.setAddress(address);
            order.setPhoneNumber(phone);
            order.setNote(note);
            //if status change, call method update product
            if (!order.getStatus().equals(Enums.OrderStatus.valueOf(status))) {
                order.setStatus(Enums.OrderStatus.valueOf(status));
                updateProductStatus(orderId, Enums.OrderStatus.valueOf(status));
            }
            return new ResultResponse("Cập Nhật Đơn Hàng Thành Công");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isValidUserIdByToken(String token, Long userId) {
        String extractedUserId = jwtTokenUtil.extractClaim(token, "userId");
        return extractedUserId.equals(userId.toString());
    }

    private boolean isAdminOrManager(String token) {
        String extractedUserId = jwtTokenUtil.extractClaim(token, "userId");
        Optional<User> user = userRepository.findById(Long.parseLong(extractedUserId));
        return user.filter(value -> value.getRole().equals(Enums.Role.ADMIN) || value.getRole().equals(Enums.Role.MANAGER)).isPresent();
    }

    public void updateProductStatus(Long orderId, Enums.OrderStatus orderStatus) {
        Optional<List<OrderDetail>> orderDetailListOptional = orderDetailRepository.findByOrder_Id(orderId);
        if (orderDetailListOptional.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Order Detail");
        }

        List<OrderDetail> orderDetailList = orderDetailListOptional.get();
        for (OrderDetail orderDetail : orderDetailList) {
            Product product = orderDetail.getProduct();
            switch (orderStatus) {
                case INIT, PENDING, SHIPPING, ASSIGNED:
                    product.setStatus(Enums.ProductStatus.ORDERED);
                    break;
                case DELIVERED:
                    product.setStatus(Enums.ProductStatus.STOCKOUT);
                    break;
                case CANCELLED:
                    product.setStatus(Enums.ProductStatus.AVAILABLE);
                    break;
                default:
                    throw new RuntimeException("Trạng Thái Đơn Hàng Không Hợp Lệ");
            }
            productRepository.save(product);
        }
    }
}

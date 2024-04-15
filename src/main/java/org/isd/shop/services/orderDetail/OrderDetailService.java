package org.isd.shop.services.orderDetail;

import lombok.RequiredArgsConstructor;
import org.isd.shop.dtos.OrderDetailDTO;
import org.isd.shop.entities.Order;
import org.isd.shop.entities.OrderDetail;
import org.isd.shop.entities.Product;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.OrderDetailReposity;
import org.isd.shop.repositories.OrderRepository;
import org.isd.shop.repositories.UserRepository;
import org.isd.shop.responses.OrderDetail.OrderDetailResponse;
import org.isd.shop.services.order.IOrderService;
import org.isd.shop.services.product.IProductService;
import org.isd.shop.services.user.IUserService;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailReposity orderDetailRepository;
    private final UserRepository userRepository;
    private final IOrderService orderService;
    private final IProductService productService;
    private final IUserService userService;


    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        Optional<List<OrderDetail>> orderDetails = Optional.of(orderDetailRepository.findByOrder(order));
        return orderDetails.get().stream().map(OrderDetailResponse::fromOrderDetail).toList();
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long id) {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (orderDetail.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Chi Tiết Đơn Hàng");
        }
        return OrderDetailResponse.fromOrderDetail(orderDetail.get());
    }

    @Override
    public OrderDetailResponse createOrderDetail(Long userId, Long productId) {
        if (!userService.checkUserExist(userId)) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }

        Product product = productService.getProductById(productId);
        Order order = orderService.findInitOrderByUserId(userId);
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .build();

        return OrderDetailResponse.fromOrderDetail(orderDetailRepository.save(orderDetail));
    }


}

package org.isd.shop.services.orderDetail;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.entities.Order;
import org.isd.shop.entities.OrderDetail;
import org.isd.shop.entities.Product;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.OrderDetailReposity;
import org.isd.shop.responses.OrderDetail.OrderDetailResponse;
import org.isd.shop.services.order.IOrderService;
import org.isd.shop.services.product.IProductService;
import org.isd.shop.services.user.IUserService;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailReposity orderDetailRepository;
    private final IOrderService orderService;
    private final IProductService productService;
    private final IUserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public OrderDetailResponse createOrderDetail(String authHeader, Long userId, Long productId) {

        String token = authHeader.substring(7).trim();

        if (!jwtTokenUtil.isValidUserIdByToken(token, userId)) {
            throw new RuntimeException("Bạn Không Thể Thêm Sản Phẩm Cho Người Dùng Khác");
        }
        if (!userService.checkUserExist(userId)) {
            throw new RuntimeException("ID Người Dùng Không Tồn Tại");
        }
        Order order;
        try {
            order = orderService.getOrderByUserIdAndStatus(userId, Enums.OrderStatus.INIT).get(0);
        } catch (Exception e) {
            order = orderService.createOrder(userId);
        }

        Product product = productService.getProductById(productId);
        Enums.ProductStatus productStatus = product.getStatus();
        if (productStatus == Enums.ProductStatus.ORDERED) {
            throw new RuntimeException("Sản Phẩm Hiện Đã Có Người Đặt");
        }

        if (productStatus == Enums.ProductStatus.STOCKOUT) {
            throw new RuntimeException("Sản Phẩm Đã Được Bán");
        }

        product.setStatus(Enums.ProductStatus.ORDERED);

        OrderDetail orderDetail = OrderDetail.builder()
            .order(order)
            .product(product)
            .build();

        return OrderDetailResponse.fromOrderDetail(orderDetailRepository.save(orderDetail));
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("Không Tìm Thấy Order Detail"));
        return OrderDetailResponse.fromOrderDetail(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        return orderDetails.stream().map(OrderDetailResponse::fromOrderDetail).toList();
    }


}

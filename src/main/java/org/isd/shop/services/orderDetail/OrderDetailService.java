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
import org.isd.shop.repositories.ProductRepository;
import org.isd.shop.repositories.UserRepository;
import org.isd.shop.responses.OrderDetail.OrderDetailResponse;
import org.isd.shop.responses.common.ResultResponse;
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
    private final ProductRepository productRepository;
    private final IOrderService orderService;
    private final IProductService productService;
    private final IUserService userService;


    @Override
    public OrderDetailResponse createOrderDetail(Long userId, Long productId) {
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

    @Override
    public ResultResponse deleteOrderDetail(Long id) {
        //get order detail
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (orderDetail.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Order Detail");
        }
        //set product status to available
        Product product = orderDetail.get().getProduct();
        product.setStatus(Enums.ProductStatus.AVAILABLE);
        //save product
        productRepository.save(product);
        //delete order detail
        orderDetailRepository.delete(orderDetail.get());
        return new ResultResponse("Xóa Thành Công");
    }


}

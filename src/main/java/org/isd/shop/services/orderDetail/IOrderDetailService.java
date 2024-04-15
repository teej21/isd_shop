package org.isd.shop.services.orderDetail;

import org.isd.shop.dtos.OrderDetailDTO;
import org.isd.shop.entities.OrderDetail;
import org.isd.shop.responses.OrderDetail.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {


    List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId);

    OrderDetailResponse getOrderDetailById(Long id);


    OrderDetailResponse createOrderDetail(Long userId, Long productId);
}

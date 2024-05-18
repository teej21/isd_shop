package org.isd.shop.services.orderDetail;

import org.isd.shop.enums.Enums;
import org.isd.shop.responses.OrderDetail.OrderDetailResponse;
import org.isd.shop.responses.common.ResultResponse;

import java.util.List;

public interface IOrderDetailService {

    OrderDetailResponse createOrderDetail(Long userId, Long productId);

    OrderDetailResponse getOrderDetailById(Long id);

    List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId);

    ResultResponse deleteOrderDetail(Long id);

}

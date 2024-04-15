package org.isd.shop.responses.OrderDetail;

import lombok.*;
import org.isd.shop.entities.OrderDetail;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
public class OrderDetailResponse {

    private Long orderdetail_id;
    private Long order_id;
    private Long product_id;
    private String product_name;
    private Double product_price;
    private String product_thumbnail;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .orderdetail_id(orderDetail.getId())
                .order_id(orderDetail.getOrder().getId())
                .product_id(orderDetail.getProduct().getId())
                .product_name(orderDetail.getProduct().getName())
                .product_price(orderDetail.getProduct().getPrice())
                .product_thumbnail(orderDetail.getProduct().getThumbnail())
                .build();
    }
}

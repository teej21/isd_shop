package org.isd.shop.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UpdateOrderInfoDTO {
    private Long orderId;
    private String name;
    private String address;
    private String phone;
    private String note;
    private String status;
}

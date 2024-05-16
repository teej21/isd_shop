package org.isd.shop.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UpdateOrderEmployeeDTO {
    private Long employeeId;
    private Long orderId;
}

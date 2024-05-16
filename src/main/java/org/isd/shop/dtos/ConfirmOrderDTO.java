package org.isd.shop.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ConfirmOrderDTO {


    @NotNull(message = "Tên không được để trống")
    private String name;

    @NotNull(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Số điện thoại không được để trống")
    private String phoneNumber;


    private String note;
}

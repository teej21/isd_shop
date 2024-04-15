package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class OrderDetailDTO {

    @NotNull(message = "ID sản phẩm không được để trống")
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("user_id")
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;
}

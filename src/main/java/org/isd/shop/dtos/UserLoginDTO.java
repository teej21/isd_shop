package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
//@Setter
public class UserLoginDTO {

    @NotEmpty(message = "Email hoặc số điện thoại không được để trống")
    @JsonProperty("username")
    private String username;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @JsonProperty("password")
    private String password;


}

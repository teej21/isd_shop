package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.isd.shop.enums.Enums;

import java.util.Date;

@Data
@Getter
//@Setter
public class UserDTO {
    @NotEmpty(message = "Tên không được để trống")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Email không được để trống")
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Giới tính không được để trống")
    @JsonProperty("gender")
    private String gender;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @JsonProperty("password")
    private String password;

    @NotEmpty(message = "Vai trò không được để trống")
    @JsonProperty("role")
    private String role;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;


    private String address;


}

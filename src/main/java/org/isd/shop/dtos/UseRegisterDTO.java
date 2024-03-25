package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UseRegisterDTO {

    @JsonProperty("full_name")
    private String fullName;


    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be blank")
    private String password;


    @JsonProperty("gender")
    private String gender;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;
}

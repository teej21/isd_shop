package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("username")
    @NotBlank(message = "Email or phone number is required")
    private String username;


    @JsonProperty("password")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("role_id")
    private Long roleId;


}

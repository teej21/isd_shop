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
public class UserDTO {
    /**
     * {
     *     "first_name": "John",
     *     "last_name": "Doe",
     *     "email": "john@gmail.com",
     *     "address": "123, Main Street, New York",
     *     "password": "password",
     *     "retype_password": "password",
     *     "date_of_birth": "1990-01-01",
     *     "role_id": 4
     * }
     */
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private String email;

    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;



    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;
}

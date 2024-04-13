package org.isd.shop.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.isd.shop.entities.User;

import java.util.Date;

@Data
@Builder
public class UserResponse {

    private Long id;
    @JsonProperty("full_name")
    private String fullName;

    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private String gender;
    private String role;
    @JsonProperty("is_active")
    private boolean isActive;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .isActive(user.isActive())
                .build();
    }
}

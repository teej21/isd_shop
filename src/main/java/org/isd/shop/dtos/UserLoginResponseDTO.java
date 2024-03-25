package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {
    @JsonProperty("token")
    private String token;

    @JsonProperty("role_id")
    private Long roleId;


    @JsonProperty("full_name")
    private String fullName;
}

package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@Getter
public class CategoryDTO {
    @JsonProperty("name")
    @NotEmpty(message = "Danh Mục Không Được Để Trống")
    public String name;

    @JsonProperty("description")
    @NotEmpty(message = "Mô Tả Không Được Để Trống")
    public String description;
}

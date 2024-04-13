package org.isd.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
public class ProductDTO {

    @NotEmpty(message = "Tên sản phẩm không được để trống")
    public String name;

    @NotEmpty(message = "Mô tả sản phẩm không được để trống")
    public String description;


    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", message = "Giá sản phẩm phải lớn hơn 0", inclusive = false)
    public Double price;


    @NotNull(message = "Ảnh sản phẩm không được để trống")
    private MultipartFile thumbnailImage;


    @NotNull(message = "Danh mục sản phẩm không được để trống")
    public Long categoryId;


    @NotNull(message = "Chất liệu không được để trống")
    public String material;


    @NotNull(message = "Chiều rộng không được để trống")
    @DecimalMin(value = "0", inclusive = false, message = "Chiều rộng phải lớn hơn 0")
    public Double width;


    @NotNull(message = "Chiều cao không được để trống")
    @DecimalMin(value = "0", inclusive = false, message = "Chiều cao phải lớn hơn 0")
    public Double height;


    @NotNull(message = "Năm sản xuất không được để trống")
    public Integer publishYear;
}


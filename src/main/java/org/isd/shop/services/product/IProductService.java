package org.isd.shop.services.product;

import org.isd.shop.dtos.ProductDTO;
import org.isd.shop.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();


    Product getProductById(Long id);

    Product addProduct(String name, String description, double price, Long categoryId, double width, double height, String material, int publishYear, MultipartFile thumbnailImage);

    Product updateProduct(Long id, String name, String description, double price, Long categoryId, double width, double height, String material, int publishYear, MultipartFile thumbnailImage);

    void deleteProduct(Long id);
}

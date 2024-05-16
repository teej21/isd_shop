package org.isd.shop.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.dtos.ProductDTO;
import org.isd.shop.services.product.IProductService;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", exposedHeaders = "Authorization")
public class ProductController {
    private final IProductService productService;
    private final Utils utils;


    @GetMapping("")
    private ResponseEntity<?> getAllProducts() {
        try {
            return ResponseEntity.ok(productService.getAllProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
            } else {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    private ResponseEntity<?> addProduct(@ModelAttribute @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(utils.handleBindingResultError(bindingResult));
        }
        try {

            return ResponseEntity.ok(productService.addProduct(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getCategoryId(),
                productDTO.getWidth(),
                productDTO.getHeight(),
                productDTO.getMaterial(),
                productDTO.getPublishYear(),
                productDTO.getThumbnailImage(),
                productDTO.getStatus()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/category={id}")
    private ResponseEntity<?> getProductsByCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductsByCategory(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateProduct(@PathVariable Long id, @ModelAttribute @Valid ProductDTO
        productDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            return ResponseEntity.ok(productService.updateProduct(
                id,
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getCategoryId(),
                productDTO.getWidth(),
                productDTO.getHeight(),
                productDTO.getMaterial(),
                productDTO.getPublishYear(),
                productDTO.getThumbnailImage(),
                productDTO.getStatus()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return utils.ResultResponse("Xóa sản phẩm thành công");
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }


}

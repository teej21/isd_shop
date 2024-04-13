package org.isd.shop.services.product;

import lombok.RequiredArgsConstructor;
import org.isd.shop.entities.Category;
import org.isd.shop.entities.Product;
import org.isd.shop.repositories.CategoryRepository;
import org.isd.shop.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProducts() {
        Optional<List<Product>> products = Optional.of(productRepository.findAll());
        return products.get();
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Sản Phẩm");
        }
        return product.get();
    }

    @Override
    public Product addProduct(String name, String description, double price, Long categoryId, double width, double height, String material, int publishYear, MultipartFile thumbnailImage) {

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Danh Mục");
        }
        String thumbnailImageUrl;
        try {
            thumbnailImageUrl = uploadImage(thumbnailImage);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tải ảnh lên");
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .category(category.get())
                .thumbnail(thumbnailImageUrl)
                .height(height)
                .width(width)
                .material(material)
                .publishYear(publishYear)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, String name, String description, double price, Long categoryId, double width, double height, String material, int publishYear, MultipartFile thumbnailImage) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Sản Phẩm");
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Danh Mục");
        }
        Product productToUpdate = product.get();

        if (thumbnailImage != null) {
            String thumbnailImageUrl;
            try {
                thumbnailImageUrl = updateImageByName(thumbnailImage, productToUpdate.getThumbnail());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải ảnh lên");
            }
            productToUpdate.setThumbnail(thumbnailImageUrl);
        }

        productToUpdate.setName(name);
        productToUpdate.setDescription(description);
        productToUpdate.setPrice(price);
        productToUpdate.setCategory(category.get());
        productToUpdate.setHeight(height);
        productToUpdate.setWidth(width);
        productToUpdate.setMaterial(material);
        productToUpdate.setPublishYear(publishYear);

        return productRepository.save(productToUpdate);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Sản Phẩm");
        }
        productRepository.delete(product.get());
    }

    private String updateImageByName(MultipartFile image, String oldImageName) throws IOException {
        String newImageName = uploadImage(image);
        Path oldImage = Paths.get("uploads").resolve(oldImageName);
        Files.deleteIfExists(oldImage);
        return newImageName;
    }

    private String uploadImage(MultipartFile image) throws IOException {
//        check max size is 10 mb
        if (image.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Kích thước ảnh tối đa là 10MB");
        }
//        check file type is image
        if (!image.getContentType().startsWith("image")) {
            throw new RuntimeException("File không phải là ảnh");
        }
//        change name to unique
        String newUniqueName = "image" + System.currentTimeMillis() + image.getOriginalFilename();

        // save image to server
        Path uploadDir = Paths.get("uploads");
        if (Files.notExists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        Path newFileDestination = uploadDir.resolve(newUniqueName);
        Files.copy(image.getInputStream(), newFileDestination, StandardCopyOption.REPLACE_EXISTING);

        return newUniqueName;
    }
}




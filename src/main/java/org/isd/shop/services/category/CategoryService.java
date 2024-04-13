package org.isd.shop.services.category;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.isd.shop.entities.Category;
import org.isd.shop.repositories.CategoryRepository;
import org.isd.shop.responses.common.ResultResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        Optional<List<Category>> categories = Optional.of(categoryRepository.findAll());
        return categories.get();
    }

    @Override
    public Category addCategory(String name, String description) {
        Category category = Category.builder().name(name).description(description).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, String name, String description) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Danh Mục");
        }
        category.get().setName(name);
        category.get().setDescription(description);
        return categoryRepository.save(category.get());
    }

    @Override
    public ResultResponse deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Danh Mục");
        }
        categoryRepository.delete(category.get());

        return new ResultResponse("Xóa Danh Mục Thành Công");
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new RuntimeException("Không Tìm Thấy Danh Mục");
        }
        return category.get();
    }
}

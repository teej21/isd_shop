package org.isd.shop.services.category;

import org.isd.shop.entities.Category;
import org.isd.shop.responses.common.ResultResponse;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category addCategory(String name, String description);

    Category updateCategory(Long id, String name, String description);

    ResultResponse deleteCategory(Long id);

    Category getCategoryById(Long id);
}

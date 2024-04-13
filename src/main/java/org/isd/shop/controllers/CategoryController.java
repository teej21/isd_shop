package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.dtos.CategoryDTO;
import org.isd.shop.services.category.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final Utils utils;


    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.getCategoryById(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addCategory(@RequestBody @Validated CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(utils.handleBindingResultError(bindingResult));
        }
        String name = categoryDTO.getName();
        String description = categoryDTO.getDescription();
        try {
            return ResponseEntity.ok(categoryService.addCategory(name, description));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException(utils.handleBindingResultError(result));
        }
        String name = categoryDTO.getName();
        String description = categoryDTO.getDescription();
        try {
            return ResponseEntity.ok(categoryService.updateCategory(id, name, description));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok( categoryService.deleteCategory(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

}

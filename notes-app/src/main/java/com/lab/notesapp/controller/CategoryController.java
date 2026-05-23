package com.lab.notesapp.controller;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Category;
import com.lab.notesapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category Controller")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(method = "GET", summary = "Get all categories", description = "Get all categories from the Database")
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", summary = "Get category by id", description = "Get a category by its id")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(name = "id", description = "ID of the category to retrieve") @PathVariable Integer id) {

        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(method = "POST", summary = "Create a category", description = "Create new category")
    public ResponseEntity<Category> createCategory(
            @Parameter(name = "Category", description = "Category to create") @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(method = "PUT", summary = "Update a category", description = "Update an existing category by id")
    public ResponseEntity<Category> updateCategory(
            @Parameter(name = "id", description = "ID of the category to update") @PathVariable Integer id,
            @Parameter(name = "Category", description = "Updated category data") @RequestBody Category category) {

        Category updatedCategory = categoryService.updateCategory(category, id);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", summary = "Delete a category", description = "Delete a category by id")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(name = "id", description = "ID of the category to delete") @PathVariable Integer id) {

        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

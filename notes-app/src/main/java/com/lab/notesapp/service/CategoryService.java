package com.lab.notesapp.service;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Category;
import com.lab.notesapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Integer id) throws ObjectNotFoundException {
        Optional<Category> existingCategoryOpt = this.getCategoryById(id);
        if (existingCategoryOpt.isPresent()) {
            Category existingCategory = existingCategoryOpt.get();
            existingCategory.setName(category.getName()); // Używaj istniejącego obiektu
            return this.categoryRepository.save(existingCategory); // Zapisz zmiany w istniejącym obiekcie
        }
        throw new ObjectNotFoundException(id); // Rzuć wyjątek, jeśli kategoria nie istnieje
    }

    public void deleteCategory(Integer id) throws ObjectNotFoundException {
        if (this.getCategoryById(id).isEmpty()) {
            throw new ObjectNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}

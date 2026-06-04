package service;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Category;
import com.lab.notesapp.repository.CategoryRepository;
import com.lab.notesapp.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a category by id")
    void testGetCategoryById() {
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(categoryId);

        assertEquals(category.getName(), result.get().getName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Should return all categories")
    void testGetAllCategories() {
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a category")
    void testCreateCategory() {
        Category newCategory = new Category();
        newCategory.setName("New Category");

        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        Category createdCategory = categoryService.createCategory(newCategory);

        assertEquals(newCategory.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(newCategory);
    }

    @Test
    @DisplayName("Should update a category")
    void testUpdateCategory() throws ObjectNotFoundException {
        Integer categoryId = 1;
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Existing Category");

        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory); // Zapewnij, że zapisujesz ten sam obiekt

        Category result = categoryService.updateCategory(updatedCategory, categoryId);

        assertEquals(existingCategory.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory); // Upewnij się, że zapis był wywołany
    }

    @Test
    @DisplayName("Should throw ObjectNotFoundException when deleting a non-existing category")
    void testDeleteCategoryNotFound() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Should delete a category")
    void testDeleteCategory() throws ObjectNotFoundException {
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}

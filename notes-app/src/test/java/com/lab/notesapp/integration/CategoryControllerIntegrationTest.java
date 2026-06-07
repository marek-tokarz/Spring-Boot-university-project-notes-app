package com.lab.notesapp.integration;

import com.lab.notesapp.model.Category;
import com.jayway.jsonpath.JsonPath;
import com.lab.notesapp.service.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        categoryService.deleteAllCategories();
    }

    private Category buildCategory(String categoryName) throws Exception {
        Category categoryRequest = new Category();
        categoryRequest.setName(categoryName);

        String categoryResponse = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long categoryId = ((Number) JsonPath.read(categoryResponse, "$.id")).longValue();

        Category savedCategory = new Category();
        savedCategory.setId((int) categoryId);
        savedCategory.setName(categoryName);

        return savedCategory;
    }

    @Test
    public void shouldRetrieveAllCategories() throws Exception {
        buildCategory("Category 1");
        buildCategory("Category 2");

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(List.class)));
    }

    @Test
    public void shouldCreateAndRetrieveCategory() throws Exception {
        Category newCategory = buildCategory("New Category");

        mockMvc.perform(get("/api/categories/" + newCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    public void shouldUpdateExistingCategory() throws Exception {
        Category existingCategory = buildCategory("Initial Category Name");

        existingCategory.setName("Updated Category Name");

        mockMvc.perform(put("/api/categories/" + existingCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category Name"));
    }

    @Test
    public void shouldDeleteCategory() throws Exception {
        Category categoryToDelete = buildCategory("Category to Delete");

        mockMvc.perform(delete("/api/categories/" + categoryToDelete.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + categoryToDelete.getId()))
                .andExpect(status().isNotFound());
    }
}
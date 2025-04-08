package com.venyou.service.impl;

import com.venyou.exception.CategoryAlreadyExistsException;
import com.venyou.exception.CategoryNotFoundException;
import com.venyou.model.HallCategory;
import com.venyou.repository.HallCategoryRepository;
import com.venyou.service.HallCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallCategoryServiceImpl implements HallCategoryService {

    private final HallCategoryRepository hallCategoryRepository;

    public HallCategoryServiceImpl(HallCategoryRepository hallCategoryRepository) {
        this.hallCategoryRepository = hallCategoryRepository;
    }

    @Override
    public HallCategory addCategory(String categoryName) {
        if (hallCategoryRepository.existsByCategoryName(categoryName)) {
            throw new CategoryAlreadyExistsException("Category already exists with name: " + categoryName);
        }
        HallCategory category = new HallCategory();
        category.setCategoryName(categoryName);
        return hallCategoryRepository.save(category);
    }

    @Override
    public HallCategory getCategoryByName(String categoryName) {
        return hallCategoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryName));
    }

    @Override
    public List<HallCategory> getAllCategories() {
        return hallCategoryRepository.findAll();
    }

    @Override
    public HallCategory updateCategory(long id, HallCategory updatedCategory) {
        HallCategory existingCategory = hallCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        return hallCategoryRepository.save(existingCategory);
    }

    @Override
    public void deleteByCategoryId(long categoryId) {
        if (!hallCategoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        hallCategoryRepository.deleteById(categoryId);
    }
}

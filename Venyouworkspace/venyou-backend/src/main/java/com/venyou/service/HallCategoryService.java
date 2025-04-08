package com.venyou.service;

import com.venyou.model.HallCategory;

import java.util.List;

public interface HallCategoryService {
    HallCategory addCategory(String categoryName);
    HallCategory getCategoryByName(String categoryName);
    List<HallCategory> getAllCategories();
    HallCategory updateCategory(long id, HallCategory updatedCategory);
    void deleteByCategoryId(long categoryId);
}

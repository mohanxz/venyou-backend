package com.venyou.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.venyou.model.HallCategory;

@Service
public class CategoryService {

    private static final String CATEGORY_URL = "http://localhost:8086/api/categories";

    @Autowired
    private RestTemplate restTemplate;

    public List<HallCategory> getAllCategories() {
        try {
            HallCategory[] categories = restTemplate.getForObject(CATEGORY_URL, HallCategory[].class);
            return categories != null ? Arrays.asList(categories) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching categories: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public HallCategory getCategoryById(Long id) {
        try {
            return restTemplate.getForObject(CATEGORY_URL + "/" + id, HallCategory.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching category by ID: " + e.getMessage());
            return null;
        }
    }

    public HallCategory createCategory(HallCategory category) {
        try {
            return restTemplate.postForObject(CATEGORY_URL, category, HallCategory.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating category: " + e.getMessage());
            return null;
        }
    }
}

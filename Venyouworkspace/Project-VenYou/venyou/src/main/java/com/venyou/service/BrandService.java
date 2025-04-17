package com.venyou.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.venyou.model.Brand;

@Service
public class BrandService {

    private static final String BRAND_URL = "http://localhost:8086/api/brands";

    @Autowired
    private RestTemplate restTemplate;

    public List<Brand> getAllBrands() {
        try {
            Brand[] brands = restTemplate.getForObject(BRAND_URL, Brand[].class);
            return brands != null ? Arrays.asList(brands) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching brands: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Brand getBrandById(Long id) {
        try {
            return restTemplate.getForObject(BRAND_URL + "/" + id, Brand.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching brand by ID: " + e.getMessage());
            return null;
        }
    }

    public Brand createBrand(Brand brand) {
        try {
            return restTemplate.postForObject(BRAND_URL, brand, Brand.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating brand: " + e.getMessage());
            return null;
        }
    }
}

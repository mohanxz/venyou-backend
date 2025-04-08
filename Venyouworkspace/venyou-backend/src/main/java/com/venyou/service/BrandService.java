package com.venyou.service;

import com.venyou.dto.BrandRequest;
import com.venyou.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    Brand saveBrand(BrandRequest brandRequest); 

    List<Brand> getAllBrands();

    Optional<Brand> getBrandById(Long brandId);

    Optional<Brand> getBrandByName(String name);

    void deleteBrand(Long brandId);
}

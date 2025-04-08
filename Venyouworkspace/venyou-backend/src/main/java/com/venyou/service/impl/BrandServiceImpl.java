package com.venyou.service.impl;

import com.venyou.dto.BrandRequest;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.model.Brand;
import com.venyou.model.Owner;
import com.venyou.repository.BrandRepository;
import com.venyou.repository.OwnerRepository;
import com.venyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, OwnerRepository ownerRepository) {
        this.brandRepository = brandRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Brand saveBrand(BrandRequest brandRequest) {
        Owner owner = ownerRepository.findById(brandRequest.getOwnerId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + brandRequest.getOwnerId()));

        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setOwner(owner);

        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Long brandId) {
        return brandRepository.findById(brandId);
    }

    @Override
    public Optional<Brand> getBrandByName(String name) {
        return brandRepository.findByName(name);
    }

    @Override
    public void deleteBrand(Long brandId) {
        brandRepository.deleteById(brandId);
    }
}

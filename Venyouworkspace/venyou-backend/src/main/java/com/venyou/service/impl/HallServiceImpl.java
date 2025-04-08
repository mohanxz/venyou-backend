package com.venyou.service.impl;

import com.venyou.dto.HallDTO;
import com.venyou.exception.HallNotFoundException;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.model.*;
import com.venyou.repository.*;
import com.venyou.service.HallService;
import com.venyou.service.dto.HallRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final OwnerRepository ownerRepository;
    private final HallCategoryRepository hallCategoryRepository;
    private final BrandRepository brandRepository;

    public HallServiceImpl(HallRepository hallRepository, OwnerRepository ownerRepository,
                           HallCategoryRepository hallCategoryRepository, BrandRepository brandRepository) {
        this.hallRepository = hallRepository;
        this.ownerRepository = ownerRepository;
        this.hallCategoryRepository = hallCategoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public List<HallDTO> getAllHalls() {
        return hallRepository.findAll()
                .stream()
                .map(HallDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public HallDTO getHallById(Long hallId) throws HallNotFoundException {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException("Hall not found with ID: " + hallId));
        return new HallDTO(hall);
    }

    @Transactional
    @Override
    public HallDTO addHall(HallRequest hallRequest) throws OwnerNotFoundException {
        Owner owner = ownerRepository.findById(hallRequest.getOwnerId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + hallRequest.getOwnerId()));

        Hall hall = new Hall();
        hall.setOwner(owner);
        hall.setName(hallRequest.getName());
        hall.setCapacity(hallRequest.getCapacity());
        hall.setTotalRooms(hallRequest.getTotalRooms());
        hall.setRoomPrice(hallRequest.getRoomPrice());
        hall.setRoomInfo(hallRequest.getRoomInfo());
        hall.setPrice(hallRequest.getPrice());
        hall.setAddressLine1(hallRequest.getAddressLine1());
        hall.setAddressLine2(hallRequest.getAddressLine2());
        hall.setCity(hallRequest.getCity());
        hall.setState(hallRequest.getState());
        hall.setPostalCode(hallRequest.getPostalCode());
        hall.setCountry(hallRequest.getCountry());
        hall.setDescription(hallRequest.getDescription());
        hall.setMapEmbedUrl(hallRequest.getMapEmbedUrl());

        // Set image paths
        if (hallRequest.getImagePaths() != null && !hallRequest.getImagePaths().isEmpty()) {
            hall.setImagePaths(hallRequest.getImagePaths());
        }

        // Handle Category
        HallCategory category = null;
        if (hallRequest.getCategoryId() != null) {
            category = hallCategoryRepository.findById(hallRequest.getCategoryId())
                    .orElseThrow(() -> new HallNotFoundException("Category not found with ID: " + hallRequest.getCategoryId()));
        } else if (hallRequest.getCategoryName() != null) {
            category = new HallCategory();
            category.setCategoryName(hallRequest.getCategoryName());
            category = hallCategoryRepository.save(category);
        }
        hall.setCategory(category);

        // Handle Brand
        Brand brand = null;
        if (hallRequest.getBrandId() != null) {
            brand = brandRepository.findById(hallRequest.getBrandId())
                    .orElseThrow(() -> new HallNotFoundException("Brand not found with ID: " + hallRequest.getBrandId()));
        } else if (hallRequest.getBrandName() != null) {
            brand = brandRepository.findByName(hallRequest.getBrandName()).orElse(null);
            if (brand == null) {
                brand = new Brand();
                brand.setName(hallRequest.getBrandName());
                brand.setDescription("Automatically created brand");
                brand.setOwner(owner);
                brand = brandRepository.save(brand);
            }
        }
        hall.setBrand(brand);

        return new HallDTO(hallRepository.save(hall));
    }

    @Transactional
    @Override
    public HallDTO updateHall(Long hallId, HallRequest hallRequest) throws HallNotFoundException, OwnerNotFoundException {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException("Hall not found with ID: " + hallId));

        hall.setName(hallRequest.getName());
        hall.setCapacity(hallRequest.getCapacity());
        hall.setTotalRooms(hallRequest.getTotalRooms());
        hall.setRoomPrice(hallRequest.getRoomPrice());
        hall.setRoomInfo(hallRequest.getRoomInfo());
        hall.setPrice(hallRequest.getPrice());
        hall.setAddressLine1(hallRequest.getAddressLine1());
        hall.setAddressLine2(hallRequest.getAddressLine2());
        hall.setCity(hallRequest.getCity());
        hall.setState(hallRequest.getState());
        hall.setPostalCode(hallRequest.getPostalCode());
        hall.setCountry(hallRequest.getCountry());
        hall.setDescription(hallRequest.getDescription());
        hall.setMapEmbedUrl(hallRequest.getMapEmbedUrl());

        // Update image paths if provided
        if (hallRequest.getImagePaths() != null) {
            hall.setImagePaths(hallRequest.getImagePaths());
        }

        if (hallRequest.getOwnerId() != null) {
            Owner owner = ownerRepository.findById(hallRequest.getOwnerId())
                    .orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + hallRequest.getOwnerId()));
            hall.setOwner(owner);
        }

        if (hallRequest.getCategoryId() != null) {
            HallCategory category = hallCategoryRepository.findById(hallRequest.getCategoryId())
                    .orElseThrow(() -> new HallNotFoundException("Category not found with ID: " + hallRequest.getCategoryId()));
            hall.setCategory(category);
        }

        if (hallRequest.getBrandId() != null) {
            Brand brand = brandRepository.findById(hallRequest.getBrandId())
                    .orElseThrow(() -> new HallNotFoundException("Brand not found with ID: " + hallRequest.getBrandId()));
            hall.setBrand(brand);
        }

        return new HallDTO(hallRepository.save(hall));
    }

    @Override
    public void deleteHall(Long hallId) throws HallNotFoundException {
        if (!hallRepository.existsById(hallId)) {
            throw new HallNotFoundException("Hall not found with ID: " + hallId);
        }
        hallRepository.deleteById(hallId);
    }

    @Override
    public List<HallDTO> getHallsByOwner(Long ownerId) throws OwnerNotFoundException {
        if (!ownerRepository.existsById(ownerId)) {
            throw new OwnerNotFoundException("Owner not found with ID: " + ownerId);
        }
        return hallRepository.findByOwnerOwnerId(ownerId)
                .stream()
                .map(HallDTO::new)
                .collect(Collectors.toList());
    }
}

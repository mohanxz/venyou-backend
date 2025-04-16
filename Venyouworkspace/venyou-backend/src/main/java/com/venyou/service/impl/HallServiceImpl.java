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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Owner owner;

        // Either use existing owner or create new
        if (hallRequest.getOwnerId() != null) {
            owner = ownerRepository.findById(hallRequest.getOwnerId())
                    .orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + hallRequest.getOwnerId()));
        } else if (hallRequest.getOwnerName() != null) {
            owner = new Owner();
            owner.setName(hallRequest.getOwnerName());
            owner.setEmail(hallRequest.getOwnerEmail());
            owner.setPhone(hallRequest.getOwnerPhone());
            owner.setAadharNumber(hallRequest.getOwnerAadhar());
            owner = ownerRepository.save(owner);
        } else {
            throw new IllegalArgumentException("Either ownerId or full owner details must be provided.");
        }

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

        if (hallRequest.getImagePaths() != null && !hallRequest.getImagePaths().isEmpty()) {
            hall.setImagePathsFromList(hallRequest.getImagePaths());
        }

        // Set the virtual tour map URL
        hall.setVirtualTourMap(hallRequest.getVirtualTourMap());

        // Category handling
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

        // Brand handling
        Brand brand = null;
        if (hallRequest.getBrandId() != null) {
            brand = brandRepository.findById(hallRequest.getBrandId())
                    .orElseThrow(() -> new HallNotFoundException("Brand not found with ID: " + hallRequest.getBrandId()));
        } else if (hallRequest.getBrandName() != null) {
            brand = brandRepository.findByName(hallRequest.getBrandName()).orElse(null);
            if (brand == null) {
                brand = new Brand();
                brand.setName(hallRequest.getBrandName());
                brand.setDescription("Auto-created brand");
                brand.setOwner(owner);
                brand = brandRepository.save(brand);
            }
        }
        hall.setBrand(brand);

        // Optional Fields
        if (hallRequest.getFeatureBannerImage() != null) {
            hall.setFeatureBannerImage(hallRequest.getFeatureBannerImage());
        }
        if (hallRequest.getVideoSrc() != null) {
            hall.setVideoSrc(hallRequest.getVideoSrc());
        }
        if (hallRequest.getAmenities() != null && !hallRequest.getAmenities().isEmpty()) {
            hall.setAmenities(hallRequest.getAmenities());
        }

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

        // Update virtual tour map URL
        hall.setVirtualTourMap(hallRequest.getVirtualTourMap());

        if (hallRequest.getImagePaths() != null) {
            hall.setImagePathsFromList(hallRequest.getImagePaths());
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

        // Optional Fields Update
        if (hallRequest.getFeatureBannerImage() != null) {
            hall.setFeatureBannerImage(hallRequest.getFeatureBannerImage());
        }
        if (hallRequest.getVideoSrc() != null) {
            hall.setVideoSrc(hallRequest.getVideoSrc());
        }
        if (hallRequest.getAmenities() != null && !hallRequest.getAmenities().isEmpty()) {
            hall.setAmenities(hallRequest.getAmenities());
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

    @Override
    public List<String> getHallAmenities(Long hallId) throws HallNotFoundException {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException("Hall not found with ID: " + hallId));

        // Return the amenities list or an empty list if null
        return Optional.ofNullable(hall.getAmenities()).orElse(Collections.emptyList());
    }
}

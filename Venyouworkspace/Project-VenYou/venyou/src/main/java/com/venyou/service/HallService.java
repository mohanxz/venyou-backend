package com.venyou.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.venyou.dto.HallRequestDto;
import com.venyou.model.Brand;
import com.venyou.model.Hall;
import com.venyou.model.HallCategory;
import com.venyou.model.Owner;

@Service
public class HallService {

    private static final String BASE_URL = "http://localhost:8086/api";
    private static final String HALL_URL = BASE_URL + "/halls";
    private static final String OWNER_URL = BASE_URL + "/owners";
    private static final String BRAND_URL = BASE_URL + "/brands";
    private static final String CATEGORY_URL = BASE_URL + "/categories";

    private final RestTemplate restTemplate;
    
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public HallService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Hall> getAllHalls() {
        try {
            Hall[] hallsArray = restTemplate.getForObject(HALL_URL, Hall[].class);
            return hallsArray != null ? Arrays.asList(hallsArray) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching all halls: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Hall getHallById(Long id) {
        try {
            return restTemplate.getForObject(HALL_URL + "/" + id, Hall.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching hall by ID: " + e.getMessage());
            return null;
        }
    }

    public void deleteHall(Long id) {
        try {
            restTemplate.delete(HALL_URL + "/" + id);
        } catch (RestClientException e) {
            System.err.println("❌ Error deleting hall: " + e.getMessage());
        }
    }

    public Hall saveOrUpdate(HallRequestDto dto) {
        Hall hall = mapDtoToHall(dto);

        hall.setOwner(resolveOwner(dto));
        hall.setBrand(resolveBrand(dto));
        hall.setCategory(resolveCategory(dto));

        if (dto.getHallId() == null) {
            return createHall(hall);
        } else {
            hall.setHallId(dto.getHallId());
            updateHall(hall);
            return hall;
        }
    }

    private Hall mapDtoToHall(HallRequestDto dto) {
        Hall hall = new Hall();
        hall.setHallId(dto.getHallId());
        hall.setName(dto.getName());
        hall.setDescription(dto.getDescription());
        hall.setStatus(dto.getStatus());
        hall.setCapacity(dto.getCapacity());
        hall.setTotalRooms(dto.getTotalRooms());
        hall.setRoomPrice(dto.getRoomPrice());
        hall.setRoomInfo(dto.getRoomInfo());
        hall.setPrice(dto.getPrice());

        hall.setAddressLine1(dto.getAddressLine1());
        hall.setAddressLine2(dto.getAddressLine2());
        hall.setCity(dto.getCity());
        hall.setState(dto.getState());
        hall.setPostalCode(dto.getPostalCode());
        hall.setCountry(dto.getCountry());
        hall.setMapEmbedUrl(dto.getMapEmbedUrl());

        hall.setFeatureBannerImage(dto.getFeatureBannerImage());
        hall.setVideoSrc(dto.getVideoSrc());
        hall.setVirtualTourMap(dto.getVirtualTourMap());
        hall.setImagePaths(dto.getImagePaths());
        hall.setAmenities(dto.getAmenities());

        return hall;
    }

    private Owner resolveOwner(HallRequestDto dto) {
        if (dto.getOwnerId() == null && dto.getOwnerName() != null) {
            Owner owner = new Owner();
            owner.setName(dto.getOwnerName());
            owner.setEmail(dto.getOwnerEmail());
            owner.setPhone(dto.getOwnerPhone());
            owner.setAadharNumber(dto.getOwnerAadhar());
            return createOwner(owner);
        } else {
            return getOwnerById(dto.getOwnerId());
        }
    }

    private Brand resolveBrand(HallRequestDto dto) {
        if (dto.getBrandId() == null && dto.getBrandName() != null) {
            Brand brand = new Brand();
            brand.setBrandName(dto.getBrandName());
            return createBrand(brand);
        } else {
            return getBrandById(dto.getBrandId());
        }
    }

    private HallCategory resolveCategory(HallRequestDto dto) {
        if (dto.getCategoryId() == null && dto.getCategoryName() != null) {
            HallCategory category = new HallCategory();
            category.setCategoryName(dto.getCategoryName());
            return createCategory(category);
        } else {
            return getCategoryById(dto.getCategoryId());
        }
    }

    // REST API Calls
    public Hall createHall(Hall hall) {
        try {
            return restTemplate.postForObject(HALL_URL, hall, Hall.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating hall: " + e.getMessage());
            return null;
        }
    }

    public void updateHall(Hall hall) {
        try {
            restTemplate.put(HALL_URL + "/" + hall.getHallId(), hall);
        } catch (RestClientException e) {
            System.err.println("❌ Error updating hall: " + e.getMessage());
        }
    }

    public Owner createOwner(Owner owner) {
        try {
            return restTemplate.postForObject(OWNER_URL, owner, Owner.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating owner: " + e.getMessage());
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

    public HallCategory createCategory(HallCategory category) {
        try {
            return restTemplate.postForObject(CATEGORY_URL, category, HallCategory.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating category: " + e.getMessage());
            return null;
        }
    }

    public Owner getOwnerById(Long id) {
        try {
            return restTemplate.getForObject(OWNER_URL + "/" + id, Owner.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching owner by ID: " + e.getMessage());
            return null;
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

    public HallCategory getCategoryById(Long id) {
        try {
            return restTemplate.getForObject(CATEGORY_URL + "/" + id, HallCategory.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching category by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Owner> getAllOwners() {
        try {
            Owner[] owners = restTemplate.getForObject(OWNER_URL, Owner[].class);
            return owners != null ? Arrays.asList(owners) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching owners: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Brand> getAllBrands() {
        try {
            Brand[] brands = restTemplate.getForObject(BRAND_URL, Brand[].class);
            return brands != null ? Arrays.asList(brands) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching brands: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public Hall convertToEntity(HallRequestDto dto) {
        Hall hall = new Hall();
    
        hall.setHallId(dto.getHallId());
        hall.setName(dto.getName());
        hall.setDescription(dto.getDescription());
        hall.setStatus(dto.getStatus());
        hall.setCapacity(dto.getCapacity());
        hall.setTotalRooms(dto.getTotalRooms());
        hall.setRoomPrice(dto.getRoomPrice());
        hall.setRoomInfo(dto.getRoomInfo());
        hall.setPrice(dto.getPrice());
    
        hall.setAddressLine1(dto.getAddressLine1());
        hall.setAddressLine2(dto.getAddressLine2());
        hall.setCity(dto.getCity());
        hall.setState(dto.getState());
        hall.setPostalCode(dto.getPostalCode());
        hall.setCountry(dto.getCountry());
        hall.setMapEmbedUrl(dto.getMapEmbedUrl());
    
        hall.setFeatureBannerImage(dto.getFeatureBannerImage());
        hall.setVideoSrc(dto.getVideoSrc());
        hall.setVirtualTourMap(dto.getVirtualTourMap());
        hall.setImagePaths(dto.getImagePaths());
        hall.setAmenities(dto.getAmenities());
    
        // Fetch owner/brand/category based on ID
        if (dto.getOwnerId() != null) {
            hall.setOwner(ownerService.getOwnerById(dto.getOwnerId()));
        }
        if (dto.getBrandId() != null) {
            hall.setBrand(brandService.getBrandById(dto.getBrandId()));
        }
        if (dto.getCategoryId() != null) {
            hall.setCategory(categoryService.getCategoryById(dto.getCategoryId()));
        }
    
        return hall;
    }
    public HallRequestDto convertToDto(Hall hall) {
        HallRequestDto dto = new HallRequestDto();
    
        dto.setHallId(hall.getHallId());
        dto.setName(hall.getName());
        dto.setDescription(hall.getDescription());
        dto.setStatus(hall.getStatus());
        dto.setCapacity(hall.getCapacity());
        dto.setTotalRooms(hall.getTotalRooms());
        dto.setRoomPrice(hall.getRoomPrice());
        dto.setRoomInfo(hall.getRoomInfo());
        dto.setPrice(hall.getPrice());
        dto.setAddressLine1(hall.getAddressLine1());
        dto.setAddressLine2(hall.getAddressLine2());
        dto.setCity(hall.getCity());
        dto.setState(hall.getState());
        dto.setPostalCode(hall.getPostalCode());
        dto.setCountry(hall.getCountry());
        dto.setMapEmbedUrl(hall.getMapEmbedUrl());
        dto.setFeatureBannerImage(hall.getFeatureBannerImage());
        dto.setVideoSrc(hall.getVideoSrc());
        dto.setVirtualTourMap(hall.getVirtualTourMap());
        dto.setImagePaths(hall.getImagePaths());
        dto.setAmenities(hall.getAmenities());
    
        if (hall.getOwner() != null) {
            dto.setOwnerId(Long.valueOf(hall.getOwner().getOwnerId()));
        }
        if (hall.getBrand() != null) {
            dto.setBrandId(Long.valueOf(hall.getBrand().getBrandId()));
        }
        if (hall.getCategory() != null) {
            dto.setCategoryId(Long.valueOf(hall.getCategory().getCategoryId()));
        }
        
        return dto;
    }
    
    
}

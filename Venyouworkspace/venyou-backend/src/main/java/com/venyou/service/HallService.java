package com.venyou.service;

import com.venyou.dto.HallDTO;
import com.venyou.exception.HallNotFoundException;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.service.dto.HallRequest;

import java.math.BigDecimal;
import java.util.List;

public interface HallService {

    List<HallDTO> getAllHalls();

    HallDTO getHallById(Long hallId) throws HallNotFoundException;

    HallDTO addHall(HallRequest hallRequest) throws OwnerNotFoundException, HallNotFoundException;

    HallDTO updateHall(Long hallId, HallRequest hallRequest) throws HallNotFoundException, OwnerNotFoundException;

    void deleteHall(Long hallId) throws HallNotFoundException;

    List<HallDTO> getHallsByOwner(Long ownerId) throws OwnerNotFoundException;
<<<<<<< HEAD

    List<HallDTO> filterHalls(
            String name, String city, String state, String address,
            BigDecimal minPrice, BigDecimal maxPrice,
            Integer minCapacity, Integer maxCapacity,
            String categoryName, String brandName,
            String startDate, String endDate, String startTime, String endTime,
            int page, int size
    );
}
=======
    List<String> getHallAmenities(Long hallId);
}
>>>>>>> a5e482a41f457dcce9bc23e3901833c351db1d17

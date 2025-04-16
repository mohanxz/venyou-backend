package com.venyou.service.impl;

import com.venyou.dto.HallAvailabilityDTO;
import com.venyou.model.Hall;
import com.venyou.model.HallAvailability;
import com.venyou.repository.HallAvailabilityRepository;
import com.venyou.repository.HallRepository;
import com.venyou.service.HallAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HallAvailabilityServiceImpl implements HallAvailabilityService {

    @Autowired
    private HallAvailabilityRepository hallAvailabilityRepository;

    @Autowired
    private HallRepository hallRepository;

    @Override
    public HallAvailabilityDTO saveHallAvailability(HallAvailability hallAvailability) {
        // Fetch the Hall entity by ID
        Long hallId = hallAvailability.getHall().getHallId();
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found with ID: " + hallId));

        // Set the full Hall object
        hallAvailability.setHall(hall);

        // Save the HallAvailability
        HallAvailability saved = hallAvailabilityRepository.save(hallAvailability);

        // Map to DTO
        HallAvailabilityDTO dto = new HallAvailabilityDTO();
        dto.setAvailabilityId(saved.getAvailabilityId());
        dto.setHallId(saved.getHall().getHallId());
        dto.setHallName(saved.getHall().getName());
        dto.setDate(saved.getDate());
        dto.setStartTime(saved.getStartTime());
        dto.setEndTime(saved.getEndTime());
        dto.setStatus(saved.getStatus().name());

        return dto;
    }
}
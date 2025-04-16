package com.venyou.controller;

import com.venyou.dto.HallAvailabilityDTO;
import com.venyou.model.HallAvailability;
import com.venyou.service.HallAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hall-availability")
public class HallAvailabilityController {

    @Autowired
    private HallAvailabilityService hallAvailabilityService;

    @PostMapping
    public ResponseEntity<HallAvailabilityDTO> createHallAvailability(@RequestBody HallAvailability hallAvailability) {
        HallAvailabilityDTO savedAvailability = hallAvailabilityService.saveHallAvailability(hallAvailability);
        return ResponseEntity.ok(savedAvailability);
    }
}
package com.venyou.service;

import com.venyou.dto.HallAvailabilityDTO;
import com.venyou.model.HallAvailability;

public interface HallAvailabilityService {
    HallAvailabilityDTO saveHallAvailability(HallAvailability hallAvailability);
}
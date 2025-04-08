package com.venyou.service;

import com.venyou.dto.HallDTO;
import com.venyou.exception.HallNotFoundException;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.service.dto.HallRequest;
import java.util.List;

public interface HallService {

    List<HallDTO> getAllHalls();

    HallDTO getHallById(Long hallId) throws HallNotFoundException;

    HallDTO addHall(HallRequest hallRequest) throws OwnerNotFoundException, HallNotFoundException;

    HallDTO updateHall(Long hallId, HallRequest hallRequest) throws HallNotFoundException, OwnerNotFoundException;

    void deleteHall(Long hallId) throws HallNotFoundException;

    List<HallDTO> getHallsByOwner(Long ownerId) throws OwnerNotFoundException;
}

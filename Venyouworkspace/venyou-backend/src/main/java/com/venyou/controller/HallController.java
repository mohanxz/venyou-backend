
package com.venyou.controller;

import com.venyou.dto.HallDTO;
import com.venyou.service.HallService;
import com.venyou.exception.HallNotFoundException;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.service.dto.HallRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public ResponseEntity<List<HallDTO>> getAllHalls() {
        List<HallDTO> halls = hallService.getAllHalls();
        if (halls.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(halls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallDTO> getHallById(@PathVariable Long id) {
        try {
            HallDTO hall = hallService.getHallById(id);
            return ResponseEntity.ok(hall);
        } catch (HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HallDTO> createHall(@RequestBody HallRequest hallRequest) {
        try {
            HallDTO createdHall = hallService.addHall(hallRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHall);
        } catch (OwnerNotFoundException | HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HallDTO> updateHall(@PathVariable Long id, @RequestBody HallRequest hallRequest) {
        try {
            HallDTO updatedHall = hallService.updateHall(id, hallRequest);
            return ResponseEntity.ok(updatedHall);
        } catch (HallNotFoundException | OwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        try {
            hallService.deleteHall(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<HallDTO>> getHallsByOwner(@PathVariable Long ownerId) {
        try {
            List<HallDTO> halls = hallService.getHallsByOwner(ownerId);
            return ResponseEntity.ok(halls);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/amenities/{hallId}")
    public ResponseEntity<List<String>> getHallAmenities(@PathVariable Long hallId) {
        return ResponseEntity.ok(hallService.getHallAmenities(hallId));
    }
}

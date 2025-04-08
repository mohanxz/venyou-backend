package com.venyou.controller;

import com.venyou.dto.HallDTO;
import com.venyou.service.HallService;
import com.venyou.exception.HallNotFoundException;
import com.venyou.exception.OwnerNotFoundException;
import com.venyou.service.dto.HallRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Endpoint to fetch all halls
    @GetMapping
    public ResponseEntity<List<HallDTO>> getAllHalls() {
        List<HallDTO> halls = hallService.getAllHalls();
        if (halls.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no halls found
        }
        return ResponseEntity.ok(halls);  // 200 OK with halls data
    }

    // Endpoint to fetch a hall by ID
    @GetMapping("/{id}")
    public ResponseEntity<HallDTO> getHallById(@PathVariable Long id) {
        try {
            HallDTO hall = hallService.getHallById(id);
            return ResponseEntity.ok(hall);
        } catch (HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
    }

    // Endpoint to create a new hall
    @PostMapping
    public ResponseEntity<HallDTO> createHall(@RequestBody HallRequest hallRequest) {
        try {
            HallDTO createdHall = hallService.addHall(hallRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHall);  // 201 Created
        } catch (OwnerNotFoundException | HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // 400 Bad Request
        }
    }

    // Endpoint to update an existing hall
    @PutMapping("/{id}")
    public ResponseEntity<HallDTO> updateHall(@PathVariable Long id, @RequestBody HallRequest hallRequest) {
        try {
            HallDTO updatedHall = hallService.updateHall(id, hallRequest);
            return ResponseEntity.ok(updatedHall);  // 200 OK with updated hall
        } catch (HallNotFoundException | OwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found
        }
    }

    // Endpoint to delete a hall by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        try {
            hallService.deleteHall(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (HallNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }

    // Endpoint to fetch halls by owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<HallDTO>> getHallsByOwner(@PathVariable Long ownerId) {
        try {
            List<HallDTO> halls = hallService.getHallsByOwner(ownerId);
            return ResponseEntity.ok(halls);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }
}
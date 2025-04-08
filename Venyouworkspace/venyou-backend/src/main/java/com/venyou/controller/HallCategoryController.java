package com.venyou.controller;

import com.venyou.model.HallCategory;
import com.venyou.service.HallCategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/categories")
public class HallCategoryController {

    private final HallCategoryService hallCategoryService;

    public HallCategoryController(HallCategoryService hallCategoryService) {
        this.hallCategoryService = hallCategoryService;
    }

    @PostMapping
    public ResponseEntity<HallCategory> addCategory(@RequestBody HallCategory category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hallCategoryService.addCategory(category.getCategoryName()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<HallCategory> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(hallCategoryService.getCategoryByName(name));
    }

    @GetMapping
    public ResponseEntity<List<HallCategory>> getAllCategories() {
        return ResponseEntity.ok(hallCategoryService.getAllCategories());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<HallCategory> updateCategory(@PathVariable long categoryId, @RequestBody HallCategory category) {
        return ResponseEntity.ok(hallCategoryService.updateCategory(categoryId, category));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteByCategoryId(@PathVariable long categoryId) {
        hallCategoryService.deleteByCategoryId(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }
}

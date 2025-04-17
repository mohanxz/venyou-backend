package com.venyou.controller;

import com.venyou.dto.HallRequestDto;
import com.venyou.model.Hall;
import com.venyou.service.BrandService;
import com.venyou.service.CategoryService;
import com.venyou.service.HallService;
import com.venyou.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/halls")
public class HallAdminController {

    @Autowired
    private HallService hallService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    private final List<String> amenitiesList = Arrays.asList("WiFi", "Parking", "AC", "Projector", "Catering", "Pool Access");

    @GetMapping
    public String getAllHalls(Model model) {
        model.addAttribute("halls", hallService.getAllHalls());
        model.addAttribute("hallDto", new HallRequestDto());
        model.addAttribute("owners", ownerService.getAllOwners());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("amenitiesList", amenitiesList);

        return "AdminHallManagement";
    }

    @GetMapping("/edit/{id}")
    public String editHall(@PathVariable Long id, Model model) {
        Hall hall = hallService.getHallById(id);
        if (hall == null) return "redirect:/admin/halls";

        HallRequestDto dto = hallService.convertToDto(hall); // Convert Hall -> DTO

        model.addAttribute("hallDto", dto);
        model.addAttribute("halls", hallService.getAllHalls());
        model.addAttribute("owners", ownerService.getAllOwners());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("amenitiesList", amenitiesList);

        return "AdminHallManagement";
    }

    @PostMapping("/save")
    public String saveOrUpdateHall(@ModelAttribute("hallDto") HallRequestDto hallDto) {
        hallService.saveOrUpdate(hallDto); // Already implemented
        return "redirect:/admin/halls";
    }

    @GetMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return "redirect:/admin/halls";
    }
}

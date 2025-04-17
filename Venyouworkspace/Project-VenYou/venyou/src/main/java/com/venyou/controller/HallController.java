package com.venyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.venyou.model.Hall;
import com.venyou.service.HallService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HallController {

    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    // 🔥 Route: /halls → Show all halls
    @GetMapping("/halls")
    public String viewHallsPage(Model model) {
        List<Hall> halls = hallService.getAllHalls();
        model.addAttribute("halls", halls);
        return "explore"; // explore.html
    }
    @GetMapping("/hall/{id}")
    public String getHallById(@PathVariable Long id, Model model) {
        Hall hall = hallService.getHallById(id);
    
        if (hall == null) {
            return "redirect:/halls"; // fallback
        }
    
        model.addAttribute("hall", hall);
        return "hall-details";
    }
    

    // 🛑 Fix: Prevent favicon.ico error spam
    @GetMapping("/favicon.ico")
    public void faviconHandler(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

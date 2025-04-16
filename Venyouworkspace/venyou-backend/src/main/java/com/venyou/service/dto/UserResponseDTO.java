package com.venyou.service.dto;


import com.venyou.model.User.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;

}
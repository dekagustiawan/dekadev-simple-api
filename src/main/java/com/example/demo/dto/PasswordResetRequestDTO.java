package com.example.demo.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    private Long userId;
    private String newPassword;
}
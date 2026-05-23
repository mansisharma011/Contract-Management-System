package com.contractmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    String userName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}

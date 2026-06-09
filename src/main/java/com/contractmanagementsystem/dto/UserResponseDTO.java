package com.contractmanagementsystem.dto;

import com.contractmanagementsystem.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserResponseDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String userName;
    @NotBlank
    private Role role;

}

package com.contractmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ContractRequestDTO {
    @NotBlank(message = "Contract name is required")
    private String contractName;
    @NotNull(message="Contract file is required")
    private MultipartFile file;
}

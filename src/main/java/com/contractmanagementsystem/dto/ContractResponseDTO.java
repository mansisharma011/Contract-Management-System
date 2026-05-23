package com.contractmanagementsystem.dto;

import com.contractmanagementsystem.model.ContractStatus;
import lombok.Setter;

import java.util.UUID;

@Setter
public class ContractResponseDTO {
    private UUID id;
    private String contractName;
    private ContractStatus status;
}

package com.contractmanagementsystem.dto;

import com.contractmanagementsystem.model.ContractStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ContractResponseDTO {
    private String id;
    private String contractName;
    private ContractStatus status;
}

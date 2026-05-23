package com.contractmanagementsystem.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
public class Contract {
    @Id
    private UUID id = UUID.randomUUID();
    @NotBlank
    private String contractName;
    @NotBlank
    private String contractPath;
    private String extractedText;
    private ContractStatus status = ContractStatus.DRAFT;




}

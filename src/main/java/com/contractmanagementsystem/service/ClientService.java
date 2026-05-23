package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import com.contractmanagementsystem.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ContractRepository contractRepository;

    private final FileStorageUtil fileStorageUtil;



    public ResponseEntity<Map<String, Object>> uploadContract(
            ContractRequestDTO dto
    ) throws IOException {

        Map<String, Object> response =
                new HashMap<>();

        String filePath =
                fileStorageUtil
                        .saveFile(
                                dto.getFile()
                        );

        Contract contract =
                new Contract();

        contract.setId(
                UUID.randomUUID()
        );

        contract.setContractName(
                dto.getContractName()
        );

        contract.setContractPath(
                filePath
        );

        contract.setStatus(
                ContractStatus.DRAFT
        );

        Contract savedContract =
                contractRepository
                        .save(contract);

        response.put(
                "message",
                "Contract uploaded successfully"
        );

        response.put(
                "contract",
                savedContract
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    public ResponseEntity<Map<String, Object>>
    updateContract(
            UUID id,
            ContractRequestDTO dto
    ) throws IOException {

        Map<String, Object> response =
                new HashMap<>();

        Contract contract =
                contractRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ContractException(
                                        "No Contract with this id exists"
                                )
                        );

        // Delete old file
        fileStorageUtil
                .deleteFile(
                        contract.getContractPath()
                );

        // Save new file
        String filePath =
                fileStorageUtil
                        .saveFile(
                                dto.getFile()
                        );

        // Update fields
        contract.setContractName(
                dto.getContractName()
        );

        contract.setContractPath(
                filePath
        );

        contract.setStatus(
                ContractStatus.DRAFT
        );

        Contract updatedContract =
                contractRepository
                        .save(contract);

        response.put(
                "message",
                "Contract updated successfully"
        );

        response.put(
                "contract",
                updatedContract
        );

        return ResponseEntity.ok(
                response
        );
    }

    public ResponseEntity<Resource>
    downloadContract(
            UUID id
    ) throws MalformedURLException {

        Contract contract =
                contractRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ContractException(
                                        "Contract not found"
                                )
                        );

        Path path =
                Paths.get(
                        contract
                                .getContractPath()
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {

            throw new ContractException(
                    "File not found"
            );
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + resource.getFilename()
                                + "\"")
                .body(resource);
    }
}


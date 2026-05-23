package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import com.contractmanagementsystem.utils.FileStorageUtil;
import com.contractmanagementsystem.utils.TextExtractionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.net.MalformedURLException;


@RequiredArgsConstructor
@Service
public class ClientService {
    private final ContractRepository contractRepository;

    private final TextExtractionUtil textExtractionUtil;
    private final FileStorageUtil fileStorageUtil;



    public ResponseEntity<Map<String, Object>> uploadContract(ContractRequestDTO dto) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String filePath = fileStorageUtil.saveFile(dto.getFile());
        String extractedText=textExtractionUtil.extractText(filePath);

        Contract contract = new Contract();


        contract.setContractName(dto.getContractName());

        contract.setContractPath(filePath);

        contract.setExtractedText(extractedText);

        contract.setStatus(ContractStatus.DRAFT);

        Contract savedContract = contractRepository.save(contract);
        ContractResponseDTO responseDTO=new ContractResponseDTO();
        responseDTO.setId(savedContract.getId());
        responseDTO.setContractName(savedContract.getContractName());
        responseDTO.setStatus(savedContract.getStatus());

        response.put("message", "Contract uploaded successfully");

        response.put("contract", responseDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    public ResponseEntity<Map<String, Object>> updateContract(String id, ContractRequestDTO dto) throws Exception {

        Map<String, Object> response = new HashMap<>();

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract with this id exists"));

        // Delete old file
        fileStorageUtil.deleteFile(contract.getContractPath());

        // Save new file
        String filePath = fileStorageUtil.saveFile(dto.getFile());
        String extractedText=textExtractionUtil.extractText(filePath);

        // Update fields
        contract.setContractName(dto.getContractName());

        contract.setContractPath(filePath);

        contract.setExtractedText(extractedText);

        contract.setStatus(ContractStatus.DRAFT);

        Contract updatedContract = contractRepository.save(contract);
        ContractResponseDTO responseDTO=new ContractResponseDTO();
        responseDTO.setId(updatedContract.getId());
        responseDTO.setContractName(updatedContract.getContractName());
        responseDTO.setStatus(updatedContract.getStatus());

        response.put("message", "Contract updated successfully");

        response.put("contract", responseDTO);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Resource> downloadContract(String id) throws MalformedURLException {

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("Contract not found"));

        Path path = Paths.get(contract.getContractPath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new ContractException("File not found");
        }

        String fileName = resource.getFilename();
        MediaType mediaType;

        if (fileName != null && fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        }
        else if (fileName != null && fileName.endsWith(".docx")) {
            mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
        else {
            throw new ContractException("Only PDF and DOCX files are allowed");
        }

        return ResponseEntity.ok().contentType(mediaType).
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").
                body(resource);
    }
}


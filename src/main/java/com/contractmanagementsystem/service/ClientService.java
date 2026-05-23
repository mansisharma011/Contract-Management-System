package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.exception.UserException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import com.contractmanagementsystem.utils.AskQuestionUtil;
import com.contractmanagementsystem.utils.DownloadFileUtil;
import com.contractmanagementsystem.utils.FileStorageUtil;
import com.contractmanagementsystem.utils.TextExtractionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class ClientService {
    private final ContractRepository contractRepository;

    private final TextExtractionUtil textExtractionUtil;
    private final FileStorageUtil fileStorageUtil;
    private final DownloadFileUtil downloadFileUtil;
    private final AskQuestionUtil askQuestionUtil;



    public ResponseEntity<Map<String, Object>> uploadContract(ContractRequestDTO dto, String id, String clientName) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String filePath = fileStorageUtil.saveFile(dto.getFile());
        String extractedText=textExtractionUtil.extractText(filePath);

        Contract contract = new Contract();


        contract.setContractName(dto.getContractName());

        contract.setContractPath(filePath);

        contract.setExtractedText(extractedText);

        contract.setClientId(id);

        contract.setClientName(clientName);

        contract.setConsultantId(dto.getConsultantId());

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

    public ResponseEntity<Map<String, Object>> updateContract(String userId,String id, ContractRequestDTO dto) throws Exception {

        Map<String, Object> response = new HashMap<>();

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract with this id exists"));

        if(!Objects.equals(contract.getClientId(), userId)){
            throw new UserException("This Contract don't belong to you So you can't update it");
        }

        // Delete old file
        fileStorageUtil.deleteFile(contract.getContractPath());

        // Save new file
        String filePath = fileStorageUtil.saveFile(dto.getFile());
        String extractedText=textExtractionUtil.extractText(filePath);

        // Update fields
        contract.setContractName(dto.getContractName());

        contract.setContractPath(filePath);

        contract.setExtractedText(extractedText);

        contract.setConsultantId(dto.getConsultantId());

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

    public ResponseEntity<Map<String, Object>>getAllContracts(String id) {

        List<Contract> contracts = contractRepository.findByClientId(id);

        Map<String, Object> response = new HashMap<>();

        response.put("message",contracts.isEmpty() ? "No contracts found" : "Contracts fetched successfully");
        List<ContractResponseDTO> responseDTOList=contracts.stream()
                .map(contract -> {
                    ContractResponseDTO dto = new ContractResponseDTO();

                    dto.setId(contract.getId());

                    dto.setContractName(contract.getContractName());

                    dto.setStatus(contract.getStatus());
                    return dto;
                })
                .toList();

        response.put("data",responseDTOList);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Resource> downloadFile(String clientId, String id) throws MalformedURLException {
        Contract contract=contractRepository.findById(id).orElseThrow(()->new ContractException("No Such Contract Exists"));
        if(!Objects.equals(contract.getClientId(),clientId)){
            throw new UserException("You are not Authorized to access this");
        }
        return downloadFileUtil.downloadContract(contract);

    }

    public ResponseEntity<Map<String,Object>>getContract(String clientId,String id){
        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Such Contract Exist"));
        if(!Objects.equals(contract.getClientId(),clientId)){
            throw new UserException("You are not Authorized to access this");
        }
        ContractResponseDTO responseDTO=new ContractResponseDTO();
        responseDTO.setId(contract.getId());
        responseDTO.setContractName(contract.getContractName());
        responseDTO.setStatus(contract.getStatus());
        Map<String,Object> response=new HashMap<>();
        response.put("message","Contract details Successfully fetched");
        response.put("Contract Details",responseDTO);
        return ResponseEntity.ok().body(response);
    }

    public QuestionAnswerResponseDTO askQuestion(String clientId, String id, String question){
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if (!Objects.equals(contract.getClientId(), clientId)) {
            throw new UserException("You are not Authorized to perform this operation");
        }
        return askQuestionUtil.askQuestion(contract,question);

    }

}


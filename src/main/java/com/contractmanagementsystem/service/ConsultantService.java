package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConsultantService {

    private final ContractRepository contractRepository;


    public ResponseEntity<Map<String,Object>>getContract(String id){
        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Such Contract Exist"));
        ContractResponseDTO responseDTO=new ContractResponseDTO();
        responseDTO.setId(contract.getId());
        responseDTO.setContractName(contract.getContractName());
        responseDTO.setStatus(contract.getStatus());
        Map<String,Object> response=new HashMap<>();
        response.put("message","Contract details Successfully fetched");
        response.put("Contract Details",responseDTO);
        return ResponseEntity.ok().body(response);
    }


    public ResponseEntity<Map<String, Object>>getAllContracts() {

        List<Contract> contracts = contractRepository.findAll();

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

    public ResponseEntity<Map<String,String>> draftToReview(String id){

        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if(contract.getStatus() == ContractStatus.DRAFT){
            contract.setStatus(ContractStatus.REVIEW);
            contractRepository.save(contract);
            Map<String,String> response=new HashMap<>();
            response.put("message","Status Successfully updated to Review");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not draft");

    }

    public ResponseEntity<Map<String,String>> reviewToApproved(String id){

        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if(contract.getStatus() == ContractStatus.REVIEW){
            contract.setStatus(ContractStatus.APPROVED);
            contractRepository.save(contract);
            Map<String,String> response=new HashMap<>();
            response.put("message","Status Successfully updated to Approved");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not Review");

    }


}

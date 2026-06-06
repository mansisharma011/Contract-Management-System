package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.exception.UserException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import com.contractmanagementsystem.utils.AskQuestionUtil;
import com.contractmanagementsystem.utils.DownloadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ConsultantService {

    private final ContractRepository contractRepository;
    private final DownloadFileUtil downloadFileUtil;
    private final AskQuestionUtil askQuestionUtil;


    public ResponseEntity<Map<String,Object>>getContract(String consultantId,String id){
        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Such Contract Exist"));
        if(!Objects.equals(contract.getConsultantId(),consultantId)){
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

    public ResponseEntity<Resource> downloadFile(String consultantId, String id) throws MalformedURLException {
        Contract contract=contractRepository.findById(id).orElseThrow(()->new ContractException("No Such Contract Exists"));
        if(!Objects.equals(contract.getConsultantId(),consultantId)){
            throw new UserException("You are not Authorized to access this");
        }
        return downloadFileUtil.downloadContract(contract);

    }


    public ResponseEntity<Map<String, Object>>getAllContracts(String id) {

        List<Contract> contracts = contractRepository.findByConsultantId(id);

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

    public ResponseEntity<Map<String,String>> draftToReview(String consultantId,String id){

        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if(!Objects.equals(contract.getConsultantId(),consultantId)){
            throw new UserException("You are not Authorized to perform this operation");
        }
        if(contract.getStatus() == ContractStatus.DRAFT){
            contract.setStatus(ContractStatus.REVIEW);
            contractRepository.save(contract);
            Map<String,String> response=new HashMap<>();
            response.put("message","Status Successfully updated to Review");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not draft");

    }

    public ResponseEntity<Map<String,String>> reviewToApproved(String consultantId,String id) {

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if (!Objects.equals(contract.getConsultantId(), consultantId)) {
            throw new UserException("You are not Authorized to perform this operation");
        }
        if (contract.getStatus() == ContractStatus.REVIEW) {
            contract.setStatus(ContractStatus.APPROVED);
            contractRepository.save(contract);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Status Successfully updated to Approved");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not Review");

    }

    public QuestionAnswerResponseDTO askQuestion(String consultantId,String id,String question){
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if (!Objects.equals(contract.getConsultantId(), consultantId)) {
            throw new UserException("You are not Authorized to perform this operation");
        }
        return askQuestionUtil.askQuestion(contract,question);

    }

}

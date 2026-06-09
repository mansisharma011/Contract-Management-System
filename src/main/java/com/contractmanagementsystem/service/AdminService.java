package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.dto.UserResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.exception.UserException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.model.Role;
import com.contractmanagementsystem.model.User;
import com.contractmanagementsystem.repository.ContractRepository;
import com.contractmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private  final ContractRepository contractRepository;

    public ResponseEntity<Map<String,Object>>getAllConsultants(){

        return getAllData(Role.CONSULTANT);
    }
    public ResponseEntity<Map<String,Object>>getAllClients(){

        return getAllData(Role.CLIENT);
    }


    private ResponseEntity<Map<String,Object>> getAllData(Role role){
        List<UserResponseDTO> users= userRepository.findByRole(role).stream().map(user -> {
                UserResponseDTO dto = new UserResponseDTO();
                dto.setId(user.getId());
                dto.setUserName(user.getUserName());
                dto.setRole(user.getRole());
                return dto;
        }).toList();
        Map<String,Object> response=new HashMap<>();
        response.put("data",users);

        if(users.isEmpty()){
            response.put("message","No data Found");
            return ResponseEntity.ok().body(response);
        }
        response.put("message","data successfully fetched ");
        return ResponseEntity.ok().body(response);




    }
    public ResponseEntity<Map<String,Object>>getAllContracts(){
        List<Contract> contracts = contractRepository.findAll();

        return utilForFetch(contracts);
    }
    public ResponseEntity<Map<String,Object>>getContractByClientId(String id){
        List<Contract> contracts=contractRepository.findByClientId(id);
        return utilForFetch(contracts);


    }
    public ResponseEntity<Map<String,Object>>getContractByConsultantId(String id){
        List<Contract> contracts=contractRepository.findByConsultantId(id);
        return utilForFetch(contracts);
    }

    private ResponseEntity<Map<String,Object>> utilForFetch(List<Contract> contracts){
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

    public ResponseEntity<Map<String, Object>> changeRoleToConsultant(String id) {
        User user=userRepository.findById(id).orElseThrow(()-> new UserException("No such user Exist"));
        if(user.getRole() !=Role.CLIENT){
            throw new UserException("Role Change not Possible as Role is not Client");
        }
        user.setRole(Role.CONSULTANT);
        userRepository.save(user);
        Map<String,Object> response=new HashMap<>();
        response.put("message","Role Successfully Updated");
        return ResponseEntity.ok().body(response);
    }


    public ResponseEntity<Map<String, Object>> getContractsInDraft() {
        List<Contract> contracts =contractRepository.findByContractStatus(ContractStatus.DRAFT);
        return utilForFetch(contracts);
    }

    public ResponseEntity<Map<String, Object>> getContractsInReview() {
        List<Contract> contracts =contractRepository.findByContractStatus(ContractStatus.REVIEW);
        return utilForFetch(contracts);
    }

    public ResponseEntity<Map<String, Object>> getContractsApproved() {
        List<Contract> contracts =contractRepository.findByContractStatus(ContractStatus.APPROVED);
        return utilForFetch(contracts);
    }

    public ResponseEntity<Map<String, Object>> getContract(String id) {
        Contract contract =contractRepository.findById(id).orElseThrow(()-> new ContractException("No Such Contract Exists"));
        Map<String,Object> response=new HashMap<>();
        response.put("Contract",contract);
        response.put("message","Successfully Fetched");
        return ResponseEntity.ok().body(response);

    }
}

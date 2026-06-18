package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.service.AdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getConsultant")
    public ResponseEntity<Map<String,Object>>getAllConsultants(){
        return adminService.getAllConsultants();
    }

    @GetMapping("/getClients")
    public ResponseEntity<Map<String,Object>>getAllClients(){
        return adminService.getAllClients();
    }

    @GetMapping("/getAllContracts")
    public ResponseEntity<Map<String,Object>>getAllContracts(){

        return adminService.getAllContracts();
    }

    @GetMapping("/getContracts/{id}")
    public ResponseEntity<Map<String,Object>>getContractByClientId(@NotBlank @PathVariable String id){
        return adminService.getContractByClientId(id);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>>getContractByConsultantId(@NotBlank @PathVariable String id){
        return adminService.getContractByConsultantId(id);

    }

    @GetMapping("/contract/{id}")
    public ResponseEntity<Map<String,Object>>getContract(@NotBlank @PathVariable String id){
        return adminService.getContract(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>>changeRoleToConsultant(@NotBlank @PathVariable String id){
        return adminService.changeRoleToConsultant(id);
    }

    @GetMapping("/draft")
    public ResponseEntity<Map<String,Object>>getContractsInDraft(){
        return adminService.getContractsInDraft();

    }
    @GetMapping("/review")
    public ResponseEntity<Map<String,Object>>getContractsInReview(){
        return adminService.getContractsInReview();

    }
    @GetMapping("/approved")
    public ResponseEntity<Map<String,Object>>getContractsApproved(){
        return adminService.getContractsApproved();

    }


}

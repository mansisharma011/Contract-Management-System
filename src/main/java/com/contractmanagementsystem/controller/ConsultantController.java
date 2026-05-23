package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.service.ClientService;
import com.contractmanagementsystem.service.ConsultantService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.net.MalformedURLException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Consultant")
public class ConsultantController {

    private final ConsultantService consultantService;
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllContracts(){
        return consultantService.getAllContracts();
    }
    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadContract(
            @PathVariable String id
    ) throws MalformedURLException {

        return clientService.downloadContract(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getContractByID(@PathVariable String id){
        return consultantService.getContract(id);

    }

    @PostMapping("/{id}/ask")
    public ResponseEntity<QuestionAnswerResponseDTO> askQuestion(
            @PathVariable String id,
            @RequestBody String question
    ) {
        return ResponseEntity.ok(consultantService.askQuestion(id, question));
    }

    @PutMapping("/updateStatusToReview/{id}")
    public ResponseEntity<Map<String,String>> draftToReview(@PathVariable String id){
        return consultantService.draftToReview(id);
    }

    @PutMapping("/updateStatusToApproved/{id}")
    public ResponseEntity<Map<String,String>> reviewToApproved(@PathVariable String id){
        return consultantService.reviewToApproved(id);
    }
}

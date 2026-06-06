package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.security.AuthenticatedUser;
import com.contractmanagementsystem.service.ConsultantService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.net.MalformedURLException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/consultant")
public class ConsultantController {

    private final ConsultantService consultantService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllContracts(Authentication authentication){
        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String id= user.getUserId();
        return consultantService.getAllContracts(id);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadContract(
            @PathVariable String id, Authentication authentication
    ) throws MalformedURLException {

        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String userId= user.getUserId();

        return consultantService.downloadFile(userId,id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getContractByID(@PathVariable String id, Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.getContract(userId,id);

    }

    @PostMapping("/{id}/ask")
    public ResponseEntity<QuestionAnswerResponseDTO> askQuestion(@PathVariable String id, @RequestBody String question, Authentication authentication
    ) {
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(consultantService.askQuestion(userId,id, question));
    }

    @PutMapping("/updateStatusToReview/{id}")
    public ResponseEntity<Map<String,String>> draftToReview(@PathVariable String id,Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.draftToReview(userId,id);
    }

    @PutMapping("/updateStatusToApproved/{id}")
    public ResponseEntity<Map<String,String>> reviewToApproved(@PathVariable String id, Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.reviewToApproved(userId,id);
    }
}

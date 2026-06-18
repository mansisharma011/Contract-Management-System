package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.security.AuthenticatedUser;
import com.contractmanagementsystem.service.ConsultantService;

import jakarta.validation.constraints.NotBlank;
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
            @NotBlank @PathVariable String id, Authentication authentication
    ) throws MalformedURLException {

        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String userId= user.getUserId();

        return consultantService.downloadFile(userId,id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getContractByID(@NotBlank @PathVariable String id, Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.getContract(userId,id);

    }

    @PostMapping("/{id}/ask")
    public ResponseEntity<QuestionAnswerResponseDTO> askQuestion(@NotBlank @PathVariable String id,@NotBlank @RequestBody String question, Authentication authentication
    ) {
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(consultantService.askQuestion(userId,id, question));
    }

    @PutMapping("/updateStatusToReview/{id}")
    public ResponseEntity<Map<String,String>> draftToReview(@NotBlank @PathVariable String id,Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.draftToReview(userId,id);
    }

    @PutMapping("/updateStatusToApproved/{id}")
    public ResponseEntity<Map<String,String>> reviewToApproved(@NotBlank @PathVariable String id, Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return consultantService.reviewToApproved(userId,id);
    }
}

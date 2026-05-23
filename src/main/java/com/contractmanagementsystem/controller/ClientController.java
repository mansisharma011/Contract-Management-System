package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.security.AuthenticatedUser;
import com.contractmanagementsystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadContract(@ModelAttribute ContractRequestDTO contractRequestDTO, Authentication authentication) throws Exception {
        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String id= user.getUserId();
        String clientName= user.getUserName();

        return clientService.uploadContract(contractRequestDTO, id, clientName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateContract( @ModelAttribute ContractRequestDTO contractRequestDTO,@PathVariable String id, Authentication authentication) throws Exception {
        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String userId = user.getUserId();

        return clientService.updateContract(userId, id, contractRequestDTO);
    }
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllContracts(Authentication authentication){
        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String id= user.getUserId();
        return clientService.getAllContracts(id);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadContract(
            @PathVariable String id, Authentication authentication
    ) throws MalformedURLException {

        AuthenticatedUser user=(AuthenticatedUser) authentication.getPrincipal();
        String userId= user.getUserId();

        return clientService.downloadFile(userId,id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getContractByID(@PathVariable String id, Authentication authentication){
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return clientService.getContract(userId,id);

    }

    @PostMapping("/{id}/ask")
    public ResponseEntity<QuestionAnswerResponseDTO> askQuestion(@PathVariable String id, @RequestBody String question, Authentication authentication
    ) {
        String userId=((AuthenticatedUser)authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(clientService.askQuestion(userId,id, question));
    }
}

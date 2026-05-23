package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadContract(@ModelAttribute ContractRequestDTO contractRequestDTO) throws IOException {

        return clientService.uploadContract(contractRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateContract(@PathVariable UUID id, @ModelAttribute ContractRequestDTO contractRequestDTO) throws IOException{

        return clientService.updateContract(id, contractRequestDTO);
    }
}

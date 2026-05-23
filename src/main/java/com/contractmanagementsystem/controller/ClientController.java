package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.ContractRequestDTO;
import com.contractmanagementsystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadContract(@ModelAttribute ContractRequestDTO contractRequestDTO) throws Exception {

        return clientService.uploadContract(contractRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateContract(@PathVariable String id, @ModelAttribute ContractRequestDTO contractRequestDTO) throws Exception {

        return clientService.updateContract(id, contractRequestDTO);
    }
}

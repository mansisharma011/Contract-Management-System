package com.contractmanagementsystem.controller;

import com.contractmanagementsystem.dto.AuthResponse;
import com.contractmanagementsystem.dto.LoginRequest;
import com.contractmanagementsystem.dto.RegisterRequest;
import com.contractmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return authService.loginUser(loginRequest);

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(
            @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }
}

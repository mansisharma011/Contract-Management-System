package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.AuthResponse;
import com.contractmanagementsystem.dto.LoginRequest;
import com.contractmanagementsystem.dto.RegisterRequest;
import com.contractmanagementsystem.exception.UserException;
import com.contractmanagementsystem.model.User;
import com.contractmanagementsystem.repository.UserRepository;
import com.contractmanagementsystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<AuthResponse> loginUser(LoginRequest dto){
        User user=userRepository.findByEmailId(dto.getEmail()).orElseThrow(()-> new UserException("No User With this Email"));
        boolean isPasswordCorrect= passwordEncoder.matches(dto.getPassword(),
                user.getPassword());

        if(!isPasswordCorrect){
            throw new UserException("Incorrect Password");
        }

        String token= jwtService.generateToken(user);
        return ResponseEntity.ok().body(new AuthResponse(token));




    }

    public ResponseEntity<Map<String,Object>> register(RegisterRequest dto){
        Map<String,Object> response=new HashMap<>();
        if(userRepository.existsByEmailId(dto.getEmail())){
            response.put("message","You are already Registered,Please try to login");
            return ResponseEntity.badRequest().body(response);
        }

        User user= new User();
        user.setUserName(dto.getUserName());
        user.setEmailId(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        response.put("message","Successfully registered, Login to perform operations ");
        return ResponseEntity.ok().body(response);



    }
}

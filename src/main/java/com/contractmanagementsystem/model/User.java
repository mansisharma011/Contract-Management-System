package com.contractmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id= UUID.randomUUID().toString();
    @NotBlank
    private String userName;
    @NotBlank
    @Email
    private String emailId;

    @NotBlank
    private String password;

    private Role role =Role.CLIENT;



}

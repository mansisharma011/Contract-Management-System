package com.contractmanagementsystem.security;

import com.contractmanagementsystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {

    private String userId;

    private String userName;

    private String email;

    private Role role;
}

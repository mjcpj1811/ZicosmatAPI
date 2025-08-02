package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;

import java.util.Set;

@Getter
public class UserCreateRequest {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<String> roles;
}

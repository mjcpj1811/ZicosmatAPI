package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;

import java.util.Set;

@Getter
public class UserUpdateRequest {
    private long id;
    private String fullName;
    private String username;
    private String phone;
    private String email;
    private Set<String> roles;
}

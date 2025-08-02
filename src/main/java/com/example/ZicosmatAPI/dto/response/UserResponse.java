package com.example.ZicosmatAPI.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {
    private long id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private List<String> roles;
}

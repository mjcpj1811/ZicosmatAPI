package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;

@Getter
public class UserPasswordUpdate {
    private long id;
    private String password;
    private String newPassword;
}

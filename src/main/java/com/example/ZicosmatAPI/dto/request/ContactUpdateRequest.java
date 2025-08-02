package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;

@Getter
public class ContactUpdateRequest {
    private long id;
    private String fullName;
    private String phone;
    private String email;
    private String text;
}

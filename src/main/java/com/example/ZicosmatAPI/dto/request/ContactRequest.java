package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;

@Getter
public class ContactRequest {

    private String fullName;

    private String phone;

    private String email;

    private String text;
}

package com.example.ZicosmatAPI.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ContactResponse {

    private long id;

    private String fullName;

    private String phone;

    private String email;

    private String text;

    private LocalDateTime createdAt;
}

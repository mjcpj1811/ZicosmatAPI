package com.example.ZicosmatAPI.service;

import com.example.ZicosmatAPI.dto.request.ContactRequest;
import com.example.ZicosmatAPI.dto.request.ContactUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ContactResponse;

import java.util.List;

public interface ContactService {

    ContactResponse getContact(long id);

    List<ContactResponse> getContacts();

    long sendContact(ContactRequest request);

    long createContact(ContactRequest request);

    void updateContact(ContactUpdateRequest request);

    void deleteContact(long id);
}

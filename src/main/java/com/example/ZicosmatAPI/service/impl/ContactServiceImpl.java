package com.example.ZicosmatAPI.service.impl;

import com.example.ZicosmatAPI.dto.request.ContactRequest;
import com.example.ZicosmatAPI.dto.request.ContactUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ContactResponse;
import com.example.ZicosmatAPI.exception.ResourceNotFoundException;
import com.example.ZicosmatAPI.model.Contact;
import com.example.ZicosmatAPI.repository.ContactRepository;
import com.example.ZicosmatAPI.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CONTACT-SERVICE")
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public long sendContact(ContactRequest request) {
        log.info("Sending contact request: " + request);

        Contact contact = new Contact();
        contact.setFullName(request.getFullName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setText(request.getText());
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);

        return contact.getId();
    }

    @Override
    public long createContact(ContactRequest request) {
        log.info("Creating contact request: " + request);

        Contact contact = new Contact();
        contact.setFullName(request.getFullName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setText(request.getText());
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);

        return contact.getId();
    }

    @Override
    public ContactResponse getContact(long id) {
        log.info("Getting contact with id: " + id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        return ContactResponse.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .text(contact.getText())
                .createdAt(contact.getCreatedAt()).build();
    }

    @Override
    public List<ContactResponse> getContacts() {
        log.info("Getting contacts");

        List<Contact> contacts = contactRepository.findAll();

        return contacts.stream().map(contact -> {
            return ContactResponse.builder()
                    .id(contact.getId())
                    .fullName(contact.getFullName())
                    .phone(contact.getPhone())
                    .email(contact.getEmail())
                    .text(contact.getText())
                    .createdAt(contact.getCreatedAt())
                    .build();
        }).toList();
    }

    @Override
    public void updateContact(ContactUpdateRequest request) {
        log.info("Updating contact request: " + request);

        Contact contact = contactRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        contact.setFullName(request.getFullName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setText(request.getText());
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);

        log.info("Contact updated: " + contact);
    }

    @Override
    public void deleteContact(long id) {
        log.info("Deleting contact: " + id);

        contactRepository.deleteById(id);
    }

}

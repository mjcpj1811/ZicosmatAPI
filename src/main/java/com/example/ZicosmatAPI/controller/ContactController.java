package com.example.ZicosmatAPI.controller;

import com.example.ZicosmatAPI.dto.request.ContactRequest;
import com.example.ZicosmatAPI.dto.request.ContactUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ApiResponse;
import com.example.ZicosmatAPI.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j(topic = "CONTACT-CONTROLLER")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "get all contact", description = "Get all contact")
    @GetMapping("/all")
    public ApiResponse getAllContacts() {
        log.info("Get all contact");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all contact successfully")
                .data(contactService.getContacts()).build();
    }

    @Operation(summary = "Get contact detail", description = "Get contact description")
    @GetMapping("/{id}")
    public ApiResponse getContact(@PathVariable long id) {
        log.info("Get contact with id {}", id);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get contact detail successfully")
                .data(contactService.getContact(id)).build();
    }

    @Operation(summary = "Send contact", description = "Send contact")
    @PostMapping("/send")
    public ApiResponse sendContact(@RequestBody ContactRequest request) {
        log.info("Send contact");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Send contact successfully")
                .data(contactService.sendContact(request)).build();
    }

    @Operation(summary = "Create contact", description = "Create contact")
    @PostMapping("/create")
    public ApiResponse createContact(@RequestBody ContactRequest request) {
        log.info("Create contact");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Create contact successfully")
                .data(contactService.createContact(request)).build();
    }

    @Operation(summary = "Update contact", description = "update contact")
    @PutMapping("/update")
    public ApiResponse updateContact(@RequestBody ContactUpdateRequest request) {
        log.info("Update contact");

        contactService.updateContact(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Update contact successfully")
                .data("").build();
    }

    @Operation(summary = "Delete contact", description = "delete contact")
    @DeleteMapping("/del/{id}")
    public ApiResponse deleteContact(@PathVariable long id) {
        log.info("Delete contact: " + id);

        contactService.deleteContact(id);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Delete contact successfully")
                .data("").build();
    }
}

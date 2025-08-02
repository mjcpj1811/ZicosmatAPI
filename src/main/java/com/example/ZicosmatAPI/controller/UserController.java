package com.example.ZicosmatAPI.controller;

import com.example.ZicosmatAPI.dto.request.UserCreateRequest;
import com.example.ZicosmatAPI.dto.request.UserUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ApiResponse;
import com.example.ZicosmatAPI.repository.UserRepository;
import com.example.ZicosmatAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get All User", description = "Get all user from db")
    @GetMapping("/all")
    public ApiResponse getAllUsers() {
        log.info("Get all users");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get All User")
                .data(userService.getAllUsers()).build();
    }

    @Operation(summary = "Get user detail by Id", description = "Get user detail by Id")
    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable long id) {
        log.info("Get user detail by Id");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get user detail by id")
                .data(userService.getUserDetail(id)).build();
    }

    @Operation(summary = "Create user", description = "Create new user")
    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody UserCreateRequest request) {
        log.info("Create user");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Created user successfully")
                .data(userService.createUser(request)).build();
    }

    @Operation(summary = "Update user", description = "Update user")
    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody UserUpdateRequest request) {
        log.info("Update user");

        userService.updateUser(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("User update successfully")
                .data("").build();
    }

    @Operation(summary = "Delete user", description = "Delete user")
    @DeleteMapping("/del/{id}")
    public ApiResponse deleteUser(@PathVariable long id) {
        log.info("Delete user");

        userService.deleteUser(id);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Delete user successfully")
                .data("").build();
    }

}

package com.example.ZicosmatAPI.service;

import com.example.ZicosmatAPI.dto.request.UserCreateRequest;
import com.example.ZicosmatAPI.dto.request.UserPasswordUpdate;
import com.example.ZicosmatAPI.dto.request.UserUpdateRequest;
import com.example.ZicosmatAPI.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserDetail(Long id);

    List<UserResponse> getAllUsers();

    long createUser(UserCreateRequest user);

    void updateUser(UserUpdateRequest user);

    void deleteUser(Long id);

    void changePassword(UserPasswordUpdate request);

}

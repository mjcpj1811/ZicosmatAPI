package com.example.ZicosmatAPI.service.impl;

import com.example.ZicosmatAPI.dto.request.UserCreateRequest;
import com.example.ZicosmatAPI.dto.request.UserPasswordUpdate;
import com.example.ZicosmatAPI.dto.request.UserUpdateRequest;
import com.example.ZicosmatAPI.dto.response.UserResponse;
import com.example.ZicosmatAPI.exception.DuplicateResourceException;
import com.example.ZicosmatAPI.exception.ResourceNotFoundException;
import com.example.ZicosmatAPI.model.Role;
import com.example.ZicosmatAPI.model.User;
import com.example.ZicosmatAPI.repository.RoleRepository;
import com.example.ZicosmatAPI.repository.UserRepository;
import com.example.ZicosmatAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserDetail(Long id) {
        log.info("getUserDetail");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(roles).build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("getAllUsers");

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {

            List<String> roles = new ArrayList<>();
            user.getRoles().forEach(role -> {
                roles.add(role.getName());
            });

            return UserResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .roles(roles).build();
        }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createUser(UserCreateRequest user) {
        log.info("Creating user " + user.getUsername());

        User userFind = userRepository.findByUsername(user.getUsername());
        User userFindByEmail = userRepository.findByEmail(user.getEmail());

        if(userFind != null) {
            throw new DuplicateResourceException("Username already exists");
        }

        if(userFindByEmail != null) {
            throw new DuplicateResourceException("Email already exists");
        }

        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (String roleName : user.getRoles()) {
                Role role = roleRepository.findByName(roleName);
                if (role == null) {
                    throw new IllegalArgumentException("Role '" + roleName + "' not found");
                }
                roles.add(role);
            }
        } else {
            // Gán vai trò mặc định nếu không có roles
            Role defaultRole = roleRepository.findByName("User");
            if (defaultRole == null) {
                throw new IllegalArgumentException("Default role 'User' not found");
            }
            roles.add(defaultRole);
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setRoles(roles);
        userRepository.save(newUser);

        log.info("Created user " + user.getUsername());

        return newUser.getId();
    }

    @Override
    public void updateUser(UserUpdateRequest user) {
        log.info("Updating user " + user.getUsername());

        User userFind = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (String roleName : user.getRoles()) {
                Role role = roleRepository.findByName(roleName);
                if (role == null) {
                    throw new IllegalArgumentException("Role " + roleName + " not found");
                }
                roles.add(role);
            }
        } else {
            // Nếu không có roles, giữ nguyên roles hiện tại hoặc gán mặc định (tùy chính sách)
            roles.addAll(userFind.getRoles());
        }

        userFind.setFullName(user.getFullName());
        userFind.setUsername(user.getUsername());
        userFind.setPhone(user.getPhone());
        userFind.setEmail(user.getEmail());
        userFind.setRoles(roles);
        userRepository.save(userFind);

        log.info("Updated user " + user.getUsername());
    }



    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user " + id);

        userRepository.deleteById(id);

        log.info("Deleted user " + id);
    }

    @Override
    public void changePassword(UserPasswordUpdate request) {

    }
}

package com.shopease.controller;

import com.shopease.dto.RegisterRequestDTO;
import com.shopease.dto.UserResponseDTO;
import com.shopease.dto.LoginRequestDTO;
import com.shopease.dto.LoginResponseDTO;
import com.shopease.entity.User;
import com.shopease.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody RegisterRequestDTO request) {
        User user = authService.registerUser(request);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.loginUser(request);
    }
}


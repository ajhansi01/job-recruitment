package com.example.capstoneWorkline.service;

import com.example.capstoneWorkline.dto.LoginRequest;
import com.example.capstoneWorkline.dto.RegisterRequest;
import com.example.capstoneWorkline.entity.User;
import com.example.capstoneWorkline.repository.UserRepository;
import com.example.capstoneWorkline.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole(request.role);
        userRepository.save(user);
        return jwtTokenProvider.generateToken(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtTokenProvider.generateToken(user);
    }
}


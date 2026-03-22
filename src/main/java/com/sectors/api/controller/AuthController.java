package com.sectors.api.controller;

import com.sectors.api.model.dto.UserDto;
import com.sectors.api.model.entity.User;
import com.sectors.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Auth", description = "Auth operations")
@AllArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public void register(@RequestBody UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }
}

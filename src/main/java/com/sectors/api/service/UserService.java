package com.sectors.api.service;

import com.sectors.api.model.dto.UserRequest;
import com.sectors.api.model.dto.UserSettingsDto;
import com.sectors.api.model.entity.User;
import com.sectors.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void register(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setAcceptTerms(false);
        userRepository.save(user);
    }

    public UserSettingsDto getUserSettings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserSettingsDto(user.getFirstName(), user.getLastName(), List.of(), user.isAcceptTerms());
    }
}

package com.sectors.api.controller;

import com.sectors.api.model.dto.UserSettingsDto;
import com.sectors.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "User", description = "User settings")
@RestController
@RequestMapping("/api/user/settings")
@AllArgsConstructor
public class UserSettingsController {

    private final UserService userService;

    @Operation(summary = "Get user settings")
    @GetMapping()
    public UserSettingsDto getUserSettings(Authentication auth) {
        return userService.getSettings(auth.getName());
    }

    @Operation(summary = "Set user settings")
    @PostMapping()
    public UserSettingsDto saveUserSettings(@RequestBody UserSettingsDto userSettingsDto, Authentication auth) {
        return userService.saveSettings(auth.getName(), userSettingsDto);
    }
}

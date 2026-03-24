package com.sectors.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
}

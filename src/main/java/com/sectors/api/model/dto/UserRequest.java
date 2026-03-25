package com.sectors.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.sectors.api.constants.ValidationConstants.*;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = USERNAME_REQUIRED)
    @Size(max = 100, message = EMAIL_SIZE)
    @Email(message = EMAIL_INVALID)
    private String username;

    @NotBlank(message = FIRST_NAME_REQUIRED)
    @Size(max = 50, message = FIRST_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = FIST_NAME_INVALID)
    private String firstName;

    @NotBlank(message = LAST_NAME_REQUIRED)
    @Size(max = 50, message = LAST_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = LAST_NAME_INVALID)
    private String lastName;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 8, max = 100, message = PASSWORD_SIZE)
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_INVALID)
    private String password;
}

package com.sectors.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.sectors.api.constants.ValidationConstants.*;
import static com.sectors.api.constants.ValidationConstants.LAST_NAME_INVALID;
import static com.sectors.api.constants.ValidationConstants.LAST_NAME_SIZE;

@Getter
@Setter
@AllArgsConstructor
public class UserSettingsDto {

    @NotBlank(message = FIRST_NAME_REQUIRED)
    @Size(max = 50, message = FIRST_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = FIST_NAME_INVALID)
    private String firstName;

    @NotBlank(message = LAST_NAME_REQUIRED)
    @Size(max = 50, message = LAST_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = LAST_NAME_INVALID)
    private String lastName;

    @NotNull(message = SECTORS_CANNOT_BE_NULL)
    @NotEmpty(message = SECTORS_CANNOT_BE_EMPTY)
    private List<Long> selectedSectors;

    private boolean acceptTerms;
}

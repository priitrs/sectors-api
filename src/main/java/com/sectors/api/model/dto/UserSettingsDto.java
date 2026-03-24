package com.sectors.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserSettingsDto {
    private String firstName;
    private String lastName;
    private List<Long> selectedSectors;
    private boolean acceptTerms;
}

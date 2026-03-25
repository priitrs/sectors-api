package com.sectors.api.service;

import com.sectors.api.model.dto.UserRequest;
import com.sectors.api.model.dto.UserSettingsDto;
import com.sectors.api.model.entity.User;
import com.sectors.api.model.entity.UserSector;
import com.sectors.api.model.entity.UserTermsAcceptance;
import com.sectors.api.repository.UserRepository;
import com.sectors.api.repository.UserSectorRepository;
import com.sectors.api.repository.UserTermsAcceptanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSectorRepository userSectorRepository;
    private final UserTermsAcceptanceRepository userTermsAcceptanceRepository;
    private final PasswordEncoder encoder;

    public void registerNew(UserRequest userRequest) {
        //TODO: validate user input
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        userRepository.save(user);
    }

    public UserSettingsDto getSettings(String username) {
        User user = getUser(username);

        List<Long> userSectors = userSectorRepository.findByUserId(user.getId()).stream()
                .map(UserSector::getSectorId)
                .toList();

        Boolean isAcceptTerms = userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId())
                .map(UserTermsAcceptance::isAcceptTerms)
                .orElse(false);

        return new UserSettingsDto(user.getFirstName(), user.getLastName(), userSectors, isAcceptTerms);
    }

    @Transactional
    public void saveSettings(String username, UserSettingsDto userSettingsDto) {
        //TODO: validate user input
        User user = getUser(username);

        handleUserNameChanges(user, userSettingsDto);
        handleSectorSelectionChanges(user.getId(), userSettingsDto.getSelectedSectors());
        handleAcceptTermsChanges(user.getId(), userSettingsDto.isAcceptTerms());
    }

    private void handleUserNameChanges(User user, UserSettingsDto userSettingsDto) {
        user.setFirstName(userSettingsDto.getFirstName());
        user.setLastName(userSettingsDto.getLastName());
        userRepository.save(user);
    }

    private void handleAcceptTermsChanges(UUID userId, boolean acceptTerms) {
        UserTermsAcceptance userTermsAcceptance = new UserTermsAcceptance();
        userTermsAcceptance.setUserId(userId);
        userTermsAcceptance.setAcceptTerms(acceptTerms);
        userTermsAcceptanceRepository.save(userTermsAcceptance);
    }

    private void handleSectorSelectionChanges(UUID userId, List<Long> selectedSectors) {
        userSectorRepository.deleteAllByUserId(userId);

        selectedSectors.forEach(sectorId -> {
            UserSector userSector = new UserSector();
            userSector.setUserId(userId);
            userSector.setSectorId(sectorId);
            userSectorRepository.save(userSector);
        });
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

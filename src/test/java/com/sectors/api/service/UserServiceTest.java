package com.sectors.api.service;

import com.sectors.api.model.dto.UserRequest;
import com.sectors.api.model.dto.UserSettingsDto;
import com.sectors.api.model.entity.User;
import com.sectors.api.model.entity.UserSector;
import com.sectors.api.model.entity.UserTermsAcceptance;
import com.sectors.api.repository.UserRepository;
import com.sectors.api.repository.UserSectorRepository;
import com.sectors.api.repository.UserTermsAcceptanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSectorRepository userSectorRepository;

    @Mock
    private UserTermsAcceptanceRepository userTermsAcceptanceRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setUsername("john.doe@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("encodedPassword");
    }

    @Test
    void registerNew_success_savesUser() {
        UserRequest request = buildUserRequest("jane.doe@example.com", "Jane", "Doe", "Password1!");
        when(userRepository.findByUsername("jane.doe@example.com")).thenReturn(Optional.empty());
        doReturn("hashedPassword").when(encoder).encode("Password1!");

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("jane.doe@example.com", saved.getUsername());
        assertEquals("Jane", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertEquals("hashedPassword", saved.getPassword());
    }

    @Test
    void registerNew_lowercasesAndTrimsUsername() {
        UserRequest request = buildUserRequest("  Jane.Doe@Example.COM  ", "Jane", "Doe", "Password1!");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        doReturn("hash").when(encoder).encode(any());

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("jane.doe@example.com", captor.getValue().getUsername());
    }

    @Test
    void registerNew_usernameTaken_throwsIllegalArgumentException() {
        UserRequest request = buildUserRequest("john.doe@example.com", "John", "Doe", "Password1!");
        when(userRepository.findByUsername("john.doe@example.com")).thenReturn(Optional.of(testUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.registerNew(request));
        assertEquals("Username already taken", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerNew_sanitizesFirstAndLastName() {
        UserRequest request = buildUserRequest("new@example.com", "  john-paul  ", "  van der Berg  ", "Password1!");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        doReturn("hash").when(encoder).encode(any());

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("John-Paul", captor.getValue().getFirstName());
        assertEquals("Van Der Berg", captor.getValue().getLastName());
    }


    @Test
    void getSettings_returnsCorrectDto() {
        UserSector activeSector = buildUserSector(testUserId, 1L, true);
        UserSector inactiveSector = buildUserSector(testUserId, 2L, false);
        UserTermsAcceptance terms = new UserTermsAcceptance();
        terms.setUserId(testUserId);
        terms.setAcceptTerms(true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(activeSector, inactiveSector));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.of(terms));

        UserSettingsDto result = userService.getSettings(testUser.getUsername());

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(List.of(1L), result.getSelectedSectors());
        assertTrue(result.isAcceptTerms());
    }

    @Test
    void getSettings_noTermsAcceptance_defaultsFalse() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of());
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.empty());

        UserSettingsDto result = userService.getSettings(testUser.getUsername());

        assertFalse(result.isAcceptTerms());
        assertTrue(result.getSelectedSectors().isEmpty());
    }

    @Test
    void getSettings_deduplicatesActiveSectors() {
        UserSector s1 = buildUserSector(testUserId, 5L, true);
        UserSector s2 = buildUserSector(testUserId, 5L, true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(s1, s2));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.empty());

        UserSettingsDto result = userService.getSettings(testUser.getUsername());

        assertEquals(List.of(5L), result.getSelectedSectors());
    }

    @Test
    void getSettings_userNotFound_throwsUsernameNotFoundException() {
        when(userRepository.findByUsername("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getSettings("unknown@example.com"));
    }


    @Test
    void saveSettings_updatesFirstAndLastName() {
        UserSettingsDto dto = new UserSettingsDto("  alice  ", "  smith  ", List.of(1L), true);
        UserSector activeSector = buildUserSector(testUserId, 1L, true);
        UserTermsAcceptance terms = new UserTermsAcceptance();
        terms.setUserId(testUserId);
        terms.setAcceptTerms(true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(activeSector));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.of(terms));

        UserSettingsDto result = userService.saveSettings(testUser.getUsername(), dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("Alice", captor.getValue().getFirstName());
        assertEquals("Smith", captor.getValue().getLastName());
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    @Test
    void saveSettings_deactivatesOldSectorsAndSavesNew() {
        UserSettingsDto dto = new UserSettingsDto("Alice", "Smith", List.of(3L, 1L, 2L), false);
        UserSector s1 = buildUserSector(testUserId, 1L, true);
        UserSector s2 = buildUserSector(testUserId, 2L, true);
        UserSector s3 = buildUserSector(testUserId, 3L, true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(s1, s2, s3));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.empty());

        UserSettingsDto result = userService.saveSettings(testUser.getUsername(), dto);

        verify(userSectorRepository).deactivateAllActiveByUserId(testUserId);

        ArgumentCaptor<UserSector> captor = ArgumentCaptor.forClass(UserSector.class);
        verify(userSectorRepository, times(3)).save(captor.capture());
        List<Long> savedIds = captor.getAllValues().stream().map(UserSector::getSectorId).toList();
        assertEquals(List.of(1L, 2L, 3L), savedIds);
        assertNotNull(result);
    }

    @Test
    void saveSettings_deduplicatesSectorsBeforeSaving() {
        UserSettingsDto dto = new UserSettingsDto("Alice", "Smith", List.of(1L, 1L, 2L), false);
        UserSector s1 = buildUserSector(testUserId, 1L, true);
        UserSector s2 = buildUserSector(testUserId, 2L, true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(s1, s2));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.empty());

        userService.saveSettings(testUser.getUsername(), dto);

        verify(userSectorRepository, times(2)).save(any(UserSector.class));
    }

    @Test
    void saveSettings_savesTermsAcceptance() {
        UserSettingsDto dto = new UserSettingsDto("Alice", "Smith", List.of(), true);
        UserTermsAcceptance terms = new UserTermsAcceptance();
        terms.setUserId(testUserId);
        terms.setAcceptTerms(true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of());
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.of(terms));

        UserSettingsDto result = userService.saveSettings(testUser.getUsername(), dto);

        ArgumentCaptor<UserTermsAcceptance> captor = ArgumentCaptor.forClass(UserTermsAcceptance.class);
        verify(userTermsAcceptanceRepository).save(captor.capture());
        assertEquals(testUserId, captor.getValue().getUserId());
        assertTrue(captor.getValue().isAcceptTerms());
        assertNotNull(result);
        assertTrue(result.isAcceptTerms());
    }

    @Test
    void saveSettings_returnsUpdatedSettings() {
        UserSettingsDto dto = new UserSettingsDto("Alice", "Smith", List.of(1L, 2L), true);
        UserSector s1 = buildUserSector(testUserId, 1L, true);
        UserSector s2 = buildUserSector(testUserId, 2L, true);
        UserTermsAcceptance terms = new UserTermsAcceptance();
        terms.setUserId(testUserId);
        terms.setAcceptTerms(true);

        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userSectorRepository.findByUserId(testUserId)).thenReturn(List.of(s1, s2));
        when(userTermsAcceptanceRepository.findFirstByUserIdOrderByCreatedAtDesc(testUserId))
                .thenReturn(Optional.of(terms));

        UserSettingsDto result = userService.saveSettings(testUser.getUsername(), dto);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(List.of(1L, 2L), result.getSelectedSectors());
        assertTrue(result.isAcceptTerms());
    }

    @Test
    void saveSettings_userNotFound_throwsUsernameNotFoundException() {
        when(userRepository.findByUsername("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.saveSettings("ghost@example.com",
                        new UserSettingsDto("A", "B", List.of(), false)));
    }


    @Test
    void sanitizeName_nullInput_returnsNull() {
        UserRequest request = buildUserRequest("n@example.com", null, "Doe", "Password1!");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        doReturn("hash").when(encoder).encode(any());

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertNull(captor.getValue().getFirstName());
    }

    @Test
    void sanitizeName_collapseExtraWhitespaceAndCapitalize() {
        UserRequest request = buildUserRequest("n@example.com", "  mary   ann  ", "O'brien", "Password1!");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        doReturn("hash").when(encoder).encode(any());

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("Mary Ann", captor.getValue().getFirstName());
        assertEquals("O'brien", captor.getValue().getLastName());
    }

    @Test
    void sanitizeName_hyphenatedName_capitalizesEachPart() {
        UserRequest request = buildUserRequest("n@example.com", "anne-marie", "Doe", "Password1!");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        doReturn("hash").when(encoder).encode(any());

        userService.registerNew(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("Anne-Marie", captor.getValue().getFirstName());
    }


    private UserRequest buildUserRequest(String username, String firstName, String lastName, String password) {
        UserRequest r = new UserRequest();
        r.setUsername(username);
        r.setFirstName(firstName);
        r.setLastName(lastName);
        r.setPassword(password);
        return r;
    }

    private UserSector buildUserSector(UUID userId, Long sectorId, boolean active) {
        UserSector us = new UserSector();
        us.setUserId(userId);
        us.setSectorId(sectorId);
        us.setActive(active);
        return us;
    }
}
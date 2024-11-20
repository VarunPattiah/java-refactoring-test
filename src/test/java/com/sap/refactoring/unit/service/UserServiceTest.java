package com.sap.refactoring.unit.service;

import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.persistence.repository.UserRepository;
import com.sap.refactoring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private UserInfo user;

    @BeforeEach
    void setUp() {
        user = new UserInfo();
        user.setUserId(1);
        user.setEmail("test@example.com");
    }

    @Test
    void testGetUsers() {
        // Given
        UserInfo user1 = new UserInfo();
        user1.setUserId(1);
        user1.setEmail("user1@example.com");

        UserInfo user2 = new UserInfo();
        user2.setUserId(2);
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<UserInfo> users = userService.getUsers();

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser_validEmail() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(List.of());

        // When
        userService.createUser(user);

        // Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_invalidEmail() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(List.of(user));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        assertEquals("Email already Used", exception.getMessage());
    }

    @Test
    void testUpdateUser_existingUser() {
        // Given
        user.setEmail("updated@example.com");
        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(List.of());
        when(userRepository.save(user)).thenReturn(user);

        // When
        UserInfo updatedUser = userService.updateUser(user);

        // Then
        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_userNotExist() {
        // Given
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        // When
        UserInfo updatedUser = userService.updateUser(user);

        // Then
        assertNull(updatedUser);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void testGetUser_existingUser() {
        // Given
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // When
        UserInfo fetchedUser = userService.getUser(user.getUserId());

        // Then
        assertNotNull(fetchedUser);
        assertEquals(user.getUserId(), fetchedUser.getUserId());
        verify(userRepository, times(1)).findById(user.getUserId());
    }

    @Test
    void testGetUser_userNotExist() {
        // Given
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // When
        UserInfo fetchedUser = userService.getUser(user.getUserId());

        // Then
        assertNull(fetchedUser);
        verify(userRepository, times(1)).findById(user.getUserId());
    }

    @Test
    void testDeleteUser() {
        // Given
        Integer userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testValidateEmail_emptyEmail() {
        // Given
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(new UserInfo()));

        // Then
        assertEquals("Email Field is Missing", exception.getMessage());
    }

    @Test
    void testValidateEmail_existingEmail() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(List.of(user));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        assertEquals("Email already Used", exception.getMessage());
    }
}

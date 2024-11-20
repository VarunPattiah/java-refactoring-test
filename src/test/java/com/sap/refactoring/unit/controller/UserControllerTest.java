package com.sap.refactoring.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.refactoring.api.controller.UserController;
import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper; // To convert objects to JSON

    private UserInfo user;

    @BeforeEach
    void setUp() {
        user = new UserInfo();
        user.setUserId(1);
        user.setName("Varun");
        user.setEmail("test@example.com");
        user.setRoles(Arrays.asList("ROLEA"));
    }

    @Test
    void testGetUsers() throws Exception {
        // Given
        List<UserInfo> users = Arrays.asList(user);
        when(userService.getUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }

    @Test
    void testCreateUser_valid() throws Exception {
        // Given
        when(userService.createUser(any(UserInfo.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User was created successfully"));
    }

    @Test
    void testCreateUser_invalid() throws Exception {
        // Given an invalid user with no role
        user.setRoles(null); // No roles set

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User must have a role"));
    }

    @Test
    void testGetUser_found() throws Exception {
        // Given
        when(userService.getUser(1)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/v1/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testGetUser_notFound() throws Exception {
        // Given
        when(userService.getUser(1)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/v1/users/{userId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser_valid() throws Exception {
        // Given
        when(userService.updateUser(any(UserInfo.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testUpdateUser_invalid() throws Exception {
        // Given an invalid user with no role
        user.setRoles(null); // No roles set

        // When & Then
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User must have a role"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1);

        // When & Then
        mockMvc.perform(delete("/api/v1/users/{userId}", 1))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }
}


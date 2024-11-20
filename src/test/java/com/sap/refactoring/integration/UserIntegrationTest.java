package com.sap.refactoring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private UserInfo user;

	@BeforeEach
	void setUp() {
		// Set up a sample user before each test
		user = new UserInfo();
		user.setName("Varun Pattiah");
		user.setEmail("varun.pattiah@gmail.com");
		user.setRoles(List.of("USER", "ADMIN"));

		// Save the user to the database
		userRepository.save(user);
	}

	@Test
	void testCreateUser() throws Exception {
		UserInfo newUser = new UserInfo();
		newUser.setName("Varun Sankaralingam");
		newUser.setEmail("varun.sankaralingam@gmail.com");
		newUser.setRoles(List.of("ROLE"));

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUser)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$").value("User was created successfully"));
	}

	@Test
	void testCreateUserBadRequest() throws Exception {
		UserInfo invalidUser = new UserInfo();
		// Missing user name and email
		invalidUser.setRoles(List.of("USER"));

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("User name is empty"));
	}

	@Test
	void testGetUser() throws Exception {
		// Assuming the user exists in the database
		mockMvc.perform(get("/api/v1/users/{userId}", user.getUserId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(user.getUserId()))
				.andExpect(jsonPath("$.name").value(user.getName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()));
	}

	@Test
	void testGetUserNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/users/{userId}", 9999)) // assuming userId 9999 doesn't exist
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateUser() throws Exception {
		user.setName("Varun Updated");
		user.setEmail("varun.updated@gmail.com");

		mockMvc.perform(put("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Varun Updated"))
				.andExpect(jsonPath("$.email").value("varun.updated@gmail.com"));
	}

	@Test
	void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/api/v1/users/{userId}", user.getUserId()))
				.andExpect(status().isNoContent());

		// Ensure the user is deleted by trying to fetch it again
		mockMvc.perform(get("/v1/users/{userId}", user.getUserId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetUsers() throws Exception {
		mockMvc.perform(get("/api/v1/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThan(0)))) // Check that we have users
				.andExpect(jsonPath("$[0].name").value(user.getName()))
				.andExpect(jsonPath("$[0].email").value(user.getEmail()));
	}
}

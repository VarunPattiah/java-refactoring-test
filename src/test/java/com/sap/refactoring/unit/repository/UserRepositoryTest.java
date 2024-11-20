package com.sap.refactoring.unit.repository;

import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserInfo user1;
    private UserInfo user2;

    @BeforeEach
    void setUp() {
        user1 = new UserInfo();
        user1.setEmail("test1@example.com");
        user1.setUserId(1);

        user2 = new UserInfo();
        user2.setEmail("test2@example.com");
        user2.setUserId(2);

        // Save the test users into the in-memory database
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void testFindByEmail() {
        // Test for an existing email
        List<UserInfo> result = userRepository.findByEmail("test1@example.com");

        // Assert that the correct user is returned
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test1@example.com", result.get(0).getEmail());

        // Test for a non-existing email
        result = userRepository.findByEmail("nonexistent@example.com");

        // Assert that no users are returned
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}


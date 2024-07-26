package com.web10.taskmanagement.repository;

import com.web10.taskmanagement.entity.User;
import com.web10.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Clean up the database before each test
        userRepository.deleteAll();
    }

    @Test
    public void testFindByUsername_whenUserExists() {
        User user = createUser("testuser", "password", "testuser@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent(), "User should be present in the database");
        assertEquals("testuser", foundUser.get().getUsername(), "Username should match");
    }

    @Test
    public void testFindByUsername_whenUserDoesNotExist() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");
        assertFalse(foundUser.isPresent(), "User should not be present in the database");
    }

    @Test
    public void testFindByEmail_whenUserExists() {
        User user = createUser("testuser", "password", "testuser@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("testuser@example.com");
        assertTrue(foundUser.isPresent(), "User should be present in the database");
        assertEquals("testuser@example.com", foundUser.get().getEmail(), "Email should match");
    }

    @ParameterizedTest
    @ValueSource(strings = {"testuser1", "testuser2", "testuser3"})
    public void testFindByUsername_multipleUsers(String username) {
        User user = createUser(username, "password", "testuser@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(username);
        assertTrue(foundUser.isPresent(), "User should be present in the database");
        assertEquals(username, foundUser.get().getUsername(), "Username should match");
    }

    // Helper method to create a User
    private User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }
}

package com.example.demo.controllers;

import com.example.demo.Data;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test class for testing UserController
 * Mocks UserRepo, CartRepo and BCryptPasswordEncoder
 * Uses Data to produce a data object of User
 */
public class TestUserController {
    private UserController userController;
    private Data data;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        data = new Data();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    /**
     * Tests createUser from UserController
     * Creates a user request with String values to populate the fields
     * Stubs the encoder to return a string value for the password when returned
     * Checks the response from the UserController against the expected id (0) and
     * the username from the CreateUserRequest
     * Checks that the password value that is returned is the encoded value from the stubbing
     */
    public void create_user_happy_path(){
        when(encoder.encode("1234567")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("1234567");
        r.setConfirmPassword("1234567");

        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    /**
     * Tests getUser from UserController
     * Gets a user object from the data class and checks that the returned value from the UserController
     * equals the same username
     */
    public void get_user(){
        User user = data.user();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> byUserName = userController.findByUserName(user.getUsername());
        assertNotNull(byUserName);
        assertEquals(200, byUserName.getStatusCodeValue());
        assertEquals(user.getUsername(), byUserName.getBody().getUsername());
    }

    @Test
    /**
     * Tests getUserById from UserController
     * Gets a user object from the data class and checks that the returned value from the UserController
     * equals the same id
     */
    public void get_user_by_id(){
        User user = data.user();

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));

        ResponseEntity<User> byId = userController.findById(user.getId());
        assertNotNull(byId);
        assertEquals(200,byId.getStatusCodeValue());
        assertEquals(user.getId(), byId.getBody().getId());
    }
}

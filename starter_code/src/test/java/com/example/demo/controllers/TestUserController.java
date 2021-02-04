package com.example.demo.controllers;

import com.example.demo.TestUtils;
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

public class TestUserController {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
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
        assertEquals(0, response.getBody().getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void get_user(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("1234567");
        r.setConfirmPassword("1234567");
        ResponseEntity<User> response = userController.createUser(r);

        ResponseEntity<User> byUserName = userController.findByUserName(r.getUsername());
        assertNotNull(byUserName);
        assertEquals(200, byUserName.getStatusCodeValue());

    }
}

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setPassword("password");
        when(userRepo.findByUsername("username")).thenReturn(user);
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user));
    }

    @Test
    public void createUserTest() {
        when(encoder.encode("password")).thenReturn("thisIsHashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("username");
        req.setPassword("password");
        req.setConfirmPassword("password");
        final ResponseEntity<User> response = userController.createUser(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void passwordLengthTest() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("username");
        req.setPassword("1234");
        req.setConfirmPassword("1234");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void passwordMismatchTest() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("username");
        req.setPassword("1234");
        req.setConfirmPassword("abcd");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getUserByName() {
        final ResponseEntity<User> response = userController.findByUserName("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("username", user.getUsername());
    }

    @Test
    public void getUserById() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1, user.getId());;
    }

}

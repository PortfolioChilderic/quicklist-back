package com.quicklist.quicklist.service;

import com.quicklist.quicklist.domain.Role;
import com.quicklist.quicklist.domain.User;
import com.quicklist.quicklist.dto.UserDTO;
import com.quicklist.quicklist.mapper.UserMapper;
import com.quicklist.quicklist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_NAME = "test";
    private static final String TEST_PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashedPassword";

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User userCreate;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userCreate = User.builder()
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .password(TEST_PASSWORD)
                .role(Role.USER)
                .build();
        userDTO = UserDTO.builder().email(TEST_EMAIL).name(TEST_NAME).role(Role.USER).build();
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn(HASHED_PASSWORD);
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(userCreate);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO createdUserDTO = userService.createUser(userCreate);

        assertNotNull(createdUserDTO);
        assertEquals(userDTO, createdUserDTO);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        logger.info("testCreateUser successful");
    }
}

package com.quicklist.quicklist.service;

import com.quicklist.quicklist.domain.User;
import com.quicklist.quicklist.dto.UserDTO;
import com.quicklist.quicklist.mapper.UserMapper;
import com.quicklist.quicklist.repository.UserRepository;
import lombok.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    public UserDTO createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserCurrently() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        return Optional.ofNullable(user.map(userMapper::toDTO).orElseThrow(() -> new RuntimeException("User not found")));
    }
}

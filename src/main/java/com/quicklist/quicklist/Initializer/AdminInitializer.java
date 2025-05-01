package com.quicklist.quicklist.Initializer;

import com.quicklist.quicklist.domain.Role;
import com.quicklist.quicklist.domain.User;
import com.quicklist.quicklist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = new User();
            admin.setName("coco");
            admin.setEmail("corentinbozecpro@gmail.com");
            admin.setPassword(passwordEncoder.encode("azerty"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin créé avec succès !");
        }
    }
}

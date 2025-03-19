package com.quicklist.quicklist.repository;

import com.quicklist.quicklist.domain.Role;
import com.quicklist.quicklist.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Collection<User> findByRole(Role role);

    Optional<User> findByEmail(String email);
}

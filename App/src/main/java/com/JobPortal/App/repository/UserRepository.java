package com.JobPortal.App.repository;


import com.JobPortal.App.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user by their username
    Optional<User> findByUsername(String username);

    // Checks if a user exists with that username
    Boolean existsByUsername(String username);

    // Checks if a user exists with that email
    Boolean existsByEmail(String email);
}

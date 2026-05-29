package com.busynuts.backend.repository;

import com.busynuts.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring is smart enough to generate the SQL for this method based on its name!
    Optional<User> findByUsername(String username);
    
    // This will help us check if a username is already taken during signup
    boolean existsByUsername(String username);
}
package com.ankit.learn_springboot.UserModule.userRepo;

import com.ankit.learn_springboot.UserModule.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

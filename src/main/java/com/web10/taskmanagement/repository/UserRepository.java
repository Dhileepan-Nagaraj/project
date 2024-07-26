package com.web10.taskmanagement.repository;

import com.web10.taskmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
	Optional<User> findById(Integer userId);
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	void deleteByUsername(String username);
}

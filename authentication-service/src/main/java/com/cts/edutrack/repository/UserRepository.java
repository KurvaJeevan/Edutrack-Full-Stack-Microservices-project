package com.cts.edutrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.edutrack.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<User> findByUserId(Long userId);

	boolean existsByUserIdAndRole(Long userId, String role);

}

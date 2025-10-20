package com.kkuk.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}

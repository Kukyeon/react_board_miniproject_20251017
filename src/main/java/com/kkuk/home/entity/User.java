package com.kkuk.home.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동증가

	@Column(unique = true, nullable = false)
    private String username; // 유니크 유저 네임
    
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
}

package com.kkuk.home.controller;

import java.net.ResponseCache;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.SiteUserDto;
import com.kkuk.home.entity.User;
import com.kkuk.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		User user = new User();
		user.setUsername(siteUserDto.getUsername());
		user.setUsername(siteUserDto.getPassword());
		
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			Map<String, String> error = new HashMap<>();
			error.put("iderror", "이미 존재하는 아이디입니다.");
			
			return ResponseEntity.badRequest().body(error);
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		
		return ResponseEntity.ok("회원가입 완료");
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> me(Authentication auth){
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}
}

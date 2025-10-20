package com.kkuk.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kkuk.home.entity.User;
import com.kkuk.home.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	//spring security -> 유저에게 받은 username과 password를 조회
	//username이 존재하지 않으면 " 사용자없음" 으로 에러 발생
	//username이 존재하면 password 확인 -> 성공 -> 권한 부여
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
		
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				.authorities("USER")
				.build();
				
	}
	
	
}
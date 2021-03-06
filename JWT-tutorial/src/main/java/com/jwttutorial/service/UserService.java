package com.jwttutorial.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwttutorial.dto.UserDto;
import com.jwttutorial.entity.Authority;
import com.jwttutorial.entity.User;
import com.jwttutorial.repository.UserRepository;
import com.jwttutorial.util.SecurityUtil;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
//	UserRepository, PasswordEncoder를 주입받음
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
//	회원가입 로직을 수행하는 메소드
//	username이 DB에 존재하지 않으면 Authority와 User정보를 생성해서 UserRepository의 save메소드를 통해 DB에 정보를 저장
	@Transactional
	public User signup(UserDto userDto) {
		if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다");
		}
		
		Authority authority = Authority.builder()
				.authorityName("ROLE_USER")
				.build();
		
		User user = User.builder()
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();
		
//		여기서 중요한 것은, signup 메소드를 통해 가입한 회원은 USER ROLE을 가지고 있고,
//		data.sql에서 자동생성되는 admin 계정은 USER, ADMIN ROLE을 가지고 있다.
//		이 차이를 통해 권한 검증 부분을 테스트
		
		return userRepository.save(user);
	}
	
//	SecurityContext에 저장된 username을 기준으로 정보를 가져옴
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities(String username){
		return userRepository.findOneWithAuthoritiesByUsername(username);
	}
	
//	SecurityContext에 저장된 username의 정보만 가져옴
	@Transactional(readOnly = true)
	public Optional<User> getMyUserWithAuthorities(){
		return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
	}
	
//	두 메소드의 허용권한을 다르게 해서 권한 검증에 대한 부분을 테스트
}

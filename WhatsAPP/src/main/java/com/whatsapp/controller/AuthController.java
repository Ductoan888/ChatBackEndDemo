package com.whatsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.User;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.LoginRequest;
import com.whatsapp.response.AuthResponse;
import com.whatsapp.service.CustomUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	CustomUserService customUserService;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	TokenProvider tokenProvider;
   
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler (@RequestBody User user) throws UserException{
		String email = user.getEmail();
		String full_name = user.getFull_name();
		String password = user.getPassword();
		
		User isUser = userRepository.findByEmail(email);
		if (isUser != null ) {
			throw new UserException("Email is userd with another account" + email);
			
			
		}
		User createdUser  =new User();
		createdUser.setEmail(email);
		createdUser.setFull_name(full_name);
		createdUser.setPassword(passwordEncoder.encode(password));
		userRepository.save(createdUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);
		
	   return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.ACCEPTED);
	}
	@PostMapping("/SignIn")
	public ResponseEntity<AuthResponse> loginHandler (@RequestBody LoginRequest req){
		
		String email = req.getEmail();
		String password = req.getPassword();
		Authentication authentication = authentication(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse(jwt, true);
		
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
		
	}
	
	public Authentication authentication(String username , String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		if (userDetails ==null) {
			throw new BadCredentialsException("Invalid Username");
			
			
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password or username");
			
			
		}
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,  userDetails.getAuthorities());
	}
}

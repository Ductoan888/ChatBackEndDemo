package com.whatsapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.User;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.UpdateUserRequest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Data
public class UserServiceImpl implements UserService {
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenProvider;

	@Override
	public User findUserById(Integer id) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
			
		}
		throw new UserException("User not found with id"+id);
		
		
	}

	@Override
	public User findUserProfile(String jwt) {
		// TODO Auto-generated method stub
		String email = tokenProvider.getEmailFromToken(jwt);
		if (email == null) {
			throw new BadCredentialsException("in valid token");
			
			
		}
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email"+email);
			
		}
		return user;
	}

	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		// TODO Auto-generated method stub
		User user  = findUserById(userId);
		if (req.getFull_name() != null) {
			user.setFull_name(req.getFull_name());;
			
			
		}
		if (req.getProfile_picture() != null) {
			user.setProfile_picture(req.getProfile_picture());
			
			
		}
		return userRepository.save(user);
	}

	@Override
	public List<User> searchUser(String query) {
		// TODO Auto-generated method stub
		List<User> listuser = userRepository.searchUser(query);
		
		return listuser;
	}

}

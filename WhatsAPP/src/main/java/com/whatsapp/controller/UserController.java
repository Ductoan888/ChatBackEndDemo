package com.whatsapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.exception.UserException;
import com.whatsapp.modal.User;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.response.ApiResponse;
import com.whatsapp.service.UserService;

import lombok.Data;
@Data
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler (@RequestHeader("Authorization") String token){
		
		User user = userService.findUserProfile(token);
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
	}
	@GetMapping("/{query}")
	public ResponseEntity<List<User>> searchUserHandler (@PathVariable("query") String query){
		List<User> list = userService.searchUser(query);
		return new ResponseEntity<List<User>>(list , HttpStatus.OK);
		
	}
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest updateUserRequest, @RequestHeader("Authorization") String token) throws UserException{
		User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), updateUserRequest);
        ApiResponse apiResponse = new ApiResponse("user updated succesfully ", true);
        
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.ACCEPTED);
		
	}

}

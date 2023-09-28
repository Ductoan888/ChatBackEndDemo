package com.whatsapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Chat;
import com.whatsapp.modal.User;
import com.whatsapp.repository.ChatRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.request.SingleChatRequest;
import com.whatsapp.response.ApiResponse;
import com.whatsapp.service.ChatService;
import com.whatsapp.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@RestController
@RequestMapping("/api/chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatController {
 
	private ChatService chatService;
	
	private ChatRepository chatRepository;
	
	private UserRepository userRepository;
	
	private UserService userService;
	
	
	@PostMapping("/single")
	public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest , @RequestHeader("Authorization") String jwt) throws UserException{
		User reqUser = userService.findUserProfile(jwt);
		Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
		
	}
	@PostMapping("/group")
	public ResponseEntity<Chat> createGroupHandler (@RequestBody GroupChatRequest groupChatRequest , @RequestHeader("Authorization") String jwt) throws UserException{
		User reqUser = userService.findUserProfile(jwt);
		Chat chat = chatService.createGroup(groupChatRequest, reqUser);
		
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
		
	}
	@GetMapping("/{chatId}")
	public ResponseEntity<Chat> findChatByIdHandler (@PathVariable Integer chatId , @RequestHeader("Authorization") String jwt) throws ChatException{
		Chat chat = chatService.findChatById(chatId);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
		
	}
	@GetMapping("/user")
	public ResponseEntity<List<Chat>> findChatByUser(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.findUserProfile(jwt);
		List<Chat> chats = chatService.findAllChatByUserId(user.getId());
		return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
	}
	
	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<Chat> addUserToGroupHandler ( @PathVariable Integer chatId , @PathVariable Integer userId , @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User user = userService.findUserProfile(jwt);
		Chat chat = chatService.addUserToGroup(userId, chatId, user);
		return new ResponseEntity<>(chat,HttpStatus.OK);
	}
	@PutMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<Chat> removeUserFromGroupHandler (@PathVariable Integer chatId, @PathVariable Integer userId , @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User user = userService.findUserProfile(jwt);
		Chat chat = chatService.removeFromGroup(chatId, userId, user);
		return new ResponseEntity<>(chat,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{chatId}")
	public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId , @RequestHeader("Authorization") String jwt) throws ChatException, UserException{
		User user = userService.findUserProfile(jwt);
		 chatService.deleteChat(chatId, user.getId());
		 ApiResponse res = new ApiResponse("chat is deleted succesfully",true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}

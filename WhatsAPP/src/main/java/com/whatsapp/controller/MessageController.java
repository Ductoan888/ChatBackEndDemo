package com.whatsapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Message;
import com.whatsapp.modal.User;
import com.whatsapp.request.SendMessageRequest;
import com.whatsapp.response.ApiResponse;
import com.whatsapp.service.MessageService;
import com.whatsapp.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageController {

	private MessageService messageService;
	
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest request , @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		Message message = messageService.sendMessage(request);
		User user = userService.findUserProfile(jwt);
		request.setUserId(user.getId());
		
		return new ResponseEntity<Message>(message,HttpStatus.OK);
	}
	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<Message>> getChatsMessageHandler(  @PathVariable("chatId") Integer chatId,@RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User user = userService.findUserProfile(jwt);
		List<Message> message = messageService.getChatsMessages(chatId, user);
		
		return new ResponseEntity<List<Message>>(message,HttpStatus.OK);
	}
	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse> deleteMessageHandler(  @PathVariable("messageId") Integer messageId,@RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException{
		User user = userService.findUserProfile(jwt);
		messageService.deleteMessage(messageId, user);
		ApiResponse apiResponse = new ApiResponse("message deleted succesfully",false);
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}
}

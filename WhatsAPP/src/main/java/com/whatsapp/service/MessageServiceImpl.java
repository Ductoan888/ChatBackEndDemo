package com.whatsapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Chat;
import com.whatsapp.modal.Message;
import com.whatsapp.modal.User;
import com.whatsapp.repository.ChatRepository;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.SendMessageRequest;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class MessageServiceImpl implements MessageService {

	@Autowired 
	UserRepository userRepository;
	
    @Autowired
    ChatRepository chatRepository;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ChatService chatService;
    @Autowired
    MessageRepository messageRepository;
	@Override
	public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
		// TODO Auto-generated method stub
		User user = userService.findUserById(req.getUserId());
		Chat chat = chatService.findChatById(req.getChatId());
		
		Message message = new Message();
		message.setChat(chat);
		message.setUser(user);
		message.setContent(req.getContent());
		message.setTimestamp(LocalDateTime.now());
		return message;
	}
 
	@Override
	public List<Message> getChatsMessages(Integer chatId , User user) throws ChatException, UserException {
		// TODO Auto-generated method stub
		Chat chat= chatService.findChatById(chatId);
		
		if (!chat.getUsers().contains(user)) {
			throw new UserException("You are not releted to this chat" + chat.getId());
			
		}
		List<Message> messages = messageRepository.findByChatId(chat.getId());
		
		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		// TODO Auto-generated method stub
		Optional<Message> message = messageRepository.findById(messageId);
		if (message.isPresent()) {
			return message.get();
			
		}
		throw new MessageException("Message not found with id" + messageId);
		
	}

	@Override
	public void deleteMessage(Integer messageId , User reqUser) throws MessageException, UserException {
		// TODO Auto-generated method stub
		
         Message message  = findMessageById(messageId);
         if (message.getUser().getId().equals(reqUser.getId())) {
			messageRepository.deleteById(messageId);
		}
         throw new UserException("you cant delete another user message" + reqUser.getFull_name());
		
	}

}

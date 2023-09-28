package com.whatsapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Chat;
import com.whatsapp.modal.User;
import com.whatsapp.repository.ChatRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.GroupChatRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

	
	@Autowired
	UserRepository userRepository;
	@Autowired
	ChatRepository chatRepository;
	@Autowired
	UserService userService;
	@Override
	public Chat createChat(User reqUser, Integer userId ) throws UserException {
		// TODO Auto-generated method stub
		User user = userService.findUserById(userId);
		
		Chat isChatExist =  chatRepository.findSingleChatByUserIds(user, reqUser);
		
		if (isChatExist !=null) {
		 return isChatExist;	
		}
		Chat chat = new Chat();
		chat.setCreatedBy(reqUser);
		chat.getUsers().add(user);
		chat.getUsers().add(reqUser);
		chat.setGroup(false);
		return chat;
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		// TODO Auto-generated method stub
		Optional<Chat> chat = chatRepository.findById(chatId);
		if (chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException("Chat no found with id" +  chatId );
		
	}

	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
		// TODO Auto-generated method stub
	    User user = userService.findUserById(userId);
	    List<Chat> chats = chatRepository.findChatByUserId(user.getId());
	    
		return chats;
	}

	@Override
	public Chat createGroup(GroupChatRequest req, User reqUserId) throws UserException {
		// TODO Auto-generated method stub
		Chat group  = new Chat();
		group.setGroup(true);
		group.setChat_image(req.getChat_image());
		group.setChat_name(req.getChat_name());
		group.setCreatedBy(reqUserId);
		group.getAdmins().add(reqUserId);
		for(Integer userId:req.getUserId()) {
			User user = userService.findUserById(userId);
			group.getUsers().add(user);
		}
		return group;
	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId , User reqUser) throws UserException, ChatException {
		// TODO Auto-generated method stub
		Optional<Chat> opt = chatRepository.findById(chatId);
		User user = userService.findUserById(userId);
		
		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().add(user);
				return chat;
				
			}else {
				throw new UserException("You are not admin");
			}
			
		}
		throw new ChatException("chat not found with id" + chatId);
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {
		// TODO Auto-generated method stub
		Optional<Chat> chat = chatRepository.findById(chatId);
		
		if (chat.isPresent()) {
			Chat chat2 = chat.get();
			if (chat2.getUsers().contains(reqUser)) {
				chat2.setChat_name(groupName);
				return chatRepository.save(chat2);
			}
			
			throw new UserException("You are not memeber of this group");
			
		}
		throw new ChatException("not change group name" + groupName);
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
		// TODO Auto-generated method stub
		Optional<Chat> opt = chatRepository.findById(chatId);
		User user = userService.findUserById(userId);
		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(opt);
				return chatRepository.save(chat);
				
			}else if (chat.getUsers().contains(reqUser)) {
				if (user.getId().equals(reqUser.getId())) {
					chat.getUsers().remove(user);
					return chatRepository.save(chat);
				}
				
					throw new UserException("You cant remove another user");
				
				
			}
		}
		throw new ChatException("Chat not found with id" +chatId);
	
	}

	@Override
	public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
		// TODO Auto-generated method stub
		Optional<Chat> chat = chatRepository.findById(chatId);
		User user = userService.findUserById(userId);
		
		if (chat.isPresent()) {
			Chat chat2 = chat.get();
			chatRepository.delete(chat2);
		}
		
	}

}

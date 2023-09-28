package com.whatsapp.service;

import java.util.List;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Chat;
import com.whatsapp.modal.User;
import com.whatsapp.request.GroupChatRequest;


public interface ChatService {
	
	public Chat createChat(User reqUser, Integer userId ) throws UserException;
	
	public Chat findChatById (Integer chatId) throws ChatException;
	
	public List<Chat> findAllChatByUserId (Integer userId) throws UserException;
	
	public Chat createGroup (GroupChatRequest req , User reqUserId) throws UserException;
	
	public Chat addUserToGroup (Integer userId , Integer chatId, User reqUser) throws UserException , ChatException;
	
	public Chat renameGroup(Integer chatId , String groupName ,  User reqUser) throws UserException, ChatException;
	
	public Chat removeFromGroup (Integer chatId , Integer userId , User reqUser) throws UserException,ChatException;
	
	public void deleteChat(Integer chatId, Integer userId) throws ChatException,UserException;
	

}

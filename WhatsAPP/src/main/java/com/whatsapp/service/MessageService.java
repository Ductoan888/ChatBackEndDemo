package com.whatsapp.service;

import java.util.List;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.modal.Message;
import com.whatsapp.modal.User;
import com.whatsapp.request.SendMessageRequest;

public interface MessageService {

	public Message sendMessage(SendMessageRequest req) throws UserException,ChatException;
	
	public List<Message> getChatsMessages(Integer chatId, User user) throws ChatException , UserException;
	
	public Message findMessageById(Integer messageId) throws MessageException;
	
	public void deleteMessage(Integer messageId , User reqUser) throws MessageException, UserException;
}

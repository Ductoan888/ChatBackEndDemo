package com.whatsapp.request;

import java.util.List;

import com.whatsapp.modal.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRequest {

	private List<Integer> userId;
	
	private String chat_name;
	
	private String chat_image;
	
	
}

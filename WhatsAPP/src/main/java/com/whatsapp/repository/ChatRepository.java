package com.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whatsapp.modal.Chat;
import com.whatsapp.modal.User;
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
	@Query("select c from Chat c where c.isGroup = false And :user Member of c.users and :reqUser Member of c.users")
	public Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
	
	@Query("select c from Chat c join c.users where c.id=:userId")
	public List<Chat> findChatByUserId (@Param("userId") Integer userId);
}

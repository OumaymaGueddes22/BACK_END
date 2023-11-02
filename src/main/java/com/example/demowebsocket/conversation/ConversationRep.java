package com.example.demowebsocket.conversation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRep extends MongoRepository<Conversation, String> {
    List<Conversation> findConversationById(String Id);
}

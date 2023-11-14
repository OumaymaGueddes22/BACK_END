package com.alibou.websocket.chat;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySender(String sender);
    default void saveChatMessage(ChatMessage message) {
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoClientDatabaseFactory(new MongoClientSettings.Builder().build()), "chatdemo2");
        mongoOperations.save(message);
    }

}

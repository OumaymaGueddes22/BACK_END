package com.example.demowebsocket.mesg;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
}

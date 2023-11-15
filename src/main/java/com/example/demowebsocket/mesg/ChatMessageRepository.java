package com.example.demowebsocket.mesg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
   List<ChatMessage> findByUserIdOrDestinationOrderByTimeDesc(String userId, String destination, Pageable pageable);


}

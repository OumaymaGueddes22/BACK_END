package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

     Optional<User> findByPhoneNumber(String phoneNumber);
     User findByUsername(String username);
    User findByphoneNumber(String phoneNumber);
     List<User> findByConversationId(String convID);

     boolean existsByPhoneNumber(String phoneNumber);
    List<User> findByRole(String role);







}

package com.example.demowebsocket.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findUsersByEmail(String email);
    List<User> findUsersByPhoneNumber(String phoneNumber);
  User findByUsername(String username);
  User findByEmail(String email);

  List<User> findByConversationId(String convID);


    boolean existsByPhoneNumber(String phoneNumber);

    User findByphoneNumber(String phoneNumber);
}

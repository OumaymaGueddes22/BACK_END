package com.example.demowebsocket.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByPhoneNumber(String phoneNumber);
  User findByUsername(String username);
  List<User> findUserById(String id);

}

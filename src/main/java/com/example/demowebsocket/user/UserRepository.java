package com.example.demowebsocket.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmailOrPhoneNumber(String phoneNumber,String email);

 // Optional<User> findByPhoneNumber(String phoneNumber);
  User findByUsername(String username);
  User findByEmail(String email);



}

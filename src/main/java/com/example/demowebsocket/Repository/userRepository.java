package com.example.demowebsocket.Repository;

import com.example.demowebsocket.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface userRepository extends MongoRepository<User,String> {
}

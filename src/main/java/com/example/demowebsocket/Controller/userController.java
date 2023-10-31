package com.example.demowebsocket.Controller;


import com.example.demowebsocket.Repository.userRepository;
import com.example.demowebsocket.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequestMapping("/user")
@RestController
public class userController {

    @Autowired
    private userRepository userRepository;


    @PostMapping("/save")
    public User save(@RequestBody User user){
        return userRepository.save(user);
    }
}

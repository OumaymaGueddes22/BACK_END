package com.example.demowebsocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/createUser")
    public  User addUser(@RequestBody User user){
        return service.addUser(user);

    }

    @GetMapping("/allUsers")
    public List<User> getAllUser(){
        return service.getAllUsers();
    }


    @GetMapping("/userId")
    public User getUserById(String userId){
        return service.getUserById(userId);
    }



    @PutMapping("/updateUser")
    public User updateUser(User userRequest){
        return service.updateUser(userRequest);
    }

    @PostMapping("deleteUser")
    public User addUserToConversation(String userId, String conversationId){
        return service.addUserToConversation(userId, conversationId);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(String userId){
        service.deleteUser(userId);
    }

}


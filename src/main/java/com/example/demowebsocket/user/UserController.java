package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.mesg.ChatMessage;
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


    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return service.findById(id);
    }



    @PutMapping("/updateUser/{iduser}")
    public User updateUser(@PathVariable String iduser ,@RequestBody User userRequest){
        return service.updateUser(iduser,userRequest);
    }

    @PostMapping("/addUserToConv")
    public User addUserToConversation(String userId, String conversationId){
        return service.addUserToConversation(userId, conversationId);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public void deleteUser(@PathVariable String userId){
        service.deleteUserById(userId);
    }


    //addConvToUser
    @PostMapping("/addConvToUser/{idUser}")
    public User addConversationToUser(@PathVariable("idUser") String idUser,@RequestBody Conversation conv){
        return service.addConversationToUser(idUser, conv);
    }

    //addMsgToUser
    @PostMapping("/addMsgToUser/{idUser}")
    public User addMessageToUser(@PathVariable("idUser") String idUser,@RequestBody ChatMessage chatMsg){
        return service.addMessageToUser(idUser ,chatMsg);
    }





}


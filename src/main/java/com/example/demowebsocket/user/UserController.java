package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationService;
import com.example.demowebsocket.mesg.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private ConversationService conversationService;
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

    @GetMapping("/allUsers")//-
    public List<User> getAllUser(){
        return service.getAllUsers();
    }



    @GetMapping("/haveCommonConversation/{userId1}/{userId2}")
    public String haveCommonConversation(@PathVariable String userId1, @PathVariable String userId2) {
    //    User user1 = service.findById(userId1);
    //    User user2 = service.findById(userId2);

        boolean haveCommonConversation = service.haveCoommunConversation(userId1, userId2);

        if (haveCommonConversation) {
            return "Users have a common conversation without 'reclamation' or 'payment' type.";
        } else {
            return "Users do not have a common conversation without 'reclamation' or 'payment' type.";
        }
    }

    @GetMapping("/{id}")//done
    public User findById(@PathVariable String id) {
        return service.findById(id);
    }


    //affiche contenu image
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getUserImageAsString(@PathVariable String id) {
        User user = service.findById(id);
        if (user != null && user.getImage() != null && !user.getImage().isEmpty()) {
            String imageData = user.getImage();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN); // Set the appropriate content type

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateUser/{id}")//done
    public User updateUser(@PathVariable String id,@RequestBody User userRequest){
        return service.updateUser(id,userRequest);
    }


    @PostMapping("/addUserToConv/{idConv}/{idUser}")
    public Conversation addUserToConversation(@PathVariable String idConv, @PathVariable String idUser) {
        return service.addUserToConversation(idConv, idUser);
    }
    @DeleteMapping("/deleteUser/{userId}")//done el conersation matetna7ash//el user yetna7a mel conversation
    public void deleteUser(@PathVariable String userId){
        service.deleteUser(userId);
    }


    //addConvToUser
    @PostMapping("/addConvToUser/{idUser}/{idConv}")
    public User addConversationToUser(@PathVariable String idUser,@PathVariable String idConv){
        return service.addConversationToUser(idUser, idConv);
    }



    //addMsgToUser
    @PostMapping("/addMsgToUser/{idUser}")
    public User addMessageToUser(@PathVariable("idUser") String idUser,@RequestBody ChatMessage chatMsg){
        return service.addMessageToUser(idUser ,chatMsg);
    }



    @PostMapping("/request-code")
    public ResponseEntity<String> requestCode(@RequestParam String email) {
        service.sendPasswordResetCode(email);
        return ResponseEntity.ok("Un code de réinitialisation a été envoyé à votre e-mail.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String resetCode, @RequestParam String newPassword) {
        try {
            service.resetPassword(email, resetCode, newPassword);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Échec de réinitialisation du mot de passe : " + e.getMessage());
        }
    }

}


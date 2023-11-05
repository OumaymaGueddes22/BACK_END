package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.mesg.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private ChatMessageRepository chatRep;
    @Autowired
    private ConversationRep convRep;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User addUser(User user){

        return repository.save(user);

    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User findById(String id) {
        return repository.findById(id).orElse(null);
    }



    /*public User updateUser(User userRequest){
        User updateUser=repository.findById(userRequest.getId()).get();
        updateUser.setEmail(userRequest.getEmail());
        updateUser.setFirstname(userRequest.getFirstname());
        updateUser.setLastname(userRequest.getLastname());
        updateUser.setPhoneNumber(userRequest.getPhoneNumber());
        updateUser.setPassword(userRequest.getPassword());
        return  repository.save(updateUser);
    }*/

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }


    public User updateUser(String userId, User updatedUser) {
        // Check if the user with the provided ID exists in the database
        User existingUser = repository.findById(userId).orElse(null);

        if (existingUser == null) {
            // Handle the case when the user doesn't exist
            // You can throw an exception or return an appropriate response
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        // Update user information
        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());

        // Save the updated user to the database
        return repository.save(existingUser);
    }

    /*public User updateUser(String iduser,User userRequest){
        User existingUser=repository.findById(iduser).orElse(null);
        if(existingUser != null){
            userRequest.setId(existingUser.getId());

            return repository.save(existingUser);
        }else {
            return null;
        }
    }*/


    //addMessageToUser
    public User addMessageToUser(String idUser, ChatMessage chatMsg){
        chatMsg=chatRep.save(chatMsg);
        User user=repository.findById(idUser).get();
        List<ChatMessage> chatMessg= new ArrayList<>();
        if(user.getChatMessages() != null){
            chatMessg=user.getChatMessages();
        }
        chatMessg.add(chatMsg);
        user.setChatMessages(chatMessg);
        repository.save(user);
        return user;

    }

    public User addConversationToUser(String idUser, Conversation conv){
        conv=convRep.save(conv);
        User user=repository.findById(idUser).get();
        List<Conversation>conversations=new ArrayList<>();
        if(user.getConversation()!=null){
            conversations=user.getConversation();
        }
        conversations.add(conv);
        user.setConversation(conversations);
        repository.save(user);
        return user;
    }
    //addUserToConveration
    public User addUserToConversation(String userId, String conversationId) {
        User user = repository.findById(userId).orElse(null);
        Conversation conversation = convRep.findById(conversationId).orElse(null);

        if (user != null && conversation != null) {
            user.getConversation().add(conversation);
            repository.save(user);
        }

        return user;
    }

   /* public void deleteUser(String userId){
        repository.deleteById(userId);

    }*/

    public void deleteUserById(String userId) {
        // Check if the user with the provided ID exists in the database
        User existingUser = repository.findById(userId).orElse(null);

        if (existingUser == null) {
            // Handle the case when the user doesn't exist
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        // Delete the user
        repository.delete(existingUser);
    }

    public User findUserByUsername(String username) {
        return repository.findByUsername(username); // Assuming you have a findByUsername method in your UserRepository
    }


}

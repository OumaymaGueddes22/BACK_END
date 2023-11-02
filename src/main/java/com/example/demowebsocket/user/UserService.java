package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    @Autowired
    private ConversationRep convRep;
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
        user.setId(UUID.randomUUID().toString().split("-")[0]);
        return repository.save(user);

    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(String userId){
        return repository.findById(userId).get();
    }


    //update
    public User updateUser(User userRequest){
        User updateUser=repository.findById(userRequest.getId()).get();
        updateUser.setEmail(userRequest.getEmail());
        updateUser.setFirstname(userRequest.getFirstname());
        updateUser.setLastname(userRequest.getLastname());
        updateUser.setPhoneNumber(userRequest.getPhoneNumber());
        updateUser.setPassword(userRequest.getPassword());
        return  repository.save(updateUser);
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

    public void deleteUser(String userId){
        repository.deleteById(userId);

    }


}

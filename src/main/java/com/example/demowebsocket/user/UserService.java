package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.mesg.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Autowired
    private  UserRepository repository;

    private final JavaMailSender mailSender;

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }
    public void sendPasswordResetCode(String email) {
        User user = findByEmail(email);
        if (user != null) {
            String resetCode = generateRandomCode();

            user.setResetCode(resetCode);
            repository.save(user);

            Thread emailThread = new Thread(() -> {
                try {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setSubject("Réinitialisation de mot de passe");
                    mailMessage.setText("Votre code de réinitialisation est : " + resetCode);
                    mailSender.send(mailMessage);
                } catch (Exception e) {

                    throw new RuntimeException("Erreur lors de l'envoi de l'e-mail de réinitialisation.");
                }
            });

            emailThread.start();
        } else {
            throw new RuntimeException("Utilisateur non trouvé pour l'adresse e-mail fournie.");
        }
    }


    public void resetPassword(String email, String resetCode, String newPassword) {
        User user = findByEmail(email);
        if (user != null && resetCode.equals(user.getResetCode())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetCode(null);
            repository.save(user);
        } else {
            throw new RuntimeException("Code de réinitialisation invalide");
        }
    }

    private String generateRandomCode() {
        int randomCode = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomCode);
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



    public User updateUser(User userRequest){
        User updateUser=repository.findById(userRequest.getId()).get();
        updateUser.setEmail(userRequest.getEmail());
        updateUser.setFirstname(userRequest.getFirstname());
        updateUser.setLastname(userRequest.getLastname());
        updateUser.setPhoneNumber(userRequest.getPhoneNumber());
        updateUser.setPassword(userRequest.getPassword());
        return  repository.save(updateUser);
    }


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

    public void deleteUser(String userId){
        repository.deleteById(userId);

    }

    public User findUserByUsername(String username) {
        return repository.findByUsername(username); // Assuming you have a findByUsername method in your UserRepository
    }


}

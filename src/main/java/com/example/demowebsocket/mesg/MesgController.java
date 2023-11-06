package com.example.demowebsocket.mesg;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class MesgController {


    @Autowired
    private MesgService mesgService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/mesg.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String senderFirstName = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", senderFirstName);
        return chatMessage;
    }



    @MessageMapping("/chat.send/{userId}")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String userId) {



        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            chatMessage.setTime(new Date());
            chatMessage.setUser(user);
            chatMessageRepository.save(chatMessage);
        }

        return chatMessage;
    }



    @PostMapping("/createMsg")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessage createMessage(@RequestBody ChatMessage msg){
        return  mesgService.addChatMessage(msg);
    }

    @GetMapping("/allmesg")
    public List<ChatMessage> getAllMessge(){
        return mesgService.findAllMessages();
    }

    @GetMapping("/getMessage/{id}")
    public ChatMessage getMessage(@PathVariable String id){
        return mesgService.getMessageById(id);
    }

    @PutMapping("/updateChatMessage/{id}")
    public ChatMessage updateMessage(@PathVariable String id, @RequestBody ChatMessage msgRequest) {
        return mesgService.updateMessage(id, msgRequest);
    }

    /*@DeleteMapping("/deleteUpdate/{id}")
    public  void  deleteMesg(@PathVariable String mesgId){
        mesgService.deleteMesg( mesgId);
    }*/

    @DeleteMapping("/deleteId/{id}")
    public ResponseEntity<HttpStatus> deleteCandidat(@PathVariable String id) {
        mesgService.deleteMesg(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/ChangeVisibility/{id}")
    public ChatMessage messageVisible(@PathVariable String id , @RequestBody ChatMessage msgReq){
        return mesgService.messageVisible(id ,msgReq);
    }


   /* @PostMapping("/addUserToMsg/:{msgId}")
    public ChatMessage addUserToMessage(String msgId, User user){
        return mesgService.addUserToMessage(msgId, user);
    }*/


    @PostMapping("/addUserToMsg/{id}")
    public ChatMessage addUserToMessage(@PathVariable String id, @RequestBody ChatMessage chatMessage) {

        return mesgService.addUserToMessage(id,chatMessage);
    }



    @PostMapping("/addConvToMesg/{msgId}")
    public ChatMessage addConvToMesaage(String msgId , Conversation conv){
        return mesgService.addConvToMesaage(msgId, conv);
    }




}

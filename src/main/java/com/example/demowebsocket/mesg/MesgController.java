package com.example.demowebsocket.mesg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MesgController {


    @Autowired
    private MesgService mesgService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/mesg.sendMessage")
    @SendTo("/topic/public")

    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
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

   /*@GetMapping("/{id}")
    public ChatMessage getMessage(@PathVariable String id){
        return mesgService.getMessageById(id);
    }*/

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
    @PutMapping("/ChangeVisibility")
    public ChatMessage messageVisible(ChatMessage msgReq){
        return mesgService.messageVisible(msgReq);
    }






}

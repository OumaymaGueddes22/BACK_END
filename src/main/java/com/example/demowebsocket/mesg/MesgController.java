package com.example.demowebsocket.mesg;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MesgController {

    @MessageMapping("/mesg.sendMessage")
    @SendTo("/topic/public")
    //when we i
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccss

    ){
        headerAccss.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}

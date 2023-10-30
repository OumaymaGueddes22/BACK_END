package com.example.demowebsocket.config;


import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.mesg.MsgType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations msgTemplate;

    @EventListener
    public void handleWebSocketDisconnetListenner(
            SessionDisconnectEvent event
    ){
        //imform the other user if the user has left the chat
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
        String username=(String) headerAccessor.getSessionAttributes().get("username");
        if (username !=null){
            log.info("User disconnected:{}",username);
            var chatMessage = ChatMessage.builder().type(MsgType.LEAVER).sender(username).build();
            msgTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}

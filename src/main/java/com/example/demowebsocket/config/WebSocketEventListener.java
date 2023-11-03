package com.example.demowebsocket.config;


import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.mesg.MsgType;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    User msgUser;
    private UserService userService;
    private final SimpMessageSendingOperations msgTemplate;

    @EventListener
    public void handleWebSocketDisconnetListenner(
            SessionDisconnectEvent event
    ){ StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User disconnected: {}", username);

            // Create a ChatMessage with the new structure
            var chatMessage = new ChatMessage();
            chatMessage.setTxt("User disconnected: " + username);
            chatMessage.setType(MsgType.LEAVER);
            chatMessage.setTime(new Date());

            // Fetch the User entity by username (assuming username is unique)
            User user = userService.findUserByUsername(username);
            if (user != null) {
                chatMessage.setUser(user);
            }

            chatMessage.setIsDeleted(false);

            // Create an empty conversation or set it as needed
            chatMessage.setMsgConv(new Conversation());

            // Send the chatMessage to the specified destination
            msgTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

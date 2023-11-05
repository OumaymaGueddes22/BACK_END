package com.example.demowebsocket.config;


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
    ){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User disconnected: {}", username);

            // Fetch the User entity by username (assuming username is unique)
            User user = userService.findUserByUsername(username);

            if (user != null) {
                // Use the user's firstname in the ChatMessage
                String firstname = user.getFirstname();
                var chatMessage = ChatMessage.builder()
                        .type(MsgType.LEAVER)
                        .msgUser(user)
                        .build();

                msgTemplate.convertAndSend("/topic/public", chatMessage);
            }
        }
    }
}

package com.example.demowebsocket.auth;

import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationRequest {
    private String id;
    private Boolean isgroup;

    private String typeConv;

    @DBRef
    private List<User> user ;

    //lezem id message yetsab fel base
    private List<ChatMessage> messages;
}

package com.example.demowebsocket.conversation;


import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversation")
public class Conversation {

    @Id
    private String id;
    private Boolean isgroup;

    private List<User> user ;

    private List<ChatMessage> messages;



}

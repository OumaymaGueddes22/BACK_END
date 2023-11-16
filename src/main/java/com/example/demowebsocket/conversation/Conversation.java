package com.example.demowebsocket.conversation;


import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "conversation")
public class Conversation {

    @Id
    private String id;
    private Boolean isgroup;

    private String typeConv;

//lezem user bel ID
    @JsonBackReference
    private List<User> user ;

    //lezem id message yetsab fel base

    private List<ChatMessage> messages;

    private String firstNameUser;


    public List<User> getUser() {
        return user;
    }
}

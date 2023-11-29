package com.example.demowebsocket.mesg;


import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Messages")

public class ChatMessage {

    @Id
    private String id;
    private String txt;
    private MsgType type;
    private String typeMessage;
    private byte[] imageContent;

    private byte[] AudioContent;
    private byte[] pdfContent;


    private byte[] videoContent;
    private Date time;

    @JsonBackReference
    @DBRef
    private User user;

    @DBRef
    private Conversation conversation;

    private String sender;
    private String reciever;
    private User msgUser;
    private Boolean isDeleted;
    private Conversation msgConv;
    private String destination;

}
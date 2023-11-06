package com.example.demowebsocket.mesg;


import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.user.User;
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
@Document(collection = "Messges")
public class ChatMessage {

   @Id
    private String id;
    private String txt;
    //private String userID;
    private MsgType type;

    private Date time;

    @DBRef
    private User user;

    private String sender;

    private String convName;

    User msgUser;

    private Boolean isDeleted;
    Conversation msgConv;


}

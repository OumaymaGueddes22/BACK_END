package com.example.demowebsocket.mesg;


import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    //private String sender;

    User msgUser;

    private Boolean isDeleted;
    Conversation msgConv;


}

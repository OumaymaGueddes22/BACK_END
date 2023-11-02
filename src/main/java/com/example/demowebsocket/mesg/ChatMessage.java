package com.example.demowebsocket.mesg;


import lombok.*;
import org.springframework.data.annotation.Id;
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
    private String Id;
    private String txt;
    private String userID;
    private MsgType type;

    private Date time;
   // private String userId;

    private Boolean isDeleted;
    private String converId;


}

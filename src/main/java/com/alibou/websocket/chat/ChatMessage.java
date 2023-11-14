package com.alibou.websocket.chat;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Messages")
public class ChatMessage {


    @Id
    private String id;
    private String fileName;
    private MessageType type;
    private byte[] data ;
    private String sender;
}

package com.example.demowebsocket.mesg;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String message;
    private String sender;
    private MsgType type;
}

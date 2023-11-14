package com.example.demowebsocket.conversation;


import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.mesg.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    @Autowired
    private ChatMessageRepository chatRep;
    @Autowired
    private ConversationRep conversationRep;

    public Conversation addConversation(Conversation cver){
        cver.setId(UUID.randomUUID().toString().split("-")[0]);
        return  conversationRep.save(cver);
    }

    public List<Conversation> findAllConvesation(){
        return conversationRep.findAll();
    }


    public Conversation getConverstaionById(String convId ) {
        return conversationRep.findById(convId).orElse(null);
    }


    public Conversation updateConversation(String id, Conversation convRequest) {
        Conversation updateConversation = conversationRep.findById(id)
                .orElse(null);

        if (updateConversation != null) {

            updateConversation.setIsgroup(convRequest.getIsgroup());

            return conversationRep.save(updateConversation);
        } else {
            return null;
        }
    }




    public void deletConversation(String convId){
        conversationRep.deleteById(convId);
    }


    public Conversation addMessageToConversation(String IdConv, ChatMessage msg){
        Conversation conv=conversationRep.findById(IdConv).get();
        msg=chatRep.save(msg);
        List<ChatMessage> chatMessages=new ArrayList<>();
        if(conv.getMessages()!=null){
            chatMessages=conv.getMessages();
        }
        chatMessages.add(msg);
        conv.setMessages(chatMessages);
        conversationRep.save(conv);
        return conv ;

    }


}

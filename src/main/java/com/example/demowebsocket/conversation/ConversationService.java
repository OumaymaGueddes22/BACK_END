package com.example.demowebsocket.conversation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    @Autowired
    private ConversationRep conversationRep;

    public Conversation addConversation(Conversation cver){
        cver.setId(UUID.randomUUID().toString().split("-")[0]);
        return  conversationRep.save(cver);
    }

    public List<Conversation> findAllConvesation(){
        return conversationRep.findAll();
    }

    public Conversation getConverstaionById(String convId){
        return conversationRep.findById(convId).get();

    }

    public Conversation updateConversation(Conversation convRequest){
        Conversation updateConversation=conversationRep.findById(convRequest.getId()).get();
        updateConversation.setIsgroup(convRequest.getIsgroup());
        return conversationRep.save(convRequest);
    }

    public void deletConversation(String convId){
        conversationRep.deleteById(convId);
    }



}

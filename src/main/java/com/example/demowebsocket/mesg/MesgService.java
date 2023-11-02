package com.example.demowebsocket.mesg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MesgService {

    @Autowired
    private ChatMessageRepository mesgRep;


    public ChatMessage addChatMessage(ChatMessage msg) {
        msg.setId(UUID.randomUUID().toString().split("-")[0]);
        return mesgRep.save(msg);
    }

    public List<ChatMessage> findAllMessages(){
        return mesgRep.findAll();
    }


    public ChatMessage getMessageById(String MesgId){
        return mesgRep.findById(MesgId).get();

    }

    public ChatMessage updateMessage(ChatMessage msgRequest){

        ChatMessage existingTMesg=mesgRep.findById(msgRequest.getId()).get();
        existingTMesg.setTxt(msgRequest.getTxt());
        existingTMesg.setTime(msgRequest.getTime());
        existingTMesg.setIsDeleted(msgRequest.getIsDeleted());
        return mesgRep.save(existingTMesg);
    }


    public void  deleteMesg(String mesgId){
        mesgRep.deleteById(mesgId);
    }

    public ChatMessage messageVisible(ChatMessage msgReq){
        ChatMessage DeleMsg=mesgRep.findById(msgReq.getId()).get();
        DeleMsg.setIsDeleted(msgReq.getIsDeleted());
        return mesgRep.save(DeleMsg);
    }
}

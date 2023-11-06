package com.example.demowebsocket.mesg;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MesgService {

    @Autowired
    private ChatMessageRepository mesgRep;


    @Autowired
    private UserRepository userRep;

    @Autowired
    private ConversationRep convRep;

    public MesgService(ChatMessageRepository mesgRep,UserRepository userRep,ConversationRep convRep){
        this.convRep=convRep;
        this.userRep=userRep;
        this.mesgRep=mesgRep;
    }


    public ChatMessage addChatMessage(ChatMessage msg) {
        msg.setId(UUID.randomUUID().toString().split("-")[0]);
        return mesgRep.save(msg);
    }

    //GetAllMessages
    public List<ChatMessage> findAllMessages(){
        return mesgRep.findAll();
    }




    public ChatMessage getMessageById(String MesgId ) {

        return mesgRep.findById(MesgId).orElse(null);
    }

    /*public ChatMessage updateMessage(ChatMessage msgRequest){

        ChatMessage existingTMesg=mesgRep.findById(msgRequest.getId()).get();
        existingTMesg.setTxt(msgRequest.getTxt());
        existingTMesg.setTime(msgRequest.getTime());
        existingTMesg.setIsDeleted(msgRequest.getIsDeleted());
        return mesgRep.save(existingTMesg);
    }*/

    public ChatMessage updateMessage(String Id, ChatMessage msgRequest) {
        ChatMessage existingTMesg = mesgRep.findById(Id)
                .orElse(null);

        if (existingTMesg != null) {
            msgRequest.setId(existingTMesg.getId());

            return mesgRep.save(msgRequest);
        }
        else {
            return null;
        }
    }


    /*public ChatMessage addUserToMessage(String msgId, User user){
        user=userRep.save(user);
        ChatMessage msg=mesgRep.findById(msgId).get();
        msg.setMsgUser(user);
        mesgRep.save(msg);
        return msg ;
    }*/


    public ChatMessage addUserToMessage(String userId, ChatMessage msgRequest) {
        User user = userRep.findById(userId).orElse(null);
         msgRequest.setTime(new Date());
        if (user != null) {
            msgRequest.setUser(user);
            return mesgRep.save(msgRequest);
        } else {

            return null;
        }
    }


    public ChatMessage addConvToMesaage(String msgId ,Conversation conv){
        conv=convRep.save(conv);
        ChatMessage msg=mesgRep.findById(msgId).get();
        msg.setMsgConv(conv);
        mesgRep.save(msg);
        return msg ;
    }


    public void deleteMesg(String mesgId){
        mesgRep.deleteById(mesgId);
    }

    public ChatMessage messageVisible(String idMessage, ChatMessage msgReq){
        ChatMessage DeleMsg=mesgRep.findById(idMessage).orElse(null);
        DeleMsg.setId(msgReq.getId());
        DeleMsg.setIsDeleted(msgReq.getIsDeleted());
        return  mesgRep.save(DeleMsg);

    }


}

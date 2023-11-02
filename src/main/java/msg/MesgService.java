package msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
public class MesgService {

    @Autowired
    private MesgRep mesgRep;

    //create
   public Message addMessage(Message msg){
       msg.setId(UUID.randomUUID().toString().split("-")[0]);
       return mesgRep.save(msg);
   }

   public List<Message> findAllMessages(){
       return mesgRep.findAll();
   }


   public Message getMessageById(String MesgId){
       return mesgRep.findById(MesgId).get();

   }

   //updatae
   public Message updateMessage(Message msgRequest){

       Message existingTMesg=mesgRep.findById(msgRequest.getId()).get();
       existingTMesg.setTxt(msgRequest.getTxt());
       existingTMesg.setTime(msgRequest.getTime());
       existingTMesg.setIsdeleted(msgRequest.getIsdeleted());
       return mesgRep.save(existingTMesg);
   }



   //Delete
    public void  deleteMesg(String mesgId){
       mesgRep.deleteById(mesgId);
    }



}

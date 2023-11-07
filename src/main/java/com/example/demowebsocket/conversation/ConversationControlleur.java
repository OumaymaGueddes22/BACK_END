package com.example.demowebsocket.conversation;


import com.example.demowebsocket.mesg.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/conversation")
public class ConversationControlleur {

    @Autowired
    private ConversationService convService;

    @PostMapping("/createConv/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public Conversation createConversation(@PathVariable String id, @RequestBody Conversation conv){
        return convService.addConversation(id, conv);
    }

    @GetMapping("/allConv")
    public List<Conversation> getAllConversations(){
        return convService.findAllConvesation();
    }

    @GetMapping("/{convId}")
    public Conversation getConversationById(@PathVariable String convId){
        return convService.getConverstaionById(convId);
    }

    @PutMapping("/updateConv/{id}")
    public Conversation updateCandidat(@PathVariable String id, @RequestBody Conversation convRequest) {
        return convService.updateConversation(id, convRequest);
    }



    @DeleteMapping("/deleteId/{id}")
    public ResponseEntity<HttpStatus> deleteCandidat(@PathVariable String id) {
        convService.deletConversation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/addMsgToConv/:{IdConv}")
    public Conversation addMessageToConversation(@PathVariable("IdConv")String IdConv,@RequestBody ChatMessage msg){
        return convService.addMessageToConversation(IdConv,msg);
    }





}

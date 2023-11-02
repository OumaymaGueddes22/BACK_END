package com.example.demowebsocket.conversation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/conversation")
public class ConversationControlleur {

    @Autowired
    private ConversationService convService;

    @PostMapping("/createConv")
    @ResponseStatus(HttpStatus.CREATED)
    public Conversation createConversation(@RequestBody Conversation conv){
        return convService.addConversation(conv);
    }

    @GetMapping("/allConv")
    public List<Conversation> getAllConversations(){
        return convService.findAllConvesation();
    }

    @GetMapping("/getConvById")
    public Conversation getConversationById(@PathVariable String convId){
        return convService.getConverstaionById(convId);
    }

    @PutMapping("/updateConv")
    public Conversation updateConversation(Conversation convRequest){
        return convService.updateConversation(convRequest);
    }

     @DeleteMapping("/deleteId")
    public void deleteConversation(String convId){
        convService.deletConversation(convId);
    }


}

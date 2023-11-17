package com.example.demowebsocket.conversation;


import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
public class ConversationControlleur {

    @Autowired
    private ConversationService convService;

    @Autowired
    private ConversationRep conversationRep;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createConv/{id}")//done +add userCreate to the user list
    @ResponseStatus(HttpStatus.CREATED)
    public Conversation createConversation(@PathVariable String id ,@RequestBody Conversation conv){
        conv.getUser().add(userRepository.findById(id).orElse(null));

        return convService.addConversation(id,conv);
    }

    @GetMapping("/allConv")
    public List<Conversation> getAllConversations(){
        return convService.findAllConvesation();
    }

    @GetMapping("/{convId}")
    public Conversation getConversationById(@PathVariable String convId){
        return convService.getConverstaionById(convId);
    }

    @GetMapping("/Conversation/{userId}")//get the whole conversation
    public List<Conversation> getConvrersationByUser(@PathVariable String userId){
        return convService.getConvrersationByUser(userId);
    }

    @PutMapping("/updateConv/{id}")//done
    public Conversation updateCandidat(@PathVariable String id, @RequestBody Conversation convRequest) {
        return convService.updateConversation(id, convRequest);
    }



    @DeleteMapping("/deleteId/{id}")//done +conv gets deleted from the user
    public ResponseEntity<HttpStatus> deleteCandidat(@PathVariable String id) {
        convService.deletConversation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/addMsgToConv/:{IdConv}")
    public Conversation addMessageToConversation(@PathVariable("IdConv")String IdConv,@RequestBody ChatMessage msg){
        return convService.addMessageToConversation(IdConv,msg);
    }






}

package com.example.demowebsocket.mesg;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
public class MesgController {


    @Autowired
    private MesgService mesgService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/mesg.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor , @RequestParam("image") MultipartFile image, @RequestParam("video") MultipartFile video) {
        String senderFirstName = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", senderFirstName);
        return chatMessage;
    }


    @MessageMapping("/chat.send/{userId}")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable String userId,
            @RequestParam(value = "imageData", required = false) String imageData,
            @RequestParam(value = "videoData", required = false) String videoData
    ) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            chatMessage.setTime(new Date());
            chatMessage.setUser(user);
            chatMessage.setIsDeleted(true);

            if (imageData != null) {
                try {
                    String[] imageParts = imageData.split(",");
                    if (imageParts.length == 2) {
                        String base64Image = imageParts[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                        chatMessage.setTypeMessage("Image");
                        chatMessage.setImageContent(imageBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding image: " + e.getMessage());
                    return null;
                }
            }

            if (videoData != null) {
                System.out.println("Received video data: " + videoData);
                try {
                    String[] videoDatas = videoData.split(",");
                    if (videoDatas.length == 2) {
                        String base64Video = videoDatas[1];
                        byte[] videoBytes = Base64.getDecoder().decode(base64Video);

                        // Set the type and content in the chatMessage
                        chatMessage.setTypeMessage("Video");
                        chatMessage.setVideoContent(videoBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding video: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            chatMessageRepository.save(chatMessage);
            user.getChatMessages().add(chatMessage);
            userRepository.save(user);
        }

        return chatMessage;
    }




    /*@PostMapping("/api/upload-chat-image/{userId}")
    public ResponseEntity<String> uploadChatImage(
            @PathVariable String userId,
            @RequestParam("image") MultipartFile image) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setTime(new Date());
            chatMessage.setUser(user);
            chatMessage.setIsDeleted(true);

            try {
                chatMessage.setImage(image.getBytes()); // Store image bytes in your ChatMessage entity
                chatMessageRepository.save(chatMessage);
                user.getChatMessages().add(chatMessage);
                userRepository.save(user);

                return ResponseEntity.ok("Image uploaded and associated with the chat message successfully.");
            } catch (IOException e) {
                // Handle the exception, e.g., return an error response
                return ResponseEntity.badRequest().body("Failed to upload the image.");
            }
        } else {
            // Handle the case where the user is not found, e.g., return an error response
            return ResponseEntity.badRequest().body("User not found.");
        }
    }*/

    @PostMapping("/createMsg")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessage createMessage(@RequestBody ChatMessage msg){
        return  mesgService.addChatMessage(msg);
    }

    @GetMapping("/allmesg")
    public List<ChatMessage> getAllMessge(){
        return mesgService.findAllMessages();
    }

   /*@GetMapping("/{id}")
    public ChatMessage getMessage(@PathVariable String id){
        return mesgService.getMessageById(id);
    }*/

    @PutMapping("/updateChatMessage/{id}")
    public ChatMessage updateMessage(@PathVariable String id, @RequestBody ChatMessage msgRequest) {
        return mesgService.updateMessage(id, msgRequest);
    }

    /*@DeleteMapping("/deleteUpdate/{id}")
    public  void  deleteMesg(@PathVariable String mesgId){
        mesgService.deleteMesg( mesgId);
    }*/

    @DeleteMapping("/deleteId/{id}")
    public ResponseEntity<HttpStatus> deleteCandidat(@PathVariable String id) {
        mesgService.deleteMesg(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/ChangeVisibility")
    public ChatMessage messageVisible(ChatMessage msgReq){
        return mesgService.messageVisible(msgReq);
    }


   /* @PostMapping("/addUserToMsg/:{msgId}")
    public ChatMessage addUserToMessage(String msgId, User user){
        return mesgService.addUserToMessage(msgId, user);
    }*/


    @PostMapping("/addUserToMsg/{id}")
    public ChatMessage addUserToMessage(@PathVariable String id, @RequestBody ChatMessage chatMessage) {

        return mesgService.addUserToMessage(id,chatMessage);
    }



    @PostMapping("/addConvToMesg/:{msgId}")
    public ChatMessage addConvToMesaage(String msgId , Conversation conv){
        return mesgService.addConvToMesaage(msgId, conv);
    }

    @GetMapping("/chatuser/{id_user}")
    public List<ChatMessage> getMessagesUser(@PathVariable String id_user) {
        return chatMessageRepository.findByUserId(id_user);
    }



}

package com.example.demowebsocket.mesg;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserRepository;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private ConversationRep conversationRep;


    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/mesg.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor , @RequestParam("image") MultipartFile image) {
        String senderFirstName = chatMessage.getSender();
        String recieverID =chatMessage.getReciever();
        headerAccessor.getSessionAttributes().put("username", senderFirstName);
        headerAccessor.getSessionAttributes().put("reciever", recieverID);
        return chatMessage;
    }

    @MessageMapping("/test.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Received message on the server: " + chatMessage.getTxt());
        return chatMessage;
    }

    @MessageMapping("/chat.send/{userId}/{conversationId}")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable String userId,
            @DestinationVariable String conversationId,
            @RequestParam(value = "imageData", required = false) String imageData,
            @RequestParam(value = "videoData", required = false) String videoData,
            @RequestParam(value = "pdfData", required = false) String pdfData,
            @RequestParam(value = "audioData", required = false) String audioData) {
        User user = userRepository.findById(userId).orElse(null);
        Conversation conversation = conversationRep.findById(conversationId).orElse(null);
        if (user != null && conversation != null) {
            chatMessage.setTime(new Date());
            chatMessage.setUser(user);
            chatMessage.setIsDeleted(true);
            chatMessage.setConversation(conversation);

            if (imageData != null) {
                chatMessage.setTypeMessage("Image");
                String[] imageParts = imageData.split(",");
                if (imageParts.length == 2) {
                    String base64Image = imageParts[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    chatMessage.setImageContent(imageBytes);
                }
            }

            if (videoData != null) {
                chatMessage.setTypeMessage("Video");
                try {
                    String[] videoParts = videoData.split(",");
                    if (videoParts.length == 2) {
                        String base64Video = videoParts[1];
                        byte[] videoBytes = Base64.getDecoder().decode(base64Video);
                        chatMessage.setVideoContent(videoBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding video: " + e.getMessage());
                    return null;
                }
            }

            if (pdfData != null) {
                chatMessage.setTypeMessage("File");
                try {
                    String[] pdfParts = pdfData.split(",");
                    if (pdfParts.length == 2) {
                        String base64Pdf = pdfParts[1];
                        byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
                        chatMessage.setPdfContent(pdfBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding PDF: " + e.getMessage());
                    return null;
                }
            }

            if (audioData != null) {
                chatMessage.setTypeMessage("Audio");
                try {
                    String[] audioParts = audioData.split(",");
                    if (audioParts.length == 2) {
                        String base64Audio = audioParts[1];
                        byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
                        chatMessage.setAudioContent(audioBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding audio: " + e.getMessage());
                    return null;
                }
            }

            chatMessage.setDestination("public");
            chatMessageRepository.save(chatMessage);
        }

        return chatMessage;
    }




    /*@MessageMapping("/chat.send/{userId}")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable String userId,
            @RequestParam(value = "imageData", required = false) String imageData,
            @RequestParam(value = "videoData", required = false) String videoData,
            @RequestParam(value = "audioData", required = false) String audioData) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            chatMessage.setTime(new Date());
            chatMessage.setUser(user);
            chatMessage.setIsDeleted(true);

            if (imageData != null) {
                String[] imageParts = imageData.split(",");
                if (imageParts.length == 2) {
                    String base64Image = imageParts[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    chatMessage.setImageContent(imageBytes);
                }
            }

            if (videoData != null) {
                String[] videoParts = videoData.split(",");
                if (videoParts.length == 2) {
                    String base64Video = videoParts[1];
                    byte[] videoBytes = Base64.getDecoder().decode(base64Video);
                    chatMessage.setVideoContent(videoBytes);
                }
            }

            if (audioData != null) {
                try {
                    String[] audioParts = audioData.split(",");
                    if (audioParts.length == 2) {
                        String base64Audio = audioParts[1];
                        byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
                        chatMessage.setAudioContent(audioBytes);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding audio: " + e.getMessage());
                    return null;
                }
            }

            chatMessageRepository.save(chatMessage);
            user.getChatMessages().add(chatMessage);
            userRepository.save(user);

            return chatMessage;
        }

        return null; // Vous pouvez retourner null ou gérer d'autres cas d'erreur ici
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
    /*@GetMapping("/getMessagesUser/{id_user}")
    public Page<ChatMessage> getMessagesUser(@PathVariable String id_user, Pageable pageable) {
        return chatMessageRepository.findByUserId(id_user, pageable);
    }*/

   /* @GetMapping("/getMessagesUser/{id_user}")
    public List<ChatMessage> getMessagesUser(@PathVariable String id_user) {
        return chatMessageRepository.findByUserId(id_user);
    }*/

    @GetMapping("/getMessagesUser/{userId}/{destination}")
    public List<ChatMessage> getMessagesUser(
            @PathVariable String userId,
            @PathVariable String destination,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        int startIndex = page - 1;
        if (startIndex < 0) {
            startIndex = 0;
        }

        return chatMessageRepository.findByUserIdOrDestinationOrderByTimeDesc(userId, destination, PageRequest.of(startIndex, pageSize));
    }

   /* @GetMapping("/msgByConv/{id}")
    public List<ChatMessage> MessageByConversationId(@PathVariable String convId){
        return chatMessageRepository.findChatMessageByConversation(convId);
    }*/

    @GetMapping("/getMessagesUserconv")
    public List<ChatMessage>getMessagesUserConv(
            @RequestParam String userId,
            @RequestParam String conversationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        int startIndex = page - 1;
        if (startIndex < 0) {
            startIndex = 0;
        }

        return chatMessageRepository.findByUserIdAndConversationIdOrderByTimeDesc(userId,conversationId,  PageRequest.of(startIndex, pageSize));


    }

}


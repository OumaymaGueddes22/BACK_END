package com.alibou.websocket.chat;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ChatService {


    private final GridFsTemplate gridFsTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(GridFsTemplate gridFsTemplate, ChatMessageRepository chatMessageRepository) {
        this.gridFsTemplate = gridFsTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    public void saveChatMessage(ChatMessage message, InputStream fileStream) {
        ObjectId fileId = gridFsTemplate.store(fileStream, message.getFileName(), message.getType());
        message.setData(fileId.toString().getBytes());
        chatMessageRepository.save(message);
    }
}

package com.example.bookchangeservicev1.service;

import com.example.bookchangeservicev1.models.Chat;
import com.example.bookchangeservicev1.models.Message;
import com.example.bookchangeservicev1.repository.MessageRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getOneMessageByIdFromRepo(Long id) {
        return messageRepository.getReferenceById(id);
    }

    public List<Message> getAllMessagesFromRepo() {
        return messageRepository.findAll();
    }

    @Transactional
    public void addNewMessageToRepository(Message message, Integer autorId) {
        message.setIdPerson(autorId);
        messageRepository.save(message);
    }

    @Transactional
    public void deleteMessageByIdFromRepo(Long id) {
        Message message = getOneMessageByIdFromRepo(id);
        Chat chat = message.getChat();
        if (chat!=null) {
            chat.getMessages().remove(message);
        }
        messageRepository.delete(message);
    }

    @Transactional
    public void deleteAllMessagesByChatIdFromRepo(Chat chat) {
        messageRepository.deleteAllByChat_Id(chat.getId());
    }
}

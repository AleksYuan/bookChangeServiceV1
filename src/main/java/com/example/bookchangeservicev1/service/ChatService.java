package com.example.bookchangeservicev1.service;

import com.example.bookchangeservicev1.dto.Chat;
import com.example.bookchangeservicev1.dto.Message;
import com.example.bookchangeservicev1.dto.Person;
import com.example.bookchangeservicev1.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat getOneChatByIdFromRepo(Long id) {
        return chatRepository.getReferenceById(id);
    }

    public void addPersonToOldChat(Chat chat, Person person) {
        if (!chat.getPeople().equals(person)) chat.getPeople().add(person);
    }

    public void deletePersonFromChat(Chat chat, Person person) {
        chat.getPeople().remove(person);
    }


    @Transactional
    public void saveChatToRepository(Chat chat) {
        generateChatCode(chat);
        if (chat.getTitle() == null || chat.getTitle().isEmpty()) chat.setTitle("чат");
        chatRepository.save(chat);
    }

    @Transactional
    public Chat generateChatCode(Chat chat) {
        StringBuilder sb = new StringBuilder();
        List<Person> people = chat.getPeople();
        int size = people.size();
        int counter = 0;
        for (Person person : people.stream().sorted(Comparator.comparing(Person::getId)).collect(Collectors.toList())) {
            sb.append(person.getId());
            counter += 1;
            if (counter < size) sb.append("&");
        }
        chat.setChatCode(sb.toString());
        return chat;
    }


    public String generateChatCode(List<Integer> people) {
        StringBuilder sb = new StringBuilder();
        int size = people.size();
        int counter = 0;
        for (Integer person : people.stream().sorted().collect(Collectors.toList())) {
            sb.append(person);
            counter += 1;
            if (counter < size) sb.append("&");
        }
        return sb.toString();
    }

    public String generateChatCode(Integer idf, Integer ids) {
        List<Integer> peopleId = new ArrayList<>();
        peopleId.add(idf);
        peopleId.add(ids);
        return generateChatCode(peopleId);
    }

    public boolean splitChatCodeAndCheckUser(Integer id, String chatCode) {
        String[] strings = chatCode.split("&");
        for (String s : strings) {
            if (Integer.parseInt(s) == id) return true;
        }
        return false;
    }

    @Transactional
    public void clearDataInOneChat(Chat chat) {
        chat.getPeople().clear();
        chat.getMessages().clear();
    }

    @Transactional
    public void deleteOneChat(Chat chat) {
        chatRepository.delete(chat);
    }

    @Transactional
    public void addNewMessageToChat(Message message, Long chatId) {
        Chat current = chatRepository.getReferenceById(chatId);
        addNewMessageToChat(message, current);
        message.setChat(current);
    }

    @Transactional
    public void addNewMessageToChat(Message message, Chat chat) {
        chat.getMessages().add(message);
    }

    @Transactional
    public void deleteMessageFromChat(Message message) {
       try {
           message.getChat().getMessages().remove(message);
       } catch (NullPointerException e) {
       }
    }

    public Chat checkExistChatBetweenTwoPerson(String chatCode) {
        Optional<Chat> chat = chatRepository.findByChatCode(chatCode);
        if (chat.isPresent()) return chat.get();
        return null;
    }

    @Transactional
    public Chat createNewChatForTwoPersonById(Person pf, Person ps) {
        Chat chat = new Chat();
        addPersonToOldChat(chat, pf);
        addPersonToOldChat(chat, ps);
        saveChatToRepository(chat);
        return chat;
    }

}



package com.example.bookchangeservicev1.web;

import com.example.bookchangeservicev1.dto.Chat;
import com.example.bookchangeservicev1.dto.Message;
import com.example.bookchangeservicev1.service.ChatService;
import com.example.bookchangeservicev1.service.MessageService;
import com.example.bookchangeservicev1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;
    private final PersonService personService;

    @Autowired
    public MessageController(MessageService messageService, ChatService chatService, PersonService personService) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.personService = personService;
    }


    @GetMapping("/message/all")
    public String getAllMessagesFromRepo(Model model) {
        model.addAttribute("messages", messageService.getAllMessagesFromRepo());
        return "html/message/all";
    }

    @GetMapping("/message/new")
    public String getNewMessageForm(@ModelAttribute("message") Message message) {
        return "html/message/new";
    }

    @PostMapping("/message/new")
    public String addNewMessage(@ModelAttribute("message") @Valid Message message, BindingResult bindingResult,
                                @RequestParam("chatId") Long idc,
                                @RequestParam("personId") Integer id) {
        if (bindingResult.hasErrors()) return "html/message/new";
        messageService.addNewMessageToRepository(message, id);
        chatService.addNewMessageToChat(message, idc);
        return "redirect:/message/all";
    }

    @GetMapping("/message/{id}/edit")
    public String getEditMessageForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute(messageService.getOneMessageByIdFromRepo(id));
        return "html/message/edit";
    }

    @DeleteMapping("/message/{id}")
    public String deleteMessageById(@PathVariable("id") Long id) {
        Message message = messageService.getOneMessageByIdFromRepo(id);
        chatService.deleteMessageFromChat(message);
        messageService.deleteMessageByIdFromRepo(id);
        return "redirect:/message/all";
    }

    @GetMapping("/message/{id}")
    public String getMessageData(@PathVariable("id") Long id, Model model) {
        model.addAttribute(messageService.getOneMessageByIdFromRepo(id));
        return "html/message/one";
    }
}

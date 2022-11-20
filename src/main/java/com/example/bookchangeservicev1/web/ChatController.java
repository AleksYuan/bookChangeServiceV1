package com.example.bookchangeservicev1.web;

import com.example.bookchangeservicev1.models.Chat;
import com.example.bookchangeservicev1.models.Message;
import com.example.bookchangeservicev1.models.Person;
import com.example.bookchangeservicev1.service.ChatService;
import com.example.bookchangeservicev1.service.MessageService;
import com.example.bookchangeservicev1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/p={idf}")
public class ChatController {


    private final PersonService personService;
    private final ChatService chatService;
    private final MessageService messageService;

    @Autowired
    public ChatController(PersonService personService, ChatService chatService, MessageService messageService) {
        this.personService = personService;
        this.chatService = chatService;
        this.messageService = messageService;
    }



    @GetMapping("/chat/new")
    public String createNewChat(@PathVariable("idf") Integer id, Model model) {
        Chat chat = new Chat();
        chatService.addPersonToOldChat(chat, personService.getOnePersonByIdFromRepo(id));
        model.addAttribute("chat", chat);
        return "html/chat/new";
    }

    @PatchMapping("/chat/new")
    public String addPersonToNewChat(@ModelAttribute("chat") Chat chat, @RequestParam("idPerson") Integer id,
                                     @PathVariable("idf") Integer idf, Model model) {
        chatService.addPersonToOldChat(chat, personService.getOnePersonByIdFromRepo(id));
        model.addAttribute("chat", chat);
        return "html/chat/new";
    }

    @PostMapping("/chat/new")
    public String createNewChat(@ModelAttribute("chat") Chat chat, @PathVariable("idf") Integer idf) {
        chatService.saveChatToRepository(chat);
        personService.addChatToPerson(chat);
        return "redirect:/p={idf}";
    }


    @GetMapping("/{idc}c={ck}")
    public String getChatByChatCode(@PathVariable("idf") Integer id, @PathVariable("ck") String chatPath, Model model,
                                    @PathVariable("idc") Long idc) {
        if (chatService.splitChatCodeAndCheckUser(id, chatPath)) {
            model.addAttribute(chatService.getOneChatByIdFromRepo(idc));
            model.addAttribute(new Message());
            return "html/chat/one";
        } else {
            return "redirect:/{idf}";
        }
    }

    @PostMapping("/{idc}c={ck}")
    public String addMessageToChat(@PathVariable("idf") Integer id,
                                   @PathVariable("ck") String chatPath,
                                   @PathVariable("idc") Long idc,
                                   @ModelAttribute("message") @Valid Message message,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "html/chat/one";
        messageService.addNewMessageToRepository(message, id);
        chatService.addNewMessageToChat(message, idc);
        return String.format("redirect:/p=%d/%dc=%s", id, idc, chatPath);
    }

    @GetMapping("/chat/{idc}/edit")
    public String getEditChatForm(@PathVariable("idf") Integer id, @PathVariable("idc") Long idc, Model model) {
        model.addAttribute(chatService.getOneChatByIdFromRepo(idc));
        return "html/chat/edit";
    }

    @PostMapping("/chat/{idc}/add")
    public String addPersonToExistingChat(@PathVariable("idf") Integer idf,
                                          @PathVariable("idc") Long idc,
                                          @RequestParam("idPerson") Integer ids,
                                          Model model) {
        Person person = personService.getOnePersonByIdFromRepo(ids);
        Chat chat = chatService.getOneChatByIdFromRepo(idc);
        chatService.addPersonToOldChat(chat, person);
        personService.addNewPersonToOldChat(person, chat);
        chatService.generateChatCode(chat);
        model.addAttribute(chat);
        return String.format("redirect:/p=%d/chat/{idc}/edit", idf);
    }

    @PostMapping("/chat/{idc}/delete")
    public String deletePersonFromExistingChat(@PathVariable("idf") Integer idf,
                                               @PathVariable("idc") Long idc,
                                               @RequestParam("iddp") Integer iddp,
                                               Model model) {
        Person person = personService.getOnePersonByIdFromRepo(iddp);
        Chat chat = chatService.getOneChatByIdFromRepo(idc);
        personService.deleteOldChatFromPerson(person, chat);
        chatService.generateChatCode(chat);
        model.addAttribute(chat);
        return String.format("redirect:/p=%d/chat/{idc}/edit", idf);
    }

    @DeleteMapping("/chat/{idc}/edit")
    public String deleteChat(@PathVariable("idf") Integer idf, @PathVariable("idc") Long idc) {
        Chat chat = chatService.getOneChatByIdFromRepo(idc);
        personService.deleteChatFromPeople(chat);
        messageService.deleteAllMessagesByChatIdFromRepo(chat);
        chatService.clearDataInOneChat(chat);
        chatService.deleteOneChat(chat);
        return String.format("redirect:/p=%d", idf);
    }

    @GetMapping("/p2={ids}")
    public String checkExistChatAndCreateOrGetChat(@PathVariable("idf") Integer idf,
                                                   @PathVariable("ids") Integer ids,
                                                   Model model) {
        String chatCode = chatService.generateChatCode(idf, ids);
        Chat chat = chatService.checkExistChatBetweenTwoPerson(chatCode);
        if (chat != null) {
            model.addAttribute("chat", chat);
            return String.format("redirect:/p=%d/%dc=%s", idf, chat.getId() , chatCode);
        } else {
            Chat sc = new Chat();
            chatService.addPersonToOldChat(sc, personService.getOnePersonByIdFromRepo(idf));
            chatService.addPersonToOldChat(sc, personService.getOnePersonByIdFromRepo(ids));
            chatService.saveChatToRepository(sc);
            personService.addChatToPerson(sc);
            model.addAttribute("chat", sc);
            return String.format("redirect:/p=%d/%dc=%s", idf, sc.getId(), chatCode);
        }
    }
}

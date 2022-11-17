package com.example.bookchangeservicev1.web;

import com.example.bookchangeservicev1.dto.Library;
import com.example.bookchangeservicev1.dto.Person;
import com.example.bookchangeservicev1.service.LibraryService;
import com.example.bookchangeservicev1.service.PersonService;
import com.example.bookchangeservicev1.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PersonController {
    private final PersonService personService;
    private final PersonValidator personValidator;
    private final LibraryService libraryService;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator, LibraryService libraryService) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.libraryService = libraryService;
    }
    // читаем из бд

    @GetMapping("/person/{id}")
    public String getFormWithOnePerson(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("person", personService.getOnePersonByIdFromRepo(id));
        return "html/person/one";
    }

    @GetMapping("/person/all")
    public String getFormWithAllPeople(Model model) {
        model.addAttribute("people", personService.getAllPersonFromRepo());
        return "html/person/all";
    }

    @GetMapping("/person/new")
    public String getNewPersonCreationForm(@ModelAttribute("person") Person person) {
        return "html/person/new";
    }

    @PostMapping("/person/new")
    public String createNewPerson(@Valid @ModelAttribute("person") Person person,
                                  BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "html/person/new";
        personService.addOnePersonToRepo(person);
        return "redirect:/";
    }

    @GetMapping("/person/{id}/edit")
    public String getEditFormWithOnePerson(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("person", personService.getOnePersonByIdFromRepo(id));
        return "html/person/edit";
    }

    @PatchMapping("/person/{id}/edit")
    public String updatePersonInRepository(@ModelAttribute("person") @Valid Person person,
                                           BindingResult bindingResult,
                                           @PathVariable("id") Integer id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "html/person/edit";
        personService.updatePersonInRepo(id, person);
        return "redirect:/person/all";
    }

    @GetMapping("/login")
    public String getServicePage(Model model) {
        model.addAttribute("person", new Person());
        return "html/person/login";
    }

    @PostMapping("/login")
    public String loginIntoPerson(@ModelAttribute("person") Person person) {
        Integer id = personService.autorizePerson(person);
        if (id > 0) {
            return String.format("redirect:/p=%d", id);
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/p={id}")
    public String getAccountPage(@PathVariable("id") Integer id,
                                 Model model) {
        model.addAttribute("person", personService.getOnePersonByIdFromRepo(id));
        return "html/person/account";
    }

    @GetMapping("/p={id}/library/new")
    public String createNewLibraryForClient(@PathVariable("id") Integer id) {
        Person person = personService.getOnePersonByIdFromRepo(id);
        Library library = libraryService.addLibraryForPersonToRepository(person);
        personService.addLibraryToPerson(id, library);
        return "redirect:/p={id}";
    }

    @GetMapping("/p={idf}/find")
    public String findPeopleByNameAndSurname(Model model, @PathVariable("idf") Integer id) {
        model.addAttribute("people", personService.getAllPersonFromRepo());
        return "html/person/find";
    }
}

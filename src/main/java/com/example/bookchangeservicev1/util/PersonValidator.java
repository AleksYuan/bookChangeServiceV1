package com.example.bookchangeservicev1.util;

import com.example.bookchangeservicev1.models.Person;
import com.example.bookchangeservicev1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personService.getOnePersonByLoginFromRepo(person.getLogin()).isPresent()) {
            if (personService.getOnePersonByLoginFromRepo(person.getLogin()).get().getId() != person.getId()) {
                errors.rejectValue("login", "", "логин уже используется");
            }
        }

        if (personService.getOnePersonByPostFromRepo(person.getPost()).isPresent()) {
            if (personService.getOnePersonByPostFromRepo(person.getPost()).get().getId() != person.getId()) {
                errors.rejectValue("post", "", "пользователь с таким адресом почты уже зарегистрирован");
            }
        }
    }
}

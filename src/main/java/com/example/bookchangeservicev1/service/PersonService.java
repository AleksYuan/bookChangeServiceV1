package com.example.bookchangeservicev1.service;

import com.example.bookchangeservicev1.dto.Chat;
import com.example.bookchangeservicev1.dto.Library;
import com.example.bookchangeservicev1.dto.Person;
import com.example.bookchangeservicev1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getOnePersonByIdFromRepo(Integer id) {
        return personRepository.getReferenceById(id);
    }

    public Optional<Person> getOnePersonByLoginFromRepo(String login) {
        return personRepository.findByLogin(login);
    }

    public Optional<Person> getOnePersonByPostFromRepo(String post) {
        return personRepository.findByPost(post);
    }

    public List<Person> getAllPersonFromRepo() {
        return personRepository.findAll();
    }

    @Transactional
    public void addOnePersonToRepo(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void updatePersonInRepo(Integer id, Person person) {
        Person old = personRepository.getReferenceById(id);
        old.setName(person.getName());
        old.setSurname(person.getSurname());
        old.setPatronymic(person.getPatronymic());
        old.setCity(person.getCity());
        old.setAddress(person.getAddress());
        old.setBirthDate(person.getBirthDate());
        old.setPost(person.getPost());
        old.setLogin(person.getLogin());
        old.setPassword(person.getPassword());
    }


    public Integer autorizePerson(Person person) {
        Optional<Person> personFromRepo = getOnePersonByLoginFromRepo(person.getLogin());
        if (personFromRepo.isPresent()) {
            Person current = personFromRepo.get();
            String currentPassword = current.getPassword();
            String actualPassword = person.getPassword();
            if (currentPassword.equals(actualPassword)) {
                return current.getId();
            }
        }
        return -1;
    }

    @Transactional
    public void addLibraryToPerson(Integer id, Library library) {
        Person current = getOnePersonByIdFromRepo(id);
        current.addLibrary(library);
    }

    @Transactional
    public void addChatToPerson(Chat chat) {
        for (Person person : chat.getPeople()) {
            person.addChat(chat);
        }
    }

    @Transactional
    public void addNewPersonToOldChat(Person person, Chat chat) {
        if (!chat.getPeople().equals(person)) person.addChat(chat);
    }

    @Transactional
    public void deleteOldChatFromPerson(Person person, Chat chat) {
        person.getChats().remove(chat);
    }

    @Transactional
    public void deleteChatFromPeople(Chat chat) {
        chat.getPeople().forEach(person -> person.getChats().remove(chat));
    }


}

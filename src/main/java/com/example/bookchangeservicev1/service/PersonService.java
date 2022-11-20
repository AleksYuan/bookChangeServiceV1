package com.example.bookchangeservicev1.service;

import com.example.bookchangeservicev1.models.Book;
import com.example.bookchangeservicev1.models.Chat;
import com.example.bookchangeservicev1.models.Library;
import com.example.bookchangeservicev1.models.Person;
import com.example.bookchangeservicev1.repository.PersonRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

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

//    @Transactional
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
        personRepository.save(old);
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

//    @Transactional
    public void addLibraryToPerson(Integer id, Library library) {
        Person current = getOnePersonByIdFromRepo(id);
        current.getLibraries().add(library);
        personRepository.save(current);
    }

//    @Transactional
    public void addChatToPerson(Chat chat) {
        for (Person person : chat.getPeople()) {
            person.getChats().add(chat);
            personRepository.save(person);
        }
    }

//    @Transactional
    public void addNewPersonToOldChat(Person person, Chat chat) {
        if (!chat.getPeople().equals(person)) {
            person.getChats().add(chat);
            personRepository.save(person);
        }

    }

//    @Transactional
    public void deleteOldChatFromPerson(Person person, Chat chat) {
        person.getChats().remove(chat);
        personRepository.save(person);
    }

    @Transactional
    public void deleteChatFromPeople(Chat chat) {
        chat.getPeople().forEach(person -> person.getChats().remove(chat));
    }

    public List<Book> getAllBooksFromOnePersonById(Integer id) {

        List<Book> res = new ArrayList<>();
        personRepository.getReferenceById(id).
                getLibraries().
                forEach(library -> library.getBooks().
                        forEach(book -> res.add(book)));
        return res.stream().
                sorted(Comparator.comparing(book -> book.isStatus())).
                collect(Collectors.toList());
    }
}

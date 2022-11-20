package com.example.bookchangeservicev1.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "имя не может быть пустым")
    @Size(min = 2, max = 30, message="должно быть от 2 до 30 символов")
    private String name;

    @NotEmpty(message = "фамилия не может быть пустой")
    @Size(min = 2, max = 30, message="должно быть от 2 до 30 символов")
    private String surname;

    private String patronymic;

    @NotEmpty(message = "обязательное поле")
    private String birthDate;

    @NotEmpty(message = "обязательное поле")
    private String city;
    private String address;

    @NotEmpty(message = "обязательное поле")
//    @UniqueElements(message = "пользователь уже зарегистрирован")
    @Email(message = "значение должно быть валидным")
    private String post;

    @NotEmpty(message = "обязательное поле")
//    @UniqueElements(message = "пользователь с таким логином уже существует")
    private String login;

    @NotEmpty(message = "обязательное поле")
    @Size(min = 5, message="пароль должен содержать не менее 5 символов")
    private String password;

    @OneToMany(mappedBy = "person")
    private List<Library> libraries;

    @ManyToMany
    @JoinTable(
            name = "person_chat",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private List<Chat> chats = new ArrayList<>();

    public Person() {
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }
    public void addLibrary(Library library) {libraries.add(library); }
}

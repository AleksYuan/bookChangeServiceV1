package com.example.bookchangeservicev1.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String chatCode;
    @ManyToMany(mappedBy = "chats")
    private List<Person> people = new ArrayList<>();
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;
}

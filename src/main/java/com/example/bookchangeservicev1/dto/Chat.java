package com.example.bookchangeservicev1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public Chat() {
    }
}

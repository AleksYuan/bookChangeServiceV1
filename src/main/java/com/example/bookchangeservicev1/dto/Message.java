package com.example.bookchangeservicev1.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "текст сообщения не может быть пустым")
    private String text;
    private Integer idPerson;
    @ManyToOne
    private Chat chat;

    public Message() {}
}

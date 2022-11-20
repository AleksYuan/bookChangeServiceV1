package com.example.bookchangeservicev1.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    private Person person;
    @OneToMany(mappedBy = "library")
    private List<Book> books;
    public Library(String name) {
        this.name = name;
    }

    public Library() {}
    public void addBook(Book book) {
        books.add(book);
    }
    public void deleteBook(Book book) {
        books.remove(book);
    }
}

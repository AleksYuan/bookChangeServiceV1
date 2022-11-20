package com.example.bookchangeservicev1.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Наименование не должно быть пустым")
    private String title;
    @NotEmpty(message = "автор не должен быть пустым")
    private String author;
    @NotNull(message = "обязательное поле")
    @Min(value = 0, message = "оценка состояния книги должна быть в диапазоне от 0 до 10")
    @Max(value = 10, message = "оценка состояния книги должна быть в диапазоне от 0 до 10")
    private Integer rate;
    @Size(min = 1, max = 100, message="введите краткое описание состояния книги, не более 100 символов")
    private String condition;
    private String comment;
    @ManyToOne
    private Library library;
    private boolean status;
}

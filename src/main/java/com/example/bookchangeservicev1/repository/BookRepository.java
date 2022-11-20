package com.example.bookchangeservicev1.repository;

import com.example.bookchangeservicev1.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByStatus(boolean status);
}

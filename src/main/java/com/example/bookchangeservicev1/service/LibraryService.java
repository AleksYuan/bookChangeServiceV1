package com.example.bookchangeservicev1.service;


import com.example.bookchangeservicev1.dto.Book;
import com.example.bookchangeservicev1.dto.Library;
import com.example.bookchangeservicev1.dto.Person;
import com.example.bookchangeservicev1.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public Library getOneLibraryByIdFromRepo(Integer id) {
        return libraryRepository.getReferenceById(id);
    }

    @Transactional
    public Library addLibraryForPersonToRepository(Person person) {
        Library current = new Library();
        libraryRepository.save(current);
        current.setName(String.format("библиотека", current.getId()));
        current.setPerson(person);
        return current;
    }

    @Transactional
    public void updateLibraryInRepo(Library library) {
        System.out.println(library.getName());
        Library old = libraryRepository.getReferenceById(library.getId());
        System.out.println(old.getName());
        old.setName(library.getName());
        System.out.println(old.getName());
    }

    @Transactional
    public void deleteLibraryByIdFromRepo(Integer id) {
        libraryRepository.deleteById(id);
    }

    public Integer getPersonIdFromLibraryById(Integer id) {
        return getOneLibraryByIdFromRepo(id).getPerson().getId();
    }

    @Transactional
    public void addBookToLibrary(Integer libraryId, Book book) {
        Library library = libraryRepository.getReferenceById(libraryId);
        library.getBooks().add(book);
    }

    @Transactional
    public void deleteBookFromLibrary(Book book) {
        Library library = book.getLibrary();
        library.deleteBook(book);
    }

}


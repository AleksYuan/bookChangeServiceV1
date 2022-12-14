package com.example.bookchangeservicev1.service;

import com.example.bookchangeservicev1.models.Book;
import com.example.bookchangeservicev1.models.Library;
import com.example.bookchangeservicev1.repository.BookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooksFromRepo() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByStatus(Boolean status) {
        return bookRepository.findBooksByStatus(status);
    }

    @Transactional
    public void addNewBookToRepository(Book book) {
        bookRepository.save(book);
    }

    public Book getOneBookByIdFromRepo(Integer id) {
        return bookRepository.getReferenceById(id);
    }

    public void updateBookInRepo(Book book) {
        Book old = bookRepository.getReferenceById(book.getId());
        old.setTitle(book.getTitle());
        old.setAuthor(book.getAuthor());
        old.setRate(book.getRate());
        old.setCondition(book.getCondition());
        old.setComment(book.getComment());
        old.setStatus(book.isStatus());
        bookRepository.save(old);
    }

    @Transactional
    public void deleteBookByIdFromRepo(Integer id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBooksByLibrary(Library library) {
        for (Book book : library.getBooks()) {
            bookRepository.deleteById(book.getId());
        }
    }

//    @Transactional
    public void addLibraryToBook(Book book, Library library) {
        book.setLibrary(library);
        bookRepository.save(book);
    }

    public Integer getLibraryIdFromBook(Book book) {
        return book.getLibrary().getId();
    }


}

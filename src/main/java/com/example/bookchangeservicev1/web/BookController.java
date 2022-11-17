package com.example.bookchangeservicev1.web;

import com.example.bookchangeservicev1.dto.Book;
import com.example.bookchangeservicev1.service.BookService;
import com.example.bookchangeservicev1.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class BookController {

    private final BookService bookService;
    private final LibraryService libraryService;


    @Autowired
    public BookController(BookService bookService, LibraryService libraryService) {
        this.bookService = bookService;
        this.libraryService = libraryService;
    }

    @GetMapping("/book/all")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooksFromRepo());
        return "html/book/all";
    }

    @GetMapping("/book/new")
    public String getNewBookForm(@ModelAttribute("book") Book book) {
        return "html/book/new";
    }



    @PostMapping("/book/new")
    public String addNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "html/book/new";
        bookService.addNewBookToRepository(book);
        return "redirect:/book/all";
    }

    @GetMapping("/book/{id}")
    public String getOneBookToRead(@PathVariable("id") Integer id, Model model) {
        model.addAttribute(bookService.getOneBookByIdFromRepo(id));
        return "html/book/one";
    }

    @GetMapping("/book/{id}/watch")
    public String getOneBookForWatch(@PathVariable("id") Integer id, Model model) {
        model.addAttribute(bookService.getOneBookByIdFromRepo(id));
        return "html/book/watch";
    }

    @GetMapping("/book/b={id}")
    public String getOneBookFromLibraryToRead(@PathVariable("id") Integer id, Model model,
                                              @RequestParam("idLibrary") Integer idl) {
        model.addAttribute(bookService.getOneBookByIdFromRepo(id));
        model.addAttribute("libraryId", idl);
        return "html/book/one-in-library";
    }

    @GetMapping("/book/{id}/edit")
    public String getEditFormForOneBook(@PathVariable("id") Integer id, Model model) {
        model.addAttribute(bookService.getOneBookByIdFromRepo(id));
        return "html/book/edit";
    }

    @GetMapping("/book/b={id}/edit")
    public String getEditFormFromLibraryForOneBook(@PathVariable("id") Integer id, Model model) {
        model.addAttribute(bookService.getOneBookByIdFromRepo(id));
        return "html/book/edit-in-library";
    }

    @PatchMapping("/book/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) return "html/book/edit";
        bookService.updateBookInRepo(book);
        return "redirect:/book/all";
    }

    @PatchMapping("/book/b={id}")
    public String updateBookInLibrary(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) return "html/book/edit-in-library";
        bookService.updateBookInRepo(book);
        return String.format("redirect:/library/l=%d", bookService.getOneBookByIdFromRepo(book.getId()).getLibrary().getId());
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        bookService.deleteBookByIdFromRepo(id);
        return "redirect:/book/all";
    }

    @DeleteMapping("/book/b={id}")
    public String deleteBookInLibrary(@PathVariable("id") Integer id) {
        Integer idl = bookService.getLibraryIdFromBook(bookService.getOneBookByIdFromRepo(id));
        libraryService.deleteBookFromLibrary(bookService.getOneBookByIdFromRepo(id));
        bookService.deleteBookByIdFromRepo(id);
        return String.format("redirect:/library/l=%d", idl);
    }

    @GetMapping("/l={id}/find")
    public String findBook(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("books", bookService.getBooksByStatus(true));
        model.addAttribute("idf", libraryService.getOneLibraryByIdFromRepo(id).getPerson().getId());
        return "html/book/find-in-library";
    }
}

package com.example.bookchangeservicev1.web;

import com.example.bookchangeservicev1.models.Book;
import com.example.bookchangeservicev1.models.Library;
import com.example.bookchangeservicev1.service.BookService;
import com.example.bookchangeservicev1.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;
    private final BookService bookService;
    @Autowired
    public LibraryController(LibraryService libraryService, BookService bookService) {
        this.libraryService = libraryService;
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public String getFormWithOneLibrary(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("library", libraryService.getOneLibraryByIdFromRepo(id));
        return "html/library/one";
    }

    @GetMapping("/l={id}")
    public String getFormWithOneLibraryInPerson(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("library", libraryService.getOneLibraryByIdFromRepo(id));
        return "html/library/one-in-person";
    }

    @GetMapping("/{id}/edit")
    public String getEditFormWithOneLibrary(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("library", libraryService.getOneLibraryByIdFromRepo(id));
        return "html/library/edit";
    }

    @PatchMapping("/{id}")
    public String updateLibrary(@ModelAttribute("library") Library library) {
        libraryService.updateLibraryInRepo(library);
        return "redirect:/library/{id}";
    }

    @PatchMapping("/l={id}")
    public String updateLibraryInPerson(@ModelAttribute("library") Library library) {
        libraryService.updateLibraryInRepo(library);
        return "redirect:/library/l={id}";
    }

    @DeleteMapping("/{id}")
    public String deleteLibrary(@PathVariable("id") Integer id) {
        Integer personId = libraryService.getPersonIdFromLibraryById(id);
        bookService.deleteAllBooksByLibrary(libraryService.getOneLibraryByIdFromRepo(id));
        libraryService.deleteLibraryByIdFromRepo(id);
        return String.format("redirect:/p=%d", personId);
    }

    @GetMapping("/l={id}/new")
    public String getNewBookFormForLibrary(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("book" , new Book());
        return "html/book/new-in-library";
    }

    @PostMapping("/l={id}/new")
    public String addNewBookToLibraryAndRepo(@PathVariable("id") Integer id,
                                             @ModelAttribute("book") @Valid Book book,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "html/book/new-in-library";
        book.setId(null);
        bookService.addLibraryToBook(book, libraryService.getOneLibraryByIdFromRepo(id));
        bookService.addNewBookToRepository(book);
        libraryService.addBookToLibrary(id, book);
        return String.format("redirect:/library/l=%d", id);
    }
}

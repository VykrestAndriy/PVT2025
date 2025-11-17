package com.example.lb_7.controller;

import com.example.lb_7.dto.BookTitleAuthorDTO;
import com.example.lb_7.model.Book;
import com.example.lb_7.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("newBook", new Book());
        model.addAttribute("searchMessage", "Список Усіх Книг");
        model.addAttribute("dtoResults", Collections.emptyList());
        return "books_list";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "edit_book";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(required = false) String param,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            Model model)
    {
        List<Book> results = Collections.emptyList();
        List<BookTitleAuthorDTO> dtoResults = Collections.emptyList();
        String message = "Результати пошуку";

        try {
            switch (searchType) {
                case "byTitle":
                    results = bookRepository.findByTitleContainingIgnoreCase(param);
                    message = "Пошук за назвою (містить): " + param;
                    break;
                case "byAuthor":
                    results = bookRepository.findByAuthorNameContainingIgnoreCase(param);
                    message = "Пошук за автором (містить): " + param;
                    break;
                case "byPageCount":
                    results = bookRepository.findByPageCount(Integer.parseInt(param));
                    message = "Пошук за кількістю сторінок: " + param;
                    break;
                case "lastEdition":
                    Optional<Book> lastBook = bookRepository.findTopByAuthorNameOrderByPublicationYearDesc(param);
                    results = lastBook.map(List::of).orElse(Collections.emptyList());
                    message = "Останнє видання автора: " + param;
                    break;
                case "publisherCurrentYear":
                    int currentYear = LocalDate.now().getYear();
                    dtoResults = bookRepository.findBooksByPublisherAndYearDTO(publisher, currentYear);
                    message = "Книги видавництва '" + publisher + "' за " + currentYear + " рік (DTO)";
                    break;
                case "byYear":
                    results = bookRepository.findByPublicationYear(year);
                    message = "Пошук за роком випуску: " + year;
                    break;
                case "byGenreAuthorYear":
                    results = bookRepository.findByGenreAndAuthorNameAndPublicationYear(genre, author, year);
                    message = "Пошук: Жанр='" + genre + "', Автор='" + author + "', Рік=" + year;
                    break;
                case "bestsellers":
                    results = bookRepository.findByIsBestsellerTrue();
                    message = "Книги-бестселери";
                    break;
                case "byDescriptionWord":
                    results = bookRepository.findByDescriptionContainingIgnoreCase(param);
                    message = "Пошук за словом в описі: " + param;
                    break;
                default:
                    results = bookRepository.findAll();
                    message = "Список Усіх Книг";
                    break;
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Помилка пошуку або некоректний ввід: " + e.getMessage());
            message = "Помилка пошуку";
        }

        model.addAttribute("books", results);
        model.addAttribute("dtoResults", dtoResults);
        model.addAttribute("searchMessage", message);
        model.addAttribute("newBook", new Book()); // Виправлення: Додано newBook
        return "books_list";
    }
}
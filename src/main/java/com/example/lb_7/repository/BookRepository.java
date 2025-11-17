package com.example.lb_7.repository; // Пакет має бути com.example.lb_7

import com.example.lb_7.dto.BookTitleAuthorDTO;
import com.example.lb_7.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);

    List<Book> findByPageCount(Integer pageCount);

    Optional<Book> findTopByAuthorNameOrderByPublicationYearDesc(String authorName);

    @Query("SELECT new com.example.lb_7.dto.BookTitleAuthorDTO(b.title, b.authorName) " +
            "FROM com.example.lb_7.model.Book b WHERE b.publisher = ?1 AND b.publicationYear = ?2")
    List<BookTitleAuthorDTO> findBooksByPublisherAndYearDTO(String publisher, Integer currentYear);

    List<Book> findByPublicationYear(Integer year);

    List<Book> findByGenreAndAuthorNameAndPublicationYear(String genre, String authorName, Integer publicationYear);

    List<Book> findByIsBestsellerTrue();

    List<Book> findByDescriptionContainingIgnoreCase(String searchWord);
}
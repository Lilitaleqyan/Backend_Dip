package org.example.backend_dip.repo;

import org.example.backend_dip.entity.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findBookByAuthorOrTitleIgnoreCase(String author, String title);

    Optional<Book> findBookById(Long id);

    boolean existsBookByAuthorAndTitle(String author, String title);

    Book findBookByAuthorOrTitle(String author, String title);

}

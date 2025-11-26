package org.example.backend_dip.repo;

import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCopyRepo extends JpaRepository<BookCopy, Long> {
    long countBookCopiesById(Long bookId);

    Optional<BookCopy> findFirstByBookIdAndStatus(Long bookId, Status status);

}

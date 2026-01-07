package org.example.backend_dip.repo;

import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCopyRepo extends JpaRepository<BookCopy, Long> {
    
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId")
    long countBookCopiesById(@Param("bookId") Long bookId);
    
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = :status")
    long countBookCopiesByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") Status status);

    Optional<BookCopy> findFirstByBookIdAndStatus(Long bookId, Status status);

}

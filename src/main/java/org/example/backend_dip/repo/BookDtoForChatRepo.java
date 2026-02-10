package org.example.backend_dip.repo;

import jakarta.transaction.Transactional;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.books.BookDtoForChat;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BookDtoForChatRepo extends JpaRepository<BookDtoForChat, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE BookDtoForChat b SET b.freeCount = b.freeCount - 1 " +

            "WHERE  b.id = :id AND b.freeCount > 0 ")
    int decrementFreeCount(@Param("id") Long id);

    Optional<BookDtoForChat> findBookDtoForChatByAuthorAndTitle(String author, String title);

//    Optional<BookDtoForChat> findByBook(Book book);



}

package org.example.backend_dip.repo;

import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.books.BookDtoForChat;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BookDtoForChatRepo extends JpaRepository<BookDtoForChat, Long> {
         void save(Book book);
}

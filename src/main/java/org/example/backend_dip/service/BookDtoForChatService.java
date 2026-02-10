package org.example.backend_dip.service;

import jakarta.transaction.Transactional;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookDtoForChat;
import org.example.backend_dip.repo.BookDtoForChatRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookDtoForChatService {
    private final BookDtoForChatRepo bookDtoForChatRepo;

    public BookDtoForChatService(BookDtoForChatRepo bookDtoForChatRepo) {
        this.bookDtoForChatRepo = bookDtoForChatRepo;
    }

    @Transactional
    public void updateCountBookDtoForChat(Book book) {
        Optional<BookDtoForChat> bookDtoForChat =
                bookDtoForChatRepo.findBookDtoForChatByAuthorAndTitle(book.getAuthor(),book.getTitle());
        if (bookDtoForChat.isPresent()) {
            BookDtoForChat bookDtoForChatEntity = bookDtoForChat.get();
            bookDtoForChatEntity.setCount(bookDtoForChatEntity.getCount() + 1);
            bookDtoForChatEntity.setFreeCount(bookDtoForChatEntity.getFreeCount() + 1);
        }
        else {
            BookDtoForChat bookDtoForChat1 = BookDtoForChat.builder()
                    .title(book.getTitle()
                            )
                    .author(book.getAuthor())
                    .count(1)
                    .freeCount(1)
                    .build();
            bookDtoForChatRepo.save(bookDtoForChat1);
        }

    }
}

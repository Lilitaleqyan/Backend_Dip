package org.example.backend_dip.service;

import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.repo.BookCopyRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCopyService {
    private final BookCopyRepo bookCopyRepo;

    public BookCopyService(BookCopyRepo bookCopyRepo) {
        this.bookCopyRepo = bookCopyRepo;
    }

    public void save(BookCopy bookCopy) {
        bookCopyRepo.save(bookCopy);
    }

}

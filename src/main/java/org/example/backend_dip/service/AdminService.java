package org.example.backend_dip.service;


import org.example.backend_dip.entity.AdminForControl;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.repo.AdminRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.BookRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepo adminRepo;
    private final BookRepo bookRepo;
    private final BookReaderRepo bookReaderRepo;
    private final ReadersService readersService;

    public AdminService(AdminRepo adminRepo, BookRepo bookRepo, BookReaderRepo bookReaderRepo, ReadersService readersService) {
        this.adminRepo = adminRepo;
        this.bookRepo = bookRepo;
        this.bookReaderRepo = bookReaderRepo;
        this.readersService = readersService;
    }

    public boolean existsByUsername(String username) {
        return adminRepo.existsByUsername(username);
    }

    Optional<AdminForControl> findByUsername(String username) {
        return adminRepo.findByUsername(username);
    }

    public void save(AdminForControl admin) {
        adminRepo.save(admin);
    }

    public void addBook(Book book) {
        bookRepo.save(book);
    }

    public void removeBook(long id) {
        bookRepo.deleteById(id);
    }

    public void removeUser(long id) {
        bookReaderRepo.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public List<BookReader> getAllReaders() {
        return bookReaderRepo.findAll();
    }

    public ResponseEntity<Book> updateBook(Book book) {
        bookRepo.save(book);
        return ResponseEntity.ok(book);
    }

    public List<Book> findBookByAuthorOrTitle(String author, String title) {
        return bookRepo.findBookByAuthorOrTitleIgnoreCase(author, title);
    }

    public List<BookReader> findBookReaderBy(String lastName,
                                             String firstName,
                                             String email,
                                             String username) {
        return bookReaderRepo.findBookReadersByEmailOrFirstNameOrLastNameOrUsernameIgnoreCase(
                email,
                firstName,
                lastName,
                username);
    }
}



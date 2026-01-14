package org.example.backend_dip.service;


import org.example.backend_dip.entity.books.ReservBookDto;
import org.example.backend_dip.entity.AdminForControl;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.BookReaderForAdmin;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.AdminRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.repo.ReservationRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepo adminRepo;
    private final BookRepo bookRepo;
    private final BookReaderRepo bookReaderRepo;
    private final ReadersService readersService;
    private final ReservationRepo reservationRepo;


    public AdminService(AdminRepo adminRepo, BookRepo bookRepo, BookReaderRepo bookReaderRepo, ReadersService readersService, ReservationRepo reservationRepo) {
        this.adminRepo = adminRepo;
        this.bookRepo = bookRepo;
        this.bookReaderRepo = bookReaderRepo;
        this.readersService = readersService;
        this.reservationRepo = reservationRepo;
    }

    public boolean existsByUsername(String username) {
        return adminRepo.existsByUsername(username);
    }

    public Optional<AdminForControl> findByUsername(String username) {
        return adminRepo.findByUsername(username);
    }

    public void save(AdminForControl admin) {
        adminRepo.save(admin);
    }

    public Book addBook(Book book) {
        return bookRepo.save(book);
    }

    public void removeBook(long id) throws IOException {
        Book book = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("file not found"));
        Path path = Path.of(book.getFilePath());
        System.out.println("path  " + path);

        if (Files.exists(path) && Files.isRegularFile(path)) {
            Files.delete(path);
        }
        if ("audiobook".equalsIgnoreCase(book.getCategory())
                && book.getAudioUrl() != null
                && !book.getAudioUrl().isBlank()) {

            Path audioPath = Path.of("/home/user/Backend_Dip/" + book.getAudioUrl());

            System.out.println("Deleting audio: " + audioPath);

            Files.deleteIfExists(audioPath);
        }
        bookRepo.deleteById(id);


    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public void removeUser(long id) {
        bookReaderRepo.deleteById(id);
    }

    public List<BookReader> getAllReaders() {
        return bookReaderRepo.findAllReadersWithRelations();
    }

    public List<BookReaderForAdmin> getAllReadersForAdmin() {
        List<BookReader> readers = bookReaderRepo.findAllReadersWithRelations();
        if (readers == null || readers.isEmpty()) {
            return List.of();
        }
        return readers.stream()
                .map(r -> {
                    long availableCount = 0L;
                    long reservedCount = 0L;
                    long returnedCount = 0L;

                    if (r.getBookRelations() != null && !r.getBookRelations().isEmpty()) {
                        availableCount = r.getBookRelations().stream()
                                .filter(res -> res != null && res.getBookCopy() != null &&
                                        res.getBookCopy().getStatus() == Status.AVAILABLE)
                                .count();
                        reservedCount = r.getBookRelations().stream()
                                .filter(res -> res != null && res.getBookCopy() != null &&
                                        res.getBookCopy().getStatus() == Status.RESERVED)
                                .count();
                        returnedCount = r.getBookRelations().stream()
                                .filter(res -> res != null && res.getBookCopy() != null &&
                                        res.getBookCopy().getStatus() == Status.RETURNED)
                                .count();
                    }

                    return BookReaderForAdmin.builder()
                            .id(r.getId())
                            .firstName(r.getFirstName() != null ? r.getFirstName() : "")
                            .lastName(r.getLastName() != null ? r.getLastName() : "")
                            .email(r.getEmail() != null ? r.getEmail() : "")
                            .availableCount(availableCount)
                            .reservedCount(reservedCount)
                            .returnedCount(returnedCount)
                            .build();
                })
                .toList();
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



    public List<ReservBookDto> getReservationBooks(Long readerId) {
        return reservationRepo.findByReaderId(readerId)
                .stream()
                .map(r->new ReservBookDto(
                        r.getId(),
                        r.getBookCopy().getBook().getId(),
                        r.getBookCopy().getBook().getTitle(),
                        r.getBookCopy().getBook().getAuthor(),
                        r.getBookCopy().getBook().getCoverUrl(),
                        r.getReservationDate(),
                        r.getBookCopy().getStatus().name()
                )).toList();
    }

    public List<ReservBookDto> getReturnedBooks(Long readerId) {
        return reservationRepo
                .findByReaderId(readerId)
                .stream()
                .map(r -> new ReservBookDto(
                        r.getId(),
                        r.getBookCopy().getBook().getId(),
                        r.getBookCopy().getBook().getTitle(),
                        r.getBookCopy().getBook().getCoverUrl(),
                        r.getBookCopy().getBook().getAuthor(),
                        r.getReservationDate(),
                        r.getBookCopy().getStatus().name()
                ))
                .toList();
    }

}



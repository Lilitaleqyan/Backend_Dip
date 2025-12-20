package org.example.backend_dip.controller;

import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.service.ReadersService;
import org.example.backend_dip.service.ReservService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reader")
public class ReaderController {
    private final ReadersService service;
    private final ReservService reservService;
    private final BookRepo bookRepo;
    public ReaderController(ReadersService service, ReservService reservService, BookRepo bookRepo) {
        this.service = service;
        this.reservService = reservService;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/findBook")
    public ResponseEntity<List<Book>> findBookByAuthorOrTitle(@RequestParam(required = false) String author, @RequestParam(required = false) String title) {
        List<Book> books = service.findBookByAuthorOrTitle(author, title);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/reserv")
    public ResponseEntity<Book> reserveBook(@RequestBody BookCopy bookCopy, @RequestParam("id") long readerId) {
        boolean hasActive = reservService.existsByReaderId(readerId);
        if (hasActive) {
            return ResponseEntity.badRequest().build();
        } else {
            reservService.reserveBook(readerId, bookCopy.getId());
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Book> returnBook(@RequestParam("id") long reservationId) {
        reservService.returnBook(reservationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addComment")
    public ResponseEntity<BookComments> addComment(@RequestBody BookComments bookComments, @RequestParam("id") long bookId, @RequestParam("readerId") long readerId) {
        bookComments.setCreationDate(LocalDateTime.now());
        bookComments.setBook(service.findBookById(bookId).get());
        bookComments.setBookReader(service.findBookReaderById(readerId).get());
        service.addComment(bookComments);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BookReader bookReader, @RequestParam("id") long readerId) {
        return service.findById(readerId).map(eBook ->
        {
            if (service.existsByUsername(bookReader.getUsername())) {
                Optional<BookReader> userWithUsername = service.findByUsername(bookReader.getUsername());
                if (userWithUsername.isPresent() && !userWithUsername.get().getId().equals(readerId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("username already exists");
                }
            }

            if (service.existsByEmail(bookReader.getEmail())) {
                Optional<BookReader> userWithEmail = service.findByEmail(bookReader.getEmail());
                if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(readerId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("email already exists");
                }
            }
            eBook.setLastName(bookReader.getLastName());
            eBook.setFirstName(bookReader.getFirstName());
            eBook.setEmail(bookReader.getEmail());
            eBook.setPhone(bookReader.getPhone());
            eBook.setPassword(bookReader.getPassword());
            eBook.setUsername(bookReader.getUsername());
            return ResponseEntity.ok(service.update(eBook));

        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookRepo.findAll();
        return ResponseEntity.ok(books);
    }
}

package org.example.backend_dip.service;

import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.repo.CommentRepo;
import org.example.backend_dip.repo.ReservationRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReadersService {
    private final BookReaderRepo repo;
    private final BookRepo bookRepo;
    private final CommentRepo commentRepo;
    private final ReservationRepo reservationRepo;



    public ReadersService(BookReaderRepo repo, BookRepo bookRepo, CommentRepo commentRepo, ReservationRepo reservationRepo) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.commentRepo = commentRepo;
        this.reservationRepo = reservationRepo;
    }

    public BookReader save(BookReader reader) {
          repo.save(reader);
        return reader;
    }

    public List<Book> findBookByAuthorOrTitle(String author, String title) {
        return bookRepo.findBookByAuthorOrTitleIgnoreCase(author, title);
    }

    public void addComment(BookComments comment) {
        commentRepo.save(comment);
    }

    public Optional<Book> findBookById(long bookId) {
       return bookRepo.findById(bookId);
    }


    public Optional<BookReader> findBookReaderById(long readerId) {
        return repo.findById(readerId);
    }

    public boolean existsByUsernameAndEmail(String username, String email) {
        return repo.existsByUsernameAndEmail(username,email);
    }

    public Optional<BookReader> findById(long readerId) {
        return repo.findById(readerId);
    }

    public BookReader update(BookReader updateReader) {
//       if(repo.existsByUsernameAndEmail(updateReader.getUsername(), updateReader.getEmail())) {
//           return new BookReader();
//       };
      return   repo.save(updateReader);
    }

    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public Optional<BookReader> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Optional<BookReader> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public long getReservedCount(Long readerId) {
        return reservationRepo.countByReaderIdAndStatus(
                readerId,
                Status.RESERVED
        );
    }

    public long getReturnedCount(Long readerId) {
        return reservationRepo.countByReaderIdAndStatus(
                readerId,
                Status.RETURNED
        );
    }

    public BookReader getCurrentReader(Authentication authentication) {
        String username = authentication.getName();
        return repo.findByUsername(username).orElseThrow(() -> new RuntimeException("reader not found"));
    }
}

package org.example.backend_dip.service;

import org.example.backend_dip.entity.Reservation;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookCopyRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.repo.ReservationRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservService {
    private final ReservationRepo reservationRepo;
    private final BookCopyRepo bookCopyRepo;
    private final BookReaderRepo bookReaderRepo;
    private final BookRepo bookRepo;

    public ReservService(ReservationRepo reservationRepo, BookCopyRepo bookCopyRepo, BookReaderRepo bookReaderRepo, BookRepo bookRepo) {
        this.reservationRepo = reservationRepo;
        this.bookCopyRepo = bookCopyRepo;
        this.bookReaderRepo = bookReaderRepo;
        this.bookRepo = bookRepo;
    }

    public void reserveBook(Long readerId, Long bookId) {
        long available = bookCopyRepo.countBookCopiesById(bookId);
        if (available == 0) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BookCopy bookCopy = bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Book  not found"));

        bookCopy.setStatus(Status.RESERVED);
        bookCopyRepo.save(bookCopy);

        Reservation reservation = Reservation.builder()
                .reader(bookReaderRepo.findById(readerId).orElseThrow())
                .bookCopy(bookCopy)
                .reservationDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(10))
                .active(true)
                .build();
        reservationRepo.save(reservation);
    }

    public void returnBook(Long reservationId) {
        Reservation reservation = reservationRepo.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setActive(false);
        reservation.setReturnDate(LocalDate.now());

        BookCopy bookCopy = reservation.getBookCopy();
        bookCopy.setStatus(Status.AVAILABLE);
        reservationRepo.save(reservation);
        bookCopyRepo.save(bookCopy);
    }

    public boolean existsByReaderId(long readerId) {
        return reservationRepo.existsByReaderId(readerId);
    }
}

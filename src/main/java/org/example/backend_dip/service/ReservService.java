package org.example.backend_dip.service;

import jakarta.transaction.Transactional;
import org.example.backend_dip.entity.Reservation;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookCopyRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.ReservationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservService {
    private final ReservationRepo reservationRepo;
    private final BookCopyRepo bookCopyRepo;
    private final BookReaderRepo bookReaderRepo;

    public ReservService(ReservationRepo reservationRepo, BookCopyRepo bookCopyRepo, BookReaderRepo bookReaderRepo) {
        this.reservationRepo = reservationRepo;
        this.bookCopyRepo = bookCopyRepo;
        this.bookReaderRepo = bookReaderRepo;
    }

    public void reserveBook(Long readerId, Long bookId) {

        if (bookId == null) {
            throw new RuntimeException("Book ID cannot be null");
        }
        
        if (readerId == null) {
            throw new RuntimeException("Reader ID cannot be null");
        }
        
        if (existsByReaderId(readerId)) {
            throw new RuntimeException("Reader already has an active reservation");
        }
        
        BookCopy bookCopy = bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE).or(() -> bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.RETURNED))
                .orElseThrow(() -> new RuntimeException("No available book copy found"));


        bookCopy.setStatus(Status.RESERVED);
        bookCopyRepo.save(bookCopy);

        // Create reservation
        Reservation reservation = Reservation.builder()
                .reader(bookReaderRepo.findById(readerId)
                        .orElseThrow(() -> new RuntimeException("Reader not found")))
                .bookCopy(bookCopy)
                .reservationDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(10))
                .active(true)
                .build();
        reservationRepo.save(reservation);
    }

    @Transactional
    public void returnBook(Long reservationId) {
        if (reservationId == null) {
            throw new RuntimeException("Reservation ID cannot be null");
        }
        
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));


        if (!reservation.isActive()) {
            throw new RuntimeException("Reservation is already returned");
        }

        reservation.setStatus(Status.RETURNED);
        reservation.setActive(false);
        reservation.setReturnDate(LocalDate.now());

        BookCopy bookCopy = reservation.getBookCopy();
        if (bookCopy == null) {
            throw new RuntimeException("Book copy not found for this reservation");
        }

        bookCopy.setStatus(Status.RETURNED);
        
        reservationRepo.save(reservation);
        bookCopyRepo.save(bookCopy);
    }

    public boolean existsByReaderId(long readerId) {
        return reservationRepo.existsByReaderId(readerId);
    }
}

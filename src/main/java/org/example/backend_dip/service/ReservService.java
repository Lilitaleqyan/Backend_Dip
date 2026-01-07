package org.example.backend_dip.service;

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
        // Check if book exists
        if (bookId == null) {
            throw new RuntimeException("Book ID cannot be null");
        }
        
        // Check if reader exists
        if (readerId == null) {
            throw new RuntimeException("Reader ID cannot be null");
        }
        
        // Check if reader already has an active reservation
        if (existsByReaderId(readerId)) {
            throw new RuntimeException("Reader already has an active reservation");
        }
        
        // Find available book copy
        BookCopy bookCopy = bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("No available book copy found"));

        // Update book copy status to RESERVED
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

    public void returnBook(Long reservationId) {
        if (reservationId == null) {
            throw new RuntimeException("Reservation ID cannot be null");
        }
        
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Check if reservation is already returned
        if (!reservation.isActive()) {
            throw new RuntimeException("Reservation is already returned");
        }

        // Update reservation
        reservation.setActive(false);
        reservation.setReturnDate(LocalDate.now());

        // Update book copy status to AVAILABLE
        BookCopy bookCopy = reservation.getBookCopy();
        if (bookCopy == null) {
            throw new RuntimeException("Book copy not found for this reservation");
        }
        bookCopy.setStatus(Status.AVAILABLE);
        
        reservationRepo.save(reservation);
        bookCopyRepo.save(bookCopy);
    }

    public boolean existsByReaderId(long readerId) {
        return reservationRepo.existsByReaderId(readerId);
    }
}

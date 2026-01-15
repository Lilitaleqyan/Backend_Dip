package org.example.backend_dip.service;

import jakarta.transaction.Transactional;
import org.example.backend_dip.entity.Reservation;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.books.ReservBookDto;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookCopyRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.ReservationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public void reserveBook(Long readerId, Long bookId) {
        if (bookId == null) {
            throw new RuntimeException("Book ID cannot be null");
        }
        
        if (readerId == null) {
            throw new RuntimeException("Reader ID cannot be null");
        }
        
        // Check if reader already has an active reservation
        if (existsByReaderId(readerId)) {
            throw new RuntimeException("Reader already has an active reservation");
        }
        
        // Find available book copy (first try AVAILABLE, then RETURNED)
        BookCopy bookCopy = bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE)
                .or(() -> bookCopyRepo.findFirstByBookIdAndStatus(bookId, Status.RETURNED))
                .orElseThrow(() -> new RuntimeException("No available book copy found"));

        // Update book copy status to RESERVED
        bookCopy.setStatus(Status.RESERVED);
        bookCopyRepo.save(bookCopy);

        // Create reservation with correct reader
        Reservation reservation = Reservation.builder()
                .reader(bookReaderRepo.findById(readerId)
                        .orElseThrow(() -> new RuntimeException("Reader not found")))
                .bookCopy(bookCopy)
                .reservationDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(10))
                .active(true)
                .status(Status.RESERVED)
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

        bookCopy.setStatus(Status.AVAILABLE);
        
        reservationRepo.save(reservation);
        bookCopyRepo.save(bookCopy);
    }

    public boolean existsByReaderId(long readerId) {
        return reservationRepo.existsByReaderId(readerId);
    }


//        public List<ReservBookDto> getReturnedBooks(Long readerId) {
//
//            if (readerId == null) {
//                return List.of();
//            }
//
//            List<Reservation> returned =
//                    reservationRepo.findReturnedReservationsByReaderId(readerId);
//
//            return returned.stream()
//                    .filter(r -> r.getBookCopy() != null && r.getBookCopy().getBook() != null)
//                    .map(this::toDto)
//                    .toList();
//        }

//        public List<ReservBookDto> getReservedBooks(Long readerId) {
//            if (readerId == null) {
//                return List.of();
//            }
//
//            List<Reservation> active =
//                    reservationRepo.findActiveReservationsByReaderId(readerId);
//
//            return active.stream()
//                    .filter(r -> r.getBookCopy() != null && r.getBookCopy().getBook() != null)
//                    .map(this::toDto)
//                    .toList();
//        }
//

//        public long countReserved(Long readerId) {
//            return reservationRepo.countActiveReservedByReaderId(readerId);
//        }

//        public long countReturned(Long readerId) {
//            return reservationRepo.countReturnedByReaderId(readerId);
//        }

//        private ReservBookDto toDto(Reservation r) {
//            return new ReservBookDto(
//                    r.getId(),
//                    r.getBookCopy().getBook().getId(),
//                    r.getBookCopy().getBook().getTitle(),
//                    r.getBookCopy().getBook().getAuthor(),
//                    r.getBookCopy().getBook().getCoverUrl(),
//                    r.getReservationDate(),
//                    r.isActive() ? "RESERVED" : "RETURNED"
//            );
//        }
    }



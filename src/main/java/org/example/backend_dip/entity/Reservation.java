package org.example.backend_dip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.books.BookCopy;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    BookReader reader;

    @ManyToOne
    BookCopy bookCopy;

    private LocalDate reservationDate;
    private LocalDate returnDate;

    private boolean active;
}

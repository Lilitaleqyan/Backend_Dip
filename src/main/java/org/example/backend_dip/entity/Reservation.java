package org.example.backend_dip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;

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
    @JoinColumn(name = "reader_id")
     @JsonIgnore
    BookReader reader;

    @ManyToOne
    BookCopy bookCopy;

    private LocalDate reservationDate;
    private LocalDate returnDate;

    private boolean active;

    private Status status;
}

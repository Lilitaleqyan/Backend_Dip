package org.example.backend_dip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.books.Book;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    Book book;

    @ManyToOne
    BookReader bookReader;

    @ManyToOne
    AdminForControl admin;

    private String comment;
    @JsonFormat(pattern = "dd/MM/yyyy/ HH:mm")
    private LocalDateTime creationDate;
    private  String username;

}

package org.example.backend_dip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.books.Book;

import java.time.LocalDateTime;


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

    private LocalDateTime creationDate;
    private  String username;

}

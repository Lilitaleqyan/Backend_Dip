package org.example.backend_dip.entity.books;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend_dip.entity.enums.Status;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookCopy {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookC_seq")
//    @SequenceGenerator(
//            name = "bookC_seq",
//            sequenceName = "book_copy_sequence",
//            allocationSize = 1,
//            initialValue = 1)0
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    Book book;

    @Enumerated(EnumType.STRING)
    private Status status;

}
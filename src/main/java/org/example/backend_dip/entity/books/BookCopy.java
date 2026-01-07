package org.example.backend_dip.entity.books;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    @JoinColumn(name = "book_id")
    Book book;

    @Enumerated(EnumType.STRING)
    private Status status;

}
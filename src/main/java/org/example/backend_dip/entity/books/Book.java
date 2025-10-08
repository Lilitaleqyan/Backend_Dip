package org.example.backend_dip.entity.books;//package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.BookComments;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Book {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
//    @SequenceGenerator(
//            name = "book_seq",
//            sequenceName = "book_sequence",
//
//            allocationSize = 1,
//            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String fileType;

    @Column
    private String filePath;

    @Column
    private int count;

//    @Enumerated(EnumType.STRING)
//    private String genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookCopy> bookCopyList;


    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookComments> commentList;
}


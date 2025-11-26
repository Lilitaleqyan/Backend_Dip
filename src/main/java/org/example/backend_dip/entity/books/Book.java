package org.example.backend_dip.entity.books;//package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private String coverUrl;

    @Column
    private String audioUrl;

    @Column
    private String duration;

    @Column
    private String narrator;


    @Column
    private Integer pages;

    @Column
    private String fileType;

    @Column
    private String filePath;

    @Column
    private int count;

//    @Enumerated(EnumType.STRING)
//    private String genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookCopy> bookCopyList;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookComments> commentList;
}


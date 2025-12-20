package org.example.backend_dip.entity.books;

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

    @Column(length = 1000)
    private String title;

    @Column(length = 1000)
    private String author;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 1000)
    private String category;

    @Column(columnDefinition = "text")
    private String coverUrl;

    @Column(columnDefinition = "text")
    private String audioUrl;

    private String duration;

    private String narrator;


    @Column
    private Integer pages;

    private String fileType;

    @Column(columnDefinition = "text")
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


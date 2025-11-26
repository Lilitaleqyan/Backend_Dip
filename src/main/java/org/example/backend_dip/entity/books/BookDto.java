package org.example.backend_dip.entity.books;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {

    private String title;
    private String author;
    private String description;
    private String category;
    private String coverUrl;
    private String audioUrl;
    private String duration;
    private String narrator;
    private Integer pages;
    private String fileType;
    private String filePath;
    private int count;
}

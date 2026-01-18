package org.example.backend_dip.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

@RequiredArgsConstructor
public class BookCommentsDto {
    private Long id;
    private String comment;
    private String creationDate;
    private  String username;
    private AdminForControl admin;
    private BookReader reader;

    public BookCommentsDto(Long id, String comment, String creationDate, String username) {
        this.id = id;
        this.comment = comment;
        this.creationDate = creationDate;
        this.username = username;
    }

}

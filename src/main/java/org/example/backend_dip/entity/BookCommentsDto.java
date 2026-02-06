package org.example.backend_dip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BookCommentsDto {
    private Long id;
    private String comment;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime creationDate;
    private  String username;


    public BookCommentsDto(Long id, String comment, LocalDateTime creationDate, String username) {
        this.id = id;
        this.comment = comment;
        this.creationDate = creationDate;
        this.username = username;
    }

}

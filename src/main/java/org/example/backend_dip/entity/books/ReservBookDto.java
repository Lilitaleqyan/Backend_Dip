package org.example.backend_dip.entity.books;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ReservBookDto {
    private Long id;
    private  Long bookId;
    private String title;
    private String author;
    private  String coverUrl;
    private LocalDate reservationDate;
    private String status ;
}

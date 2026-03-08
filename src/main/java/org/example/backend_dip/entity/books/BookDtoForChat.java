package org.example.backend_dip.entity.books;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.enums.Status;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDtoForChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private String author;
    @Column
    private int count;
    @Column
    private int freeCount;

    @ManyToOne
    @JoinColumn
    private Book book;
}

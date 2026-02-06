package org.example.backend_dip.entity.books;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend_dip.entity.enums.Status;
import org.springframework.context.annotation.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDtoForChat {
    @Id
    private Long id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private int count;

}

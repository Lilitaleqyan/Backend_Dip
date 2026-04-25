package org.example.backend_dip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Սխալ email ֆորմատ"
    )
    @NotBlank
    private String email;
    @Pattern(
            regexp = "^(\\+374|0)?[1-9]{2}[0-9]{6}$",
            message = "Հեռախոսահամարը սխալ է (օրինակ՝ 094123456 կամ +37494123456)"
    )
    private String phone;

    @Column(name = "user_name", unique = true)
    private String username;

    @Size(min = 8, message = "Առնվազն 8 նիշ")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.]).{8,}$",
            message = "Գաղտնաբառը պետք է պարունակի մեծատառ, փոքրատառ, թիվ և սիմվոլ"
    )
    @Column(unique = true)
    private String password;

    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> bookRelations;


    @OneToMany(mappedBy = "bookReader", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookComments> comments;

    @ManyToMany
    @JoinTable
    @JsonIgnore
    private Set<Book> favoriteBooks = new HashSet<>();

    @OneToMany(mappedBy = "reader")
    @JsonIgnoreProperties("reservation")
    private Set<Reservation> reservations = new HashSet<>();

}

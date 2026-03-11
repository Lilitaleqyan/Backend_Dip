package org.example.backend_dip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
        private String email;
    private String phone;
    @Column(name = "user_name")
    private String username;
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

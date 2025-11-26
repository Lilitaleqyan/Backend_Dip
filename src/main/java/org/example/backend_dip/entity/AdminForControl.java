package org.example.backend_dip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend_dip.entity.enums.Role;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdminForControl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}

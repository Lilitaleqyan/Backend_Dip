package org.example.backend_dip.entity.requestOrresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.backend_dip.entity.enums.Role;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String username;
    private Role role;
}

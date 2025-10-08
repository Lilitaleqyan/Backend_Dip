package org.example.backend_dip.entity.requestOrresponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}

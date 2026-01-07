package org.example.backend_dip.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.enums.Role;
import org.example.backend_dip.entity.requestOrresponse.AuthRequest;
import org.example.backend_dip.entity.requestOrresponse.AuthResponse;
import org.example.backend_dip.security.JwtUtil;
import org.example.backend_dip.service.MyUserDetailsService;
import org.example.backend_dip.service.ReadersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ReadersService readerService;

    public LoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, ReadersService readerService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.readerService = readerService;
    }
   @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody BookReader bookReader) {
        bookReader.setRole(Role.USER);
        bookReader = BookReader.builder()
                .id(bookReader.getId())
                .username(bookReader.getUsername())
                .email(bookReader.getEmail())
                .phone(bookReader.getPhone())
                .firstName(bookReader.getFirstName())
                .lastName(bookReader.getLastName())
                .password(passwordEncoder.encode(bookReader.getPassword()))
                .role(bookReader.getRole())
                .build();
        if (readerService.existsByUsernameAndEmail(bookReader.getUsername(), bookReader.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else {
            readerService.save(bookReader);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration successful");
            return ResponseEntity.ok(response);

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateJwt(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/log_out")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "Log out successfully";
    }


}
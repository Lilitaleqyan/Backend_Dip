package org.example.backend_dip.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.backend_dip.entity.AdminForControl;
import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Subscription;
import org.example.backend_dip.entity.enums.Role;
import org.example.backend_dip.entity.requestOrresponse.AuthRequest;
import org.example.backend_dip.entity.requestOrresponse.AuthResponse;
import org.example.backend_dip.repo.BookReaderRepo;
import org.example.backend_dip.repo.SubscriptionRepository;
import org.example.backend_dip.security.JwtUtil;
import org.example.backend_dip.service.AdminService;
import org.example.backend_dip.service.EmailService;
import org.example.backend_dip.service.MyUserDetailsService;
import org.example.backend_dip.service.ReadersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ReadersService readerService;
    private final AdminService adminService;
    private final EmailService emailService;
    private final SubscriptionRepository subscriptionRepository;


    public LoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                           MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, ReadersService readerService,
                           AdminService adminService, EmailService emailService,
                           SubscriptionRepository subscriptionRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.readerService = readerService;
        this.adminService = adminService;
        this.emailService = emailService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody BookReader bookReader) {
        if (readerService.existsByUsernameAndEmail(bookReader.getUsername(), bookReader.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists"));
        }

        bookReader.setRole(Role.USER);
        bookReader.setPassword(passwordEncoder.encode(bookReader.getPassword()));

        BookReader savedReader = readerService.save(bookReader);


        Map<String, Object> response = new HashMap<>();
        response.put("id", savedReader.getId());
        response.put("username", savedReader.getUsername());
        response.put("email", savedReader.getEmail());
        response.put("firstName", savedReader.getFirstName());
        response.put("lastName", savedReader.getLastName());
        response.put("role", savedReader.getRole());

        Subscription subscription = new Subscription();
        subscription.setEmail(bookReader.getEmail());

        subscriptionRepository.save(subscription);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateJwt(userDetails.getUsername());

        if ("admin_manager".equals(request.getUsername())) {
            AdminForControl admin = adminService.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    admin.getId(),
                    admin.getUsername(),
                    admin.getRole()
            ));
        }
        BookReader user = readerService.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getRole()
        ));
    }

    @PostMapping("/log_out")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "Log out successfully";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        BookReader user = readerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        readerService.save(user);

        String resetLink = "http://localhost:5000/reset-password?token=" + token;

        emailService.sendResetEmail(
                email,
                resetLink
        );

        return ResponseEntity.ok(Map.of("message", "Password updated"));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String token = request.get("token");
        String newPassword = request.get("password");
        System.out.println("RESET TOKEN SAVED FOR " + ": " + token);
        BookReader user = readerService.findBookReaderByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        readerService.save(user);

        return ResponseEntity.ok("Password updated");
    }
}
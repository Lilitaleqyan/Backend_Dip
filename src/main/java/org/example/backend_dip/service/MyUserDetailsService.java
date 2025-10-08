package org.example.backend_dip.service;

import org.example.backend_dip.repo.AdminRepo;
import org.example.backend_dip.repo.BookReaderRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final BookReaderRepo bookReaderRepo;
    private final AdminRepo adminRepo;

    public MyUserDetailsService(BookReaderRepo bookReaderRepo, AdminRepo adminRepo) {
        this.bookReaderRepo = bookReaderRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepo.findByUsername(username)
                .map(a -> User
                        .withUsername(a.getUsername())
                        .password(a.getPassword())
                        .authorities("ROLE_ADMIN")
                        .build())
                .orElseGet(() -> bookReaderRepo.findByUsername(username)
                        .map(r -> User
                                .withUsername(r.getUsername())
                                .password(r.getPassword())
                                .authorities("ROLE_USER")
                                .build())
                        .orElseThrow(() -> new UsernameNotFoundException("user nit found" + username))
                );
    }

}


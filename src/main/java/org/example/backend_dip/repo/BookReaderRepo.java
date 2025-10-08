package org.example.backend_dip.repo;

import org.example.backend_dip.entity.BookReader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BookReaderRepo extends JpaRepository<BookReader, Long> {

    Optional<BookReader> findByUsername(String username);

    List<BookReader> findBookReadersByEmailOrFirstNameOrLastNameOrUsernameIgnoreCase(String email,
                                                                                     String firstName,
                                                                                     String lastName,
                                                                                     String username);



    boolean existsByUsernameAndEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

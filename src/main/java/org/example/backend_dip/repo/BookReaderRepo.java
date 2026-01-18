package org.example.backend_dip.repo;

import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.BookReaderForAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BookReaderRepo extends JpaRepository<BookReader, Long> {

    Optional<BookReader> findByUsername(String username);

    Optional<BookReader> findByEmail(String email);

    List<BookReader> findBookReadersByEmailOrFirstNameOrLastNameOrUsernameIgnoreCase(String email,
                                                                                     String firstName,
                                                                                     String lastName,
                                                                                     String username);


    boolean existsByUsernameAndEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//
//    @Query("""
//    SELECT new org.example.backend_dip.entity.BookReaderForAdmin(
//        r.id,
//        r.firstName,
//        r.lastName,
//        r.email,
//        COALESCE(SUM(CASE WHEN rc.bookCopy.status = org.example.backend_dip.entity.enums.Status.AVAILABLE THEN 1 ELSE 0 END), 0),
//        COALESCE(SUM(CASE WHEN rc.bookCopy.status = org.example.backend_dip.entity.enums.Status.RESERVED THEN 1 ELSE 0 END), 0),
//        COALESCE(SUM(CASE WHEN rc.bookCopy.status = org.example.backend_dip.entity.enums.Status.RETURNED THEN 1 ELSE 0 END), 0)
//    )
//    FROM BookReader r
//    LEFT JOIN r.bookRelations rc
//    LEFT JOIN rc.bookCopy bc
//    GROUP BY r.id, r.firstName, r.lastName, r.email
//    ORDER BY r.firstName, r.lastName
//""")
//    List<BookReaderForAdmin> findAllForAdminWithStats();

    @Query("SELECT DISTINCT r FROM BookReader r LEFT JOIN FETCH r.bookRelations rc LEFT JOIN FETCH rc.bookCopy")
    List<BookReader> findAllReadersWithRelations();


    Optional<BookReader> findBookReaderById(long readerId);
}

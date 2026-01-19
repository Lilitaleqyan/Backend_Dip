package org.example.backend_dip.repo;

import org.example.backend_dip.entity.enums.ReaderStats;
import org.example.backend_dip.entity.Reservation;
import org.example.backend_dip.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.reader.id = :readerId
              AND r.active = true
            """)
    boolean existsByReaderId(@Param("readerId") Long readerId);


    @Query("""
                SELECT r
                FROM Reservation r
                LEFT JOIN FETCH r.bookCopy bc
                LEFT JOIN FETCH bc.book b
                WHERE r.reader.id = :readerId
            """)
    List<Reservation> findByReaderIdWithRelations(@Param("readerId") Long readerId);

    @Query("""
                SELECT DISTINCT r
                FROM Reservation r
                LEFT JOIN FETCH r.bookCopy bc
                LEFT JOIN FETCH bc.book b
                WHERE r.reader.id = :readerId
                AND r.active = true
                AND r.status = org.example.backend_dip.entity.enums.Status.RESERVED
                ORDER BY r.reservationDate DESC
            """)
    List<Reservation> findActiveReservationsByReaderId(@Param("readerId") Long readerId);


    @Query("""
                    SELECT COUNT(rc)
                    FROM Reservation rc
                    WHERE rc.reader.id = :readerId
                    AND rc.status = :status
            """)
    long countByReaderIdAndStatus(@Param("readerId") Long readerId, @Param("status") Status status);

    @Query("""
                    SELECT COUNT(rc)
                    FROM Reservation rc
                    WHERE rc.reader.id = :readerId
                    AND rc.active = true
                    AND rc.status = org.example.backend_dip.entity.enums.Status.RESERVED
            """)
    long countActiveReservedByReaderId(@Param("readerId") Long readerId);

    @Query("""
                    SELECT COUNT(rc)
                    FROM Reservation rc
                    WHERE rc.reader.id = :readerId
                    AND rc.active = false
            """)
    long countReturnedByReaderId(@Param("readerId") Long readerId);


}

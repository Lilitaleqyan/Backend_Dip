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
  AND r.bookCopy.status = org.example.backend_dip.entity.enums.Status.RESERVED
""")
    boolean existsByReaderId(@Param("readerId") Long readerId);


    List<Reservation> findByReaderIdAndStatus(Long readerId, Status status);

    List<Reservation> findByReaderId(long readerId);

//    @Query("""
//    SELECT new org.example.backend_dip.entity.enums.ReaderStats(
//        SUM(CASE WHEN r.bookCopy.status = org.example.backend_dip.entity.enums.Status.RESERVED THEN 1 ELSE 0 END),
//        SUM(CASE WHEN r.bookCopy.status = org.example.backend_dip.entity.enums.Status.RETURNED THEN 1 ELSE 0 END)
//    )
//    FROM Reservation r
//    WHERE r.reader.id = :readerId
//    """)
//    ReaderStats getStats(@Param("readerId") Long readerId);


}

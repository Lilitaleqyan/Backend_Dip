package org.example.backend_dip.repo;

import org.example.backend_dip.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.reader.id = :readerId AND r.active = true")
    boolean existsByReaderId(@Param("readerId") Long readerId);

}

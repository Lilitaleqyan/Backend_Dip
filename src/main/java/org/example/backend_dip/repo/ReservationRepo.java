package org.example.backend_dip.repo;

import org.example.backend_dip.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    boolean existsByReaderId(Long readerId);

}

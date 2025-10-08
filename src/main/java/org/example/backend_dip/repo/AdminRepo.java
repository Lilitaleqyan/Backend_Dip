package org.example.backend_dip.repo;

import org.example.backend_dip.entity.AdminForControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<AdminForControl, Integer> {
    boolean existsByUsername(String username);

    Optional<AdminForControl> findByUsername(String username);
}

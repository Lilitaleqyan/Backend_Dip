package org.example.backend_dip.repo;

import org.example.backend_dip.entity.books.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.concurrent.Flow;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s.email FROM Subscription s")
    List<String> findAllEmails();
}

package org.example.backend_dip.repo;

import org.example.backend_dip.entity.BookComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<BookComments, Long> {
}

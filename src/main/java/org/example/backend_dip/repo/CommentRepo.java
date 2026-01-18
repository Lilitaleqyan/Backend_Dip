package org.example.backend_dip.repo;

import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookCommentsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<BookComments, Long> {
    List<BookComments> findByBookId(long bookId);
}

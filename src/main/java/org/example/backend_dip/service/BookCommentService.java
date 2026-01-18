package org.example.backend_dip.service;

import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookCommentsDto;
import org.example.backend_dip.repo.CommentRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCommentService {
    private final CommentRepo commentRepo;

    public BookCommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public List<BookCommentsDto> viewAllCommentsByBookId(long bookId) {
        return commentRepo.findByBookId(bookId).stream()
                .map(c -> new BookCommentsDto(
                        c.getId(),
                        c.getComment(),
                        c.getCreationDate().toString(),
                        c.getUsername()
                ))
                .collect(Collectors.toList());
    }


}

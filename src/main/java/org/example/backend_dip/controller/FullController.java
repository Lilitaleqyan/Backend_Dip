package org.example.backend_dip.controller;

import jakarta.annotation.Priority;
import org.example.backend_dip.entity.AdminForControl;
import org.example.backend_dip.entity.BookComments;
import org.example.backend_dip.entity.BookCommentsDto;
import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.enums.Role;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.service.AdminService;
import org.example.backend_dip.service.BookCommentService;
import org.example.backend_dip.service.BookCopyService;
import org.example.backend_dip.service.ReadersService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/full")
public class FullController {
    private final ReadersService readerService;
    private final AdminService adminService;
    private final BookRepo bookRepo;
    private final BookCommentService bookCommentService;


    public FullController(ReadersService readerService, AdminService adminService, BookRepo bookRepo, BookCommentService bookCommentService) {
        this.readerService = readerService;
        this.adminService = adminService;
        this.bookRepo = bookRepo;
        this.bookCommentService = bookCommentService;
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = adminService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable() Long id) throws IOException {

        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Path path = Paths.get(book.getFilePath());
        System.out.println(path);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found");
        }

        Resource resource = new UrlResource(path.toUri());

        String fileName = path.getFileName().toString();
        System.out.println(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/addComment")
    public ResponseEntity<Void> addComment(
            @RequestBody BookComments bookComments,
            @RequestParam long bookId,
            Authentication authentication
    ) {
        bookComments.setCreationDate(LocalDateTime.now());

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("user"))) {
            BookReader reader = readerService.getCurrentReader(authentication);
            bookComments.setBook(readerService.findBookById(bookId).orElseThrow());
            bookComments.setBookReader(reader);
            readerService.addComment(bookComments);

            return ResponseEntity.ok().build();
        }

        AdminForControl admin = adminService.getCurrentAdmin(authentication);

        bookComments.setBook(adminService.findBookById(bookId).orElseThrow());
        bookComments.setAdmin(admin);
        adminService.addComment(bookComments);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/viewComments")
    public ResponseEntity<List<BookCommentsDto>> comments(@RequestParam("bookId") long bookId) {

        List<BookCommentsDto> bookComments = bookCommentService.viewAllCommentsByBookId(bookId);
        return ResponseEntity.ok(bookComments);
    }
}

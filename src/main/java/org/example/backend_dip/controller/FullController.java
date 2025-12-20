package org.example.backend_dip.controller;

import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.service.AdminService;
import org.example.backend_dip.service.BookCopyService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/full")
public class FullController {
    private final AdminService service;
    private final BookRepo bookRepo;

    public FullController(AdminService service, BookRepo bookRepo) {
        this.service = service;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = service.getAllBooks();
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

}

package org.example.backend_dip.controller;

import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.service.AdminService;
import org.example.backend_dip.service.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

//    @Value("${file.upload-dir}")
    private String uploadDir="C\\Users\\User";
    private final AdminService service;
    private final BookRepo bookRepo;
    private final BookCopyService bookCopyService;

    public AdminController(AdminService service, BookRepo bookRepo, BookCopyService bookCopyService) {
        this.service = service;
        this.bookRepo = bookRepo;
        this.bookCopyService = bookCopyService;
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestParam("title") String title,
                                        @RequestParam("author") String author,
                                        @RequestParam("file") MultipartFile multipartFile) {

        int count = 1;
        try {
            File file = new File(uploadDir);
            if (!file.exists()) file.mkdirs();

            String originalFileName = multipartFile.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            if (!extension.equalsIgnoreCase("pdf")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            String filePath = uploadDir + File.separator + originalFileName;

            Path path = Paths.get(filePath);
            Files.write(path, multipartFile.getBytes());

            if (bookRepo.existsBookByAuthorAndTitle(author, title)) {
                Book existingBook = bookRepo.findBookByAuthorOrTitle(author, title);
                BookCopy bookCopy = new BookCopy();
                bookCopy.setBook(existingBook);
                bookCopy.setStatus(Status.AVAILABLE);
                bookCopyService.save(bookCopy);

                existingBook.setCount(existingBook.getCount() + 1);
                service.updateBook(existingBook);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                Book book = Book.builder()
                        .title(title)
                        .author(author)
                        .fileType(extension)
                        .filePath(filePath)
                        .count(count)
                        .build();
                service.addBook(book);

                BookCopy bookCopy = new BookCopy();
                bookCopy.setBook(book);
                bookCopy.setStatus(Status.AVAILABLE);
                bookCopyService.save(bookCopy);
                return ResponseEntity.status(HttpStatus.CREATED).body(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/removed/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
        service.removeBook(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        service.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = service.getAllBooks();
        return ResponseEntity.ok(books);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<BookReader>> getAllUsers() {
        List<BookReader> readers = service.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id,
                                        @RequestParam String title,
                                        @RequestParam String author,
                                        @RequestParam(required = false) MultipartFile multipartFile) {
        return bookRepo.findBookById(id).map(existingBook -> {
            try {
                existingBook.setTitle(title);
                existingBook.setAuthor(author);

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String originalFileName = multipartFile.getOriginalFilename();
                    String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    if (!extension.equalsIgnoreCase("pdf")) {
                        return ResponseEntity.badRequest().build();
                    }
                    String uniqueFileName = UUID.randomUUID() + "." + extension;
                    String filePath = uploadDir + File.separator + uniqueFileName;

                    Path path = Paths.get(filePath);
                    Files.write(path, multipartFile.getBytes());

                    existingBook.setFileType(extension);
                    existingBook.setFilePath(filePath);
                }

                return ResponseEntity.ok(service.updateBook(existingBook));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/findBook")
    public ResponseEntity<List<Book>> findBookByAuthorOrTitle(@RequestParam(required = false) String
                                                                      author, @RequestParam(required = false) String title) {
        List<Book> books = service.findBookByAuthorOrTitle(author, title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/findReader")
    public ResponseEntity<List<BookReader>> findReaderBy(@RequestParam(required = false) String email,
                                                         @RequestParam(required = false) String firstName,
                                                         @RequestParam(required = false) String lastName,
                                                         @RequestParam(required = false) String username) {
        List<BookReader> readers = service.findBookReaderBy(email, firstName, lastName, username);
        return ResponseEntity.ok(readers);
    }
}

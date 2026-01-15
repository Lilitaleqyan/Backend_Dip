package org.example.backend_dip.controller;

import org.example.backend_dip.entity.BookReader;
import org.example.backend_dip.entity.BookReaderForAdmin;
import org.example.backend_dip.entity.books.Book;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.books.BookDto;
import org.example.backend_dip.entity.books.ReservBookDto;
import org.example.backend_dip.entity.enums.Status;
import org.example.backend_dip.repo.BookRepo;
import org.example.backend_dip.service.AdminService;
import org.example.backend_dip.service.BookCopyService;
import org.example.backend_dip.service.ReadersService;
import org.example.backend_dip.service.ReservService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final AdminService adminService;
    private final ReadersService readersService;
    private final BookRepo bookRepo;
    private final BookCopyService bookCopyService;
    private final ReservService reservService;

    public AdminController(AdminService adminService, ReadersService readersService, BookRepo bookRepo, BookCopyService bookCopyService, ReservService reservService) {
        this.readersService = readersService;
        this.adminService = adminService;
        this.bookRepo = bookRepo;
        this.bookCopyService = bookCopyService;
        this.reservService = reservService;
    }

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Book> addBook(@RequestPart("book") BookDto bookDto, @RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "audioFile", required = false) MultipartFile audioFile) {

        System.out.println(bookDto.getTitle());
        System.out.println("File: " + (file != null ? file.getOriginalFilename() : "no file"));
        try {
            Book bookEntity = Book.builder()
                    .title(bookDto.getTitle())
                    .author(bookDto.getAuthor())
                    .category(bookDto.getCategory())
                    .description(bookDto.getDescription())
                    .pages(bookDto.getPages())
                    .coverUrl(bookDto.getCoverUrl())
//                    .audioUrl(bookDto.getAudioUrl())
                    .narrator(bookDto.getNarrator())
                    .duration(bookDto.getDuration()).
                    build();

            if (file != null && !file.isEmpty()) {

                String originalFileName = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();

                String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);

//                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

                Path filePath = Paths.get(uploadDir + File.separator + uniqueFileName);

                Files.write(filePath, file.getBytes());

                bookEntity.setFilePath(filePath.toString());
                bookEntity.setFileType(extension);
            }
            bookEntity = adminService.addBook(bookEntity);


            if (bookDto.getCategory().equalsIgnoreCase("audiobook") && audioFile != null && !audioFile.isEmpty()) {


                Path audioDir = Paths.get(uploadDir, "audio");
                Files.createDirectories(audioDir);

                String uniqueFileName = "audio" + bookEntity.getId() + "_" + audioFile.getOriginalFilename();

                Path path = audioDir.resolve(uniqueFileName); // uploads/audio/audio<ID>_file.mp3

                Files.copy(audioFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                String fileUrl = "/uploads/audio/" + uniqueFileName;
                bookEntity.setAudioUrl(fileUrl);
            }

            adminService.addBook(bookEntity);

            BookCopy bookCopy = new BookCopy();
            bookCopy.setBook(bookEntity);
            bookCopy.setStatus(Status.AVAILABLE);
            bookCopyService.save(bookCopy);

            return ResponseEntity.ok(bookEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/removed/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) throws IOException {
        adminService.removeBook(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        adminService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = adminService.getAllBooks();
        return ResponseEntity.ok(books);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<BookReaderForAdmin>> getAllUsers() {
        List<BookReaderForAdmin> readers = adminService.getAllReadersForAdmin();
        return ResponseEntity.ok(readers);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id,
                                        @RequestPart("book") BookDto bookDto,
                                        @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                        @RequestPart(value = "audioFile", required = false) MultipartFile audioFile) {
        return bookRepo.findBookById(id).map(existingBook -> {
            try {
                existingBook.setTitle(bookDto.getTitle());
                existingBook.setAuthor(bookDto.getAuthor());
                existingBook.setCategory(bookDto.getCategory());
                existingBook.setDescription(bookDto.getDescription());
                existingBook.setPages(bookDto.getPages());
                existingBook.setCoverUrl(bookDto.getCoverUrl());
                existingBook.setFileType(bookDto.getFileType());
                existingBook.setFilePath(bookDto.getFilePath());
//               existingBook.setAudioUrl(bookDto.getAudioUrl());
                existingBook.setNarrator(bookDto.getNarrator());
                existingBook.setDuration(bookDto.getDuration());


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
                if (bookDto.getCategory().equalsIgnoreCase("audiobook") && audioFile != null && !audioFile.isEmpty()) {


                    Path audioDir = Paths.get(uploadDir, "audio");
                    Files.createDirectories(audioDir);

                    String uniqueFileName = "audio" + existingBook.getId() + "_" + audioFile.getOriginalFilename();

                    Path path = audioDir.resolve(uniqueFileName); // uploads/audio/audio<ID>_file.mp3

                    Files.copy(audioFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    String fileUrl = "/uploads/audio/" + uniqueFileName;
                    existingBook.setAudioUrl(fileUrl);
                }


                return ResponseEntity.ok(adminService.updateBook(existingBook));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/findBook")
    public ResponseEntity<List<Book>> findBookByAuthorOrTitle(@RequestParam(required = false) String author, @RequestParam(required = false) String title) {
        List<Book> books = adminService.findBookByAuthorOrTitle(author, title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/findReader")
    public ResponseEntity<List<BookReader>> findReaderBy(@RequestParam(required = false) String email, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String username) {
        List<BookReader> readers = adminService.findBookReaderBy(email, firstName, lastName, username);
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/reservDetail/{id}")
    public ResponseEntity<List<ReservBookDto>> getReservBook(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getReservationBooks(id));
    }

    @GetMapping("/returnDetail/{id}")
    public ResponseEntity<List<ReservBookDto>> getReturnBook(@PathVariable Long id) {
        List<ReservBookDto> result = adminService.getReturnedBooks(id);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/return/{reservationId}")
    public ResponseEntity<?> returnBook(@PathVariable long reservationId) {
        try {
            reservService.returnBook(reservationId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


}

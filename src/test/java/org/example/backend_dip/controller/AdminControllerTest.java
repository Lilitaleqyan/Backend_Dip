package org.example.backend_dip.controller;

import org.example.backend_dip.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void shouldDeleteBookAndReturnedNoContent() throws IOException {
        Long bookId = 1L;
        ResponseEntity<Void> response = adminController.deleteBook(bookId);
        verify(adminService, times(1)).removeBook(bookId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

}

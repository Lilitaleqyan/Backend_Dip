package org.example.backend_dip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;

@SpringBootApplication
public class BackendDipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDipApplication.class, args);
    }
}

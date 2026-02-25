package org.example.backend_dip.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail); // email пользователя
        message.setSubject("Восстановление пароля");
        message.setText("Нажмите на ссылку, чтобы сбросить пароль:\n" + resetLink);

        mailSender.send(message);
    }
}
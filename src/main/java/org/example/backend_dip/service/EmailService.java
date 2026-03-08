package org.example.backend_dip.service;


import org.example.backend_dip.entity.books.Subscription;
import org.example.backend_dip.repo.SubscriptionRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {


    private final JavaMailSender mailSender;
    private  final SubscriptionRepository subscriptionRepository;

    public EmailService(JavaMailSender mailSender, SubscriptionRepository subscriptionRepository) {
        this.mailSender = mailSender;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail); // email пользователя
        message.setSubject("Восстановление пароля");
        message.setText("Нажмите на ссылку, чтобы сбросить пароль:\n" + resetLink);

        mailSender.send(message);
    }

    public void sendNewBookNotification(String bookTitle) {
        List<String> subscriptions = subscriptionRepository.findAllEmails();
        for (String email : subscriptions) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Նոր գիրք Գրքապտույտում!");
            message.setText("Բարև ձեզ, արդեն կարող եք ընթերցել «" + bookTitle + "» գիրքը մեր հարթակում:");
            mailSender.send(message);
        }
    }
    public void sendMessageForAdmin(String massage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("lilaleqyan38@gmail.com"); //admin mail
        message.setSubject("Հաղորդագրություն օգտատիրոջ կողմից");
        message.setText(massage);
        mailSender.send(message);
    }
}
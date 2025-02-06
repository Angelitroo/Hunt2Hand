package com.hunt2hand.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Validated
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }
}
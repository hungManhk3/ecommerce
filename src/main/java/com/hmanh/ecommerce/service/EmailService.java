package com.hmanh.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public interface EmailService {
    Boolean sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException;
}

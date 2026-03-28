package com.kuafu.common.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * Mock Mail Sender for development
 */
@Component
@Slf4j
public class MockJavaMailSender implements JavaMailSender {

    @Override
    public MimeMessage createMimeMessage() {
        log.info("Mock createMimeMessage");
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
        log.info("Mock createMimeMessage from stream");
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) {
        log.info("Mock send mimeMessage");
    }

    @Override
    public void send(MimeMessage... mimeMessages) {
        log.info("Mock send multiple mimeMessages");
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) {
        log.info("Mock send mimeMessagePreparator");
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) {
        log.info("Mock send multiple mimeMessagePreparators");
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) {
        log.info("Mock send simpleMessage to: {}", simpleMessage.getTo());
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) {
        log.info("Mock send multiple simpleMessages");
    }
}
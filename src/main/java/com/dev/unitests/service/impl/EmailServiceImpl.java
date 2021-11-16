package com.dev.unitests.service.impl;

import com.dev.unitests.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String emailMessage, List<String> emailsList) {

        String[] mails = emailsList.toArray(new String[emailsList.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("axavierga@gmail.com");
        mailMessage.setSubject("Livro com empréstimo atrasado");
        mailMessage.setText(emailMessage);
        mailMessage.setTo(mails);

        javaMailSender.send(mailMessage);
    }
}

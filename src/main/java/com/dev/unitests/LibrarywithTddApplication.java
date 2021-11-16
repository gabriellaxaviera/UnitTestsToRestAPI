package com.dev.unitests;

import com.dev.unitests.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibrarywithTddApplication {

    @Autowired
    private EmailService emailService;

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            List<String> emails = Collections.singletonList("362a3958dd-de470f@inbox.mailtrap.io");
			emailService.sendMails("Testando emails", emails);
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(LibrarywithTddApplication.class, args);
    }

}

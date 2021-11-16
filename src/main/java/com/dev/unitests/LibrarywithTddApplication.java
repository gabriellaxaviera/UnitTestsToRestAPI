package com.dev.unitests;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibrarywithTddApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Scheduled(cron = "0 15 20 1/1 * ?")
	public void agendamentos(){
		System.out.println("AGENDAMENTO OK");

	}

	public static void main(String[] args) {
		SpringApplication.run(LibrarywithTddApplication.class, args);
	}

}

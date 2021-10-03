package com.cowin.project.sessionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class CoWinProjectSessionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoWinProjectSessionServiceApplication.class, args);
	}

}

package com.cowin.project.client.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

import com.cowin.project.client.service.customresttemplate.ClientRestTemplateCustomizer;

@SpringBootApplication
public class CoWinProjectClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoWinProjectClientServiceApplication.class, args);
	}

	@Bean
	public ClientRestTemplateCustomizer customRestTemplateCustomizer() {
	    return new ClientRestTemplateCustomizer();
	}
	
	@Bean
	@DependsOn(value = {"customRestTemplateCustomizer"})
	public RestTemplateBuilder restTemplateBuilder() {
	    return new RestTemplateBuilder(customRestTemplateCustomizer());
	}
}

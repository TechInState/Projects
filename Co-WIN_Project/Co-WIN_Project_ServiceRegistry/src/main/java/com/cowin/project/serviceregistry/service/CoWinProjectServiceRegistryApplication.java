package com.cowin.project.serviceregistry.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CoWinProjectServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoWinProjectServiceRegistryApplication.class, args);
	}

}

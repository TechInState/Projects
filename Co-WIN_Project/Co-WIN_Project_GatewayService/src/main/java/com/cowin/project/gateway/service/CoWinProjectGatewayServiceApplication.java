package com.cowin.project.gateway.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
/* All Eureka Client configuration is in configuration in 
   github: https://github.com/TechInState/co-win-project-configserver/blob/main/co-win-sessionservice-production.yml
   loaded by Config Server
*/
public class CoWinProjectGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoWinProjectGatewayServiceApplication.class, args);
	}

}

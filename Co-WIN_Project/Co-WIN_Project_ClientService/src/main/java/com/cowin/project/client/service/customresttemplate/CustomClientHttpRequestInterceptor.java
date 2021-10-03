package com.cowin.project.client.service.customresttemplate;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private static Logger client_logger = org.slf4j.LoggerFactory
		      .getLogger(CustomClientHttpRequestInterceptor.class);	

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		
        logRequestDetails(request);
        return execution.execute(request, body);
	}

	private void logRequestDetails(HttpRequest request) {		
		client_logger.info("Headers: {}", request.getHeaders());
		client_logger.info("Request Method: {}", request.getMethod());
		client_logger.info("Request URI: {}", request.getURI());
	}

}

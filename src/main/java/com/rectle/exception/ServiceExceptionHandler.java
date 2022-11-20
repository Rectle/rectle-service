package com.rectle.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
		return ResponseEntity
				.status(exception.getHttpStatus())
				.body(ErrorResponse.builder()
						.statusCode(exception.getHttpStatus().value())
						.message(exception.getMessage())
						.build());
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException exception) {
		return ResponseEntity
				.status(exception.getStatusCode())
				.body(ErrorResponse.builder()
						.statusCode(exception.getStatusCode().value())
						.message(exception.getMessage())
						.build());
	}

}
package com.tus.accounts.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tus.accounts.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomerExistenceException.class)
	public ResponseEntity<ErrorDto> handleCustomerExistenceException(CustomerExistenceException ce, WebRequest wr) {
		ErrorDto ed = new ErrorDto(wr.getDescription(false), ce.getMessage(), HttpStatus.BAD_REQUEST,
				LocalDateTime.now());
		return new ResponseEntity<>(ed, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ce, WebRequest wr) {
		ErrorDto ed = new ErrorDto(wr.getDescription(false), ce.getMessage(), HttpStatus.NOT_FOUND,
				LocalDateTime.now());
		return new ResponseEntity<>(ed, HttpStatus.NOT_FOUND);

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
	Map<String, String> validationErrors = new HashMap<>();
	List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
	validationErrorList.forEach((error) -> {
	String fieldName = ((FieldError) error).getField();
	String validationMsg = error.getDefaultMessage();
	validationErrors.put(fieldName, validationMsg);
	});

	return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
	}
}

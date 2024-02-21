package com.tus.accounts.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ErrorDto {
	private String apiPath;
	private String errorMessage;
	private HttpStatus errorCode;
	private LocalDateTime errorTime;
}

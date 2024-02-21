package com.tus.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(String resouce, String name, String val) {
		super(String.format(resouce+ "not found with the given input data "+name+" : "+val));
	}

}


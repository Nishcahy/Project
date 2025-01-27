package com.busreservation.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException exception){
			Map<String,Object> res=new HashMap<>();
//			res.put("timestamp", LocalDateTime.now());
			res.put("msg",exception.getMessage());
			res.put("status", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);

			
		}
	
}

package com.example.main.exceptions;

import com.example.main.dto.FieldErrorResponse;
import com.example.main.template.Response;
import com.example.main.utils.FormattingText;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(
                        FormattingText.toSnakeCase(error.getField()), 
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        Response<Object> response = Response.errorSpec("VALIDATION_ERROR", "Invalid request", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Customer Not Found
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Response<Object>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        
        Response<Object> response = Response.errorSpec("CUSTOMER_NOT_FOUND", ex.getMessage(), new ArrayList<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGenericException(Exception ex) {
        
        Response<Object> response = Response.errorSpec("INTERNAL_SERVER_ERROR", "Unexpected error occurred", new ArrayList<>());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
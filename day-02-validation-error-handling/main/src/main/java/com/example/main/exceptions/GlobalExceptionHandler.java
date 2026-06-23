package com.example.main.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.main.dto.response.FieldErrorResponse;
import com.example.main.template.Response;
import com.example.main.utils.FormattingText;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MDC_KEY = "correlation_id";

    // Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"validation_error\",\"correlation_id\":\"{}\"}", correlationId);
        
        List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(
                        FormattingText.toSnakeCase(error.getField()), 
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        Response<Object> response = Response.errorSpec("VALIDATION_ERROR", "Invalid request", correlationId, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Response<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"access_denied\",\"error_code\":\"UNAUTHORIZED\",\"correlation_id\":\"{}\"}", correlationId);

        Response<Object> response = Response.errorSpec(
                "UNAUTHORIZED", 
                ex.getMessage(), 
                correlationId,
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Response<Object>> handleForbiddenException(ForbiddenException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"access_denied\",\"error_code\":\"FORBIDDEN\",\"correlation_id\":\"{}\"}", correlationId);

        Response<Object> response = Response.errorSpec(
                "FORBIDDEN", 
                "You do not have permission to access this resource", 
                correlationId,
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Customer Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<Object>> handleCustomerNotFoundException(NotFoundException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"data_not_found\",\"message\":\"{}\",\"correlation_id\":\"{}\"}", ex.getMessage(), correlationId);
        
        Response<Object> response = Response.errorSpec("NOT_FOUND", ex.getMessage(), correlationId, new ArrayList<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Loan Application Not Found
    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public ResponseEntity<Response<Object>> handleLoanApplicationNotFoundException(
            LoanApplicationNotFoundException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"loan_not_found\",\"message\":\"{}\",\"correlation_id\":\"{}\"}", ex.getMessage(), correlationId);
        
        Response<Object> response = Response.errorSpec(
                "LOAN_APPLICATION_NOT_FOUND", 
                ex.getMessage(), 
                correlationId,
                new java.util.ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Duplicate Data
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Response<Object>> handleDuplicateException(DuplicateException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"duplicate_data_error\",\"message\":\"{}\",\"correlation_id\":\"{}\"}", ex.getMessage(), correlationId);

        Response<Object> response = Response.errorSpec(
                "DUPLICATE_DATA", 
                ex.getMessage(), 
                correlationId,
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<Object>> handleBadRequestException(BadRequestException ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.warn("{\"level\":\"warn\",\"event\":\"bad_request_error\",\"message\":\"{}\",\"correlation_id\":\"{}\"}", ex.getMessage(), correlationId);

        Response<Object> response = Response.errorSpec(
                "BAD_REQUEST", 
                ex.getMessage(), 
                correlationId,
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGenericException(Exception ex) {
        String correlationId = MDC.get(MDC_KEY);
        
        log.error("{\"level\":\"error\",\"event\":\"unexpected_error\",\"error_code\":\"INTERNAL_SERVER_ERROR\",\"correlation_id\":\"{}\"}", correlationId, ex);
        
        Response<Object> response = Response.errorSpec(
                "INTERNAL_SERVER_ERROR", 
                "Unexpected error occurred", 
                correlationId, 
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
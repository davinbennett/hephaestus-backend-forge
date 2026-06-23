package com.example.main.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.slf4j.MDC;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Response<T> {
    
    private String message;
    private T data;
    private String code;
    private List<?> errors; 

    @JsonProperty("correlation_id")
    private String correlationId;

    private Response(String message, T data) {
        this.message = message;
        this.data = data;
        this.correlationId = MDC.get("correlation_id");
    }

    private Response(String code, String message, String correlationId, List<?> errors) {
        this.code = code;
        this.message = message;
        this.correlationId = correlationId;
        this.errors = errors;
    }

    public static <T> Response<T> ok(T data, String message) {
        return new Response<>(message, data);
    }

    public static <T> Response<T> created(T data, String message) {
        return new Response<>(message, data);
    }

    public static <T> Response<T> errorSpec(String code, String message, String correlationId, List<?> errors) {
        return new Response<>(code, message, correlationId, errors);
    }
}
package com.example.main.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Response<T> {
    
    private String message;
    
    private T data;

    private String code;
    private List<?> errors; 

    private Response(int statusCode, String message, T data) {
        this.message = message;
        this.data = data;
    }

    private Response(String code, String message, List<?> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static <T> Response<T> ok(T data, String message) {
        return new Response<>(200, message, data);
    }

    public static <T> Response<T> created(T data, String message) {
        return new Response<>(201, message, data);
    }

    public static <T> Response<T> errorSpec(String code, String message, List<?> errors) {
        return new Response<>(code, message, errors);
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<?> getErrors() { return errors; }
    public void setErrors(List<?> errors) { this.errors = errors; }
}
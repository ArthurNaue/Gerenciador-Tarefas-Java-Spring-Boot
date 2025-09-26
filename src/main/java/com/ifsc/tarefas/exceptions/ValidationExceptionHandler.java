package com.ifsc.tarefas.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ValidationExceptionHandler 
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) 
    {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) 
        {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiError apiError = new ApiError
        (
            HttpStatus.BAD_REQUEST.value(), 
            "Bad Request", 
            "Erro de validação dos campos",
             request.getDescription(false), 
             errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception ex, WebRequest request) 
    {
            ApiError apiError = new ApiError
            (
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Internal Server Error", 
                "Erro no servidor",
                request.getDescription(false)
             );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}

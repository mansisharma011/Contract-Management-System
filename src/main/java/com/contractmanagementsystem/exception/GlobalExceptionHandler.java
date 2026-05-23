package com.contractmanagementsystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlesValidationException(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);

    }

    @ExceptionHandler(ContractException.class)
    public ResponseEntity<Map<String,String>> handlesContractDoesNotExistException(ContractException ex){
        Map<String,String> error=new HashMap<>();
        error.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TextExtractionException.class)
    public ResponseEntity<Map<String, String>> handleTextExtractionException(TextExtractionException ex){
        Map<String, String> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String,String>> handleUserException(UserException ex){
       Map<String,String> response = new HashMap<>();

       response.put("message",ex.getMessage());

       return ResponseEntity.badRequest().body(response);
    }
}

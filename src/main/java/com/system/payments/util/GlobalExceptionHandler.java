package com.system.payments.util;

import com.system.payments.exceptions.DuplicatePaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error)->errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<String> handleDuplicatePayment(DuplicatePaymentException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}

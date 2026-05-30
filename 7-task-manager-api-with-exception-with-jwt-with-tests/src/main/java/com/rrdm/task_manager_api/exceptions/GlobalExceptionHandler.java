package com.rrdm.task_manager_api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), request.getRequestURI())
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(404).body(
                new ErrorResponse(404,ex.getMessage(), request.getRequestURI())
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity.status(404).body(
                new ErrorResponse(404,ex.getMessage(), request.getRequestURI())
        );
    }

    //Every other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request){
        return ResponseEntity.status(500).body(
                new ErrorResponse(500,ex.getMessage(),request.getRequestURI())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ){
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((e) -> e.getField()+": "+e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400).body(
                new ErrorResponse(400,errors,request.getRequestURI())
        );
    }
}

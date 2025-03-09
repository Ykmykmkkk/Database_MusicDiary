package com.example.userservice.presentation.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex)
    {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> errors = constraintViolations.stream().map(
                constraintViolation -> extractField(constraintViolation.getPropertyPath())
                        + ", " + constraintViolation.getMessage()
        ).toList();

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_GATEWAY);
    }
    public String extractField(Path path){
        String fieldName = null;
        for (Path.Node node : path) {
            fieldName = node.getName(); // 마지막 노드의 이름이 필드 이름
        }
        return fieldName;
    }   // 도메인 객체의 유효성 검사 중 문제가 있을 시 예외 처리


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(
                fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage()
        ).toList();

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }   // Presentation 계층의 DTO 유효성 검사 중 문제가 있을 시 예외 처리

}

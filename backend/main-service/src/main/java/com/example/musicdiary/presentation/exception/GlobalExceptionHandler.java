package com.example.musicdiary.presentation.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class) // 도메인 객체의 유효성 검사 중 문제가 있을 시 예외 처리
    public ResponseEntity<ErrorMessage> handleConstraintViolatedException(
            ConstraintViolationException ex
    ){
        System.out.println("ConstraintViolationException handled!");
      Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
      List<String> errors = constraintViolations.stream().map(
              constraintViolation -> extractField(constraintViolation.getPropertyPath()) + ", " + constraintViolation.getMessage()
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
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) // DTO 유효성 검사 중 문제가 있을 시 예외 처리
    public ResponseEntity<ErrorMessage> hanldeMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ){
        System.out.println("MethodViolationException handled!"); // 디버깅 로그
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(
                fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage()
        ).toList();

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}



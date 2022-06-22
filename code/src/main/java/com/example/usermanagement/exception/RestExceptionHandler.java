package com.example.usermanagement.exception;

import com.example.usermanagement.dto.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ResponseMessage> handleEntityNotFound(EntityNotFoundException ex){

        ResponseMessage error = new ResponseMessage(HttpStatus.NOT_FOUND, "Entity not found: " + ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    private ResponseEntity<ResponseMessage> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex){

        ResponseMessage error = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Entity already exists: " + ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseMessage error = new ResponseMessage(HttpStatus.BAD_REQUEST, errors.toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
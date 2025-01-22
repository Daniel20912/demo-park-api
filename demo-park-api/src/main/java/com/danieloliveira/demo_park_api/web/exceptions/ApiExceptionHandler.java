package com.danieloliveira.demo_park_api.web.exceptions;

import com.danieloliveira.demo_park_api.exceptions.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// facilita a inclusão de um logger (registrador de log) em uma classe
@Slf4j
// essa annotation indica que quando as excessões registradas nessa classe forem laçadas, essa classe vai capturá-las e fazer o seu tratamento
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // registra a excessão
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request, BindingResult result) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", result));
    }


    @ExceptionHandler(UsernameUniqueViolationException.class) // registra a excessão
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(UsernameUniqueViolationException e, HttpServletRequest request) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.CONFLICT, e.getMessage()));
    }


}

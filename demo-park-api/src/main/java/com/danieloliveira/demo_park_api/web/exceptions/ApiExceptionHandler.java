package com.danieloliveira.demo_park_api.web.exceptions;

import com.danieloliveira.demo_park_api.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// facilita a inclusão de um logger (registrador de log) em uma classe
@Slf4j
// essa annotation indica que quando as excessões registradas nessa classe forem laçadas, essa classe vai capturá-las e fazer o seu tratamento
@RestControllerAdvice
public class ApiExceptionHandler {

    // aceita tanto a exceção MethodArgumentNotValidException quanto a CpfUniqueViolationException
    @ExceptionHandler(MethodArgumentNotValidException.class) // registra a excessão
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request, BindingResult result) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", result));
    }


    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class, CodigoUniqueViolationException.class})
    // registra a excessão
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(RuntimeException e, HttpServletRequest request) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class) // registra a excessão
    public ResponseEntity<ErrorMessage> passwordInvalidException(PasswordInvalidException e, HttpServletRequest request) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, e.getMessage()));
    }


    // excessão para quando o usuário não tem permissão de acesso a um metodo
    @ExceptionHandler(AccessDeniedException.class) // registra a excessão
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {

        log.error("Api Error - : ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request, HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(Exception.class) // registra a excessão
    public ResponseEntity<ErrorMessage> innternalServerErrorexception(Exception e, HttpServletRequest request) {
        ErrorMessage error = new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        log.error("Internal Server Error {} {} ", error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(error);
    }


}

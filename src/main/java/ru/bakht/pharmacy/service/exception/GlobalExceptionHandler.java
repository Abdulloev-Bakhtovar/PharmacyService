package ru.bakht.pharmacy.service.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST API.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение EntityNotFoundException.
     *
     * @param ex исключение EntityNotFoundException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        return ex.getMessage();
    }

    /**
     * Обрабатывает исключение IllegalArgumentException.
     *
     * @param ex исключение IllegalArgumentException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ex.getMessage();
    }

    /**
     * Обрабатывает исключение MethodArgumentNotValidException.
     *
     * @param ex исключение MethodArgumentNotValidException.
     * @return карта ошибок валидации.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error("Validation errors: {}", errors);
        return errors;
    }

    /**
     * Обрабатывает исключение ConstraintViolationException.
     *
     * @param ex исключение ConstraintViolationException.
     * @return карта ошибок валидации.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error ->
                errors.put(error.getPropertyPath().toString(), error.getMessage()));
        log.error("Constraint violation: {}", errors);
        return errors;
    }

    /**
     * Обрабатывает исключение MethodArgumentTypeMismatchException.
     *
     * @param ex исключение MethodArgumentTypeMismatchException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "Неверный тип параметра: " + ex.getValue();
        log.error("Type mismatch: {}", error);
        return error;
    }

    /**
     * Обрабатывает исключение HttpMessageNotReadableException.
     *
     * @param ex исключение HttpMessageNotReadableException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String error = "Некорректный формат JSON запроса: " + ex.getMessage();
        log.error("Message not readable: {}", error);
        return error;
    }

    /**
     * Обрабатывает исключение NoHandlerFoundException.
     *
     * @param ex исключение NoHandlerFoundException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String error = "Не найден обработчик для " + ex.getRequestURL();
        log.error("No handler found: {}", error);
        return error;
    }

    /**
     * Обрабатывает исключение IllegalStateException.
     *
     * @param ex исключение IllegalStateException.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage());
        return ex.getMessage();
    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param ex исключение.
     * @return сообщение об ошибке.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex) {
        log.error("Generic error: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }
}

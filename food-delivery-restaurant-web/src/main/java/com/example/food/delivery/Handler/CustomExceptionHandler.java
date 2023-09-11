package com.example.food.delivery.Handler;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.RestaurantManagementExceptions;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity.ok(BaseResponse.createSystemErrorResponse());
    }

    @ExceptionHandler(RestaurantManagementExceptions.UnrecognizedTokenException.class)
    public ResponseEntity<String> handleUnrecognizedTokenException(RestaurantManagementExceptions.UnrecognizedTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Object> handleMethodArguments(MethodArgumentNotValidException exception) {
        AtomicReference<String> errorMessage = new AtomicReference<>(exception.getMessage());
        exception.getFieldErrors().forEach((fieldError -> {
            errorMessage.set(fieldError.getDefaultMessage());
        }));
        return new BaseResponse<>(false, com.example.food.delivery.Response.ResponseStatus.ERROR.getStatus(), errorMessage.get(), false);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) throws IOException {
        return new BaseResponse<>(false, com.example.food.delivery.Response.ResponseStatus.ERROR.getStatus(), "Invalid request", false);
    }
}
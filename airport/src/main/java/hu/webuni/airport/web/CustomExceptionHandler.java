package hu.webuni.airport.web;

import hu.webuni.airport.service.NonUniqueIataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NonUniqueIataException.class)
    public ResponseEntity<MyError> handleNonUniqueIata(NonUniqueIataException e, WebRequest req){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MyError(e.getMessage(), 1002));
    }
}

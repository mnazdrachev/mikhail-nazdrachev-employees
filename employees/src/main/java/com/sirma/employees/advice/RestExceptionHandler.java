package com.sirma.employees.advice;

import com.sirma.employees.exception.CsvParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CsvParseException.class})
    public ResponseEntity<Object> handleCsvParseException(Exception ex) {
        return new ResponseEntity<>("Something went wrong during parsing CSV file: " + ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

package az.expressbank.book.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.FeignClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> FeignClientException(FeignException.FeignClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> NotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> JsonFormatException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json doesn't match the format expected");

    }
    @ExceptionHandler(EntityParsingException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public  ResponseEntity<String> EntityParsingException(Exception ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

}

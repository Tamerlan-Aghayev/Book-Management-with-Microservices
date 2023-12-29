package az.express.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(MissingAuthorizationHeaderException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public ResponseEntity<ErrorResponse> missingAuthorizationHeaderException(Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()));
        }
        @ExceptionHandler(TokenValidationException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ResponseEntity<ErrorResponse> tokenValidationException(Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage()));
        }


        @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> JsonFormatException(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));

    }

    }

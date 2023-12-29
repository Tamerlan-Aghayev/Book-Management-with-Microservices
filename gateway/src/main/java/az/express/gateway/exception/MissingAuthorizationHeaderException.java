package az.express.gateway.exception;

public class MissingAuthorizationHeaderException extends RuntimeException{
    public MissingAuthorizationHeaderException(String message){
        super(message);
    }
}

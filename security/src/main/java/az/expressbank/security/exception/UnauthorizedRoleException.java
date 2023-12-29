package az.expressbank.security.exception;

public class UnauthorizedRoleException extends RuntimeException{
    public UnauthorizedRoleException(String message){
        super(message);
    }
}

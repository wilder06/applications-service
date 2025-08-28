package pe.com.creditya.model.common.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

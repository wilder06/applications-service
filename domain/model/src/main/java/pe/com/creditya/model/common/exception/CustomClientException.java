package pe.com.creditya.model.common.exception;

public class CustomClientException extends RuntimeException {
    public CustomClientException(String message) {
        super(message);
    }
    public CustomClientException(String message, Throwable cause) {
        super(message, cause);
    }

}

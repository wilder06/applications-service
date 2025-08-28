package pe.com.creditya.model.common.exception;

public class TechnicalException extends RuntimeException {
    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}

package pe.com.creditya.model.common.utils;

import pe.com.creditya.model.common.exception.ApplicationException;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.exception.UserNotFoundException;

public class DomainErrorMapper {

    public static Throwable map(Throwable error) {
        return switch (error) {
            case UserNotFoundException e -> e;
            case IllegalArgumentException e -> new TechnicalException("Validación de tipo de préstamo inválido", e);
            case IllegalStateException e -> new TechnicalException("Estado inicial no configurado", e);
            default -> new ApplicationException("Error inesperado", error);
        };
    }
}
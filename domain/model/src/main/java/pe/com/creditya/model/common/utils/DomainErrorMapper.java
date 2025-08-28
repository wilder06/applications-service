package pe.com.creditya.model.common.utils;

import pe.com.creditya.model.common.exception.ApplicationException;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.exception.UserNotFoundException;

import java.util.Map;
import java.util.function.Function;

public class DomainErrorMapper {

    private static final Map<Class<? extends Throwable>, Function<Throwable, Throwable>> ERROR_MAPPING =
            Map.of(
                    UserNotFoundException.class, e -> e,
                    IllegalArgumentException.class, e -> new TechnicalException("Validación de tipo de préstamo inválido", e),
                    IllegalStateException.class, e -> new TechnicalException("Estado inicial no configurado", e)
            );

    public static Throwable map(Throwable error) {
        return ERROR_MAPPING.getOrDefault(
                error.getClass(),
                e -> new ApplicationException("Error inesperado", e)
        ).apply(error);
    }

}

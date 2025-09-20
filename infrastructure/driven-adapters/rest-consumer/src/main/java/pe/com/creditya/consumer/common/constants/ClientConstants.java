package pe.com.creditya.consumer.common.constants;

public class ClientConstants {
    public static final String LOGGER_UNAUTHORIZED = "Unauthorized error: {}";
    public static final String LOGGER_FORBIDDEN = "Forbidden error: {}";
    public static final String LOGGER_CLIENT_ERROR = "Client error {}: {}";
    public static final String LOGGER_SERVER_ERROR = "Server error {}: {}";
    public static final String LOGGER_FALLBACK_USER = "Fallback activado para documento {}. Causa: {}";
    public static final String LOGGER_FALLBACK_USERS = "Fallback activado para {} emails. Causa: {}";

    public static final String ERROR_USER_SERVICE_UNAVAILABLE = "User service unavailable: %s";
    public static final String ERROR_USERS_SERVICE_UNAVAILABLE = "Users service unavailable: %s";
    public static final String ERROR_TOKEN_INVALID = "Token inválido o expirado";
    public static final String ERROR_ACCESS_DENIED = "Acceso denegado";
    public static final String ERROR_NO_ERROR_DETAILS = "No error details";
    public static final String ERROR_CLIENT_FORMAT = "Client error %d: %s";
    public static final String ERROR_SERVER_FORMAT = "Server error %d: %s";
}

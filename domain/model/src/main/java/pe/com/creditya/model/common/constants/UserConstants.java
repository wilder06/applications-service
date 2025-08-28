package pe.com.creditya.model.common.constants;

public class UserConstants {
    public static final String USER_NOT_FOUND="User not found with documentNumber: ";
    public static final String LOGGER_INIT_CONSUME_CLIENT="Iniciando la validacion del usuario";
    public static final String PATH_CLIENT="/api/v1/usuarios/{documentNumber}";
    public static final String LOGGER_CLIENT_ERROR="Error from client API: {}";
    public static final String LOGGER_USER_NOT_FOUND="No se encontro el cliente {}: {}";
    public static final String LOGGER_USER="Se encontro el cliente con Numero de Documento: {}";
    public static final String LOGGER_TYPE_LOAN_ERROR="Tipo de préstamo no válido o inactivo";
    public static final String TYPE_STATUS="Pendiente de revisión";
    public static final String LOGGER_ERROR_TYPE_STATUS="Estado inicial no configurado";
}

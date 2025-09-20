package pe.com.creditya.security.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Constants {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String JCEKS_PREFIX = "JCEKS";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADVISOR = "ADVISOR";
    public static final String CLAIMS_NAME = "roles";
    public static final String LOG_PARSE_ERROR = "Error al parsear el token JWT";
    public static final String LOG_INVALID_TOKEN = "Token inválido o expirado: {}";
    public static final String LOG_MISSING_AUTH_HEADER = "No se encontró cabecera Authorization válida";
    public static final String LOG_MISSING_JWT_AUTHENTICATION= "Error inesperado al autenticar token JWT";
    public static final String LOG_MISSING_FAILED_PUBLIC_KEY= "Failed to load public key from keystore";
}

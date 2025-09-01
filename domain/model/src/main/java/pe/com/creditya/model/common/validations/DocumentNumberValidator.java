package pe.com.creditya.model.common.validations;

import pe.com.creditya.model.common.exception.BusinessValidationException;

import java.util.regex.Pattern;

public class DocumentNumberValidator {

    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}$");
    private static final Pattern CE_PATTERN = Pattern.compile("^[A-Z0-9]{9,12}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^[0-9]{11}$");

    public static void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new BusinessValidationException("documentNumber", "no debe estar vacío");
        }

        String cleanedDocument = documentNumber.trim().toUpperCase();

        if (DNI_PATTERN.matcher(cleanedDocument).matches()) {
            validateDNI(cleanedDocument);
            return;
        }

        if (CE_PATTERN.matcher(cleanedDocument).matches()) {
            validateCarnetExtranjeria(cleanedDocument);
            return;
        }

        if (PASSPORT_PATTERN.matcher(cleanedDocument).matches()) {
            validatePassport(cleanedDocument);
            return;
        }

        if (RUC_PATTERN.matcher(cleanedDocument).matches()) {
            validateRUC(cleanedDocument);
            return;
        }

        throw new BusinessValidationException("documentNumber",
                "formato inválido. Debe ser DNI (8 dígitos), CE (9-12 caracteres), " +
                        "Pasaporte (6-12 caracteres) o RUC (11 dígitos)");
    }

    private static void validateDNI(String dni) {
        if (dni.matches("^0+$")) {
            throw new BusinessValidationException("documentNumber", "DNI no válido");
        }
    }

    private static void validateCarnetExtranjeria(String ce) {
        if (!ce.matches("^[A-Z]?[0-9]+$")) {
            throw new BusinessValidationException("documentNumber",
                    "Formato de Carnet de Extranjería inválido");
        }
    }

    private static void validatePassport(String passport) {
        if (!passport.matches("^(?=.*[A-Z])(?=.*[0-9]).+$")) {
            throw new BusinessValidationException("documentNumber",
                    "Formato de Pasaporte inválido");
        }
    }

    private static void validateRUC(String ruc) {
        if (ruc.matches("^0+$")) {
            throw new BusinessValidationException("documentNumber", "RUC no válido");
        }

        if (!isValidRUC(ruc)) {
            throw new BusinessValidationException("documentNumber", "RUC inválido");
        }
    }

    private static boolean isValidRUC(String ruc) {
        if (ruc.length() != 11) return false;
        int[] factors = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int sum = 0;

        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(ruc.charAt(i)) * factors[i];
        }

        int checkDigit = 11 - (sum % 11);
        if (checkDigit == 11) checkDigit = 0;
        if (checkDigit == 10) checkDigit = 1;

        return checkDigit == Character.getNumericValue(ruc.charAt(10));
    }
}
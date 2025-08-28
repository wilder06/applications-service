package pe.com.creditya.model.common.exception;

import pe.com.creditya.model.common.constants.UserConstants;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String documentNumber) {
    super(UserConstants.USER_NOT_FOUND + documentNumber);
  }
}

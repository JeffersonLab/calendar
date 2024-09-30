package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class LiaisonValidator {
  public void validate(String liaison) throws ValidationException {
    if (liaison != null && liaison.length() > 64) {
      throw new ValidationException("Liaison cannot be more than 64 characters");
    }
  }
}

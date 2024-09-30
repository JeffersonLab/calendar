package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class ShiftValidator {
  public void validate(Shift shift) throws ValidationException {
    if (shift == null) {
      throw new ValidationException("Shift is required");
    }
  }
}

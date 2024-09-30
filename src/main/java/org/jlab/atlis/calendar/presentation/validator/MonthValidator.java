package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class MonthValidator {
  public void validate(int month) throws ValidationException {
    if (month > 12 || month < 1) {
      throw new ValidationException("Month must be between 1 and 12");
    }
  }
}

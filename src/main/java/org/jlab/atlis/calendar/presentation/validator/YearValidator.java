package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class YearValidator {
  public void validate(int year) throws ValidationException {
    if (year > 6000 || year < 1970) {
      throw new ValidationException("Year must be between 1970 and 6000");
    }
  }
}

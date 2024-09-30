package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class WeekValidator {
  public void validate(int week) throws ValidationException {
    if (week > 54 || week < 1) {
      throw new ValidationException("Week must be between 1 and 54");
    }
  }
}

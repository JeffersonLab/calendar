package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class TitleValidator {
  public void validate(String title) throws ValidationException {
    if (title == null || title.trim().isEmpty()) {
      throw new ValidationException("Title is required");
    }

    if (title.length() > 128) {
      throw new ValidationException("Title cannot be more than 128 characters");
    }
  }
}

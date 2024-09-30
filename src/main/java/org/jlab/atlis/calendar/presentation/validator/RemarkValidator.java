package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class RemarkValidator {
  public void validate(String remark) throws ValidationException {
    if (remark != null && remark.length() > 512) {
      throw new ValidationException("Operability Comments cannot be more than 512 characters");
    }
  }
}

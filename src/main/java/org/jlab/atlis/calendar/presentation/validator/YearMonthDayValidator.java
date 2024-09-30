package org.jlab.atlis.calendar.presentation.validator;

import java.util.Date;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
public class YearMonthDayValidator {
  public void validate(Date yearMonthDay) throws ValidationException {
    if (yearMonthDay == null) {
      throw new ValidationException("Date is required");
    }
  }

  public void validate(Date startYearMonthDay, Date endYearMonthDay) throws ValidationException {
    if (startYearMonthDay == null ^ endYearMonthDay == null) {
      throw new ValidationException("Both start and end dates must either be specified or not");
    }

    if (startYearMonthDay != null && endYearMonthDay != null) {
      if (startYearMonthDay.after(endYearMonthDay)) {
        throw new ValidationException("The start date must not come after the end date");
      }
    }
  }
}

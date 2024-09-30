package org.jlab.atlis.calendar.presentation.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
public class YearMonthDayConverter {
  public Date getObject(String yearMonthDayStr) throws ConverterException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);

    Date yearMonthDayObj = null;

    if (yearMonthDayStr == null || yearMonthDayStr.trim().isEmpty()) {
      return null;
    } else {
      try {
        yearMonthDayObj = format.parse(yearMonthDayStr);
      } catch (ParseException e) {
        throw new ConverterException("Date must be in the format yyyy-mm-dd");
      }
    }

    return yearMonthDayObj;
  }

  public String getString(Date yearMonthDayObj) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);

    String yearMonthDayStr = format.format(yearMonthDayObj);

    return yearMonthDayStr;
  }
}

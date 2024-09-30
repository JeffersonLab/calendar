package org.jlab.atlis.calendar.presentation.converter;

import java.util.Date;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.persistence.projection.DayAndShift;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
public class DayAndShiftConverter {
  private final YearMonthDayConverter ymdConverter = new YearMonthDayConverter();
  private final ShiftConverter shiftConverter = new ShiftConverter();

  public DayAndShift getObject(String dasStr) throws ConverterException {
    DayAndShift dasObj = new DayAndShift();

    String[] tokens = dasStr.split(" ");

    if (tokens.length != 2) {
      throw new ConverterException("Day and Shift must be in the format yyyy-mm-dd shift");
    }

    Date day = ymdConverter.getObject(tokens[0]);
    Shift shift = shiftConverter.getObject(tokens[1]);

    dasObj.setDay(day);
    dasObj.setShift(shift);

    return dasObj;
  }

  public String getString(DayAndShift dasObj) {
    String yearMonthDayStr = ymdConverter.getString(dasObj.getDay());
    String shiftStr = shiftConverter.getString(dasObj.getShift());

    return yearMonthDayStr + " " + shiftStr;
  }
}

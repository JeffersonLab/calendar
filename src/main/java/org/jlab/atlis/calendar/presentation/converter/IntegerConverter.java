package org.jlab.atlis.calendar.presentation.converter;

import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
public class IntegerConverter {

  public Integer getObject(String iStr) throws ConverterException {
    Integer i = null;

    if (iStr != null && !iStr.trim().isEmpty()) {
      try {
        i = Integer.valueOf(iStr);
      } catch (NumberFormatException e) {
        throw new ConverterException("Not a number");
      }
    }

    return i;
  }

  public String getString(Integer i) {
    return i.toString();
  }
}

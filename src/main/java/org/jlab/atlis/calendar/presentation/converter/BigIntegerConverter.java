package org.jlab.atlis.calendar.presentation.converter;

import java.math.BigInteger;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
public class BigIntegerConverter {

  public BigInteger getObject(String iStr) throws ConverterException {
    BigInteger i = null;

    if (iStr != null && !iStr.trim().isEmpty()) {
      try {
        i = new BigInteger(iStr);
      } catch (NumberFormatException e) {
        throw new ConverterException("Not a number");
      }
    }

    return i;
  }

  public String getString(BigInteger i) {
    return i.toString();
  }
}

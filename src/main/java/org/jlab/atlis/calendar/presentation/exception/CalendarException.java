package org.jlab.atlis.calendar.presentation.exception;

import jakarta.servlet.ServletException;

/**
 * @author ryans
 */
public class CalendarException extends ServletException {
  public CalendarException(String message) {
    super(message);
  }
}

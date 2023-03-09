package org.jlab.atlis.calendar.presentation.exception;

import javax.servlet.ServletException;

/**
 *
 * @author ryans
 */
public class CalendarException extends ServletException {
    public CalendarException(String message) {
        super(message);
    }
}

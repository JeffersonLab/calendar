package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.utility.CalendarServletHelper;

/**
 * @author ryans
 */
@WebServlet(
    name = "ViewOccurrence",
    urlPatterns = {"/view-occurrence"})
public class ViewOccurrence extends HttpServlet {

  @EJB private OccurrenceFacade occurrenceFacade;

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String occurrenceIdStr = request.getParameter("occurrenceId");

    BigIntegerConverter bic = new BigIntegerConverter();

    if (occurrenceIdStr == null || occurrenceIdStr.trim().isEmpty()) {
      throw new CalendarException("Unable to view occurrence: occurrenceId must be specified");
    }

    BigInteger occurrenceId = null;

    try {
      occurrenceId = bic.getObject(occurrenceIdStr);
    } catch (ConverterException e) {
      throw new CalendarException(
          "Unable to view occurrence: Invalid occurrenceId: " + e.getMessage());
    }

    Occurrence occurrence = occurrenceFacade.find(occurrenceId);

    if (occurrence == null) {
      throw new CalendarException(
          "Unable to view occurrence: Unable to locate occurrence with ID = " + occurrenceId);
    }

    Collections.sort(occurrence.getStyles());

    request.setAttribute("occurrence", occurrence);

    Integer calendarId = occurrence.getEvent().getCalendar().getCalendarId();
    request.setAttribute("calendarId", calendarId);

    getServletConfig()
        .getServletContext()
        .getRequestDispatcher("/WEB-INF/views/view-occurrence.jsp")
        .forward(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String returnUrl = CalendarServletHelper.determineAndSetReturnPath(request);

    String occurrenceIdStr = request.getParameter("occurrenceId");

    BigIntegerConverter bic = new BigIntegerConverter();

    if (occurrenceIdStr == null || occurrenceIdStr.trim().isEmpty()) {
      throw new CalendarException("Unable to delete occurrence: occurrenceId must be specified");
    }

    BigInteger occurrenceId = null;

    try {
      occurrenceId = bic.getObject(occurrenceIdStr);
    } catch (ConverterException e) {
      throw new CalendarException(
          "Unable to delete occurrence: Invalid occurrenceId: " + e.getMessage());
    }

    Occurrence occurrence = occurrenceFacade.find(occurrenceId);

    if (occurrence == null) {
      throw new CalendarException(
          "Unable to delete occurrence: Unable to locate occurrence with ID = " + occurrenceId);
    }

    BigInteger eventId = occurrence.getEvent().getEventId();

    Integer calendarId = occurrence.getEvent().getCalendar().getCalendarId();

    boolean eventRemoved = occurrenceFacade.removeAndCleanupEvent(occurrence);

    if (eventRemoved && returnUrl.contains("view-event")) {
      response.sendRedirect("view-outlook?calendar=" + calendarId);
    } else if (returnUrl.contains("view-occurrence")) {
      response.sendRedirect("view-outlook?calendar=" + calendarId);
    } else {
      response.sendRedirect(returnUrl);
    }
  }
}

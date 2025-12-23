package org.jlab.atlis.calendar.presentation.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import org.jlab.atlis.calendar.business.session.AbstractFacade;
import org.jlab.atlis.calendar.business.session.CalendarFacade;
import org.jlab.atlis.calendar.business.session.EventFacade;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
@WebServlet(
    name = "ViewEvent",
    urlPatterns = {"/view-event"})
public class ViewEvent extends HttpServlet {

  @EJB private EventFacade eventFacade;
  @EJB private CalendarFacade calendarFacade;

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
    String eventIdStr = request.getParameter("eventId");

    BigIntegerConverter bic = new BigIntegerConverter();

    if (eventIdStr == null || eventIdStr.trim().isEmpty()) {
      throw new CalendarException("Unable to view event: eventId must be specified");
    }

    BigInteger eventId = null;

    try {
      eventId = bic.getObject(eventIdStr);
    } catch (ConverterException e) {
      throw new CalendarException("Unable to view event: Invalid eventId: " + e.getMessage());
    }

    Event event = eventFacade.findWithOccurrences(eventId);

    if (event == null) {
      throw new CalendarException(
          "Unable to view event: Unable to locate event with ID = " + eventId);
    }

    request.setAttribute("event", event);

    Integer calendarId = event.getCalendar().getCalendarId();

    request.setAttribute("calendarId", calendarId);

    Calendar selectedCalendar = calendarFacade.find(calendarId);
    request.setAttribute("selectedCalendar", selectedCalendar);

    List<Calendar> calendarList =
        calendarFacade.findAll(new AbstractFacade.OrderDirective("orderId"));
    request.setAttribute("calendarList", calendarList);

    getServletConfig()
        .getServletContext()
        .getRequestDispatcher("/WEB-INF/views/view-event.jsp")
        .forward(request, response);
  }

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String eventIdStr = request.getParameter("eventId");

    BigIntegerConverter bic = new BigIntegerConverter();

    if (eventIdStr == null || eventIdStr.trim().isEmpty()) {
      throw new CalendarException("Unable to delete event: eventId must be specified");
    }

    BigInteger eventId = null;

    try {
      eventId = bic.getObject(eventIdStr);
    } catch (ConverterException e) {
      throw new CalendarException("Unable to delete event: Invalid eventId: " + e.getMessage());
    }

    Event event = eventFacade.find(eventId);

    if (event == null) {
      throw new CalendarException(
          "Unable to delete event: Unable to locate event with ID = " + eventId);
    }

    Integer calendarId = event.getCalendar().getCalendarId();

    eventFacade.remove(event);

    response.sendRedirect("view-outlook?calendar=" + calendarId);
  }
}

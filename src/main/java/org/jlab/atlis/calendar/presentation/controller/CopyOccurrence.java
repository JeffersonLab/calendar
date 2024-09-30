package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.AbstractFacade;
import org.jlab.atlis.calendar.business.session.CalendarFacade;
import org.jlab.atlis.calendar.business.session.EventFacade;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.business.session.OccurrenceStyleChoiceFacade;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.persistence.projection.DayAndShift;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.DisplayConverter;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.OccurrenceStyleChoiceConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;
import org.jlab.atlis.calendar.presentation.utility.CalendarServletHelper;
import org.jlab.atlis.calendar.presentation.validator.DescriptionValidator;
import org.jlab.atlis.calendar.presentation.validator.DisplayValidator;
import org.jlab.atlis.calendar.presentation.validator.LiaisonValidator;
import org.jlab.atlis.calendar.presentation.validator.RemarkValidator;
import org.jlab.atlis.calendar.presentation.validator.TitleValidator;

/**
 * @author ryans
 */
@WebServlet(
    name = "CopyOccurrence",
    urlPatterns = {"/copy-occurrence"})
public class CopyOccurrence extends HttpServlet {

  @EJB private EventFacade eventFacade;
  @EJB private OccurrenceFacade occurrenceFacade;
  @EJB private OccurrenceStyleChoiceFacade styleChoiceFacade;
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
    CalendarServletHelper.determineAndSetReturnPath(request);

    String copyIdStr = request.getParameter("copyId");

    BigIntegerConverter bic = new BigIntegerConverter();

    Occurrence occurrence = null;

    if (copyIdStr == null || copyIdStr.trim().isEmpty()) {
      throw new CalendarException("Unable to copy occurrence: copyId must not be empty");
    }

    BigInteger copyId = null;
    Occurrence copy = null;

    try {
      copyId = bic.getObject(copyIdStr);
    } catch (ConverterException e) {
      throw new CalendarException("Unable to copy occurrence: Invalid copyId: " + e.getMessage());
    }

    copy = occurrenceFacade.find(copyId);

    if (copy == null) {
      throw new CalendarException(
          "Unable to copy occurrence: Unable to locate occurrence to copy with ID = " + copyId);
    }

    occurrence = new Occurrence(copy);
    occurrence.copyStyles(copy);

    occurrence.setShift(Shift.DAY); // Set default

    List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();

    request.setAttribute("styleChoices", styleChoices);

    request.setAttribute("occurrence", occurrence);

    Integer calendarId = occurrence.getEvent().getCalendar().getCalendarId();

    request.setAttribute("calendarId", calendarId);

    Calendar selectedCalendar = calendarFacade.find(calendarId);
    request.setAttribute("selectedCalendar", selectedCalendar);

    List<Calendar> calendarList =
        calendarFacade.findAll(new AbstractFacade.OrderDirective("orderId"));
    request.setAttribute("calendarList", calendarList);

    getServletConfig()
        .getServletContext()
        .getRequestDispatcher("/WEB-INF/views/copy-occurrence.jsp")
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
    String returnPath = CalendarServletHelper.determineAndSetReturnPath(request);

    Map<String, String> messages = new HashMap<>();
    request.setAttribute("messages", messages);

    // If client included a session ID, but it is not valid or if remote user is null
    if ((request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid())
        || request.getRemoteUser() == null) {
      throw new CalendarException("Your session has expired.  Please re-login.");
    }

    Occurrence occurrence = convertAndValidateOccurrenceWithoutDate(request);
    List<OccurrenceStyleChoice> styles = convertAndValidateStyles(request);

    Event event = null;

    try {
      event = convertAndValidateEvent(request);
    } catch (Exception e) {
      messages.put("eventId", e.getMessage());
    }

    Set<DayAndShift> instances = CalendarServletHelper.convertAndValidateDayAndShifts(request);

    if (!messages.isEmpty()) {
      String action = "copy";
      messages.put("error", "Unable to " + action + " occurrence");

      for (String m : messages.keySet()) {
        System.out.println("message key: " + m);
        System.out.println("messgae value:" + messages.get(m));
      }

      List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();
      request.setAttribute("styleChoices", styleChoices);

      getServletConfig()
          .getServletContext()
          .getRequestDispatcher("/WEB-INF/views/copy-occurrence.jsp")
          .forward(request, response);
      return;
    }

    Integer calendarId; // Existing events will ignore URL parameters, but create new uses params
    try {
      IntegerConverter intConverter = new IntegerConverter();
      calendarId = intConverter.getObject(request.getParameter("calendar"));

      if (calendarId == null) {
        calendarId = 1;
      }

    } catch (ConverterException e) {
      throw new CalendarException("Unable to convert calendar id: " + e.getMessage());
    }

    Integer existingCalendarId = event.getCalendar().getCalendarId();

    if (Objects.equals(
        calendarId, existingCalendarId)) { // Copy within same Calendar, use existing event
      occurrence.setEvent(event);

      eventFacade.copyOccurrence(occurrence, styles, instances);
    } else { // Copy to a new Calendar, use new event
      Event newEvent = new Event();

      Calendar selectedCalendar = calendarFacade.find(calendarId);
      newEvent.setCalendar(selectedCalendar);

      occurrence.setEvent(newEvent);

      newEvent.setTaskId(event.getTaskId());

      occurrenceFacade.updateStyles(styles, occurrence);

      eventFacade.createEventWithOccurrences(occurrence, instances);
    }

    // calendar might have changed
    returnPath = returnPath.replaceFirst("calendar=[0-9]+", "calendar=" + calendarId);

    response.sendRedirect(returnPath);
  }

  private Event convertAndValidateEvent(HttpServletRequest request)
      throws ConverterException, ValidationException {
    String eventIdStr = request.getParameter("eventId");

    BigInteger eventId = null;
    Event event = null;

    if (eventIdStr != null && !eventIdStr.trim().isEmpty()) {

      BigIntegerConverter bic = new BigIntegerConverter();

      try {
        eventId = bic.getObject(eventIdStr);
      } catch (ConverterException e) {
        throw new ConverterException("Invaild eventId:  Not a number");
      }

      if (eventId != null) {
        event = eventFacade.findWithOccurrences(eventId);

        if (event == null) {
          throw new ValidationException("Unable to locate event with ID = " + eventId);
        }
      }
    } else {
      throw new ValidationException("eventId must be specified");
    }

    return event;
  }

  private List<OccurrenceStyleChoice> convertAndValidateStyles(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    String[] styleIdStrArray = request.getParameterValues("style");
    List<OccurrenceStyleChoice> styles = null;

    OccurrenceStyleChoiceConverter oscc = new OccurrenceStyleChoiceConverter();

    try {
      styles = oscc.getObjects(styleIdStrArray);
    } catch (ConverterException e) {
      messages.put("style", e.getMessage());
    }

    return styles;
  }

  private Occurrence convertAndValidateOccurrenceWithoutDate(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    Occurrence occurrence = null;

    String occurrenceIdStr = request.getParameter("occurrenceId");
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    String liaison = request.getParameter("liaison");
    String displayStr = request.getParameter("display");
    String remark = request.getParameter("remark");

    BigIntegerConverter bic = new BigIntegerConverter();
    TitleValidator titleValidator = new TitleValidator();
    DescriptionValidator descriptionValidator = new DescriptionValidator();
    LiaisonValidator liaisonValidator = new LiaisonValidator();
    DisplayConverter displayConverter = new DisplayConverter();
    DisplayValidator displayValidator = new DisplayValidator();
    RemarkValidator remarkValidator = new RemarkValidator();

    BigInteger occurrenceId = null;
    Date yearMonthDayObj = null;
    Shift shiftObj = null;
    Display displayObj = null;

    if (occurrenceIdStr != null && !occurrenceIdStr.trim().isEmpty()) {
      try {
        occurrenceId = bic.getObject(occurrenceIdStr);
      } catch (ConverterException e) {
        messages.put("occurrenceId", e.getMessage());
      }
    }

    try {
      titleValidator.validate(title);
    } catch (ValidationException e) {
      messages.put("title", e.getMessage());
    }

    try {
      descriptionValidator.validate(description);
    } catch (ValidationException e) {
      messages.put("description", e.getMessage());
    }

    try {
      liaisonValidator.validate(liaison);
    } catch (ValidationException e) {
      messages.put("liaison", e.getMessage());
    }

    try {
      displayObj = displayConverter.getObject(displayStr);
      displayValidator.validate(displayObj);
    } catch (ConverterException e) {
      messages.put("display", e.getMessage());
    } catch (ValidationException e) {
      messages.put("display", e.getMessage());
    }

    try {
      remarkValidator.validate(remark);
    } catch (ValidationException e) {
      messages.put("remark", e.getMessage());
    }

    if (occurrenceId != null) {
      occurrence = occurrenceFacade.find(occurrenceId);

      if (occurrence == null) {
        messages.put("error", "Unable to find occurrence with ID: " + occurrenceId);
        occurrence = new Occurrence();
      }
    } else {
      occurrence = new Occurrence();
    }

    occurrence.setYearMonthDay(yearMonthDayObj);
    occurrence.setShift(shiftObj);
    occurrence.setTitle(title);
    occurrence.setDescription(description);
    occurrence.setLiaison(liaison);
    occurrence.setDisplay(displayObj);
    occurrence.setRemark(remark);

    return occurrence;
  }
}

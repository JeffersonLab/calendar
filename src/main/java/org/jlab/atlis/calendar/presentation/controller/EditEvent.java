package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.jlab.atlis.calendar.business.session.TaskFacade;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.entity.Task;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.persistence.projection.DayAndShift;
import org.jlab.atlis.calendar.persistence.projection.SelectedOccurrenceFields;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.DisplayConverter;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.OccurrenceStyleChoiceConverter;
import org.jlab.atlis.calendar.presentation.converter.YearMonthDayConverter;
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
    name = "EditEvent",
    urlPatterns = {"/edit-event"})
public class EditEvent extends HttpServlet {

  @EJB private EventFacade eventFacade;
  @EJB private OccurrenceFacade occurrenceFacade;
  @EJB private TaskFacade taskFacade;
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

    String taskIdStr = request.getParameter("taskId");
    String eventIdStr = request.getParameter("eventId");

    BigIntegerConverter bic = new BigIntegerConverter();

    Event event = null;
    Occurrence occurrence = null; // Just holds values for batch update

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

    if (eventIdStr != null && !eventIdStr.trim().isEmpty()) { // Edit existing
      BigInteger eventId = null;

      try {
        eventId = bic.getObject(eventIdStr);
      } catch (ConverterException e) {
        throw new CalendarException("Unable to edit event: Invalid eventId: " + e.getMessage());
      }

      event = eventFacade.findWithOccurrences(eventId);

      if (event == null) {
        throw new CalendarException(
            "Unable to edit event: Unable to locate event with ID = " + eventId);
      }

      calendarId = event.getCalendar().getCalendarId();

      // Load default occurrence values if requested
      String occurrenceIdStr = request.getParameter("occurrenceId");

      if (occurrenceIdStr != null && !occurrenceIdStr.trim().isEmpty()) {
        BigInteger occurrenceId = null;

        try {
          occurrenceId = bic.getObject(occurrenceIdStr);
        } catch (ConverterException e) {
          throw new CalendarException(
              "Unable to edit event: Invalid occurrenceId: " + e.getMessage());
        }

        occurrence = occurrenceFacade.find(occurrenceId);

        if (occurrence == null) {
          throw new CalendarException(
              "Unable to edit event: Unable to locate occurrence with ID = " + occurrenceId);
        }
      }
    } else if (taskIdStr != null && !taskIdStr.trim().isEmpty()) { // Create new from ATLis

      BigInteger taskId = null;

      try {
        taskId = bic.getObject(taskIdStr);
      } catch (ConverterException e) {
        throw new CalendarException("Unable to edit event: Invalid taskId: " + e.getMessage());
      }

      Task task = taskFacade.find(taskId);

      if (task == null) {
        throw new CalendarException(
            "Unable to edit event: Unable to locate ATLis task with ID = " + taskId);
      }

      event = new Event();
      event.setTaskId(taskId);

      occurrence = new Occurrence();
      occurrence.setEvent(event);
      occurrence.setYearMonthDay(task.getScheduledDate());
      occurrence.setTitle(task.getTitle());
      occurrence.setLiaison(task.getLiaison());
      occurrence.setDescription(task.getDescription());
    } else { // Create new (not from ATLis)
      String yearMonthDayStr = request.getParameter("yearMonthDay");

      YearMonthDayConverter ymdConverter = new YearMonthDayConverter();

      Date yearMonthDay = null;

      try {
        yearMonthDay = ymdConverter.getObject(yearMonthDayStr);
      } catch (ConverterException e) {
        throw new CalendarException(
            "Unable to edit event: Invalid yearMonthDay: " + e.getMessage());
      }

      if (yearMonthDay == null) {
        yearMonthDay = TimeHelper.getCurrentYearMonthDay();
      }

      event = new Event();
      occurrence = new Occurrence();
      occurrence.setEvent(event);
      occurrence.setYearMonthDay(yearMonthDay);
    }

    if (event != null && event.getEventId() == null) { // Create new
      if (occurrence.getShift() == null) {
        occurrence.setShift(Shift.DAY); // Set default
      }

      if (occurrence.getDisplay() == null) {
        occurrence.setDisplay(Display.SHOW); // Set default
      }

      /*if (occurrence.getYearMonthDay() != null && occurrence.getShift() != null) {
      List<Occurrence> occurrences = event.getOccurrenceList();

      if (occurrences == null) {
      occurrences = new ArrayList<Occurrence>();
      }

      occurrences.add(occurrence);

      event.setOccurrenceList(occurrences);
      }*/
    }

    List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();

    request.setAttribute("styleChoices", styleChoices);
    request.setAttribute("event", event);
    request.setAttribute("occurrence", occurrence);

    request.setAttribute("calendarId", calendarId);

    Calendar selectedCalendar = calendarFacade.find(calendarId);
    request.setAttribute("selectedCalendar", selectedCalendar);

    List<Calendar> calendarList =
        calendarFacade.findAll(new AbstractFacade.OrderDirective("orderId"));
    request.setAttribute("calendarList", calendarList);

    getServletConfig()
        .getServletContext()
        .getRequestDispatcher("/WEB-INF/views/edit-event.jsp")
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

    Map<String, String> messages = new HashMap<String, String>();
    request.setAttribute("messages", messages);

    Event event = convertAndValidateEvent(request);

    BigInteger taskId = convertAndValidateTaskId(request);

    Occurrence occurrence = null;
    Set<DayAndShift> instances = null;
    SelectedOccurrenceFields selectedFields = null;
    List<BigInteger> selectedOccurrences = null;

    boolean create = (event.getEventId() == null);

    String submitButtonValue = request.getParameter("SubmitButton");

    boolean batchDelete = "Delete Selected".equals(submitButtonValue);
    boolean batchHide = "Hide Selected".equals(submitButtonValue);
    boolean batchShow = "Show Selected".equals(submitButtonValue);

    if (messages.isEmpty()) {
      if (create) {
        occurrence = convertAndValidateOccurrenceWithoutDate(request);
        instances = CalendarServletHelper.convertAndValidateDayAndShifts(request);
      } else if (batchDelete) {
        selectedOccurrences = convertAndValidateSelectedOccurrences(request);
        if (event.getOccurrenceList().size() == selectedOccurrences.size()) {
          messages.put(
              "selectedOccurrences", "Cannot delete all occurrences - delete the event to do that");
        }
      } else if (batchHide) {
        selectedOccurrences = convertAndValidateSelectedOccurrences(request);
      } else if (batchShow) {
        selectedOccurrences = convertAndValidateSelectedOccurrences(request);
      } else { // Edit
        selectedFields = convertAndValidateSelectedFields(request);
        selectedOccurrences = convertAndValidateSelectedOccurrences(request);

        // Verify occurrences actually belong to event
        List<BigInteger> ids = event.getOccurrenceIds();
        if (selectedOccurrences != null) {
          if (ids == null) {
            messages.put("selectedOccurrences", "Event does not have selected occurrence");
          } else {
            for (BigInteger id : selectedOccurrences) {
              if (!ids.contains(id)) {
                messages.put(
                    "selectedOccurrences", "Event does not have selected occurrence: " + id);
              }
            }
          }
        }

        // Verify that selected fields and occurrences (1) both have zero selected or
        // (2) both have at least one selected.
        if (selectedFields.count() == 0 && selectedOccurrences.size() > 0) {
          messages.put("selectedOccurrences", "No fields selected (but occurrences are)");
        }

        if (selectedOccurrences.isEmpty() && selectedFields.count() > 0) {
          messages.put("selectedOccurrences", "No occurrences selected (but fields are)");
        }

        occurrence = convertAndValidateOccurrenceWithSelectedFields(request, selectedFields);
      }
    }

    if (!messages.isEmpty()) {
      String action = create ? "create" : "edit";
      messages.put("error", "Unable to " + action + " event");

      List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();
      request.setAttribute("styleChoices", styleChoices);

      getServletConfig()
          .getServletContext()
          .getRequestDispatcher("/WEB-INF/views/edit-event.jsp")
          .forward(request, response);
      return;
    }

    event.setTaskId(taskId);

    //// BEGIN SET CALENDAR
    Integer calendarId;
    try {
      IntegerConverter intConverter = new IntegerConverter();
      calendarId = intConverter.getObject(request.getParameter("calendar"));

      if (calendarId == null) {
        calendarId = 1;
      }

    } catch (ConverterException e) {
      throw new CalendarException("Unable to convert calendar id: " + e.getMessage());
    }

    Calendar selectedCalendar = calendarFacade.find(calendarId);

    if (selectedCalendar == null) {
      throw new CalendarException("No calendar configured with ID: " + calendarId);
    }

    event.setCalendar(selectedCalendar);
    //// END SET CALENDAR

    if (create) { // Create New
      occurrence.setEvent(event);

      eventFacade.createEventWithOccurrences(occurrence, instances);

    } else if (batchDelete) {
      occurrenceFacade.batchDelete(event.getEventId(), selectedOccurrences);

      returnPath = "edit-event?eventId=" + event.getEventId();

    } else if (batchHide) {
      occurrenceFacade.batchHide(event.getEventId(), selectedOccurrences);

      returnPath = "edit-event?eventId=" + event.getEventId();
    } else if (batchShow) {
      occurrenceFacade.batchShow(event.getEventId(), selectedOccurrences);

      returnPath = "edit-event?eventId=" + event.getEventId();
    } else { // Edit Existing
      eventFacade.edit(event); // Only thing that can change should be TaskId

      occurrenceFacade.batchUpdate(occurrence, selectedOccurrences, selectedFields);
    }

    // calendar might have changed
    returnPath = returnPath.replaceFirst("calendar=[0-9]+", "calendar=" + calendarId);

    response.sendRedirect(returnPath);
  }

  private BigInteger convertAndValidateTaskId(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    String taskIdStr = request.getParameter("taskId");

    BigInteger taskId = null;

    if (taskIdStr != null && !taskIdStr.trim().isEmpty()) {

      BigIntegerConverter bic = new BigIntegerConverter();

      try {
        taskId = bic.getObject(taskIdStr);
      } catch (ConverterException e) {
        messages.put("taskId", e.getMessage());
      }

      if (taskId != null) {
        Task task = taskFacade.find(taskId);

        if (task == null) {
          messages.put("taskId", "ATLis task with specified ID not found");
        }
      }
    }

    return taskId;
  }

  private Event convertAndValidateEvent(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    Event event = new Event();

    String eventIdStr = request.getParameter("eventId");

    BigInteger eventId = null;

    if (eventIdStr != null && !eventIdStr.trim().isEmpty()) {

      BigIntegerConverter bic = new BigIntegerConverter();

      try {
        eventId = bic.getObject(eventIdStr);
      } catch (ConverterException e) {
        messages.put("eventId", e.getMessage());
      }

      if (eventId != null) {
        event = eventFacade.findWithOccurrences(eventId);

        if (event == null) {
          messages.put("eventId", "Event with specified ID not found");
        }
      }
    }

    return event;
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
    String[] styleIdStrArray = request.getParameterValues("style");
    String remark = request.getParameter("remark");

    BigIntegerConverter bic = new BigIntegerConverter();
    TitleValidator titleValidator = new TitleValidator();
    DescriptionValidator descriptionValidator = new DescriptionValidator();
    LiaisonValidator liaisonValidator = new LiaisonValidator();
    DisplayConverter displayConverter = new DisplayConverter();
    DisplayValidator displayValidator = new DisplayValidator();
    OccurrenceStyleChoiceConverter oscc = new OccurrenceStyleChoiceConverter();
    RemarkValidator remarkValidator = new RemarkValidator();

    BigInteger occurrenceId = null;
    Display displayObj = null;
    List<OccurrenceStyleChoice> styles = null;

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
      styles = oscc.getObjects(styleIdStrArray);
    } catch (ConverterException e) {
      messages.put("style", e.getMessage());
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

    occurrenceFacade.updateStyles(styles, occurrence);

    occurrence.setTitle(title);
    occurrence.setDescription(description);
    occurrence.setLiaison(liaison);
    occurrence.setDisplay(displayObj);
    occurrence.setRemark(remark);

    return occurrence;
  }

  private Occurrence convertAndValidateOccurrenceWithSelectedFields(
      HttpServletRequest request, SelectedOccurrenceFields selectedFields) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    Occurrence occurrence = convertAndValidateOccurrenceWithoutDate(request);

    // Remove error messages for fields we don't care about
    if (!selectedFields.isTitle()) {
      messages.remove("title");
    }

    if (!selectedFields.isDescription()) {
      messages.remove("description");
    }

    if (!selectedFields.isLiaison()) {
      messages.remove("liaison");
    }

    if (!selectedFields.isDisplay()) {
      messages.remove("display");
    }

    if (!selectedFields.isStyle()) {
      messages.remove("style");
    }

    if (!selectedFields.isRemark()) {
      messages.remove("remark");
    }

    return occurrence;
  }

  private SelectedOccurrenceFields convertAndValidateSelectedFields(HttpServletRequest request) {
    SelectedOccurrenceFields selected = new SelectedOccurrenceFields();

    String title = request.getParameter("titleForUpdate");
    String description = request.getParameter("descriptionForUpdate");
    String liaison = request.getParameter("liaisonForUpdate");
    String display = request.getParameter("displayForUpdate");
    String style = request.getParameter("styleForUpdate");
    String remark = request.getParameter("remarkForUpdate");

    if (title != null && title.equals("Y")) {
      selected.setTitle(true);
    }

    if (description != null && description.equals("Y")) {
      selected.setDescription(true);
    }

    if (liaison != null && liaison.equals("Y")) {
      selected.setLiaison(true);
    }

    if (display != null && display.equals("Y")) {
      selected.setDisplay(true);
    }

    if (style != null && style.equals("Y")) {
      selected.setStyle(true);
    }

    if (remark != null && remark.equals("Y")) {
      selected.setRemark(true);
    }

    return selected;
  }

  private List<BigInteger> convertAndValidateSelectedOccurrences(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    List<BigInteger> occurrences = new ArrayList<BigInteger>();

    String[] occurrenceIdStrArray = request.getParameterValues("occurrenceForUpdate");

    BigIntegerConverter bic = new BigIntegerConverter();

    if (occurrenceIdStrArray != null) {
      for (String idStr : occurrenceIdStrArray) {
        try {
          BigInteger id = bic.getObject(idStr);
          occurrences.add(id);
        } catch (ConverterException e) {
          messages.put("selectedOccurrences", "Selected Occurrence ID: " + e.getMessage());
        }
      }
    }

    return occurrences;
  }
}

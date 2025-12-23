package org.jlab.atlis.calendar.presentation.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.business.utility.DateRange;
import org.jlab.atlis.calendar.business.utility.ExcelExporter;
import org.jlab.atlis.calendar.business.utility.OccurrenceDayAndShiftComparator;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 * @author ryans
 */
@WebServlet(
    name = "ExportExcel",
    urlPatterns = {"/calendar.xlsx"})
public class ExportExcel extends HttpServlet {

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

    int calendarId = 1;

    DateRange range = null;

    try {
      range = convertValidateDateRange(request);
    } catch (ValidationException e) {
      throw new CalendarException("Unable to export Excel: " + e.getMessage());
    }

    boolean om = request.isUserInRole("calendar-admin");

    List<Occurrence> occurrences = null;

    if (om) {
      occurrences = occurrenceFacade.find(calendarId, range.getStart(), range.getEnd());
    } else {
      occurrences = occurrenceFacade.findExceptHidden(calendarId, range.getStart(), range.getEnd());
    }

    // Ideally the query would just return completely ordered, but JPA doesn't handle arbitrary sort
    // order on shift column well so
    // I just accept the small performance penalty and sort an almost sorted list in the application
    Collections.sort(occurrences, new OccurrenceDayAndShiftComparator());

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("content-disposition", "attachment;filename=\"calendar.xlsx\"");

    ExcelExporter exporter = new ExcelExporter();
    exporter.export(response.getOutputStream(), occurrences, om);
  }

  private DateRange convertValidateDateRange(HttpServletRequest request)
      throws ValidationException {
    String startParam = request.getParameter("start");
    String endParam = request.getParameter("end");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);

    Date start = null;
    Date end = null;

    if (startParam == null || startParam.isEmpty()) {
      throw new ValidationException("Start parameter required");
    } else {
      try {
        start = format.parse(startParam);
      } catch (ParseException e) {
        throw new ValidationException("Start parameter must be in the format yyyy-mm-dd");
      }
    }

    if (endParam == null || endParam.isEmpty()) {
      throw new ValidationException("End parameter required");
    } else {
      try {
        end = format.parse(endParam);
      } catch (ParseException e) {
        throw new ValidationException("End parameter must be in the format yyyy-mm-dd");
      }
    }

    // Checks start before end
    DateRange range = null;
    try {
      range = new DateRange(start, end);
    } catch (IllegalArgumentException e) {
      throw new ValidationException(e.getMessage());
    }

    long difference = TimeHelper.approximateDifferenceInDays(start, end);

    if (difference > 32) {
      throw new ValidationException("Date range must be no more than 32 days");
    }

    return range;
  }
}

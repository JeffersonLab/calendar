package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.TaskFacade;
import org.jlab.atlis.calendar.business.utility.AtlisSearchFilter;
import org.jlab.atlis.calendar.business.utility.Paginator;
import org.jlab.atlis.calendar.persistence.entity.Task;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.YearMonthDayConverter;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;
import org.jlab.atlis.calendar.presentation.validator.YearMonthDayValidator;

/**
 * @author ryans
 */
@WebServlet(
    name = "SearchAtlis",
    urlPatterns = {"/search-atlis"})
public class SearchAtlis extends HttpServlet {

  @EJB private TaskFacade taskFacade;

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
    Map<String, String> messages = new HashMap<String, String>();
    request.setAttribute("messages", messages);

    AtlisSearchFilter filter = convertAndValidateFilter(request);
    Paginator paginator = convertAndValidatePaginator(request);

    if (!messages.isEmpty()) {
      messages.put("error", "Unable to search ATLis due to errors");
      getServletConfig()
          .getServletContext()
          .getRequestDispatcher("/WEB-INF/views/search-atlis.jsp")
          .forward(request, response);
      return;
    }

    int count = taskFacade.count(filter);

    paginator.setCount(count);

    request.setAttribute("paginator", paginator);

    List<Task> tasks = taskFacade.findWithDynamicCriteria(filter, paginator);

    request.setAttribute("tasks", tasks);

    if (tasks.isEmpty()) {
      messages.put("noresults", "No records found.");
    }

    getServletConfig()
        .getServletContext()
        .getRequestDispatcher("/WEB-INF/views/search-atlis.jsp")
        .forward(request, response);
  }

  private AtlisSearchFilter convertAndValidateFilter(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    AtlisSearchFilter filter = new AtlisSearchFilter();

    String start = request.getParameter("start");
    String end = request.getParameter("end");
    String titlePhrase = request.getParameter("titlePhrase");
    String liaisonPhrase = request.getParameter("liaisonPhrase");

    YearMonthDayConverter ymdConverter = new YearMonthDayConverter();
    YearMonthDayValidator ymdValidator = new YearMonthDayValidator();

    Date startDate = null;
    Date endDate = null;

    try {
      startDate = ymdConverter.getObject(start);
    } catch (ConverterException e) {
      messages.put("start", e.getMessage());
    }

    try {
      endDate = ymdConverter.getObject(end);
    } catch (ConverterException e) {
      messages.put("end", e.getMessage());
    }

    if (messages.get("start") == null && messages.get("end") == null) {
      try {
        ymdValidator.validate(startDate, endDate);
      } catch (ValidationException e) {
        messages.put("start", e.getMessage());
        messages.put("end", e.getMessage());
      }
    }

    filter.setStart(startDate);
    filter.setEnd(endDate);
    filter.setTitlePhrase(titlePhrase);
    filter.setLiaisonPhrase(liaisonPhrase);

    return filter;
  }

  private Paginator convertAndValidatePaginator(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    Paginator paginator = new Paginator();

    String startIndexStr = request.getParameter("startIndex");

    IntegerConverter numberConverter = new IntegerConverter();

    Integer startIndex = 0;

    try {
      startIndex = numberConverter.getObject(startIndexStr);
    } catch (ConverterException e) {
      messages.put("startIndex", e.getMessage());
    }

    if (startIndex != null) {
      paginator.setStartIndex(startIndex);
    }

    return paginator;
  }
}

package org.jlab.atlis.calendar.presentation.utility;

import jakarta.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jlab.atlis.calendar.persistence.projection.DayAndShift;
import org.jlab.atlis.calendar.presentation.converter.DayAndShiftConverter;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 * @author ryans
 */
public class CalendarServletHelper {

  public static String determineAndSetReturnPath(HttpServletRequest request) {
    String referrer = request.getParameter("referrer"); // Postback may set this

    // System.out.println("Referrer Param: " + referrer);
    if (referrer == null || referrer.isEmpty()) {
      referrer = request.getHeader("referer");
      // System.out.println("Referrer Header: " + referrer);
    }

    Integer calendarId;
    try {
      IntegerConverter intConverter = new IntegerConverter();
      calendarId = intConverter.getObject(request.getParameter("calendar"));

      if (calendarId == null) {
        calendarId = 1;
      }

    } catch (ConverterException e) {
      calendarId = 1;
    }

    // System.out.println("Referrer: " + referrer);
    String returnPath = "view-outlook?calendar=" + calendarId;

    if (referrer != null) {
      try {
        URL url = new URL(referrer);

        String query = url.getQuery();

        if (query == null) {
          query = "";
        } else {
          query = "?" + query;
        }

        if (referrer.contains("view-outlook")) {
          returnPath = "view-outlook" + query;
        } else if (referrer.contains("view-month")) {
          returnPath = "view-month" + query;
        } else if (referrer.contains("view-occurrence")) {
          returnPath = "view-occurrence" + query;
        } else if (referrer.contains("view-event")) {
          returnPath = "view-event" + query;
        } else if (referrer.contains("view-week")) {
          returnPath = "view-week" + query;
        } else if (referrer.contains("view-day")) {
          returnPath = "view-day" + query;
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
        // Ignore referrer and use default returnPath
      }
    }

    // System.out.println("Return Path: " + returnPath);
    request.setAttribute("returnPath", returnPath);

    return returnPath;
  }

  public static Set<DayAndShift> convertAndValidateDayAndShifts(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

    Set<DayAndShift> instances = new HashSet<DayAndShift>();

    String[] dasParams = request.getParameterValues("date");

    if (dasParams == null || dasParams.length == 0) {
      messages.put("date", "At least one occurrence is required");
    } else {
      DayAndShiftConverter dasConverter = new DayAndShiftConverter();

      for (String param : dasParams) {
        try {
          DayAndShift das = dasConverter.getObject(param);
          boolean unique = instances.add(das);

          if (!unique) {
            messages.put("date", "Duplicate occurrences (" + param + ") are not allowed");
          }
        } catch (ConverterException e) {
          messages.put("date", e.getMessage());
        }
      }
    }

    return instances;
  }
}

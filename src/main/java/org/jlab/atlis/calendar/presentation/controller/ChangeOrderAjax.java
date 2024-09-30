package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.ShiftConverter;
import org.jlab.atlis.calendar.presentation.converter.YearMonthDayConverter;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;
import org.jlab.atlis.calendar.presentation.validator.ShiftValidator;
import org.jlab.atlis.calendar.presentation.validator.YearMonthDayValidator;

/**
 * @author ryans
 */
@WebServlet(
    name = "ChangeOrderAjax",
    urlPatterns = {"/change-order-ajax"})
public class ChangeOrderAjax extends HttpServlet {

  private static final Logger logger = Logger.getLogger(ChangeOrderAjax.class.getName());

  public static final int MAX_OCCURRENCE_PER_SHIFT = 50;

  @EJB private OccurrenceFacade occurrenceFacade;

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

    String errorReason = null;

    try {
      List<Occurrence> occurrences = convertAndValidateOccurrences(request);

      occurrenceFacade.editABunch(occurrences);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Unable to Drag and Drop", e);
      errorReason = "Unable to re-order";
    }

    response.setContentType("text/xml");

    PrintWriter pw = response.getWriter();

    String xml = null;

    if (errorReason == null) {
      xml = "<response><span class=\"status\">Success</span></response>";
    } else {
      xml =
          "<response><span class=\"status\">Error</span><span class=\"reason\">"
              + errorReason
              + "</span></response>";
    }

    pw.write(xml);

    pw.flush();

    boolean error = pw.checkError();

    if (error) {
      logger.log(Level.SEVERE, "PrintWriter Error in ChangeOrderAjax.doPost");
    }
  }

  private List<Occurrence> convertAndValidateOccurrences(HttpServletRequest request)
      throws ConverterException, ValidationException {

    IntegerConverter intConverter = new IntegerConverter();
    Integer calendarId = intConverter.getObject(request.getParameter("calendar"));

    if (calendarId == null) {
      calendarId = 1;
    }

    List<Occurrence> occurrences = new ArrayList<>();

    String yearMonthDayStr = request.getParameter("yearMonthDay");
    String shiftStr = request.getParameter("shift");
    String[] orderStrArray = request.getParameterValues("order");

    logger.log(
        Level.FINEST,
        "convertAndValidateOccurrences (Year Month Day Shift) = ({0} {1})",
        new Object[] {yearMonthDayStr, shiftStr});

    logger.log(Level.FINEST, "Order: ");
    if (orderStrArray != null) {
      for (String s : orderStrArray) {
        logger.log(Level.FINEST, s);
      }
    }

    YearMonthDayConverter ymdConverter = new YearMonthDayConverter();
    ShiftConverter shiftConverter = new ShiftConverter();
    YearMonthDayValidator ymdValidator = new YearMonthDayValidator();
    ShiftValidator shiftValidator = new ShiftValidator();
    BigIntegerConverter bic = new BigIntegerConverter();

    Date yearMonthDay = ymdConverter.getObject(yearMonthDayStr);
    ymdValidator.validate(yearMonthDay);

    Shift shift = shiftConverter.getObject(shiftStr);
    shiftValidator.validate(shift);

    if (orderStrArray == null || orderStrArray.length == 0) {
      throw new ValidationException("Order array must not be empty");
    }

    // ToDo: This doesn't take into account HIDE and MORE occurrences!
    if (orderStrArray.length > MAX_OCCURRENCE_PER_SHIFT) {
      throw new ValidationException(
          "The maximum number of occurrences which can be ordered is "
              + MAX_OCCURRENCE_PER_SHIFT
              + ".  You submitted: "
              + orderStrArray.length);
    }

    BigInteger moveId = null;
    List<Occurrence> senderList = null;

    int i;

    for (i = 0; i < orderStrArray.length; i++) {
      BigInteger id = null;

      try {
        if (orderStrArray[i].length() > 4 && orderStrArray[i].endsWith(":move")) {
          String addIdStr = orderStrArray[i].substring(0, orderStrArray[i].length() - 5);
          id = bic.getObject(addIdStr);
          if (moveId != null) {
            throw new ValidationException("You can only move one occurrence at a time");
          }
          moveId = id;
        } else if (orderStrArray[i].contains("spacer")) {
          continue; // Skip to next iteration
        } else {
          id = bic.getObject(orderStrArray[i]);
        }
      } catch (ConverterException e) {
        throw new ValidationException("Occurrence Id must be a number");
      }

      if (id != null) {
        Occurrence o = occurrenceFacade.find(id);

        if (o == null) {
          throw new ValidationException("Unable to find occurrence with ID: " + id);
        }

        if (o.getOccurrenceId().equals(moveId)) {
          // Note: we don't o.setOrder until AFTER we removeFromSenderList(o) because we rely on the
          // order to resort!
          senderList = removeFromSenderList(calendarId, o);
          o.setYearMonthDay(yearMonthDay);
          o.setShift(shift);
        }

        o.setOrderId(i + 1);

        occurrences.add(o);
      }
    }

    List<Occurrence> allInShift = occurrenceFacade.findShown(calendarId, yearMonthDay, shift);

    int adjustment = moveId == null ? 0 : 1;

    if (allInShift.size() + adjustment != occurrences.size()) {
      throw new ValidationException("Number of occurrences in update request don't match database");
    }

    for (Occurrence o : occurrences) {
      if (!allInShift.contains(o) && !o.getOccurrenceId().equals(moveId)) {
        throw new ValidationException(
            "Occurrence does not belong in selected day and shift: " + o.getOccurrenceId());
      }
    }

    // We must update order of HIDE and MORE occurrences as well
    List<Occurrence> hideAndMore =
        occurrenceFacade.findHiddenAndMore(calendarId, yearMonthDay, shift);

    for (Occurrence o : hideAndMore) {
      o.setOrderId(i++ + 1);
      occurrences.add(o);
    }

    /*System.out.println("Recipient List: ");
    for(Occurrence x: occurrences) {
        System.out.println("ID: " + x.getOccurrenceId() + ", Order: " + x.getOrderId());
    }*/
    if (senderList != null) {
      occurrences.addAll(senderList);
    }

    return occurrences;
  }

  /**
   * Removes the occurrence from the sender list.
   *
   * <p>Note: we assume that the occurrence passed in has NOT been modified yet!
   *
   * @param occurrence
   * @return
   */
  private List<Occurrence> removeFromSenderList(int calendarId, Occurrence occurrence) {
    List<Occurrence> senderList =
        occurrenceFacade.findInOrder(
            calendarId, occurrence.getYearMonthDay(), occurrence.getShift());

    senderList.remove(occurrence);

    for (Occurrence o : senderList) {
      if (o.getOrderId() > occurrence.getOrderId()) {
        o.setOrderId(o.getOrderId() - 1);
      }
    }

    /*System.out.println("Occurrence: " + occurrence.getOccurrenceId() + ", order: " + occurrence.getOrderId());
    System.out.println("Sender List: ");
    for(Occurrence x: senderList) {
        System.out.println("ID: " + x.getOccurrenceId() + ", Order: " + x.getOrderId());
    }*/
    return senderList;
  }
}

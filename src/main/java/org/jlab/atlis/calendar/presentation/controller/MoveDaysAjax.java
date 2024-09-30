package org.jlab.atlis.calendar.presentation.controller;

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

import javax.ejb.EJB;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ryans
 */
@WebServlet(name = "MoveDaysAjax", urlPatterns = {"/move-days"})
public class MoveDaysAjax extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MoveDaysAjax.class.getName());

    @EJB
    private OccurrenceFacade occurrenceFacade;

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

        YearMonthDayConverter ymdConverter = new YearMonthDayConverter();
        YearMonthDayValidator ymdValidator = new YearMonthDayValidator();
        IntegerConverter integerConverter = new IntegerConverter();

        String errorReason = null;

        try {
            Integer fromCalendarId = integerConverter.getObject(request.getParameter("fromCalendar"));
            if (fromCalendarId == null) {
                throw new ValidationException("From Calendar must not be empty");
            }
            Date fromStart = ymdConverter.getObject(request.getParameter("fromStart"));
            ymdValidator.validate(fromStart);
            Integer numberOfDays = integerConverter.getObject(request.getParameter("days"));
            if (numberOfDays == null) {
                throw new ValidationException("Number of days must not be empty");
            }
            if(numberOfDays < 1) {
                throw new ValidationException("Number of days must be greater than zero");
            }
            Integer toCalendarId = integerConverter.getObject(request.getParameter("toCalendar"));
            if (toCalendarId == null) {
                throw new ValidationException("To Calendar must not be empty");
            }
            Date toStart = ymdConverter.getObject(request.getParameter("toStart"));
            ymdValidator.validate(toStart);

            occurrenceFacade.move(fromCalendarId, fromStart, numberOfDays, toCalendarId, toStart);
        }   catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to move", e);
            errorReason = "Unable to move";
        }

        response.setContentType("text/xml");

        PrintWriter pw = response.getWriter();

        String xml = null;

        if (errorReason == null) {
            xml = "<response><span class=\"status\">Success</span></response>";
        } else {
            xml = "<response><span class=\"status\">Error</span><span class=\"reason\">" + errorReason + "</span></response>";
        }

        pw.write(xml);

        pw.flush();

        boolean error = pw.checkError();

        if (error) {
            logger.log(Level.SEVERE, "PrintWriter Error in ChangeOrderAjax.doPost");
        }
    }
}

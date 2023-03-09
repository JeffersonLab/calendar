package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.AbstractFacade.OrderDirective;
import org.jlab.atlis.calendar.business.session.AuditManager;
import org.jlab.atlis.calendar.business.session.CalendarFacade;
import org.jlab.atlis.calendar.business.session.EventCalendarManager;
import org.jlab.atlis.calendar.business.session.OccurrenceStyleChoiceFacade;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.CalendarRevisionInfo;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.projection.EventCalendarDay;
import org.jlab.atlis.calendar.persistence.projection.EventCalendarWeek;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;
import org.jlab.atlis.calendar.presentation.validator.WeekValidator;
import org.jlab.atlis.calendar.presentation.validator.YearValidator;

/**
 *
 * @author ryans
 */
@WebServlet(name = "ViewOutlook", urlPatterns = {"/view-outlook"})
public class ViewOutlook extends HttpServlet {

    @EJB
    private EventCalendarManager calendarManager;
    @EJB
    private OccurrenceStyleChoiceFacade styleChoiceFacade;
    @EJB
    private AuditManager auditManager;
    @EJB
    private CalendarFacade calendarFacade;

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

        Integer calendarId;
        try {
            IntegerConverter intConverter = new IntegerConverter();
            calendarId = intConverter.getObject(request.getParameter("calendar"));
            
            if(calendarId == null) {
                calendarId = 1;
            }
            
        } catch (ConverterException e) {
            throw new CalendarException("Unable to convert calendar id: " + e.getMessage());
        }
        request.setAttribute("calendarId", calendarId);
        
        Calendar selectedCalendar = calendarFacade.find(calendarId);
        request.setAttribute("selectedCalendar", selectedCalendar);        
        
        List<Calendar> calendarList = calendarFacade.findAll(new OrderDirective("orderId"));
        request.setAttribute("calendarList", calendarList);        
        
        int[] yearWeek;

        try {
            yearWeek = convertValidateYearWeek(request);
        } catch (ValidationException e) {
            String message = "Unable to validate year and week: " + e.getMessage();
            throw new CalendarException(message);
        }

        request.setAttribute("startYear", yearWeek[0]);
        request.setAttribute("startWeek", yearWeek[1]);

        List<EventCalendarWeek> weeks = calendarManager.getOutlookWeeks(calendarId, yearWeek[0], yearWeek[1]);

        request.setAttribute("weeks", weeks);

        CalendarRevisionInfo lastRevision = auditManager.getLastRevision();

        request.setAttribute("lastRevision", lastRevision);

        List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();

        request.setAttribute("styleChoices", styleChoices);

        setExportRange(request, weeks);

        request.setAttribute("view", "outlook");
        
        getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/view-outlook.jsp").forward(request, response);
    }

    private void setExportRange(HttpServletRequest request, List<EventCalendarWeek> weeks) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

        Date exportStartDate = weeks.get(0).getDays().get(0).getYearMonthDay();

        List<EventCalendarDay> days = weeks.get(weeks.size() - 1).getDays();
        Date exportEndDate = days.get(days.size() - 1).getYearMonthDay();

        String exportStartStr = formater.format(exportStartDate);
        String exportEndStr = formater.format(exportEndDate);

        request.setAttribute("exportStart", exportStartStr);
        request.setAttribute("exportEnd", exportEndStr);
    }

    private int[] convertValidateYearWeek(HttpServletRequest request) throws ValidationException {
        String yearParam = request.getParameter("year");
        String weekParam = request.getParameter("week");

        YearValidator yv = new YearValidator();
        WeekValidator wv = new WeekValidator();

        int[] yearWeek = new int[2];

        if (yearParam == null || yearParam.trim().isEmpty()) {
            yearWeek[0] = TimeHelper.getCurrentYear();
        } else {
            try {
                yearWeek[0] = Integer.parseInt(yearParam);
                yv.validate(yearWeek[0]);
            } catch (NumberFormatException e) {
                throw new ValidationException("Year must be a number");
            }
        }

        if (weekParam == null || weekParam.trim().isEmpty()) {
            yearWeek[1] = TimeHelper.getCurrentWeek();
        } else {
            try {
                yearWeek[1] = Integer.parseInt(weekParam);
                wv.validate(yearWeek[1]);
            } catch (NumberFormatException e) {
                throw new ValidationException("Week must be a number");
            }
        }

        return yearWeek;
    }
}

package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.AbstractFacade;
import org.jlab.atlis.calendar.business.session.CalendarFacade;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.projection.EventCalendarDay;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

@WebServlet(name = "ViewDay", urlPatterns = {"/view-day"})
public class ViewDay extends HttpServlet {
    @EJB
    private OccurrenceFacade occurrenceFacade;
    @EJB
    private CalendarFacade calendarFacade;
    
    /** 
     * Handles the HTTP <code>GET</code> method.
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
        
        List<Calendar> calendarList = calendarFacade.findAll(new AbstractFacade.OrderDirective("orderId"));
        request.setAttribute("calendarList", calendarList);        
        
        Date dayMonthYear = null;
        
        try {
            dayMonthYear = convertValidateDayMonthYear(request);
        }
        catch(ValidationException e) {
            throw new CalendarException("Unable to view day: " + e.getMessage());
        }
        
        List<Occurrence> occurrences = occurrenceFacade.findDayAsRole(calendarId, dayMonthYear);
        
        if(occurrences != null) {
            for(Occurrence o: occurrences) {
                Collections.sort(o.getStyles());
            }
        }
        
        EventCalendarDay day = new EventCalendarDay();
        
        day.setYearMonthDay(dayMonthYear);
        day.addOccurrences(occurrences);
        
        request.setAttribute("day", day);
        
        request.setAttribute("view", "day");        
        
        getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/view-day.jsp").forward(request, response);
    }
    
    private Date convertValidateDayMonthYear(HttpServletRequest request) throws ValidationException {
        String yearMonthDayParam = request.getParameter("yearMonthDay");
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        
        Date yearMonthDay = null;
        
        if(yearMonthDayParam == null || yearMonthDayParam.isEmpty()) {
            // Default is current day month and year
            yearMonthDay = TimeHelper.getCurrentYearMonthDay();    
        }
        else {
            try {
                yearMonthDay = format.parse(yearMonthDayParam);
            }
            catch(ParseException e) {
                throw new ValidationException("Day must be in the format yyyy-mm-dd");
            }
        }
        
        return yearMonthDay;
    }    
}

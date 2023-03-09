package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.AbstractFacade;
import org.jlab.atlis.calendar.business.session.AuditManager;
import org.jlab.atlis.calendar.business.session.CalendarFacade;
import org.jlab.atlis.calendar.business.session.EventCalendarManager;
import org.jlab.atlis.calendar.business.session.OccurrenceStyleChoiceFacade;
import org.jlab.atlis.calendar.business.utility.DateRange;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Calendar;
import org.jlab.atlis.calendar.persistence.entity.CalendarRevisionInfo;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.projection.EventCalendar;
import org.jlab.atlis.calendar.presentation.converter.IntegerConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 *
 * @author ryans
 */
@WebServlet(name = "ViewMonth", urlPatterns = {"/view-month"})
public class ViewMonth extends HttpServlet {
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
        
        Date yearMonth = null;
        
        try {
            yearMonth = convertValidateYearMonth(request);
        }
        catch(ValidationException e) {
            throw new CalendarException("Unable to validate year and month: " + e.getMessage());            
        }
        
        CalendarRevisionInfo lastRevision = auditManager.getLastRevision();
        
        request.setAttribute("lastRevision", lastRevision);
        
        EventCalendar calendar = calendarManager.getEventCalendar(calendarId, yearMonth);
        
        request.setAttribute("calendar", calendar);
        
        List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();
        
        request.setAttribute("styleChoices", styleChoices);               
        
        setExportRange(request, yearMonth);
        
        request.setAttribute("view", "month");        
        
        getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/view-month.jsp").forward(request, response);
    }
    
    private void setExportRange(HttpServletRequest request, Date yearMonth) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        
        DateRange range = TimeHelper.getMonthRange(yearMonth);
        
        Date exportStartDate = range.getStart();
        Date exportEndDate = range.getEnd();
        
        String exportStartStr = formater.format(exportStartDate);
        String exportEndStr = formater.format(exportEndDate);
        
        request.setAttribute("exportStart", exportStartStr);
        request.setAttribute("exportEnd", exportEndStr);        
    }    
    
    private Date convertValidateYearMonth(HttpServletRequest request) throws ValidationException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        format.setLenient(false);
        
        String yearMonthParam = request.getParameter("yearMonth");
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        
        if(yearMonthParam == null || yearMonthParam.isEmpty()) {
            if(yearParam != null && !yearParam.isEmpty() && monthParam != null && !monthParam.isEmpty()) {
                yearMonthParam = yearParam + "-" + monthParam;
            }
        }        
        
        Date yearMonth = null;
        
        if(yearMonthParam == null || yearMonthParam.isEmpty()) {
            // Default is current year and month
            yearMonth = TimeHelper.getCurrentYearMonth();             
        }
        else {
            try {
                yearMonth = format.parse(yearMonthParam);
            }
            catch(ParseException e) {
                throw new ValidationException("Year and Month must be in the format yyyy-mm");
            }
        }
        
        return yearMonth;
    }
}

package org.jlab.atlis.calendar.business.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jlab.atlis.calendar.business.utility.DateIterator;
import org.jlab.atlis.calendar.business.utility.DateRange;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.projection.EventCalendar;
import org.jlab.atlis.calendar.persistence.projection.EventCalendarDay;
import org.jlab.atlis.calendar.persistence.projection.EventCalendarWeek;

/**
 *
 * @author ryans
 */
@Stateless
public class EventCalendarManager {
    private static final Logger LOGGER = Logger.getLogger(EventCalendarManager.class.getName());
    @EJB
    private OccurrenceFacade occurrenceFacade;
    
    public EventCalendar getEventCalendar(int calendarId, Date yearMonth) {
        EventCalendar calendar = new EventCalendar();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        LOGGER.log(Level.FINEST, "EventCalendarManager.getEventCalendar: {0}", format.format(yearMonth));
        
        List<EventCalendarWeek> weeks = getWeeksInMonth(calendarId, yearMonth);
        
        calendar.setYearMonth(yearMonth);
        calendar.setWeeks(weeks);
        
        return calendar;
    }
    
    private List<EventCalendarDay> getPaddedDays(List<EventCalendarDay> days) {
        int firstDayOfWeekInMonth = TimeHelper.getDayOfWeek(days.get(0).getYearMonthDay());
        int lastDayOfWeekInMonth = TimeHelper.getDayOfWeek(days.get(days.size() - 1).getYearMonthDay());
        
        LOGGER.log(Level.FINEST, "EventCalendarManager.getPaddedDays: first day of week in month={0}, last day of week in month={1}", new Object[]{firstDayOfWeekInMonth, lastDayOfWeekInMonth});
        
        List<EventCalendarDay> beginPadding = new ArrayList<>();
        for(int i = 1; i < firstDayOfWeekInMonth; i++) {
            beginPadding.add(new EventCalendarDay());
        }
        
        List<EventCalendarDay> endPadding = new ArrayList<>();
        for(int i = 1; i <= 7 - lastDayOfWeekInMonth; i++) {
            endPadding.add(new EventCalendarDay());
        }
        
        List<EventCalendarDay> all = new ArrayList<>();
        
        all.addAll(beginPadding);
        all.addAll(days);
        all.addAll(endPadding);
        
        LOGGER.log(Level.FINEST, "EventCalendarManager.getPaddedDays: begin padding days={0}, end padding days={1}, total days={2}", new Object[]{beginPadding.size(), endPadding.size(), all.size()});
        
        return all;
    }
    
    public EventCalendarWeek getWeek(int calendarId, int year, int week) {
        EventCalendarWeek ecw = new EventCalendarWeek();
        
        DateRange range = TimeHelper.getWeekRange(year, week);
        List<EventCalendarDay> days = getDays(calendarId, range.getStart(), range.getEnd());
        
        ecw.setYear(year);
        ecw.setWeek(week);
        ecw.setDays(days);
        
        return ecw;
    }
    
    public List<EventCalendarWeek> getOutlookWeeks(int calendarId, int startYear, int startWeek) {
        LOGGER.log(Level.FINEST, "EventCalendarManager.getOutlookWeeks");
        
        List<EventCalendarDay> days = getOutlookDays(calendarId, startYear, startWeek);
        
        return putDaysIntoWeeks(days);
    }
    
    private List<EventCalendarWeek> putDaysIntoWeeks(List<EventCalendarDay> days) {
        List<EventCalendarWeek> weeks = new ArrayList<>();
        
        Iterator<EventCalendarDay> dayIterator = days.iterator();
        
        int numberOfWeeks = days.size() / 7;
        
        for(int i = 1; i <= numberOfWeeks; i++) {
            EventCalendarWeek week = new EventCalendarWeek();
            
            for(int j = 0; j < 7; j++) {
                week.addDay(dayIterator.next());
            }
            
            //Date firstDayOfWeek = week.getDays().get(0).getYearMonthDay();
            Date lastDayOfWeek = week.getDays().get(6).getYearMonthDay(); // Use last day of week to capture week 1 which may overlap into previous year!
            
            week.setWeek(TimeHelper.getWeekOfYear(lastDayOfWeek));
            week.setYear(TimeHelper.getYear(lastDayOfWeek));
            
            weeks.add(week);
        }
        
        return weeks;        
    }
    
    private List<EventCalendarWeek> getWeeksInMonth(int calendarId, Date monthYear) {
        
        LOGGER.log(Level.FINEST, "EventCalendarManager.getWeeksInMonth");
        
        List<EventCalendarDay> days = getMonthDays(calendarId, monthYear);
        
        return putDaysIntoWeeks(days);
    }
    
    private List<EventCalendarDay> getOutlookDays(int calendarId, int startYear, int startWeek) {
        DateRange range = TimeHelper.getOutlook(startYear, startWeek);
        
        return getDays(calendarId, range.getStart(), range.getEnd());
    }
    
    private List<EventCalendarDay> getMonthDays(int calendarId, Date yearMonth) {
        DateRange range = TimeHelper.getMonthRangeWithFullWeeks(yearMonth);
        
        return getDays(calendarId, range.getStart(), range.getEnd());
    }
    
    private List<EventCalendarDay> getDays(int calendarId, Date start, Date end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LOGGER.log(Level.FINEST, "EventCalendarManager.getDays: {0} - {1}", new Object[]{format.format(start), format.format(end)});
        
        List<Occurrence> occurrences = occurrenceFacade.find(calendarId, start, end);
        
        if(occurrences == null) {
            occurrences = new ArrayList<>(); // JPA may return null?
        }
        
        LOGGER.log(Level.FINEST, "Found {0} occurrences", occurrences.size());
        SortedMap<Date, EventCalendarDay> days = createCalendarDays(start, end);
        
        populateDays(days, occurrences);               
        
        return new ArrayList<>(days.values());        
    }
    
    private SortedMap<Date, EventCalendarDay> createCalendarDays(Date start, Date end) {
        SortedMap<Date, EventCalendarDay> days = new TreeMap<>();              
        
        DateIterator iterator = new DateIterator(start, end);
       
        while(iterator.hasNext()) {
            Date yearMonthDay = iterator.next();
            EventCalendarDay day = new EventCalendarDay();
            day.setYearMonthDay(yearMonthDay);
            days.put(yearMonthDay, day);                        
        }          
        
        return days;
    }    
    
    private void populateDays(SortedMap<Date, EventCalendarDay> days, List<Occurrence> occurrences) {
        
        LOGGER.log(Level.FINEST, "EventCalendarManager.populateDays");
        
        for(Occurrence occurrence: occurrences) {
            EventCalendarDay day = days.get(occurrence.getYearMonthDay());
            if(day != null) {
                day.addOccurrence(occurrence);
            }
            else {
                LOGGER.log(Level.WARNING, "Database occurrence: {0} not found in current calendar!", occurrence.getYearMonthDay());
            }
        }
    }
}

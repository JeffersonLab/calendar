package org.jlab.atlis.calendar.persistence.projection;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ryans
 */
public class EventCalendar {
    private Date yearMonth;
    private List<EventCalendarWeek> weeks;
    
    public Date getYearMonth() {
        return yearMonth;
    }
    
    public void setYearMonth(Date yearMonth) {
        this.yearMonth = yearMonth;
    }    
    
    public List<EventCalendarWeek> getWeeks() {
        return weeks;
    }
    
    public void setWeeks(List<EventCalendarWeek> weeks) {
        this.weeks = weeks;
    }
}

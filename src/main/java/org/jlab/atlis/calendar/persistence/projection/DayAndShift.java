package org.jlab.atlis.calendar.persistence.projection;

import java.util.Date;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;

/**
 *
 * @author ryans
 */
public class DayAndShift {
    private Date day;
    private Shift shift;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DayAndShift other = (DayAndShift) obj;
        if (this.day != other.day && (this.day == null || !this.day.equals(other.day))) {
            return false;
        }
        if (this.shift != other.shift) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = day.hashCode() + shift.hashCode();
        return hash;
    }
    
    
}

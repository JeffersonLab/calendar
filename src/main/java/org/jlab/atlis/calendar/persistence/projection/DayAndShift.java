package org.jlab.atlis.calendar.persistence.projection;

import java.util.Date;
import java.util.Objects;

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
        if (!Objects.equals(this.day, other.day)) {
            return false;
        }
        return this.shift == other.shift;
    }

    @Override
    public int hashCode() {
        int hash = day.hashCode() + shift.hashCode();
        return hash;
    }
    
    
}

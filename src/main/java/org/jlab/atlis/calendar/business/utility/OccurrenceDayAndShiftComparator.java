package org.jlab.atlis.calendar.business.utility;

import java.util.Comparator;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;

/**
 *
 * @author ryans
 */
public class OccurrenceDayAndShiftComparator implements Comparator<Occurrence> {

    @Override
    public int compare(Occurrence o1, Occurrence o2) {
        int result = o1.getYearMonthDay().compareTo(o2.getYearMonthDay());
        
        if(result == 0) {
            result = o1.getShift().compareTo(o2.getShift());
        }
        
        return result;
    }
    
}

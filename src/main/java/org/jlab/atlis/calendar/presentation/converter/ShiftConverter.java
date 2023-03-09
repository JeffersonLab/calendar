package org.jlab.atlis.calendar.presentation.converter;

import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 *
 * @author ryans
 */
public class ShiftConverter {
    public Shift getObject(String shiftStr) throws ConverterException {        
        Shift shiftObj = null;
        
        if(shiftStr != null && !shiftStr.trim().isEmpty()) {
            try {
                shiftObj = Shift.valueOf(shiftStr);
            }
            catch(IllegalArgumentException e) {
                throw new ConverterException("Shift must be one of OWL, DAY, SWING");
            }
        }
        
        return shiftObj;
    }
    
    public String getString(Shift shiftObj) {
        return shiftObj.name();
    }
}

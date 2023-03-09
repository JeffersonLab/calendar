package org.jlab.atlis.calendar.presentation.converter;

import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 *
 * @author ryans
 */
public class DisplayConverter {
    public Display getObject(String displayStr) throws ConverterException {        
        Display displayObj = null;
        
        if(displayStr != null && !displayStr.trim().isEmpty()) {
            try {
                displayObj = Display.valueOf(displayStr);
            }
            catch(IllegalArgumentException e) {
                throw new ConverterException("Display must be one of SHOW, MORE, HIDE");
            }
        }
        
        return displayObj;
    }
    
    public String getString(Display displayObj) {
        return displayObj.name();
    }
}

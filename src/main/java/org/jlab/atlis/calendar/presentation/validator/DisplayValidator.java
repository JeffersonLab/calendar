package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 *
 * @author ryans
 */
public class DisplayValidator {
    public void validate(Display display) throws ValidationException {
        if(display == null) {
            throw new ValidationException("Display is required");
        }
    }
}

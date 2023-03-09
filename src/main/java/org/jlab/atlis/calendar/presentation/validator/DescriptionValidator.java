package org.jlab.atlis.calendar.presentation.validator;

import org.jlab.atlis.calendar.presentation.exception.ValidationException;

/**
 *
 * @author ryans
 */
public class DescriptionValidator {
    public void validate(String description) throws ValidationException {
        if(description != null && description.length() > 512) {
            throw new ValidationException("Description cannot be more than 512 characters");
        }        
    }
}

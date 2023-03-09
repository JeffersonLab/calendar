package org.jlab.atlis.calendar.presentation.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jlab.atlis.calendar.business.session.OccurrenceStyleChoiceFacade;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;

/**
 *
 * @author ryans
 */
public class OccurrenceStyleChoiceConverter {
    private final IntegerConverter ic = new IntegerConverter();
    private OccurrenceStyleChoiceFacade oscFacade = null;
    
    public OccurrenceStyleChoiceConverter() {
        try {
            Context jndiContext = new InitialContext();
            oscFacade = (OccurrenceStyleChoiceFacade)jndiContext.lookup("java:global/calendar/OccurrenceStyleChoiceFacade");  
        }
        catch(NamingException e) {
            throw new RuntimeException("Unable to inject OccurrenceStyleChoiceFacade into OccurrenceStyleChoiceConverter", e);
        }
    }
    
    public List<OccurrenceStyleChoice> getObjects(String[] iStrs) throws ConverterException {
        List<OccurrenceStyleChoice> choices = new ArrayList<OccurrenceStyleChoice>();
        
        if(iStrs != null) {
            for(String str: iStrs) {
                choices.add(getObject(str));
            }
        }
        
        return choices;
    }
    
    public OccurrenceStyleChoice getObject(String iStr) throws ConverterException {
        OccurrenceStyleChoice osc = null;

        Integer id = ic.getObject(iStr);

        osc = oscFacade.find(BigInteger.valueOf(id));

        if(osc == null) {
            throw new ConverterException("Unable to find OccurrenceStyleChoice with ID: " + id);
        }
        
        return osc;
    }

    public String getString(OccurrenceStyleChoice osc) {
        return osc.getOccurrenceStyleChoiceId().toString();
    } 
}

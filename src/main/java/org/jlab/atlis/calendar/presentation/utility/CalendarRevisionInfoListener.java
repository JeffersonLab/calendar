package org.jlab.atlis.calendar.presentation.utility;

import org.hibernate.envers.RevisionListener;
import org.jlab.atlis.calendar.presentation.filter.AuditContext;
import org.jlab.atlis.calendar.persistence.entity.CalendarRevisionInfo;

/**
 *
 * @author ryans
 */
public class CalendarRevisionInfoListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        CalendarRevisionInfo revisionInfo = (CalendarRevisionInfo)o;
        
        AuditContext context = AuditContext.getCurrentInstance();
        
        String ip = context.getIp();
        String username = context.getUsername();
        
        revisionInfo.setAddress(ip);
        revisionInfo.setUsername(username);
    }
    
}

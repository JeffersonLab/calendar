package org.jlab.atlis.calendar.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CalendarRevisionInfo.class)
public class CalendarRevisionInfo_ {
    public static volatile SingularAttribute<CalendarRevisionInfo, Integer> id;   
    public static volatile SingularAttribute<CalendarRevisionInfo, Long> ts;  
    public static volatile SingularAttribute<CalendarRevisionInfo, String> username;    
    public static volatile SingularAttribute<CalendarRevisionInfo, String> address;
}

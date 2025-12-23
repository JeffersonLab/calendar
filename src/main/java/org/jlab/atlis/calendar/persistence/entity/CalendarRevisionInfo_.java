package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CalendarRevisionInfo.class)
public class CalendarRevisionInfo_ {
  public static volatile SingularAttribute<CalendarRevisionInfo, Integer> id;
  public static volatile SingularAttribute<CalendarRevisionInfo, Long> ts;
  public static volatile SingularAttribute<CalendarRevisionInfo, String> username;
  public static volatile SingularAttribute<CalendarRevisionInfo, String> address;
}

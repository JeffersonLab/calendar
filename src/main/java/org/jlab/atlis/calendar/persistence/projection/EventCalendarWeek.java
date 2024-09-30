package org.jlab.atlis.calendar.persistence.projection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryans
 */
public class EventCalendarWeek {
  private int year;
  private int week;
  private List<EventCalendarDay> days = new ArrayList<EventCalendarDay>();

  public int getWeek() {
    return week;
  }

  public void setWeek(int week) {
    this.week = week;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void addDay(EventCalendarDay day) {
    days.add(day);
  }

  public List<EventCalendarDay> getDays() {
    return days;
  }

  public void setDays(List<EventCalendarDay> days) {
    this.days = days;
  }
}

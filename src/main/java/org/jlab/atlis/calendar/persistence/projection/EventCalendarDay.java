package org.jlab.atlis.calendar.persistence.projection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;

/**
 * @author ryans
 */
public class EventCalendarDay {
  private Date yearMonthDay;
  private final List<Occurrence> allOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> owlShowOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> owlMoreOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> owlHideOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> dayShowOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> dayMoreOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> dayHideOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> swingShowOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> swingMoreOccurrences = new ArrayList<Occurrence>();
  private final List<Occurrence> swingHideOccurrences = new ArrayList<Occurrence>();

  public Date getYearMonthDay() {
    return yearMonthDay;
  }

  public void setYearMonthDay(Date yearMonthDay) {
    this.yearMonthDay = yearMonthDay;
  }

  public void addOccurrences(List<Occurrence> occurrences) {
    for (Occurrence o : occurrences) {
      addOccurrence(o);
    }
  }

  public void addOccurrence(Occurrence occurrence) {

    allOccurrences.add(occurrence);

    switch (occurrence.getShift()) {
      case OWL:
        switch (occurrence.getDisplay()) {
          case SHOW:
            owlShowOccurrences.add(occurrence);
            break;
          case MORE:
            owlMoreOccurrences.add(occurrence);
            break;
          case HIDE:
            owlHideOccurrences.add(occurrence);
            break;
        }
        break;
      case DAY:
        switch (occurrence.getDisplay()) {
          case SHOW:
            dayShowOccurrences.add(occurrence);
            break;
          case MORE:
            dayMoreOccurrences.add(occurrence);
            break;
          case HIDE:
            dayHideOccurrences.add(occurrence);
            break;
        }
        break;
      case SWING:
        switch (occurrence.getDisplay()) {
          case SHOW:
            swingShowOccurrences.add(occurrence);
            break;
          case MORE:
            swingMoreOccurrences.add(occurrence);
            break;
          case HIDE:
            swingHideOccurrences.add(occurrence);
            break;
        }
        break;
    }
  }

  public List<Occurrence> getAllOccurrences() {
    return allOccurrences;
  }

  public List<Occurrence> getOwlShowOccurrences() {
    return owlShowOccurrences;
  }

  public List<Occurrence> getOwlMoreOccurrences() {
    return owlMoreOccurrences;
  }

  public List<Occurrence> getOwlHideOccurrences() {
    return owlHideOccurrences;
  }

  public List<Occurrence> getDayShowOccurrences() {
    return dayShowOccurrences;
  }

  public List<Occurrence> getDayMoreOccurrences() {
    return dayMoreOccurrences;
  }

  public List<Occurrence> getDayHideOccurrences() {
    return dayHideOccurrences;
  }

  public List<Occurrence> getSwingShowOccurrences() {
    return swingShowOccurrences;
  }

  public List<Occurrence> getSwingMoreOccurrences() {
    return swingMoreOccurrences;
  }

  public List<Occurrence> getSwingHideOccurrences() {
    return swingHideOccurrences;
  }

  public boolean isMore() {
    return owlMoreOccurrences.size() > 0
        || dayMoreOccurrences.size() > 0
        || swingMoreOccurrences.size() > 0;
  }

  public List<Occurrence> getShowOccurrences(Shift shift) {
    List<Occurrence> result = null;

    switch (shift) {
      case OWL:
        result = getOwlShowOccurrences();
        break;
      case DAY:
        result = getDayShowOccurrences();
        break;
      case SWING:
        result = getSwingShowOccurrences();
        break;
    }

    return result;
  }

  public List<Occurrence> getOccurrences(Shift shift, Display display, boolean sort, boolean pad) {
    List<Occurrence> filteredList = new ArrayList<Occurrence>();

    if (allOccurrences != null) {
      for (Occurrence o : allOccurrences) {
        if (shift == null || shift.equals(o.getShift())) {
          if (display == null || display.equals(o.getDisplay())) {
            filteredList.add(o);
          }
        }
      }
    }

    if (sort || pad) {
      Collections.sort(filteredList);
    }

    if (pad && shift != null && filteredList.size() > 0) {
      int max = filteredList.get(filteredList.size() - 1).getOrderId();

      int numPadding = max - filteredList.size();

      if (numPadding <= 20) { // Max padding threshold
        Map<Integer, Occurrence> lookup = createOccurrenceMap(filteredList);

        for (int i = 1; i <= max; i++) {
          if (lookup.get(i) == null) {
            Occurrence padding = new Occurrence();
            padding.setYearMonthDay(yearMonthDay);
            padding.setShift(shift);
            padding.setOrderId(i);
            filteredList.add(i - 1, padding);
          }
        }
      }
    }

    return filteredList;
  }

  private Map<Integer, Occurrence> createOccurrenceMap(List<Occurrence> occurrences) {
    Map<Integer, Occurrence> lookup = new HashMap<Integer, Occurrence>();

    for (Occurrence o : occurrences) {
      lookup.put(o.getOrderId(), o);
    }

    return lookup;
  }
}

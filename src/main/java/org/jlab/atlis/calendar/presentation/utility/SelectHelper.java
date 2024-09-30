package org.jlab.atlis.calendar.presentation.utility;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyle;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;

public class SelectHelper {
  public static final String[] months = {
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  };

  public List<Shift> getShifts() {
    return Arrays.asList(Shift.values());
  }

  public List<String> getMonths() {
    return Arrays.asList(months);
  }

  public List<Display> getDisplays() {
    return Arrays.asList(Display.values());
  }

  public boolean containsIdInArray(String[] ids, Integer x) {
    boolean found = false;

    if (ids != null && x != null) {
      for (int i = 0; i < ids.length; i++) {
        if (x.toString().equals(ids[i])) {
          found = true;
          break;
        }
      }
    }

    return found;
  }

  public boolean containsStyleInArray(String[] styles, Integer x) {
    boolean found = false;

    if (styles != null && x != null) {
      for (int i = 0; i < styles.length; i++) {
        if (x.toString().equals(styles[i])) {
          found = true;
          break;
        }
      }
    }

    return found;
  }

  public boolean containsStyleInList(List<OccurrenceStyle> styles, Integer x) {
    boolean found = false;

    if (styles != null && x != null) {
      for (OccurrenceStyle os : styles) {
        if (BigInteger.valueOf(x)
            .equals(os.getOccurrenceStyleChoice().getOccurrenceStyleChoiceId())) {
          found = true;
          break;
        }
      }
    }

    return found;
  }
}

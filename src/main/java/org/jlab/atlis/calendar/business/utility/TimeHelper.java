/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.atlis.calendar.business.utility;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ryans
 */
public class TimeHelper {
  public static Date getYearMonthDay(int year, int month, int day) {
    Calendar c = Calendar.getInstance();
    month = month - 1; // We want to allow 1-12, but Calendar uses 0-11
    c.set(year, month, day, 0, 0, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }

  public static Date getYearMonth(int year, int month) {
    Calendar c = Calendar.getInstance();
    month = month - 1; // We want to allow 1-12, but Calendar uses 0-11
    c.set(year, month, 1, 0, 0, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }

  public static Date getYearWeek(int year, int week) {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);

    return c.getTime();
  }

  public static int[] getYearMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    int[] values = new int[2];

    values[0] = c.get(Calendar.YEAR);
    values[1] = c.get(Calendar.MONTH);

    values[1] = values[1] + 1; // We want to allow 1-12, but Calendar uses 0-11

    return values;
  }

  public static int[] getYearMonthDay(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);

    int[] values = new int[3];

    values[0] = c.get(Calendar.YEAR);
    values[1] = c.get(Calendar.MONTH);
    values[2] = c.get(Calendar.DAY_OF_MONTH);

    values[1] = values[1] + 1; // We want to allow 1-12, but Calendar ues 0-11

    return values;
  }

  public static Date getCurrentYearMonthDay() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return c.getTime();
  }

  public static Date getCurrentYearMonth() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return c.getTime();
  }

  public static Date add(Date date, int amount, int field) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(field, amount);
    return c.getTime();
  }

  public static Date subtract(Date date, int amount, int field) {
    return TimeHelper.add(date, -amount, field);
  }

  public static int getDayOfWeek(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);
    return c.get(Calendar.DAY_OF_WEEK);
  }

  public static int getWeekOfYear(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);
    return c.get(Calendar.WEEK_OF_YEAR);
  }

  public static int getYear(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);
    return c.get(Calendar.YEAR);
  }

  public static int getDaysInMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);
    return c.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public static Date getFirstMillisecondOfMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return c.getTime();
  }

  public static Date getFirstMillisecondOfWeek(int year, int week) {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);

    return c.getTime();
  }

  public static Date getLastMillisecondOfMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59); // Should we worry about leap seconds?
    c.set(Calendar.MILLISECOND, 999);

    return c.getTime();
  }

  public static Date getLastMillisecondOfWeek(int year, int week) {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);
    c.set(Calendar.DAY_OF_WEEK, 7);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);

    return c.getTime();
  }

  public static Date getFirstDayOfMonth(Date yearMonth) {
    return getFirstMillisecondOfMonth(yearMonth);
  }

  public static Date getLastDayOfMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return c.getTime();
  }

  public static Date getFirstDayOfWeek(int year, int week) {
    return getFirstMillisecondOfWeek(year, week);
  }

  public static Date getLastDayOfWeek(int year, int week) {
    Calendar c = Calendar.getInstance();

    c.clear();

    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);
    c.set(Calendar.DAY_OF_WEEK, 7);

    return c.getTime();
  }

  public static Date getNextMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.add(Calendar.MONTH, 1);

    return c.getTime();
  }

  public static int[] getNextMonthParts(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.add(Calendar.MONTH, 1);

    int[] yearMonthArray = new int[2];

    yearMonthArray[0] = c.get(Calendar.YEAR);
    yearMonthArray[1] = c.get(Calendar.MONTH) + 1; // java.util.Calendar.MONTH starts at zero

    return yearMonthArray;
  }

  public static Date getPreviousMonth(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.add(Calendar.MONTH, -1);

    return c.getTime();
  }

  public static int[] getPreviousMonthParts(Date yearMonth) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonth);

    c.add(Calendar.MONTH, -1);

    int[] yearMonthArray = new int[2];

    yearMonthArray[0] = c.get(Calendar.YEAR);
    yearMonthArray[1] = c.get(Calendar.MONTH) + 1; // java.util.Calendar.MONTH starts at zero

    return yearMonthArray;
  }

  public static Date getNextDay(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);

    c.add(Calendar.DAY_OF_MONTH, 1);

    return c.getTime();
  }

  public static Date getPreviousDay(Date yearMonthDay) {
    Calendar c = Calendar.getInstance();
    c.setTime(yearMonthDay);

    c.add(Calendar.DAY_OF_MONTH, -1);

    return c.getTime();
  }

  public static int[] getYearWeek(Date yearMonthDay) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(yearMonthDay);
    cal.set(Calendar.DAY_OF_WEEK, 7);

    int[] yearWeek = new int[2];

    yearWeek[0] = cal.get(Calendar.YEAR);
    yearWeek[1] = cal.get(Calendar.WEEK_OF_YEAR);

    return yearWeek;
  }

  public static int[] getNextWeek(int year, int week) {
    // Assuming default Locale.US and TimeZone "America/New_York"
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);

    // Must set to last day of week; initially set to first day due to API
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // SUNDAY
    c.add(Calendar.DAY_OF_WEEK, 6); // Must roll forward not backwards

    c.add(Calendar.WEEK_OF_YEAR, 1);

    int[] yearWeek = new int[2];

    yearWeek[0] = c.get(Calendar.YEAR);
    yearWeek[1] = c.get(Calendar.WEEK_OF_YEAR);

    return yearWeek;
  }

  public static int[] getPreviousWeek(int year, int week) {
    // Assuming default Locale.US and TimeZone "America/New_York"
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.WEEK_OF_YEAR, week);

    // Must set to last day of week; initially set to first day due to API
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // SUNDAY
    c.add(Calendar.DAY_OF_WEEK, 6); // Must roll forward not backwards

    c.add(Calendar.WEEK_OF_YEAR, -1);

    int[] yearWeek = new int[2];

    yearWeek[0] = c.get(Calendar.YEAR);
    yearWeek[1] = c.get(Calendar.WEEK_OF_YEAR);

    return yearWeek;
  }

  public static DateRange getWeekRange(int year, int week) {
    DateRange range;
    Date start = null;
    Date end = null;

    start = TimeHelper.getFirstDayOfWeek(year, week);
    end = TimeHelper.getLastDayOfWeek(year, week);

    range = new DateRange(start, end);

    return range;
  }

  public static DateRange getMonthRange(Date yearMonth) {
    Date start = TimeHelper.getFirstMillisecondOfMonth(yearMonth);
    Date end = TimeHelper.getLastMillisecondOfMonth(yearMonth);

    return new DateRange(start, end);
  }

  public static DateRange getMonthRangeWithFullWeeks(Date yearMonth) {
    DateRange monthRange = TimeHelper.getMonthRange(yearMonth);

    int firstDayOfWeekInMonth = TimeHelper.getDayOfWeek(monthRange.getStart());
    int lastDayOfWeekInMonth = TimeHelper.getDayOfWeek(monthRange.getEnd());

    int daysToSubtractFromStart = firstDayOfWeekInMonth - 1;
    int daysToAddToEnd = 7 - lastDayOfWeekInMonth;

    Date start = TimeHelper.subtract(monthRange.getStart(), daysToSubtractFromStart, Calendar.DATE);
    Date end = TimeHelper.add(monthRange.getEnd(), daysToAddToEnd, Calendar.DATE);

    return new DateRange(start, end);
  }

  public static DateRange getOutlook() {
    Date now = TimeHelper.getCurrentYearMonthDay();
    int year = TimeHelper.getYear(now);
    int week = TimeHelper.getWeekOfYear(now);
    Date start = TimeHelper.getFirstDayOfWeek(year, week);
    Date end = TimeHelper.getLastDayOfWeek(year, week);
    end = TimeHelper.add(end, 3, Calendar.WEEK_OF_YEAR);
    return new DateRange(start, end);
  }

  public static DateRange getOutlook(int year, int week) {
    Date start = TimeHelper.getFirstDayOfWeek(year, week);
    Date end = TimeHelper.getLastDayOfWeek(year, week);
    end = TimeHelper.add(end, 3, Calendar.WEEK_OF_YEAR);
    return new DateRange(start, end);
  }

  public static int getCurrentYear() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static int getCurrentWeek() {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
  }

  public static int getCurrentMonth() {
    return Calendar.getInstance().get(Calendar.MONTH) + 1; // We want to start with 1, not zero
  }

  /**
   * Calculates the the number of whole days between two dates. Hours, Minutes, seconds, and
   * milliseconds are ignored.
   *
   * <p>Note: this method does not consider daylight savings or TimeZones (Only Calendars work with
   * TimeZones and daylight savings).
   *
   * @param first the first date.
   * @param second the second date.
   * @return the number of days between the two dates.
   */
  public static long approximateDifferenceInDays(Date first, Date second) {
    long daysBetween = 0;
    long milliSecBetween = 0;

    milliSecBetween = Math.abs(second.getTime() - first.getTime());

    daysBetween = milliSecBetween / (1000 * 60 * 60 * 24);

    return daysBetween;
  }
}

package org.jlab.atlis.calendar.presentation.utility;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyle;

public class CalendarHelper {

    public String calculateEditOccurrenceCancel(String id) {

        String url;

        if (id != null && !id.isEmpty()) {
            url = "view-occurrence?occurrenceId=" + id;
        } else {
            url = "view-outlook";
        }

        return url;
    }

    public String calculateEditEventCancel(String id) {

        String url;

        if (id != null && !id.isEmpty()) {
            url = "view-event?eventId=" + id;
        } else {
            url = "view-outlook";
        }

        return url;
    }

    public String calculateNextMonthURL(int calendarId, Date yearMonth) {
        int[] parts = TimeHelper.getNextMonthParts(yearMonth);

        String url = "view-month?calendar=" + calendarId + "&year=" + parts[0] + "&month=" + parts[1];

        return url;
    }

    public String calculatePreviousMonthURL(int calendarId, Date yearMonth) {
        int[] parts = TimeHelper.getPreviousMonthParts(yearMonth);

        String url = "view-month?calendar=" + calendarId + "&year=" + parts[0] + "&month=" + parts[1];

        return url;
    }

    public String calculateNextDayURL(int calendarId, Date dayMonthYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nextDayMonthYear = TimeHelper.getNextDay(dayMonthYear);

        String yearMonthDayParam = format.format(nextDayMonthYear);

        String url = "view-day?calendar=" + calendarId + "&yearMonthDay=" + yearMonthDayParam;

        return url;
    }

    public String calculatePreviousDayURL(int calendarId, Date dayMonthYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date previousDayMonthYear = TimeHelper.getPreviousDay(dayMonthYear);

        String yearMonthDayParam = format.format(previousDayMonthYear);

        String url = "view-day?calendar=" + calendarId + "&yearMonthDay=" + yearMonthDayParam;

        return url;
    }

    public String calculateViewWeekURL(int calendarId, Date yearMonthDay) {
        int[] yearWeek = TimeHelper.getYearWeek(yearMonthDay);
        return calculateViewWeekURL(calendarId, yearWeek[0], yearWeek[1]);
    }

    public String calculateViewWeekURL(int calendarId, int year, int week) {
        String url = "view-week?calendar=" + calendarId + "&year=" + year + "&week=" + week;

        return url;
    }

    public String calculateNextWeekURL(int calendarId, int year, int week) {
        int[] yearWeek = TimeHelper.getNextWeek(year, week);

        String url = "view-week?calendar=" + calendarId + "&year=" + yearWeek[0] + "&week=" + yearWeek[1];

        return url;
    }

    public String calculatePreviousWeekURL(int calendarId, int year, int week) {
        int[] yearWeek = TimeHelper.getPreviousWeek(year, week);

        String url = "view-week?calendar=" + calendarId + "&year=" + yearWeek[0] + "&week=" + yearWeek[1];

        return url;
    }

    public String calculateNextOutlookURL(int calendarId, int year, int week) {
        int[] yearWeek = TimeHelper.getNextWeek(year, week);

        String url = "view-outlook?calendar=" + calendarId + "&year=" + yearWeek[0] + "&week=" + yearWeek[1];

        return url;
    }

    public String calculatePreviousOutlookURL(int calendarId, int year, int week) {
        int[] yearWeek = TimeHelper.getPreviousWeek(year, week);

        String url = "view-outlook?calendar=" + calendarId + "&year=" + yearWeek[0] + "&week=" + yearWeek[1];

        return url;
    }

    public String calculateAddEventURL(int calendarId, Date dayMonthYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String url = "edit-event?calendar=" + calendarId + "&yearMonthDay=" + format.format(dayMonthYear);

        return url;
    }

    public String calculateAddEventURLFromAtlis(BigInteger taskId) {

        String url = "edit-event?taskId=" + taskId;

        return url;
    }

    public String calculateEditEventURL(BigInteger occurrenceId) {
        String url = "edit-event?occurrenceId=" + occurrenceId;

        return url;
    }

    public String calculateDeleteEventURL(BigInteger occurrenceId) {
        String url = "delete-event?occurrenceId=" + occurrenceId;

        return url;
    }

    public String getAtlisURL() {
        return "https://tasklists.jlab.org";
    }

    public String calculateAtlisURL(BigInteger taskId) {

        String url = "https://tasklists.jlab.org/tasks/" + taskId;

        return url;
    }

    /*public String calculateViewMonthURL(int calendarId, int year, int week) {
        Date lastDayOfWeek = TimeHelper.getLastDayOfWeek(year, week);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

        String yearMonthParam = format.format(lastDayOfWeek);

        String url = "view-month?calendar=" + calendarId + "&yearMonth=" + yearMonthParam;

        return url;
    }*/

    public String calculateViewMonthURL(int calendarId, Date dayMonthYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

        String yearMonthParam = format.format(dayMonthYear);

        String url = "view-month?calendar=" + calendarId + "&yearMonth=" + yearMonthParam;

        return url;
    }

    public String getViewMonthURL() {
        return calculateViewMonthURL(1, new Date());
    }

    public String calculateViewMonthURLFromString(String yearMonthDayParam) {

        SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");

        Date yearMonthDay = null;

        try {
            yearMonthDay = yearMonthDayFormat.parse(yearMonthDayParam);
        } catch (Exception e) { // NullPointerException or ParseException
            yearMonthDay = TimeHelper.getCurrentYearMonthDay();
        }

        String yearMonthParam = yearMonthFormat.format(yearMonthDay);

        String url = "view-month?yearMonth=" + yearMonthParam;

        return url;
    }

    public String calculateViewDayURL(int calendarId, Date dayMonthYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String yearMonthDayParam = format.format(dayMonthYear);

        String url = "view-day?calendar=" + calendarId + "&yearMonthDay=" + yearMonthDayParam;

        return url;
    }

    public String calculateSearchAtlisURL(Date yearMonth) {
        Date first = TimeHelper.getFirstDayOfMonth(yearMonth);
        Date last = TimeHelper.getLastDayOfMonth(yearMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String start = format.format(first);
        String end = format.format(last);

        String url = "search-atlis?start=" + start + "&end=" + end;

        return url;
    }

    public String getSearchAtlisURL() {
        return calculateSearchAtlisURL(new Date());
    }

    public boolean isToday(Date dayMonthYear) {
        Date today = TimeHelper.getCurrentYearMonthDay();

        return today.equals(dayMonthYear);
    }

    public String getTodayString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(new Date());
    }

    public int getCurrentWeek() {
        return TimeHelper.getCurrentWeek();
    }

    public int getCurrentMonth() {
        return TimeHelper.getCurrentMonth();
    }

    public int getCurrentYear() {
        return TimeHelper.getCurrentYear();
    }

    public String getCSSClasses(List<OccurrenceStyle> styles) {
        String classes = "";

        if (styles != null && !styles.isEmpty()) {
            for (OccurrenceStyle os : styles) {
                classes = classes + os.getOccurrenceStyleChoice().getCssClassName() + " ";
            }

            // Remove trailing space
            classes = classes.substring(0, classes.length() - 1);
        }

        return classes;
    }

    public String getAbsoluteViewWeekURL(HttpServletRequest request, int calendarId, int year, int week) {
        return getAbsoluteBaseURL(request) + "/" + calculateViewWeekURL(calendarId, year, week);
    }

    public String getAbsoluteViewDayURL(HttpServletRequest request, int calendarId, Date yearMonthDay) {
        return getAbsoluteBaseURL(request) + "/" + calculateViewDayURL(calendarId, yearMonthDay);
    }

    public String getAbsoluteHostURL(HttpServletRequest request) {

        String scheme = "https";
        //String scheme = request.getScheme();

        String host = System.getenv("javaProxyHost");

        if (host == null || host.isEmpty()) {
            host = request.getServerName();
        }

        String portWithColon = null;

        if (scheme.equals("http")) {
            String port = System.getenv("javaProxyPort");

            if (port == null || port.isEmpty()) {
                port = String.valueOf(request.getServerPort());
            }
            
            port = "80";
            
            if (port.equals("80")) {
                portWithColon = "";
            } else {
                portWithColon = ":" + port;
            }
        } else { // https
            String port = System.getenv("javaProxySecurePort");
            
            if (port == null || port.isEmpty()) {
                port = String.valueOf(request.getServerPort());
            }

            port = "443";
            
            if (port.equals("443")) {
                portWithColon = "";
            } else {
                portWithColon = ":" + port;
            }            
        }

        return scheme + "://" + host + portWithColon;
    }
    
    public String getAbsoluteBaseURL(HttpServletRequest request) {
        return getAbsoluteHostURL(request) + request.getContextPath();
    }
}

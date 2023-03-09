package org.jlab.atlis.calendar.business.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryans
 */
public class TimeHelperTest {
    
    public TimeHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDayMonthYear method, of class TimeHelper.
     */
    @Test
    public void testGetYearMonthDay_3args() throws ParseException {
        System.out.println("getYearMonthDay");
        int year = 2011;
        int month = 1;  
        int day = 1;        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date expResult = format.parse("2011-01-01");
        Date result = TimeHelper.getYearMonthDay(year, month, day);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonthYear method, of class TimeHelper.
     */
    @Test
    public void testGetYearMonth_int_int() throws ParseException {
        System.out.println("getYearMonth");
        int year = 2011;
        int month = 1;        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date expResult = format.parse("2011-01");
        Date result = TimeHelper.getYearMonth(year, month);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeekYear method, of class TimeHelper.
     */
    @Test
    public void testGetYearWeek() throws ParseException {
        System.out.println("getYearWeek");
        int year = 2011;
        int week = 1;        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date expResult = format.parse("2010-12-26");
        Date result = TimeHelper.getYearWeek(year, week);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonthYear method, of class TimeHelper.
     */
    @Test
    public void testGetYearMonth_Date() throws ParseException {
        System.out.println("getYearMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date yearMonth = format.parse("2011-01");
        int[] expResult = {2011,1};
        int[] result = TimeHelper.getYearMonth(yearMonth);
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
    }

    /**
     * Test of getDayMonthYear method, of class TimeHelper.
     */
    @Test
    public void testGetYearMonthDay_Date() throws ParseException {
        System.out.println("getYearMonthDay");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date yearMonthDay = format.parse("2011-01-01");
        int[] expResult = {2011,1,1};
        int[] result = TimeHelper.getYearMonthDay(yearMonthDay);
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
    }

    /**
     * Test of add method, of class TimeHelper.
     */
    @Test
    public void testAdd() throws ParseException {
        System.out.println("add");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2011-01-01");
        int amount = 1;
        Date expResult = format.parse("2011-01-02");
        Date result = TimeHelper.add(date, amount, Calendar.DATE);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDayOfWeek method, of class TimeHelper.
     */
    @Test
    public void testGetDayOfWeek() throws ParseException {
        System.out.println("getDayOfWeek");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dayMonthYear = format.parse("2011-01-01");
        int expResult = 7; // SATURDAY
        int result = TimeHelper.getDayOfWeek(dayMonthYear);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDaysInMonth method, of class TimeHelper.
     */
    @Test
    public void testGetDaysInMonth() throws ParseException {
        System.out.println("getDaysInMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date monthYear = format.parse("2011-01");
        int expResult = 31;
        int result = TimeHelper.getDaysInMonth(monthYear);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstMillisecondOfMonth method, of class TimeHelper.
     */
    @Test
    public void testGetFirstMillisecondOfMonth() throws ParseException {
        System.out.println("getFirstMillisecondOfMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date monthYear = format.parse("2011-01-01 00:00:00:000");
        Date expResult = format.parse("2011-01-01 00:00:00:000");
        Date result = TimeHelper.getFirstMillisecondOfMonth(monthYear);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstMillisecondOfWeek method, of class TimeHelper.
     */
    @Test
    public void testGetFirstMillisecondOfWeek() throws ParseException {
        System.out.println("getFirstMillisecondOfWeek");
        int year = 2011;
        int week = 1;        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date expResult = format.parse("2010-12-26 00:00:00:000");
        Date result = TimeHelper.getFirstMillisecondOfWeek(year, week);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastMillisecondOfMonth method, of class TimeHelper.
     */
    @Test
    public void testGetLastMillisecondOfMonth() throws ParseException {
        System.out.println("getLastMillisecondOfMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date monthYear = format.parse("2011-01-01 00:00:00:000");
        Date expResult = format.parse("2011-01-31 23:59:59:999");
        Date result = TimeHelper.getLastMillisecondOfMonth(monthYear);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastMillisecondOfWeek method, of class TimeHelper.
     */
    @Test
    public void testGetLastMillisecondOfWeek() throws ParseException {
        System.out.println("getLastMillisecondOfWeek");
        int year = 2011;
        int week = 1;        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date expResult = format.parse("2011-01-01 23:59:59:999");
        Date result = TimeHelper.getLastMillisecondOfWeek(year, week);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstDayOfMonth method, of class TimeHelper.
     */
    @Test
    public void testGetFirstDayOfMonth() throws ParseException {
        System.out.println("getFirstDayOfMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date monthYear = format.parse("2011-01-01");
        Date expResult = format.parse("2011-01-01");
        Date result = TimeHelper.getFirstDayOfMonth(monthYear);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastDayOfMonth method, of class TimeHelper.
     */
    @Test
    public void testGetLastDayOfMonth() throws ParseException {
        System.out.println("getLastDayOfMonth");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date monthYear = format.parse("2011-01-01");
        Date expResult = format.parse("2011-01-31");
        Date result = TimeHelper.getLastDayOfMonth(monthYear);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetNextWeek_int_int() throws ParseException {
        System.out.println("getNextWeek");
        int year = 2012;
        int week = 52;
        int[] expResult = {2013, 1};
        int[] result = TimeHelper.getNextWeek(year, week);
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
    }
    
    @Test
    public void testGetPreviousWeek_int_int() throws ParseException {
        System.out.println("getPreviousWeek");
        int year = 2014;
        int week = 2;
        int[] expResult = {2014, 1};
        int[] result = TimeHelper.getPreviousWeek(year, week);
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
    }    
    
    @Test
    public void testGetMonthRangeWithFullWeeks() throws ParseException {
        System.out.println("getMonthRangeWithFullWeeks");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date yearMonth = format.parse("2011-01-01 00:00:00:000");
        DateRange expResult = new DateRange(format.parse("2010-12-26 00:00:00:000"), format.parse("2011-02-05 23:59:59:999"));
        DateRange result = TimeHelper.getMonthRangeWithFullWeeks(yearMonth);
        assertEquals(expResult.getStart(), result.getStart());
        assertEquals(expResult.getEnd(), result.getEnd());        
    }
}

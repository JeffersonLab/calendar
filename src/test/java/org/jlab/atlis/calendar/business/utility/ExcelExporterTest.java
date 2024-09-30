package org.jlab.atlis.calendar.business.utility;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ryans
 */
public class ExcelExporterTest {

  public ExcelExporterTest() {}

  @BeforeClass
  public static void setUpClass() throws Exception {}

  @AfterClass
  public static void tearDownClass() throws Exception {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testExport() throws IOException {
    System.out.println("testExport");
    ExcelExporter exporter = new ExcelExporter();

    List<Occurrence> occurrences = new ArrayList<Occurrence>();

    Occurrence o = new Occurrence();
    o.setOccurrenceId(BigInteger.ZERO);
    o.setTitle("Testing");
    o.setYearMonthDay(new Date());
    o.setShift(Shift.OWL);
    o.setDisplay(Display.SHOW);
    o.setStyles(new ArrayList<>());

    Event event = new Event();
    event.setEventId(BigInteger.ZERO);

    o.setEvent(event);

    occurrences.add(o);
    FileOutputStream fileOut = new FileOutputStream("build/workbook.xlsx");
    exporter.export(fileOut, occurrences, true);
    fileOut.close();
  }
}

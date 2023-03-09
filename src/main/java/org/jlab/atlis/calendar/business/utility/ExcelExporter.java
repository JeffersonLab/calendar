package org.jlab.atlis.calendar.business.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyle;

/**
 *
 * @author ryans
 */
public class ExcelExporter {
    public void export(OutputStream out, List<Occurrence> occurrences, boolean includeOperabilityComment) throws IOException {
    Workbook wb = new XSSFWorkbook();
    Sheet sheet1 = wb.createSheet("SAD Calendar");
    
    Row row1 = sheet1.createRow((short)0);
    row1.createCell(0).setCellValue("OCCURRENCE_ID");
    row1.createCell(1).setCellValue("YEAR_MONTH_DAY");
    row1.createCell(2).setCellValue("SHIFT");
    row1.createCell(3).setCellValue("TITLE");
    row1.createCell(4).setCellValue("DESCRIPTION");
    row1.createCell(5).setCellValue("LIAISON");
    row1.createCell(6).setCellValue("EVENT_ID");
    row1.createCell(7).setCellValue("ATLIS_ID");
    row1.createCell(8).setCellValue("DISPLAY");
    row1.createCell(9).setCellValue("STYLE");
    
    if(includeOperabilityComment) {
        row1.createCell(10).setCellValue("OPERABILITY_COMMENT");
    }
    
    CreationHelper createHelper = wb.getCreationHelper();
    CellStyle dateStyle = wb.createCellStyle();
    dateStyle.setDataFormat(
    createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

    
    for(int i = 1; i <= occurrences.size(); i++) {
        Row row = sheet1.createRow((short)i);
        row.createCell(0).setCellValue(occurrences.get(i - 1).getOccurrenceId().longValue());
        
        Cell c1 = row.createCell(1);
        c1.setCellValue(occurrences.get(i - 1).getYearMonthDay());
        c1.setCellStyle(dateStyle);
        
        row.createCell(2).setCellValue(occurrences.get(i - 1).getShift().toString());
        row.createCell(3).setCellValue(occurrences.get(i - 1).getTitle());
        row.createCell(4).setCellValue(occurrences.get(i - 1).getDescription());
        row.createCell(5).setCellValue(occurrences.get(i - 1).getLiaison());
        row.createCell(6).setCellValue(occurrences.get(i - 1).getEvent().getEventId().longValue());
        
        Cell c7 = row.createCell(7);
        BigInteger taskId = occurrences.get(i - 1).getEvent().getTaskId();
        if(taskId != null) {
            c7.setCellValue(taskId.longValue());
        }
        
        row.createCell(8).setCellValue(occurrences.get(i - 1).getDisplay().toString());
        
        String styles = "";
        
        for(OccurrenceStyle style: occurrences.get(i - 1).getStyles()) {
            styles = styles + style.getOccurrenceStyleChoice().getName() + ", ";
        }
        
        // Remove last delimiter
        if(styles.length() > 1) {
            styles = styles.substring(0, styles.length() - 2);
        }
        
        row.createCell(9).setCellValue(styles);
        
        if(includeOperabilityComment) {
            row.createCell(10).setCellValue(occurrences.get(i - 1).getRemark());        
        }        
    }
    
    sheet1.autoSizeColumn(0);
    sheet1.autoSizeColumn(1);
    sheet1.autoSizeColumn(2);
    sheet1.autoSizeColumn(3);
    sheet1.autoSizeColumn(4);
    sheet1.autoSizeColumn(5);
    sheet1.autoSizeColumn(6);
    sheet1.autoSizeColumn(7);
    sheet1.autoSizeColumn(8);
    sheet1.autoSizeColumn(9);
    
    if(includeOperabilityComment) {
        sheet1.autoSizeColumn(10);        
    }    
    
    wb.write(out);        
    }
}

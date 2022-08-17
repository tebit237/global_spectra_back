/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.SqlFileType;
import java.io.IOException;
import java.sql.SQLType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportFilee {

    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private int leng;
    private List<String> desp;
    private List<SqlFileType> listUsers;

    public ExportFilee(List<SqlFileType> listUsers, int leng, List<String> desp) {
        System.out.println("the declared ");
        this.leng = leng - 1;
        this.listUsers = listUsers;
        this.desp = desp;
        workbook = new SXSSFWorkbook();
    }

    private void writeHeaderLine() { 
        System.out.println("Heading set ");
        sheet = (SXSSFSheet) workbook.createSheet("Data");
        sheet.setRandomAccessWindowSize(100);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        for (int i = 0; i <= leng; i++) {
            createCell(row, i + 1, desp.get(i), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        System.out.println("the lenth is " + leng);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        for (SqlFileType user : listUsers) {
            if (rowCount % 250 == 0) {
                System.out.println("starting row " + rowCount + " insertion");
            }
            Row row = sheet.createRow(rowCount++);
            int columnCount = 1;
            for (int i = 0; i <= leng; i++) {
                createCell(row, columnCount++, user.cellExtra(i + 1), style);
            }
        }
    }

    public void export(HttpServletResponse response)  {
        writeHeaderLine();
        writeDataLines();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ExportFilee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}

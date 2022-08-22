/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.ReportRep;
import iwomi.base.objects.SqlFileType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ManageExcelFiles extends GlobalService {

    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private int leng;
    private List<String> desp;
    private List<SqlFileType> listUsers;
    static String SHEET = "Data";
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public ManageExcelFiles(List<SqlFileType> listData, int leng, List<String> headerDescrption) {
        System.out.println("the declared ");
        this.leng = leng - 1;
        this.listUsers = listData;
        this.desp = headerDescrption;
        workbook = new SXSSFWorkbook();
    }

    public ManageExcelFiles() {
    }

    public boolean hasExcelFormat(MultipartFile file) {
        System.out.println("the file type is : " + file.getContentType());
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public Path sesamGeneration(String filename, Statement ert) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            ResultSet er = ert.executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0038' and dele = 0");
            er.next();
            String fr1 = er.getString("lib2") + "/" + filename + ".xlsx";
            ResultSet err = ert.executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0015' and dele = 0");
            err.next();
            String fr1r = err.getString("lib2") + "/" + filename + ".xlsx";
            try {
                Files.copy((new File(fr1)).toPath(), (new File(fr1r)).toPath());
            } catch (IOException g) {
                Files.delete((new File(fr1r)).toPath());
                Files.copy((new File(fr1)).toPath(), (new File(fr1r)).toPath());
            }
            ResultSet erru = ert.executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0041' and dele = 0");
            erru.next();
            if (erru.getString(1).equalsIgnoreCase("2") || erru.getString(1).equalsIgnoreCase("3")) {
                Path path = Paths.get(fr1r);
                Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
                perms.add(PosixFilePermission.OTHERS_WRITE);
                perms.add(PosixFilePermission.OTHERS_READ);
                perms.add(PosixFilePermission.OTHERS_EXECUTE);
                Files.setPosixFilePermissions(path, perms);
            }
           return (new File(fr1r)).toPath();
        } catch (IOException e) {

        }
        return null;
    }

    public List<ReportRep> excelToReport(InputStream is, String file, Date dar, int colnum) {
        String t = "";
        try {
            ResultSet rs = connectDB().createStatement().executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0009'");
            rs.next();
            t = rs.getString("lib2");
        } catch (SQLException ex) {
            Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<String, String> hh = r(file.trim());
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<ReportRep> reportReps = new ArrayList<ReportRep>();
            int rowNumber = 0;
            DataFormatter s = new DataFormatter();
            s.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                System.out.println("verify the date r :" + colnum);
                Iterator<Cell> cellsInRow = currentRow.iterator();
                System.out.println("verify the date 2:" + rowNumber);
                String post = "";
                for (int cellIdx = 0; cellIdx < colnum; cellIdx++) {
                    System.out.println("verify the date 2:" + cellIdx);

                    Cell currentCell = null;
                    ReportRep reportRep = new ReportRep();
                    try {
                        currentCell = cellsInRow.next();
                        reportRep.setValc(s.formatCellValue(currentCell));
                    } catch (Exception e) {
                        reportRep.setValc("");
                        System.out.println("the value at " + e.getMessage() + ":" + currentCell);
                        break;
                    }
                    if (cellIdx == 0 && hh.get("result").equalsIgnoreCase("duplicateNoPost")) {
                        post = s.formatCellValue(currentCell);
                        System.out.println("the post is :" + post);
                        continue;
                    }
                    if (hh.get("result").equalsIgnoreCase("duplicateNoPost")) {
                        reportRep.setPost(post);
                    }
                    reportRep.setFeild("CH" + rowNumber + "C" + (cellIdx + 1));
                    reportRep.setEtab(t);
                    reportRep.setRang(new Long(rowNumber));
                    reportRep.setCol(Integer.toString(cellIdx + 1));
                    reportRep.setDar(dar);
                    reportRep.setFichier(file);
                    reportRep.setStatus(1);
                    reportReps.add(reportRep);
                }
                rowNumber++;
            }
            workbook.close();
            return reportReps;
        } catch (IOException e) {
            System.out.println("error during saving at line" + e.getLocalizedMessage() + " and " + e.getMessage());
        }
        return null;
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

    public void export(HttpServletResponse response) {
        writeHeaderLine();
        writeDataLines();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ManageExcelFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

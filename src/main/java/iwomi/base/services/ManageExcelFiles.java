/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.ReportRep;
import iwomi.base.objects.SqlFileType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
    private List<List> g;
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

    public ManageExcelFiles(List<List> tt, List<String> h) {
        System.out.println("the declared ");
        this.leng = h.size() - 1;
        this.g = tt;
        this.desp = h;
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

    public String sesamGeneration(String filename, Statement ert, String fname) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            ResultSet er = ert.executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0038' and dele = 0");
            er.next();
            String fr1 = er.getString("lib2") + "/" + filename + ".xlsx";
            ResultSet err = ert.executeQuery("select lib2 from sanm where tabcd = '0012' and acscd = '0015' and dele = 0");
            err.next();
            String fr1r = err.getString("lib2") + "/" + fname;
//            try {
//                Files.createDirectory((new File(err.getString("lib2") + "/" + dat)).toPath());
//            } catch (Exception re) {
//                System.out.println("the v : " + re.getLocalizedMessage());
//            }
            try {
                Files.copy((new File(fr1)).toPath(), (new File(fr1r)).toPath());
            } catch (IOException g) {
                try {
                    Files.delete((new File(fr1r)).toPath());
                    Files.copy((new File(fr1)).toPath(), (new File(fr1r)).toPath());
                } catch (Exception r) {
                    System.out.println("error is :" + r.getLocalizedMessage());
                }

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
            return fr1r;
        } catch (Exception e) {
            System.out.println("error is r:" + e.getLocalizedMessage());
        }
        return null;
    }

    public Map<String, Object> update_excel_file(Map<String, String> request) {
        System.out.println("yvo start execution ");
        Map<String, Object> result = new HashMap<>();

        ArrayList al = new ArrayList();
        // from where file is to be read
        //File file1 = new File("C:/savedexcel/FM1000.xlsx");
        File file1 = new File("C:/savedexcel/bilan.xlsx");
        // List<ReportRep> report = reportRepRepository.preparedResult12(request.get("file"), request.get("dar"));       
        List<ReportRep> report = new ArrayList();
        //CONST = 0;
        //for (int j = 0; j < report.size(); j++) {
        //System.out.println("yvo start execution "+report.get(j).getPost());
        try {
            //ajout d'element dans le fichier
            FileInputStream fileI = new FileInputStream(file1);
            Workbook workbookF = WorkbookFactory.create(fileI);
            Sheet sheet = workbookF.getSheetAt(1);
            int rowCount = sheet.getLastRowNum() + 5;
            System.out.println("yvo start execution rang2 ");
            for (int r = 0; r <= rowCount; r++) {
                System.out.println("yvo start execution cel01 ");
                Row row = sheet.getRow(r);
                System.out.println("yvo start execution cell0 ");
                row.createCell(3).setCellValue(request.get("code"));
                row.createCell(4).setCellValue(request.get("name"));
                System.out.println("yvo start execution cell2 ");
                row.createCell(5).setCellValue(request.get("age"));
                row.createCell(6).setCellValue(request.get("bonjour yves"));
                System.out.println("yvo start execution cell3 ");
            }

            fileI.close();

            FileOutputStream fileO = new FileOutputStream(file1);
            workbookF.write(fileO);
            fileO.close();
            System.out.println("Data Copied to Excel");
        } catch (IOException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        // }
        result.put("success", "01");
        result.put("data", al);
        return result;

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
                if (rowNumber == 0 ||currentRow.getCell(0)==null) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                System.out.println("line : " + rowNumber + " :" + currentRow.getCell(0));
                String post = "";
                for (int cellIdx = 0; cellIdx < colnum; cellIdx++) {

                    ReportRep reportRep = new ReportRep();
//                    System.out.println("data : " + currentRow.getCell(cellIdx).getCellType() + " : " + currentRow.getCell(cellIdx) + " : ");
                    if (currentRow.getCell(cellIdx) == null) {
                        reportRep.setValc("");
                    } else {
                        reportRep.setValc(s.formatCellValue(currentRow.getCell(cellIdx)));
                        }
                    if (cellIdx == 0 && hh.get("result").equalsIgnoreCase("duplicateNoPost")) {
                        post = s.formatCellValue(currentRow.getCell(cellIdx));
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
        CellStyle style = workbook.createCellStyle();
        sheet = (SXSSFSheet) workbook.createSheet("Data");
        sheet.setRandomAccessWindowSize(100);
        Row row = sheet.createRow(0);
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        for (int i = 0; i <= leng; i++) {
            createCell(row, i, desp.get(i), style);
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
            int columnCount = 0;
            for (int i = 0; i <= leng; i++) {
                createCell(row, columnCount++, user.cellExtra(i + 1), style);
            }
        }
    }

    private void writeDataLines1() {
        int rowCount = 1;
        System.out.println("the lenth is " + leng);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        for (List user : g) {
            if (rowCount == 1000000) {
                sheet = (SXSSFSheet) workbook.createSheet("Data2");
                sheet.setRandomAccessWindowSize(100);
                rowCount = 1;
            }
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (int i = 0; i < user.size(); i++) {
                createCell(row, columnCount++, user.get(i), style);
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

    public ByteArrayInputStream export1() {
        writeHeaderLine();
        writeDataLines1();
        try {
//            OutputStream outputStream = response.getOutputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(ManageExcelFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String etatToExcel2(ResultSet etat) throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            ResultSetMetaData rsmd = etat.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rsmd.getColumnName(i));
            }
            int rowIdx = 1;
            int columns = etat.getMetaData().getColumnCount();
            int er = 0;
            while (etat.next()) {
                er++;
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < columns; i++) {
                    row.createCell(i).setCellValue(etat.getObject(i + 1) + "");

                }
            }
            String rkn = timestamp.getTime() + "";
            String fr = "/var/www/html/reporting/ressources/etat/etat" + rkn + ".xlsx";
            FileOutputStream r = new FileOutputStream(fr);
            workbook.write(r);
            r.close();
            Path path = Paths.get(fr);
            Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
            perms.add(PosixFilePermission.OTHERS_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(path, perms);
            return rkn;
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.controllers;

//import com.iwomi.model.CamTrans;
//import com.bezkoder.spring.files.excel.service.ExcelService;
//import com.bezkoder.spring.files.excel.service.ServiceReporting;
import iwomi.base.form.ReportRepf;
import iwomi.base.objects.SqlFileType;
import iwomi.base.services.ManageExcelFiles;
import iwomi.base.services.MediaTypeUtils;
import iwomi.base.services.ServiceReporting_v1;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
//import com.iwomi.service.ServiceApiImpl;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin()
@RequestMapping("/reportingv1")
public class Reporting_v1 {

    @Autowired
    ServiceReporting_v1 serviceReporting;

    @RequestMapping(value = "/reporting_all", method = RequestMethod.POST)
    public Map reporting_all(@RequestBody Map<String, String> payload) throws Exception {
        return serviceReporting.Report_info_export(payload);
    }

    @RequestMapping(value = "/Report_dropdown", method = RequestMethod.POST)
    public Map Report_dropdown(@RequestBody Map<String, String> payload) throws Exception {
        return serviceReporting.Report_dropdown(payload);
    }

    @PostMapping(path = "/downloadGenFile")
    public ResponseEntity<Resource> exportToExcel(@RequestBody Map<String, String> p) {
        Map r;
        try {
            r = serviceReporting.Report_export(p);
            List<List> e = (List<List>) r.get("data");
            List et = (List) r.get("head");
            ManageExcelFiles excelFile = new ManageExcelFiles(e, et);
            String filename = "file.xlsx";
            InputStreamResource file = new InputStreamResource(excelFile.export1());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } catch (Exception ex) {
            Logger.getLogger(Reporting_v1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

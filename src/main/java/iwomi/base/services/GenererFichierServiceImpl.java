/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import iwomi.base.ServiceInterface.GenererFichierServices;
import iwomi.base.form.ClientToSystemForm;
import iwomi.base.form.DataIntegrationForm;
import iwomi.base.form.GenererFichierForm;
import iwomi.base.objects.ReportDatasGenerated;
import iwomi.base.objects.SqlFileType;
import iwomi.base.repositories.ReportDatasGeneratedRepository;
import iwomi.base.repositories.SqlFileTypeRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import iwomi.base.ServiceInterface.GenererFichierServices;
import iwomi.base.ServiceInterface.LiveReportingService;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.ServiceInterface.ReportCalculateService;
import iwomi.base.form.Consql;
import iwomi.base.form.GenererFichierForm;
import iwomi.base.objects.GenFile;
import iwomi.base.objects.GenFileId;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportDatasGenerated;
import iwomi.base.objects.ReportFile;
import iwomi.base.objects.ReportRep;
import iwomi.base.repositories.GenFileRepository;
import iwomi.base.repositories.InputWriteRepository;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportDatasGeneratedRepository;
import iwomi.base.repositories.ReportFileRepository;
import iwomi.base.repositories.ReportRepRepository;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLSyntaxErrorException;
import java.util.Base64;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabri
 */
@Component
@Service
public class GenererFichierServiceImpl extends GlobalService implements GenererFichierServices {

    Connection connection, con = null;
//    public static final String USERNAME="root";
//    public static final String PASSWORD="root";
    public static final String CONN_STRING = "jdbc:mysql://10.1.1.26/ares"; // production
//    public static final String CONN_STRING="jdbc:mysql://localhost/spectra:3306?autoReconnect=true&useSSL=false";   //testing
    // LOCAL my machine
    public static final String USERNAME = "root";
    public static final String PASSWORD = "ares";

    String NOMENGABTABLE_SYS = "0012";
    String NOMMENCLATURE_FICH = "3009";
    Map<String, String> PARAM = new HashMap<>();
    @Autowired
    ReportDatasGeneratedRepository reportDatasGeneratedRepository;
    List<ReportDatasGenerated> reportDatasGenerated2 = new ArrayList<ReportDatasGenerated>();
    List<SqlFileType> reportDatasGenerated3 = new ArrayList<SqlFileType>();
    Map<String, String> parameters = new HashMap<>();
    Map<String, String> dropdown = new HashMap<>();
    int CONST = 0, defineFileToSave = 0;
    String lineFile, lineFile1, fileName;
    Session session = null;
    JSch jsch = new JSch();
    Channel channel = null;
    ChannelSftp channelSftp;
    String delimiteur;
    Long idOpe, minimum;
    String idTrait, codeFichier;
    Long quotien = Long.valueOf(1);
    // Long nombreTotal =Long.valueOf(16807);
    Long nombreTotal = Long.valueOf(49);
    Long count = Long.valueOf(0);
    Long statut = Long.valueOf(3);
    Long statutope = Long.valueOf(3);
    Long nombreOpeTraite = Long.valueOf(0);
    Long nbreLigne = Long.valueOf(7);
    @Autowired
    LiveReportingService liveReportingService;
    @Autowired
    LiveReportingServiceS liveReportingServices;
    @Autowired
    private ReportCalculateService reportCalculateService;
    @Autowired
    private SqlFileTypeRepository sqlFileTypeRepository;
    @Autowired
    private GenFileRepository genFileRepository;

    public Map<String, String> getGenerationAndSavingParam()
            throws SQLException, ClassNotFoundException, JSONException {
        System.out.println("START GETTING PÄRAMS");
        Map<String, String> PARAM = new HashMap<>();
        String select = "";
        ResultSet result = null;
        try {
            Statement stmt = (Statement) reportCalculateService.conac1();
            select = "SELECT * FROM sanm  WHERE tabcd='" + NOMENGABTABLE_SYS + "' AND dele='0'";
            result = stmt.executeQuery(select);
            while (result.next()) {
                if (result.getString("acscd").equalsIgnoreCase("0009")) {
                    PARAM.put("idetab", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0010")) {
                    PARAM.put("extention", result.getString("lib2"));
                }

                if (result.getString("acscd").equalsIgnoreCase("0011")) {
                    PARAM.put("ip", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0012")) {
                    PARAM.put("port", result.getString("lib2"));

                }
                if (result.getString("acscd").equalsIgnoreCase("0013")) {
                    PARAM.put("pass", result.getString("lib2"));

                }
                if (result.getString("acscd").equalsIgnoreCase("0014")) {
                    PARAM.put("user", result.getString("lib2"));

                }
                if (result.getString("acscd").equalsIgnoreCase("0015")) {
                    PARAM.put("chemin", result.getString("lib2"));
                }

                if (result.getString("acscd").equalsIgnoreCase("0016")) {
                    PARAM.put("oracleUrl", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0017")) {
                    PARAM.put("loginUrl", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0018")) {
                    PARAM.put("passwordUrl", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0019")) {
                    PARAM.put("invEncours", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0020")) {
                    PARAM.put("invArchives", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0021")) {
                    PARAM.put("invErreurs", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0022")) {
                    PARAM.put("invErreurs", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0024")) {
                    PARAM.put("delimiteur", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0025")) {
                    PARAM.put("codePays", result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0026")) {
                    PARAM.put("formatDate", result.getString("lib2"));
                }

                if (result.getString("acscd").equalsIgnoreCase("0027")) {
                    PARAM.put("status", result.getString("lib2")); // statut du fichier
                }

                if (result.getString("acscd").equalsIgnoreCase("0028")) {
                    PARAM.put("sameServer", result.getString("lib2")); // 1 le fichier est stoqué sur le meme serveur que le
                }
                if (result.getString("acscd").equalsIgnoreCase("0029")) {
                    PARAM.put("minimumNumber", result.getString("lib2")); // 1 le fichier est stoqué sur le meme serveur que
                }
            }
        } catch (Exception r) {
            System.out.println(r.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    /* Ignored */
                }
            }
        }
        System.out.println("END GETGENARATION AND SAVING PARAMS");

        return PARAM;

    }

    public List<GenFile> getGeneratedFile() throws SQLException, ClassNotFoundException {
        return null;
    }

    public Map<String, String> getFileLengthColumn() throws SQLException, ClassNotFoundException, JSONException {
        Map<String, String> PARAM = new HashMap<>();
        String select = "";
        JSONObject obj = new JSONObject();
        Statement stmt = (Statement) reportCalculateService.conac1();
        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMMENCLATURE_FICH + "' AND dele='0'";
        ResultSet result = stmt.executeQuery(select);
        while (result.next()) {

            PARAM.put(result.getString("lib2"), result.getString("taux4"));
        }
        return PARAM;

    }

    public Map<String, String> getFileLengthColumnv1(Statement r) throws SQLException, ClassNotFoundException, JSONException {
        Map<String, String> PARAM = new HashMap<>();
        String select = "";
        JSONObject obj = new JSONObject();
        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMMENCLATURE_FICH + "' AND dele='0'";
        ResultSet result = r.executeQuery(select);
        while (result.next()) {
            PARAM.put(result.getString("lib2"), result.getString("taux4"));
        }
        if (result != null) {
            result.close();
        }
        return PARAM;

    }

    public String genererFichiers(GenererFichierForm fic) {

        // recuperation des données a ecrire sur le fichier
        lineFile = "";
        lineFile1 = "";
        delimiteur = "";
        try {
            // recuperation des paramètres
            parameters = getGenerationAndSavingParam();
            if (parameters.get("sameServer").equalsIgnoreCase("1")) {
                // return genererFichiersLocal(fic);
            }
            delimiteur = parameters.get("delimiteur");
            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeFichier().size()));
            minimum = Long.valueOf(parameters.get("minimumNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("GET DATAS FROM DATABASE*****************");
        // reportDatasGenerated2 =
        // reportDatasGeneratedRepository.findReportDatasGeneratedByDarOrderByFichierAscRangAscColAsc(fic.getDate());
        System.out.println("Sizen " + reportDatasGenerated2.size());
        // reportDatasGenerated2=getDonneesTest();
        if (reportDatasGenerated2.size() == 0) {

            return "2";
        }
        try {
            dropdown = getFileLengthColumn();
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        // acces au fichier pour ecriture
        BufferedWriter writer = null;
        JSch jsch2 = new JSch();
        try {
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            session.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            channel.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;

        try {

            System.out.println("Connection Opened\n" + parameters.get("chemin"));
            channelSftp.cd(parameters.get("chemin"));
        } catch (SftpException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < fic.getCodeFichier().size(); i++) {
            System.out.println("1NSIDE FIRST FOREACH");
            System.out.println("iNSIDE FIRST reportDatasGenerated2.size()" + reportDatasGenerated2.size());

            try {
                for (int j = 0; j < reportDatasGenerated2.size(); j++) {
                    System.out.println("UNSIDE SECOND FOREACH");
                    System.out.println("UNSIDE SECOND FOREACH code " + fic.getCodeFichier().get(i).getCode());
                    System.out.println(
                            "UNSIDE SECOND reportDatasGe" + "nerated2 " + reportDatasGenerated2.get(j).getFichier());
                    if (fic.getCodeFichier().get(i).getCode()
                            .equalsIgnoreCase(reportDatasGenerated2.get(j).getFichier())) {
                        System.out.println("READYY TO WRITE");
                        // preparation du fichier
                        if (defineFileToSave == 0) {
                            try {
                                // structure du nom de fichier yyyyMM+typede
                                // fichier(5positions)+idetab(5positions)+extension(TXT)
                                fileName = getYearMonth2(fic.getDate()) + reportDatasGenerated2.get(j).getFichier()
                                        + parameters.get("idetab") + "." + parameters.get("extention");
                            } catch (ParseException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            final File file = new File(fileName);
                            OutputStream out = null;
                            try {
                                out = channelSftp.put(parameters.get("chemin") + fileName);
                            } catch (SftpException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            writer = new BufferedWriter(new OutputStreamWriter(out));
                            defineFileToSave = 1;
                            idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                    fic.getCodeFichier().get(i).getCode(), Long.valueOf(nombreTotal / nbreLigne));
                            String ActualDate = "";
//                        try {
//                            ActualDate = getDateAtTheGoodformat(fic.getDate(),parameters.get("codePays"));
//                        } catch (ParseException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                            lineFile1 = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + reportDatasGenerated2.get(j).getFichier();
                            writer.write(lineFile1);
                            writer.newLine();
                            lineFile1 = "";
                        }
                        codeFichier = fic.getCodeFichier().get(i).getCode();

                        // contruction de la ligne
                        if (CONST < Integer.parseInt(dropdown.get(reportDatasGenerated2.get(j).getFichier())) - 1) {

//                        if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("C")){
//                            if(reportDatasGenerated2.get(j).getValc().equalsIgnoreCase("999999999999999")){
//                            
//                                lineFile+="";
//
//                            }else{
//                                lineFile+=reportDatasGenerated2.get(j).getValc()+"|";
//
//                            }
//                        
//                        
//                        }else if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("M")){
//                            
//                            if(String.valueOf(reportDatasGenerated2.get(j).getValm()).equalsIgnoreCase("999999999999999")){
//                            
//                                lineFile+="";
//
//                            }else{
//                                lineFile+=reportDatasGenerated2.get(j).getValm()+"|";
//
//                            }
//                                   
//                            
//                            
//                        
//                        }else if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("T")){
//                            
//                            
//                           if(String.valueOf(reportDatasGenerated2.get(j).getValt()).equalsIgnoreCase("999999999999999")){
//                            
//                            lineFile+="";
//
//                            }else{
//                                lineFile+=reportDatasGenerated2.get(j).getValt()+"|";
//
//                            }
//                        
//                        }else{
//                        
//                         System.out.println("TYPE ERROR .........");
//                        
//                        }
//                        
//                      
//                        CONST++;
//                        
//                    } else {
//                        
//                        if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("C")){
//                            
//                            
//                            if(reportDatasGenerated2.get(j).getValc().equalsIgnoreCase("999999999999999")){
//                                
//                            
//                                lineFile+="";
//
//                            }else{
//                                
//                                lineFile+=reportDatasGenerated2.get(j).getValc()+"|";
//
//                            }
//                        
//                        
//                        }else if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("M")){
//                            
//                            if(String.valueOf(reportDatasGenerated2.get(j).getValm()).equalsIgnoreCase("999999999999999")){
//                            
//                                lineFile+="";
//
//                            }else{
//                                lineFile+=reportDatasGenerated2.get(j).getValm()+"|";
//
//                            }
//                                   
//                            
//                            
//                        
//                        }else if(reportDatasGenerated2.get(j).getTypeval().equalsIgnoreCase("T")){
//                            
//                            
//                           if(String.valueOf(reportDatasGenerated2.get(j).getValt()).equalsIgnoreCase("999999999999999")){
//                            
//                            lineFile+="";
//
//                            }else{
//                                lineFile+=reportDatasGenerated2.get(j).getValt()+"|";
//
//                            }
//                        
//                        }else{
//                        
//                         System.out.println("TYPE ERROR .........");
//                        
//                        }
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("line" + j + "= :" + lineFile);
                            System.out.println("end of line" + j);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                    Long.valueOf(nombreTotal / nbreLigne), Long.valueOf(count), quotien, minimum);
                            CONST = 0;
                            lineFile = "";

                        }

                    } else {
                    }

                }
                // writer.close();
                defineFileToSave = 0;
                writer.close();
                defineFileToSave = 0;
                statut = Long.valueOf(1);
                liveReportingService.endDetailsReportingToTheVue(idTrait, codeFichier, statut, Long.valueOf(count));
                System.out.println("AFTERENDING DETAILS" + idTrait);
                nombreOpeTraite++;
                quotien = Long.valueOf(1);
                count = Long.valueOf(0);
            } catch (IOException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

    }

    public String genererFichiersLocal(GenererFichierForm fic) {

        lineFile = "";
        lineFile1 = "";
        delimiteur = "";
        // recuperation des données a ecrire sur le fichier
        System.out.println("startttttootttttttttttttttttttttttttttttttttttt+dddddddddddddddd" + fic.getDate());
        // reportDatasGenerated2 =
        // reportDatasGeneratedRepository.findReportDatasGeneratedByDarOrderByFichierAscRangAscColAsc(fic.getDate());
        // reportDatasGenerated2=getDonneesTest();
        System.out.println(
                "starttttttttttttttttttttttttttttttttttttttttt+dddddddddddddddd" + reportDatasGenerated2.size());
        if (reportDatasGenerated2.size() == 0) {
            System.out.println("startttEnd");
            return "2";
        }
        System.out.println("startttttoottttdddddddddddddddd");
        try {
            // recuperation des paramètres
            parameters = getGenerationAndSavingParam();
            delimiteur = parameters.get("delimiteur");

        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dropdown = getFileLengthColumn();
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                fic.getOperation(), Long.valueOf(fic.getCodeFichier().size()));
        minimum = Long.valueOf(parameters.get("minimumNumber"));
        // acces au fichier pour ecriture
        BufferedWriter writer = null;
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        for (int i = 0; i < fic.getCodeFichier().size(); i++) {
            System.out.println("BBBBBBBBBBBBBBBBB");
            try {
                for (int j = 0; j < reportDatasGenerated2.size(); j++) {
                    System.out.println("CCCCCCCCCCCCCCCC");
                    // int firstline = 0;// lorsque 0 on ne peut ecrrire sur la premiere ligne
                    // lorsque 1 nous le pouvons
                    if (fic.getCodeFichier().get(i).getCode()
                            .equalsIgnoreCase(reportDatasGenerated2.get(j).getFichier())) {
                        System.out.println("DDDDDDDDDDDDDD");
                        // preparation du fichier
                        if (defineFileToSave == 0) {
                            System.out.println("EEEEEEEEEEEEEEEEEEEEE");
                            try {
                                // structure du nom de fichier yyyyMM+typede
                                // fichier(5positions)+idetab(5positions)+extension(TXT)
                                System.out.println("BEFORE NAME GENERATION");
                                System.out
                                        .println("BEFORE NAME GENERATION" + reportDatasGenerated2.get(j).getFichier());
                                System.out.println("BEFORE NAME GENERATION" + parameters.get("idetab"));
                                System.out.println("BEFORE NAME GENERATION" + parameters.get("extention"));
                                System.out.println("BEFORE NAME GENERATION" + fic.getDate());
                                fileName = getYearMonth2(fic.getDate()) + reportDatasGenerated2.get(j).getFichier()
                                        + parameters.get("idetab") + "." + parameters.get("extention");
                                System.out.println("AFTER NAME GENERATION");
                                idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                        fic.getCodeFichier().get(i).getCode(), Long.valueOf(nombreTotal / nbreLigne));
                            } catch (ParseException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            codeFichier = fic.getCodeFichier().get(i).getCode();
                            String ActualDate = "";
                            // String ActualDate=
                            // getDateAtTheGoodformat(fic.getDate(),parameters.get("codePays"));
                            final File file = new File(fileName);
                            OutputStream out = null;
                            // writer = new BufferedWriter(new FileWriter("C:\\xch\\banque\\"+file));
                            writer = new BufferedWriter(new FileWriter(parameters.get("chemin") + file));
                            lineFile1 = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + reportDatasGenerated2.get(j).getFichier();
                            writer.write(lineFile1);
                            writer.newLine();
                            // count++;
                            // quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier,
                            // Long.valueOf(nombreTotal/nbreLigne), Long.valueOf(count), quotien, minimum);
                            lineFile1 = "";
//                        try {
//                            out = channelSftp.put(parameters.get("chemin")+fileName);
//                        } catch (SftpException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        writer = new BufferedWriter(new OutputStreamWriter(out));
                            defineFileToSave = 1;
                        }
                        // contruction de la ligne
                        System.out.println(
                                "NUM ROW*************" + dropdown.get(reportDatasGenerated2.get(j).getFichier()));
                        System.out.println("NUM ROW*************" + reportDatasGenerated2.get(j).getFichier());
                        if (CONST < Integer.parseInt(dropdown.get(reportDatasGenerated2.get(j).getFichier())) - 1) {
                            System.out.println("NUM TIME*************" + j);
                            System.out.println("NUM CONST*************" + CONST);

                            lineFile += reportDatasGenerated2.get(j).getField() + delimiteur;
                            CONST++;

                        } else {
                            System.out.println("NUM ROW*************" + reportDatasGenerated2.get(j).getFichier());
                            System.out.println("NUM ROW*************" + reportDatasGenerated2.get(j).getField());
                            System.out.println("CONST *************" + CONST);

                            lineFile += reportDatasGenerated2.get(j).getField();
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("line" + j + "= :" + lineFile);
                            System.out.println("end of line" + j);
                            CONST = 0;
                            lineFile = "";
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                    Long.valueOf(nombreTotal / nbreLigne), Long.valueOf(count), quotien, minimum);
                        }

                    }

                }

                writer.close();
                defineFileToSave = 0;
                statut = Long.valueOf(1);
                liveReportingService.endDetailsReportingToTheVue(idTrait, codeFichier, statut, Long.valueOf(count));
                System.out.println("AFTERENDING DETAILS" + idTrait);
                nombreOpeTraite++;
                quotien = Long.valueOf(1);
                count = Long.valueOf(0);

            } catch (IOException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";
    }
    @Autowired
    InputWriteRepository InputWriteRepository;
    @Autowired
    ReportRepRepository reportRepRepository;
    @Autowired
    NomenclatureRepository nomenclatureRepository;
    @Autowired
    ChargerDonneesServiceImpl chargerDonneesServiceImpl;

    public Connection connectDB() throws SQLException, ClassNotFoundException, JSONException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        try {
            Nomenclature s = nomenclatureRepository.findBytabcdAndDeleAndAcscd("4002", 0, "0001");
            return DriverManager.getConnection(s.getLib1(), s.getLib2(), s.getLib3());
        } catch (SQLException ex) {
            System.out.println("the error during connection ERRO1" + ex.getMessage());
        }
        return null;
    }

    public String genererFichiersP(GenererFichierForm fic) {

        // recuperation des données a ecrire sur le fichier
        lineFile1 = "";
        delimiteur = "";
//         parameters = new HashMap<>();
//        Long Long idOpe = 0L;
        Thread t = null;
        try {
            // recuperation des paramètres
            Connection s = connectDB();
            Statement r = s.createStatement();
            Map<String, String> parameters = chargerDonneesServiceImpl.getGenerationAndSavingParamv2(r);
            Map<String, String> dropdown = getFileLengthColumnv1(r);
            Long idOpe = liveReportingServices.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeFichier().size()));

            ExecutorService service = Executors.newFixedThreadPool(5);
            t = new Thread(new Runnable() {
                public void run() {
                    do {
                        try {
                            for (int i = 0; i < fic.getCodeFichier().size(); i++) {
                                fic.setDate(fic.getArrete().get(fic.getCodeFichier().get(i).getCode()));
                                service.execute(new GenererFichierServiceImpl.Treatement(fic, i, dropdown, parameters, idOpe, r));
                            }
                            service.shutdown();
                            try {
                                service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

                            } catch (InterruptedException e) {
                                System.out.println("service did not terminate");
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println("An error occured during the treatment " + e.getLocalizedMessage());
                        }
                        liveReportingServices.endGobalReportingToTheVue1(idOpe, 1L);
                        System.out.println("end of process-----------");//
                    } while (!service.isShutdown());

                }
            ;
        } );
         } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.start();

        return "1";

    }
    @Autowired
    LiveReportingServiceS liveReportingServicef;
    @Autowired
    ReportFileRepository reportFileRepository;

    public class Treatement implements Runnable {

        GenererFichierForm fic;
        int i;
        Map<String, String> dropdown;
        Map<String, String> parameters;
        Long idOpe;
        Statement r;

        public Treatement(GenererFichierForm fic, int i, Map<String, String> dropdown, Map<String, String> parameters, Long idOpe, Statement r) {
            this.fic = fic;
            this.i = i;
            this.dropdown = dropdown;
            this.parameters = parameters;
            this.idOpe = idOpe;
            this.r = r;
        }

        @Override
        public void run() {
            generateFileThread(fic, i, dropdown);
        }

        private void generateFileThread(GenererFichierForm fic, int i, Map<String, String> dropdown) throws NumberFormatException {
            String fileName = "";
            int CONST = 0;
            int defineFileToSave = 0;
            Long count = Long.valueOf(0);
            Map<String, String> typefile = new HashMap<String, String>();
            try {
                typefile = reportCalculateService.getType12(fic.getCodeFichier().get(i).getCode());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                fileName = getYearMonth2(fic.getDate()) + fic.getCodeFichier().get(i).getCode()
                        + parameters.get("idetab") + "." + parameters.get("extention");
                System.out.println("FILE NAME " + fileName);
            } catch (ParseException ex) {
                System.out.println("Date (yyyy-MM-dd) Could not be formated " + fic.getDate());
            }
            if (parameters.get("typeReporting").equalsIgnoreCase("2")) {

                if (typefile.get("result").equals("calculate")) {
                    calculation(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count);
                } else if (typefile.get("result").equals("duplicateNoPost")) {
                    duplicatePostFile(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count);
                } else if (typefile.get("result").equals("duplicate")) {
                    duplicateFile(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count);
                } else if (typefile.get("result").equals("sql")) {
                    System.out.println("file is :" + fileName);
                    if (typefile.get("directGenerate") != null && typefile.get("directGenerate").equalsIgnoreCase("1")) {
                        fileName = sqlTypeFileDirect(fic, i, defineFileToSave, fileName, typefile, count);
                    } else {
                        fileName = sqlTypeFile(fic, i, defineFileToSave, fileName, typefile, count);
                    }
                }
                System.out.println("file is :" + fileName);
            } else if (parameters.get("typeReporting").equalsIgnoreCase("1")) {
                ManageExcelFiles se = new ManageExcelFiles();
                String file = "";
                try {
                    file = se.sesamGeneration(fic.getCodeFichier().get(i).getCode(), r, fileName);

                } catch (Exception ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (typefile.get("result").equals("calculate")) {
                    calculationSesame(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count, file);
                } else if (typefile.get("result").equals("duplicateNoPost")) {
                    duplicatePostFileSesame(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count, file);
                } else if (typefile.get("result").equals("duplicate")) {
                    duplicateFileSesame(fic, i, CONST, defineFileToSave, fileName, typefile, dropdown, count, file);
                } else if (typefile.get("result").equals("sql")) {
                    sqlSesame(fic, i, defineFileToSave, fileName, typefile, count, file);
                }
            }
            GenFile fu;
            try {
                fu = genFileRepository.findOne(new GenFileId(fic.getCodeFichier().get(i).getCode(), parameters.get("idetab"), new SimpleDateFormat("yyyy-MM-dd").parse(fic.getDate())));
                if (fu == null) {
                    genFileRepository.save(new GenFile(fic.getCodeFichier().get(i).getCode(), fileName, new Date(), new Date(), parameters.get("idetab"), new SimpleDateFormat("yyyy-MM-dd").parse(fic.getDate())));
                } else {
                    fu.setDatemodif(new Date());
                    fu.setCode(fileName);
                    genFileRepository.save(fu);
                }
            } catch (ParseException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private String sqlTypeFile(GenererFichierForm fic1, int i1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Long count1) throws NumberFormatException {
            int CONST;
            String lineFile;
            String codeFichier;
            Long statut;
            BufferedWriter writer;
            List<SqlFileType> reportDatasGenerated3 = sqlFileTypeRepository.findSqlFileTypeByDarAndFichi(fic1.getDate(), fic1.getCodeFichier().get(i1).getCode());

            if (reportDatasGenerated3.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(reportDatasGenerated3.size()));
                int Friqunce = (int) Math.ceil(reportDatasGenerated3.size() / 100.0);
                CONST = 0;
                try {
                    FileOutputStream fileOutputStream = null;
                    for (int j = 0; j < reportDatasGenerated3.size(); j++) {
                        if (defineFileToSave1 == 0) {
                            try {
                                fileName1 = getYearMonth2(fic1.getDate()) + reportDatasGenerated3.get(j).getFichi() + parameters.get("idetab") + "." + parameters.get("extention");
                                System.out.println("FILE NAME " + fileName1);
                            } catch (ParseException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            Path e = null;//
                            try {
                                e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                            } catch (FileAlreadyExistsException ey) {
                                e = Paths.get(parameters.get("chemin") + fileName1);
                            }
                            File file = new File(parameters.get("chemin") + fileName1);
                            fileOutputStream = new FileOutputStream(file);
//                            writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                            defineFileToSave1 = 1;
                            String ActualDate = "";
                            ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                            lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + reportDatasGenerated3.get(j).getFichi();
                            //                            writer.write(lineFile);
                            fileOutputStream.write(lineFile.getBytes(StandardCharsets.UTF_8));
                            fileOutputStream.write(10);
                            //                            writer.newLine();
                            lineFile = "";
                        }
                        codeFichier = fic1.getCodeFichier().get(i1).getCode();
                        int t = Integer.parseInt(typefile.get("size"));
                        lineFile = "";
                        for (int p = 1; p <= t; p++) {
                            lineFile += (reportDatasGenerated3.get(j).cellExtra(p) == null ? "" : reportDatasGenerated3.get(j).cellExtra(p)) + (p == t ? "" : ";");
                        }
                        fileOutputStream.write(lineFile.getBytes(StandardCharsets.UTF_8));
                        fileOutputStream.write(10);
//                        try {
//                            writer.write(lineFile);
//                        } catch (IOException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        try {
//                            writer.newLine();
//                        } catch (IOException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        if (j % 10000 == 0) {
                            System.out.println("the is no change " + count1);
                            fileOutputStream.flush();
                        }
//                            Double s = Math.ceil(reportDatasGenerated3.size() / 100.0);
//                            liveReportingServices.detailsReportingToTheVue2(fic.getCodeUnique(), fic.getCodeFichier().get(i).getCode(), s.longValue());
                        if (j % Friqunce == 0) {
                            liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                        }
                        CONST = 0;
                        lineFile = "";
                    }
                    defineFileToSave1 = 0;
                    fileOutputStream.close();
                    statut = Long.valueOf(1);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(reportDatasGenerated3.size()), new Long(reportDatasGenerated3.size()));
                    nombreOpeTraite++;
                    quotien = Long.valueOf(1);
                    count1 = Long.valueOf(0);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                Path e = null;//
                try {
                    try {
                        e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                    } catch (FileAlreadyExistsException ey) {
                        e = Paths.get(parameters.get("chemin") + fileName1);
                    }
                    writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                    defineFileToSave1 = 0;
                    String ActualDate = "";
                    ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                    lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                            + parameters.get("codePays") + parameters.get("delimiteur")
                            + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                            + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                    writer.write(lineFile);
                    writer.close();
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                } catch (Exception ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return fileName1;
        }

        @Transactional
        public Integer countQuery(String sql, String se, Statement r) throws SQLException, ClassNotFoundException {

            ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
            ResultSet rs1 = null;
            Statement rtt = null;
            int o = 0;
            Long total = 0L;
            List<String[]> list1 = new ArrayList<String[]>();
            while (rsp.next()) {
                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
                String v = new String(decoder);
                Class.forName(rsp.getString("lib2"));
                rtt = DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
                rs1 = rtt.executeQuery("select count(*) r from (" + sql + ")");
                rs1.next();
                return Integer.parseInt(rs1.getString("r"));
            }
            return 0;
        }

        @Transactional
        public Statement DataDirect(String se, Statement r) throws SQLException, ClassNotFoundException {
            ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
            ResultSet rs1 = null;
            Statement rtt = null;
            int o = 0;
            Long total = 0L;
            List<String[]> list1 = new ArrayList<String[]>();
            while (rsp.next()) {
                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
                String v = new String(decoder);
                Class.forName(rsp.getString("lib2"));
                return DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
//                return rtt.executeQuery(sql);

            }
            return null;
        }

        private String sqlTypeFileDirect1(GenererFichierForm fic1, int i1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Long count1) throws NumberFormatException {
            String lineFile;
            BufferedWriter writer;
            String query = "";
            Integer countQuery = 0;
            ResultSet datao = null;
            try {
                ResultSet yy = r.executeQuery("select lib5,lib4 from sanm where tabcd = '3009' and dele=0 and lib2 = '" + fic1.getCodeFichier().get(i1).getCode() + "'");
                yy.next();
                String ppep = yy.getString("lib4");
                query = reportCalculateService.insertPeriodsValiables(yy.getString("lib5").replaceAll("//dar//", "to_date('" + fic1.getDate() + "','yyyy-mm-dd')"), r);
                countQuery = 1800000;//countQuery(query, ppep, r);
                System.out.println("total :" + countQuery);
                int oo = 150000;
                if (countQuery > 0) {
                    int tt = (countQuery / oo) + 1;
                    System.out.println("segment number :" + tt);
                    Statement g = r;
                    String ruu = query;
                    ExecutorService service = Executors.newFixedThreadPool(10);//eg 15 dat and divid 4 
                    liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(countQuery));
                    for (int uu = 0; uu < tt; uu++) {
                        int y = uu;
                        System.out.println("run :" + uu);
                        service.execute(new Runnable() {
                            public void run() {
                                System.out.println("process : " + y);
                            }
                        });
                        liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(countQuery), new Long(countQuery));
                        service.shutdown();

                        try {
                            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            System.out.println("Yann service did not terminate");
                            e.printStackTrace();
                        }

                    }
                } else {
                    liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                    Path e = null;
                    try {
                        try {
                            e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                        } catch (FileAlreadyExistsException ey) {
                            e = Paths.get(parameters.get("chemin") + fileName1);
                        }
                        writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                        defineFileToSave1 = 0;
                        String ActualDate = "";
                        ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                        lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                + parameters.get("codePays") + parameters.get("delimiteur")
                                + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                        writer.write(lineFile);
                        writer.close();
                        liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                    } catch (Exception ex) {
                        Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (countQuery > 0) {
                } else {

                }
            } catch (SQLException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return fileName;
        }

        private String sqlTypeFileDirect(GenererFichierForm fic1, int i1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Long count1) throws NumberFormatException {
            String lineFile;
            BufferedWriter writer;
            String query = "";
            Integer countQuery = 0;
            ResultSet datao = null;
            try {
                ResultSet yy = r.executeQuery("select lib5,lib4 from sanm where tabcd = '3009' and dele=0 and lib2 = '" + fic1.getCodeFichier().get(i1).getCode() + "'");
                yy.next();
                String ppep = yy.getString("lib4");
                query = reportCalculateService.insertPeriodsValiables(yy.getString("lib5").replaceAll("//dar//", "to_date('" + fic1.getDate() + "','yyyy-mm-dd')"), r);
                countQuery = countQuery(query, ppep, r);
                System.out.println("number :" + countQuery);
                datao = DataDirect(ppep, r).executeQuery(query);
//                while(datao.next()){
//                System.out.println("number :y -----------");
//                }

                if (countQuery > 0) {
                    liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(countQuery));
                    int Friqunce = (int) Math.ceil(countQuery / 100.0);
                    CONST = 0;
                    try {
                        FileOutputStream fileOutputStream = null;
                        int j = 0;
                        while (datao.next()) {
                            j++;
                            if (defineFileToSave1 == 0) {
                                try {
                                    fileName1 = getYearMonth2(fic1.getDate()) + fic1.getCodeFichier().get(i1).getCode() + parameters.get("idetab") + "." + parameters.get("extention");
                                    System.out.println("FILE NAME " + fileName1);
                                } catch (ParseException ex) {
                                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                Path e = null;
                                try {
                                    e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                                } catch (FileAlreadyExistsException ey) {
                                    e = Paths.get(parameters.get("chemin") + fileName1);
                                }
                                File file = new File(parameters.get("chemin") + fileName1);
                                fileOutputStream = new FileOutputStream(file);
//                            writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                                defineFileToSave1 = 1;
                                String ActualDate = "";
                                ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                                lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                        + parameters.get("codePays") + parameters.get("delimiteur")
                                        + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                        + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                                //                            writer.write(lineFile);
                                fileOutputStream.write(lineFile.getBytes(StandardCharsets.UTF_8));
                                fileOutputStream.write(10);
                                //                            writer.newLine();
                                lineFile = "";
                            }
                            codeFichier = fic1.getCodeFichier().get(i1).getCode();
                            int t = Integer.parseInt(typefile.get("size"));
                            lineFile = "";
                            for (int p = 1; p <= t; p++) {
                                lineFile += ((datao.getString(p) == null ? "" : datao.getString(p).trim())) + (p == t ? "" : ";");
                            }
                            fileOutputStream.write(lineFile.getBytes(StandardCharsets.UTF_8));
                            fileOutputStream.write(10);
//                        try {
//                            writer.write(lineFile);
//                        } catch (IOException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        try {
//                            writer.newLine();
//                        } catch (IOException ex) {
//                            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                            if (j % 10000 == 0) {
                                System.out.println("the is no change " + j);
                                fileOutputStream.flush();
                            }
//                            Double s = Math.ceil(reportDatasGenerated3.size() / 100.0);
//                            liveReportingServices.detailsReportingToTheVue2(fic.getCodeUnique(), fic.getCodeFichier().get(i).getCode(), s.longValue());
                            if (j % Friqunce == 0) {
                                liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                            }
                            CONST = 0;
                            lineFile = "";
                        }
                        defineFileToSave1 = 0;
                        fileOutputStream.close();
                        statut = Long.valueOf(1);
                        liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(countQuery), new Long(countQuery));
                        nombreOpeTraite++;
                        quotien = Long.valueOf(1);
                        count1 = Long.valueOf(0);
                    } catch (IOException ex) {
                        Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                    Path e = null;//
                    try {
                        try {
                            e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                        } catch (FileAlreadyExistsException ey) {
                            e = Paths.get(parameters.get("chemin") + fileName1);
                        }
                        writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                        defineFileToSave1 = 0;
                        String ActualDate = "";
                        ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                        lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                + parameters.get("codePays") + parameters.get("delimiteur")
                                + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                        writer.write(lineFile);
                        writer.close();
                        liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                    } catch (Exception ex) {
                        Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return fileName;
        }

        private void duplicateFile(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1) throws NumberFormatException {
            BufferedWriter writer = null;
            String lineFile = "";
            String codeFichier;
            Long statut;
            List<ReportRep> report = reportRepRepository.findFichierDar(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                int Friqunce = (int) Math.ceil(report.size() / 100.0);
                CONST1 = 0;
                try {
                    for (int j = 0; j < report.size(); j++) {
                        if (defineFileToSave1 == 0) {
                            Path e = null;//
                            try {
                                e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                            } catch (FileAlreadyExistsException ey) {
                                e = Paths.get(parameters.get("chemin") + fileName1);
                            }
                            writer = Files.newBufferedWriter(e, StandardCharsets.UTF_8);
                            defineFileToSave1 = 1;
                            String ActualDate = "";
                            ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                            lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + report.get(j).getFichier();
                            writer.write(lineFile);
                            writer.newLine();
                            lineFile = "";
                        }
                        codeFichier = fic1.getCodeFichier().get(i1).getCode();
                        if (CONST1 == 0 && (typefile.get("result").equals("calculate") || typefile.get("result").equals("duplicateNoPost"))) {
                            lineFile += report.get(j).getPost() + ";";
                        }
                        if (CONST1 < Integer.parseInt(dropdown1.get(report.get(j).getFichier())) - 1) {
                            if (report.get(j).getValc() != null) {
                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValc() + ";";

                                }
                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm())) + ";";

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getVald() + ";";

                                }

                            } else if (report.get(j).getValt() != null) {
                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValt() + ";";

                                }

                            } else {

                                lineFile += ";";

                            }
                            CONST1++;
                        } else {
                            //last like which does not have comer

                            if (report.get(j).getValc() != null) {

                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValc();

                                }

                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm()));

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getVald();

                                }

                            } else if (report.get(j).getValt() != null) {

                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValt();

                                }

                            } else {

                                System.out.println("TYPE ERROR .........");
                                lineFile += "";

                            }
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            count1++;
//                                Double s = Math.ceil(report.size() / 100.0);
//                                liveReportingServices.detailsReportingToTheVue2(fic.getCodeUnique(), fic.getCodeFichier().get(i).getCode(), s.longValue());
                            if (j % Friqunce == 0) {
                                liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                            }
                            CONST1 = 0;
                            lineFile = "";
                        }
                    }
                    defineFileToSave1 = 0;
                    writer.close();
                    defineFileToSave1 = 0;
                    statut = Long.valueOf(1);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));
                    nombreOpeTraite++;
                    quotien = Long.valueOf(1);
                    count1 = Long.valueOf(0);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                Path e = null;//
                try {
                    try {
                        e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                    } catch (FileAlreadyExistsException ey) {
                        e = Paths.get(parameters.get("chemin") + fileName1);
                    }
                    writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                    defineFileToSave1 = 0;
                    String ActualDate = "";
                    ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                    lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                            + parameters.get("codePays") + parameters.get("delimiteur")
                            + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                            + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                    //                    System.out.println("the file name is :");
                    writer.write(lineFile);
                    writer.close();
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        private void duplicatePostFile(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1) throws NumberFormatException {
            BufferedWriter writer = null;
            String lineFile = "";
            String codeFichier;
            Long statut;
            System.out.println("File " + fic1.getCodeFichier().get(i1).getCode() + " entering duplicateNoPost");
            List<ReportRep> report = reportRepRepository.preparedResult12(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                int Friqunce = (int) Math.ceil(report.size() / 100.0);
                CONST1 = 0;
                try {
                    for (int j = 0; j < report.size(); j++) {
                        if (defineFileToSave1 == 0) {
                            Path e = null;//
                            try {
                                e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                            } catch (FileAlreadyExistsException ey) {
                                e = Paths.get(parameters.get("chemin") + fileName1);
                            }
                            writer = Files.newBufferedWriter(e, StandardCharsets.UTF_8);
                            defineFileToSave1 = 1;
                            String ActualDate = "";
                            ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                            lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + report.get(j).getFichier();
                            writer.write(lineFile);
                            writer.newLine();
                            lineFile = "";
                        }
                        codeFichier = fic1.getCodeFichier().get(i1).getCode();
                        if (CONST1 == 0 && (typefile.get("result").equals("calculate") || typefile.get("result").equals("duplicateNoPost"))) {
                            lineFile += report.get(j).getPost() + ";";
                        }
                        if (CONST1 < Integer.parseInt(dropdown1.get(report.get(j).getFichier())) - (InputWriteRepository.existsByFichi(report.get(j).getFichier()) ? 2 : 1)) {
                            if (report.get(j).getValc() != null) {
                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValc() + ";";

                                }
                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm())) + ";";

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getVald() + ";";

                                }

                            } else if (report.get(j).getValt() != null) {
                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValt() + ";";

                                }

                            } else {

                                lineFile += ";";

                            }
                            CONST1++;
                        } else {
                            //last like which does not have comer
                            System.out.println("verify last : " + report.get(j).getCol());
                            if (report.get(j).getValc() != null) {

                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValc();

                                }

                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
//	                            	lineFile+=reportDatasGenerated2.get(j).getValm();
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm()));

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getVald();

                                }

                            } else if (report.get(j).getValt() != null) {

                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValt();

                                }

                            } else {

                                System.out.println("TYPE ERROR .........");

                            }
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            count1++;
//                                Double s = Math.ceil(report.size() / 100.0);
//                                liveReportingServices.detailsReportingToTheVue2(fic.getCodeUnique(), fic.getCodeFichier().get(i).getCode(), s.longValue());
                            if (j % Friqunce == 0) {
                                liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                            }
                            CONST1 = 0;
                            lineFile = "";
                        }
                    }
                    defineFileToSave1 = 0;
                    writer.close();
                    defineFileToSave1 = 0;
                    statut = Long.valueOf(1);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                Path e = null;//
                try {
                    try {
                        e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                    } catch (FileAlreadyExistsException ey) {
                        e = Paths.get(parameters.get("chemin") + fileName1);
                    }
                    writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                    defineFileToSave1 = 0;
                    String ActualDate = "";
                    ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                    lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                            + parameters.get("codePays") + parameters.get("delimiteur")
                            + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                            + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                    System.out.println("the file name is :");
                    writer.write(lineFile);
                    writer.close();
                    System.out.println("no data Collected for :" + fic1.getCodeFichier().get(i1).getCode());
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                } catch (Exception ex) {
                    System.out.println("it was not property closed : " + ex.getLocalizedMessage());
                }
            }
        }

        private void calculation(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1) throws NumberFormatException {
            BufferedWriter writer = null;
            String lineFile = "";
            String codeFichier;
            Long statut;
            System.out.println("File " + fic1.getCodeFichier().get(i1).getCode() + " entering Calculation Type generation");
            List<ReportRep> report = reportRepRepository.preparedResult12(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            int Friqunce = (int) Math.ceil(report.size() / 100.0);
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                CONST1 = 0;
                try {
                    for (int j = 0; j < report.size(); j++) {
                        if (defineFileToSave1 == 0) {
                            Path e = null;//
                            try {
                                e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                            } catch (FileAlreadyExistsException ey) {
                                e = Paths.get(parameters.get("chemin") + fileName1);
                            }
                            writer = Files.newBufferedWriter(e, StandardCharsets.UTF_8);
                            defineFileToSave1 = 1;
                            String ActualDate = "";
                            ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                            lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                                    + parameters.get("codePays") + parameters.get("delimiteur")
                                    + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                    + parameters.get("delimiteur") + report.get(j).getFichier();
                            writer.write(lineFile);
                            writer.newLine();
                            lineFile = "";
                        }
                        codeFichier = fic1.getCodeFichier().get(i1).getCode();
                        if (CONST1 == 0 && (typefile.get("result").equals("calculate") || typefile.get("result").equals("duplicateNoPost"))) {
                            lineFile += report.get(j).getPost() + ";";
                        }
                        if (CONST1 < Integer.parseInt(dropdown1.get(report.get(j).getFichier())) - 2) {
                            if (report.get(j).getValc() != null) {
                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValc() + ";";

                                }
                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm())) + ";";

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getVald() + ";";

                                }

                            } else if (report.get(j).getValt() != null) {
                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += ";";

                                } else {
                                    lineFile += report.get(j).getValt() + ";";

                                }

                            } else {

                                lineFile += ";";

                            }
                            CONST1++;
                        } else {
                            //last like which does not have comer

                            if (report.get(j).getValc() != null) {

                                if (report.get(j).getValc()
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValc();

                                }

                            } else if (report.get(j).getValm() != null) {

                                if (String.valueOf(report.get(j).getValm())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += Math.abs((int) Math.round(report.get(j).getValm()));

                                }

                            } else if (report.get(j).getVald() != null) {

                                if (String.valueOf(report.get(j).getVald())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getVald();

                                }

                            } else if (report.get(j).getValt() != null) {

                                if (String.valueOf(report.get(j).getValt())
                                        .equalsIgnoreCase("***")) {

                                    lineFile += "";

                                } else {
                                    lineFile += report.get(j).getValt();

                                }

                            } else {

                                System.out.println("TYPE ERROR .........");

                            }
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null,
                                        ex);
                            }
                            count1++;
//                                Double s = Math.ceil(report.size() / 100.0);
//                                liveReportingServices.detailsReportingToTheVue2(fic.getCodeUnique(), fic.getCodeFichier().get(i).getCode(), s.longValue());
                            if (j % Friqunce == 0) {
                                liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                            }
                            CONST1 = 0;
                            lineFile = "";
                        }
                    }
                    defineFileToSave1 = 0;
                    writer.close();
                    defineFileToSave1 = 0;
                    statut = Long.valueOf(1);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));
                    nombreOpeTraite++;
                    quotien = Long.valueOf(1);
                    count1 = Long.valueOf(0);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("no data Collected for :" + fic1.getCodeFichier().get(i1).getCode());
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), 1L);
                Path e = null;//
                try {
                    try {
                        e = Files.createFile(Paths.get(parameters.get("chemin") + fileName1));
                    } catch (FileAlreadyExistsException ey) {
                        e = Paths.get(parameters.get("chemin") + fileName1);
                    }
                    writer = Files.newBufferedWriter(e, StandardCharsets.ISO_8859_1);
                    defineFileToSave1 = 0;
                    String ActualDate = "";
                    ActualDate = getDateAtTheGoodformat1(fic1.getDate());
                    lineFile = parameters.get("idetab") + parameters.get("delimiteur")
                            + parameters.get("codePays") + parameters.get("delimiteur")
                            + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                            + parameters.get("delimiteur") + fic1.getCodeFichier().get(i1).getCode();
                    writer.write(lineFile);
                    writer.close();
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, 1L, 1L);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        private void calculationSesame(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1, String file1) throws NumberFormatException {
            BufferedWriter writer = null;
            List<ReportFile> report = reportFileRepository.preparedResultSesame(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            int Friqunce = (int) Math.ceil(report.size() / 100.0);
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                FileInputStream fileI;
                System.out.println("file heer :" + file1);
                try {
                    fileI = new FileInputStream(file1);
                    Workbook workbookF = WorkbookFactory.create(fileI);
                    Sheet sheet = workbookF.getSheetAt(0);
                    int j = 0;
                    for (ReportFile o : report) {
                        //x = t, y = 
                        if (o.getGen() != null) {
                            Cell cell = sheet
                                    .getRow(o.getY().intValue() - 1)
                                    .getCell(o.getX().intValue() - 1);
                            cell.setCellValue(Integer.parseInt(o.getGen().equalsIgnoreCase("999999999999999") ? "0" : o.getGen()));
                        }
                        if (j++ % Friqunce == 0) {
                            liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                        }
                    }
                    HSSFFormulaEvaluator.evaluateAllFormulaCells(workbookF);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));

                    FileOutputStream fileO = new FileOutputStream(file1);
                    workbookF.write(fileO);
                    fileO.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncryptedDocumentException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void duplicatePostFileSesame(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1, String file1) throws NumberFormatException {
            BufferedWriter writer = null;
            List<ReportFile> report = reportFileRepository.preparedFichSesame(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            int Friqunce = (int) Math.ceil(report.size() / 100.0);
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                FileInputStream fileI;
                System.out.println("file heer :" + file1);
                try {
                    fileI = new FileInputStream(file1);
                    Workbook workbookF = WorkbookFactory.create(fileI);
                    Sheet sheet = workbookF.getSheetAt(0);
                    int j = 0;
                    for (ReportFile o : report) {
                        System.out.println(o.getY() + " : " + o.getX() + " : " + o.getGen());
                        Cell cell = sheet
                                .getRow(o.getY().intValue() - 1)
                                .getCell(o.getX().intValue() - 1);
                        cell.setCellValue(o.getGen() == null ? "" : o.getGen());
                        if (j++ % Friqunce == 0) {
                            liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                        }
                    }
                    HSSFFormulaEvaluator.evaluateAllFormulaCells(workbookF);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));

                    FileOutputStream fileO = new FileOutputStream(file1);
                    workbookF.write(fileO);
                    fileO.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncryptedDocumentException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void sqlSesame(GenererFichierForm fic1, int i1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Long count1, String file1) throws NumberFormatException {
            BufferedWriter writer = null;
            List<SqlFileType> report = sqlFileTypeRepository.findSqlFileTypeByDarAndFichi(fic1.getDate(), fic1.getCodeFichier().get(i1).getCode());
            int Friqunce = (int) Math.ceil(report.size() / 100.0);
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                FileInputStream fileI;
                System.out.println("file heer :" + file1);
                try {
                    fileI = new FileInputStream(file1);
                    Workbook workbookF = WorkbookFactory.create(fileI);
                    Sheet sheet = workbookF.getSheetAt(0);
                    int j = 0;
                    int lineStart = 0;
                    int colStart = 0;
                    int ncol = 0;
                    try {
                        ResultSet r = connectDB().createStatement().executeQuery("select mnt1,mnt2,taux4 from sanm where lib2 = '" + fic1.getCodeFichier().get(i1).getCode() + "'");
                        r.next();
                        lineStart = r.getInt("mnt1");
                        colStart = r.getInt("mnt2");
                        ncol = r.getInt("taux4");
                    } catch (Exception r) {
                        lineStart = 0;
                        colStart = 0;
                    }
                    int rown = lineStart - 1;
                    for (SqlFileType o : report) {
                        //x = t, y = 
                        for (int y = 1; y <= ncol; y++) {
                            try {
                                Cell cell = sheet
                                        .getRow(rown)
                                        .getCell(colStart + y - 1);
                                cell.setCellValue(o.cellExtra(y) == null ? "" : o.cellExtra(y));
                            } catch (Exception t) {
                                System.out.println("return error y : " + y + "  and i:" + o.getId());
                            }
                        }
                        rown++;
                        if (j++ % Friqunce == 0) {
                            liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                        }
                    }
                    HSSFFormulaEvaluator.evaluateAllFormulaCells(workbookF);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));

                    FileOutputStream fileO = new FileOutputStream(file1);
                    workbookF.write(fileO);
                    fileO.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncryptedDocumentException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void duplicateFileSesame(GenererFichierForm fic1, int i1, int CONST1, int defineFileToSave1, String fileName1, Map<String, String> typefile, Map<String, String> dropdown1, Long count1, String file1) throws NumberFormatException {
            BufferedWriter writer = null;
            List<ReportRep> report = reportRepRepository.findFichierDar(fic1.getCodeFichier().get(i1).getCode(), fic1.getDate());
            int Friqunce = (int) Math.ceil(report.size() / 100.0);
            if (report.size() > 0) {
                liveReportingServicef.beginDetailsReportingToTheVue2(idOpe, fic1.getCodeFichier().get(i1).getCode(), new Long(report.size()));
                try {
                    FileInputStream fileI;
                    fileI = new FileInputStream(file1);
                    Workbook workbookF = WorkbookFactory.create(fileI);
                    Sheet sheet = workbookF.getSheetAt(0);
                    int j = 0;
                    int lineStart = 0;
                    int colStart = 0;
                    try {
                        ResultSet r = connectDB().createStatement().executeQuery("select mnt1,mnt2 from sanm where lib2 = '" + fic1.getCodeFichier().get(i1).getCode() + "'");
                        r.next();
                        lineStart = r.getInt("mnt1");
                        colStart = r.getInt("mnt2");
                    } catch (Exception r) {
                        lineStart = 0;
                        colStart = 0;
                    }
                    int lineincr = lineStart;
                    int r = 0;//Integer.parseInt(dropdown1.get(report.get(j).getFichier())) - 1;

                    for (ReportRep o : report) {
                        //x = t, y = 
                        //verif
                        if (r > Integer.parseInt(o.getCol())) {
                            lineincr++;
                            r = 0;
                        }
                        //then take r
                        r = Integer.parseInt(o.getCol());
                        Cell cell = sheet
                                .getRow(lineincr - 1)
                                .getCell(colStart + Integer.parseInt(o.getCol()) - 1);
                        try {
                            cell.setCellValue(o.getValc() == null ? "" : o.getValc());
                        } catch (Exception re) {
                            System.out.println("valc : " + o.getValc() + " ,line :" + lineincr + " ,column :" + (colStart + Integer.parseInt(o.getCol())));
                        }
                        if (j++ % Friqunce == 0) {
                            liveReportingServices.detailsReportingToTheVue3(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), new Long(Friqunce));
                        }
                    }
                    HSSFFormulaEvaluator.evaluateAllFormulaCells(workbookF);
                    liveReportingServices.endDetailsReportingToTheVue2(fic1.getCodeUnique(), fic1.getCodeFichier().get(i1).getCode(), 1L, new Long(report.size()), new Long(report.size()));

                    FileOutputStream fileO = new FileOutputStream(file1);
                    workbookF.write(fileO);
                    fileO.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncryptedDocumentException ex) {
                    Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static String randoms() {

        Date date = new Date();
        long current = date.getTime();
        Random rand = new Random();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String tezst = "";
        int longueur = alphabet.length();
        for (int i = 0; i < 9; i++) {
            int k = rand.nextInt(longueur);
            tezst = tezst + alphabet.charAt(k);
        }
        String code = current + "IWARES" + tezst;
        return code;

    }

    public Date getYearMonth() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        return date;

    }

    public Date getYearMonthfrom() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        return date;

    }

    public String getYearMonth2(String d) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
        String newstring = new SimpleDateFormat("yyyyMM").format(date);
        return newstring;

    }

    public String getYearMonth2(Date d) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));

        System.out.println("AFETR PREPARING DATE FORMAT");

        // Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(d);
        String newstring = new SimpleDateFormat("yyyyMM").format(date);

        System.out.println("AFETR PARSING DAYE TO STRING");

        // DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        // Date date = new Date();
        return newstring;

    }

    public String getDateAtTheGoodformat(String d, String format) throws ParseException {

        System.out.println("before parsing date");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int century = (calendar.get(Calendar.YEAR) / 100) + 1;
        System.out.println("After century parsing value");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        DateFormat dateFormat2 = new SimpleDateFormat("YY");
        System.out.println("dateFormat1" + dateFormat.format(date));
        System.out.println("dateFormat2" + dateFormat2.format(date)); // retourne la date dans le format jj/MM/SSAA
        System.out.println(
                "dateFormat3" + dateFormat.format(date) + "/" + String.valueOf(century) + dateFormat2.format(date)); // retourne
        // la
        // date
        // dans
        // le
        // format
        // jj/MM/SSAA
        return dateFormat.format(date) + "/" + String.valueOf(century) + dateFormat2.format(date);

    }

    public String getDateAtTheGoodformat1(String d) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public String getDateAtTheGoodformat(Date d, String format) throws ParseException {

        System.out.println("before parsing date");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int century = (calendar.get(Calendar.YEAR) / 100) + 1;
        System.out.println("After century parsing value");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        DateFormat dateFormat2 = new SimpleDateFormat("YY");
        System.out.println("dateFormat1" + dateFormat.format(date));
        System.out.println("dateFormat2" + dateFormat2.format(date)); // retourne la date dans le format jj/MM/SSAA
        System.out.println(
                "dateFormat3" + dateFormat.format(date) + "/" + String.valueOf(century) + dateFormat2.format(date)); // retourne
        // la
        // date
        // dans
        // le
        // format
        // jj/MM/SSAA
        return dateFormat.format(date) + "/" + String.valueOf(century) + dateFormat2.format(date);

    }

    /*
	 * public static void main (String[] args) { // Date date = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(yourString);
	 * 
	 * // Calendar calendar = Calendar.getInstance(); // calendar.setTime(date);
	 * 
	 * // int century = (calendar.get(Calendar.YEAR) / 100) +1;
	 * 
	 * 
	 * 
	 * DateFormat dateFormat = new SimpleDateFormat("dd/MM"); DateFormat dateFormat2
	 * = new SimpleDateFormat("YY"); DateFormat dateFormat3 = new
	 * SimpleDateFormat("dd/MM/SSYY"); Date date = new Date();
	 * System.out.println(dateFormat.format(date));
	 * System.out.println(dateFormat2.format(date));
	 * System.out.println(dateFormat3.format(date)); }/* Timestamp timestamp = new
	 * Timestamp(System.currentTimeMillis()); System.out.println("START date");
	 * System.out.println(dateFormat.format(date));
	 * 
	 * System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
	 * System.out.println("START fic"); List<String> nums = new ArrayList<String>()
	 * {{ add("BAL"); add("SOLDE");}}; System.out.println("END fic");
	 * GenererFichierForm fic=new GenererFichierForm(date,"O","M",nums);
	 * DataIntegrationForm fic2=new DataIntegrationForm(date,"O","M",nums);
	 * GenererFichierServiceImpl chr=new GenererFichierServiceImpl();
	 * chr.genererFichiers(fic);
	 * 
	 * 
	 * }
     */
    public Consql getCon() {

        // String FILENAME = "/xch/IWOMICORE/con.txt";
        // String FILENAME = "/xch/IWOMI/conIWOMI.txt";
        String FILENAME = "c:\\xch\\iwomiCon.txt";
        Consql conn = new Consql();
        BufferedReader bufferedreader = null;
        FileReader filereader = null;
        try {
            filereader = new FileReader(FILENAME);
            bufferedreader = new BufferedReader(filereader);
            String strCurrentLine;
            int i = 0;
            while ((strCurrentLine = bufferedreader.readLine()) != null) {
                String[] tab = strCurrentLine.split("\\|", -1);
                if (i == 0) {
                    conn.setIP(tab[1]);
                }
                if (i == 1) {
                    conn.setUSER(tab[1]);
                }
                if (i == 2) {
                    conn.setPASS(tab[1]);
                }
                // if(i==3){ conn.setPORT(tab[1]);}
                i++;
                System.out.println("connexion" + tab[1]);
                // System.out.println(tab[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedreader != null) {
                    bufferedreader.close();
                }
                if (filereader != null) {
                    filereader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public Date getDa(String d) {
        System.out.println("DATE VALUE " + d);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(d);
        } catch (ParseException ex) {
            Logger.getLogger(GenererFichierServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DATE VALUE RETURNED " + date1);
        return date1;

    }

}

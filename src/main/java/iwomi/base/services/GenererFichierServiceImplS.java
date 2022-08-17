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
import iwomi.base.objects.SqlFileTypeS;
import iwomi.base.repositories.ReportDatasGeneratedRepository;
import iwomi.base.repositories.SqlFileTypeRepositoryS;

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

import iwomi.base.ServiceInterface.GenererFichierServicesS;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.ServiceInterface.ReportCalculateServiceS;
import iwomi.base.form.Consql;
import iwomi.base.form.GenererFichierForm;
import iwomi.base.form.GenererFichierFormS;
import iwomi.base.objects.ReportDatasGeneratedS;
import iwomi.base.repositories.ReportDatasGeneratedRepositoryS;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabri
 */
@Component
@Service
public class GenererFichierServiceImplS implements GenererFichierServicesS {

    Connection connection, con = null;
//    public static final String USERNAME="root";
//    public static final String PASSWORD="root";
    public static final String CONN_STRING = "jdbc:mysql://localhost/spectra"; // production
//    public static final String CONN_STRING="jdbc:mysql://localhost/spectra:3306?autoReconnect=true&useSSL=false";   //testing
    // LOCAL my machine
    public static final String USERNAME = "root";
//	public static final String PASSWORD = "root";
    public static final String PASSWORD = "root";

    String NOMENGABTABLE_SYS = "0012";
    String NOMMENCLATURE_FICH = "3020";
    Map<String, String> PARAM = new HashMap<>();
    @Autowired
    ReportDatasGeneratedRepositoryS reportDatasGeneratedRepository;
    List<ReportDatasGeneratedS> reportDatasGenerated2 = new ArrayList<ReportDatasGeneratedS>();
    List<SqlFileTypeS> reportDatasGenerated3 = new ArrayList<SqlFileTypeS>();
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
    LiveReportingServiceS liveReportingService;
    @Autowired
    private ReportCalculateServiceS reportCalculateService;
    @Autowired
    private SqlFileTypeRepositoryS sqlFileTypeRepository;

    public Map<String, String> getGenerationAndSavingParam()
            throws SQLException, ClassNotFoundException, JSONException {

        // Consql c= getCon();
//    public static final String USERNAME="root";
//    public static final String PASSWORD="";
//    public static final String CONN_STRING="jdbc:mysql://localhost:8888/spectra";  
//    
//        USERNAME=c.getUSER();
//        PASSWORD=c.getPASS();
//        CONN_STRING=c.getIP();  
        System.out.println("START GETTING PÄRAMS");
        String select = "";
        // JSONObject obj = new JSONObject();
        // boolean val = false;

        Class.forName("com.mysql.jdbc.Driver");
        try {
            System.out.println("BEFORE CONNEXION");
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("AFTER CONNEXION");
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMENGABTABLE_SYS + "' AND dele='0'";
        System.out.println("BEFORE QUERRY");
        ResultSet result = stmt.executeQuery(select);
        System.out.println("AFTER QUERRY");
        while (result.next()) {
            if (result.getString("acscd").equalsIgnoreCase("0009")) {
                PARAM.put("idetab", result.getString("lib2"));
            }
            if (result.getString("acscd").equalsIgnoreCase("0010")) {
                PARAM.put("extention", result.getString("lib2"));
            }

            if (result.getString("acscd").equalsIgnoreCase("0011")) {
                PARAM.put("ip", result.getString("lib2"));
                System.out.println("ip :" + result.getString("lib2"));
            }
            if (result.getString("acscd").equalsIgnoreCase("0012")) {
                PARAM.put("port", result.getString("lib2"));
                System.out.println("port :" + result.getString("lib2"));

            }
            if (result.getString("acscd").equalsIgnoreCase("0013")) {
                PARAM.put("pass", result.getString("lib2"));
                System.out.println("pass :" + result.getString("lib2"));

            }
            if (result.getString("acscd").equalsIgnoreCase("0014")) {
                PARAM.put("user", result.getString("lib2"));
                System.out.println("user :" + result.getString("lib2"));

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
                // webservice 2 il est sur un serveur distant.
            }
            if (result.getString("acscd").equalsIgnoreCase("0029")) {
                PARAM.put("minimumNumber", result.getString("lib2")); // 1 le fichier est stoqué sur le meme serveur que
                // le webservice 2 il est sur un serveur
                // distant.
            }
            /*
             * if(result.getString("acscd").equalsIgnoreCase("0022")){
             * PARAM.put("portOracle", result.getString("lib2")); }
             */
            System.out.println("END DISTRIBUTION");
        }
        System.out.println("END GETGENARATION AND SAVING PARAMS");

        return PARAM;

    }

    public Map<String, String> getFileLengthColumn() throws SQLException, ClassNotFoundException, JSONException {

        String select = "";
        JSONObject obj = new JSONObject();
        // boolean val = false;
        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMMENCLATURE_FICH + "' AND dele='0'";
        ResultSet result = stmt.executeQuery(select);
        while (result.next()) {
            if (result.getString("taux1").equalsIgnoreCase("0")) {
                PARAM.put(result.getString("lib2"), ((Integer.parseInt(result.getString("taux4"))) + 1) + "");
            } else {
                PARAM.put(result.getString("lib2"), result.getString("taux4"));
            }
        }
        return PARAM;

    }

    public String genererFichiersS(GenererFichierFormS fic) {

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
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        // acces au fichier pour ecriture
        BufferedWriter writer = null;
        JSch jsch2 = new JSch();
        try {
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            session.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            channel.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;

        try {

            System.out.println("Connection Opened\n" + parameters.get("chemin"));
            channelSftp.cd(parameters.get("chemin"));
        } catch (SftpException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            final File file = new File(fileName);
                            OutputStream out = null;
                            try {
                                out = channelSftp.put(parameters.get("chemin") + fileName);
                            } catch (SftpException ex) {
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

    }

    public String genererFichiersLocal(GenererFichierFormS fic) {

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
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dropdown = getFileLengthColumn();
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

    }

    public String genererFichiersP(GenererFichierFormS fic) {

        // recuperation des données a ecrire sur le fichier
        lineFile = "";
        lineFile1 = "";
        delimiteur = "";
        try {
            // recuperation des paramètres
            parameters = getGenerationAndSavingParam();
            delimiteur = parameters.get("delimiteur");
            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeFichier().size()));
            minimum = Long.valueOf(parameters.get("minimumNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("GET DATAS FROM DATABASE*****************");
        try {
            dropdown = getFileLengthColumn();
        } catch (SQLException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }

        // get file type
        try {
            reportCalculateService.connec();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // acces au fichier pour ecriture
        BufferedWriter writer = null;
        JSch jsch2 = new JSch();
        try {
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            session.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            channel.connect();
        } catch (JSchException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;
        try {
            System.out.println("Connection Opened\n" + parameters.get("chemin"));

            channelSftp.cd(parameters.get("chemin"));
        } catch (SftpException ex) {
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        int srre = 0;
        for (int i = 0; i < fic.getCodeFichier().size(); i++) {
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
            if (typefile.get("result").equals("calculate") || typefile.get("result").equals("duplicateNoPost")
                    || typefile.get("result").equals("duplicate")) {
                System.out.println("GET DATA FILE FROM THE DATABASE");
                System.out.println("iNSIDE FIRST date value" + fic.getDate());
                reportDatasGenerated2 = reportDatasGeneratedRepository
                        .findReportDatasGeneratedSByDarOrderByFichierAscRangAscColAsc(fic.getDate(),
                                fic.getCodeFichier().get(i).getCode(), 1);
                System.out.println("iNSIDE FIRST reportDatasGenerated2.size()" + reportDatasGenerated2.size());
                if (reportDatasGenerated2.size() > 0) {
                    CONST = 0;
                    try {
                        for (int j = 0; j < reportDatasGenerated2.size(); j++) {
                            System.out.println("UNSIDE SECOND FOREACH");
                            System.out.println("UNSIDE SECOND FOREACH code " + fic.getCodeFichier().get(i).getCode());
                            System.out.println(
                                    "UNSIDE SECOND reportDatasGenerated2 " + reportDatasGenerated2.get(j).getFichier());
                            // if(fic.getCodeFichier().get(i).getCode().equalsIgnoreCase(reportDatasGenerated2.get(j).getFichier())){
                            System.out.println("READYY TO WRITE");
                            // preparation du fichier
                            if (defineFileToSave == 0) {
                                try {
                                    System.out.println("FILE NAME " + getYearMonth2(fic.getDate())
                                            + reportDatasGenerated2.get(j).getFichier() + parameters.get("idetab") + "."
                                            + parameters.get("extention"));
                                    // structure du nom de fichier yyyyMM+typede
                                    // fichier(5positions)+idetab(5positions)+extension(TXT)
                                    fileName = getYearMonth2(fic.getDate()) + reportDatasGenerated2.get(j).getFichier()
                                            + parameters.get("idetab") + "." + parameters.get("extention");
                                } catch (ParseException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                OutputStream out = null;
                                try {
                                    out = channelSftp.put(parameters.get("chemin") + fileName);
                                } catch (SftpException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                writer = new BufferedWriter(new OutputStreamWriter(out));
                                defineFileToSave = 1;
                                idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                        fic.getCodeFichier().get(i).getCode(), Long.valueOf(nombreTotal / nbreLigne));
                                String ActualDate = "";
                                ActualDate = getDateAtTheGoodformat1(fic.getDate());
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

                                if (reportDatasGenerated2.get(j).getValc() != null) {
                                    if (reportDatasGenerated2.get(j).getValc()
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += ";";

                                    } else {
                                        if (typefile.get("result").equals("duplicateNoPost") && reportDatasGenerated2.get(j).getCol() == 2) {
                                            lineFile += reportDatasGenerated2.get(j).getPost() + ";";
                                        }
                                        lineFile += reportDatasGenerated2.get(j).getValc().replaceAll("[\\n\\t]", "") + ";";

                                    }
                                } else if (reportDatasGenerated2.get(j).getValm() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getValm())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += ";";

                                    } else {
                                        srre = (int) Math.round(reportDatasGenerated2.get(j).getValm());
                                        lineFile += (srre >= 0 ? srre : -srre) + ";";

                                    }

                                } else if (reportDatasGenerated2.get(j).getVald() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getVald())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += ";";

                                    } else {
                                        lineFile += reportDatasGenerated2.get(j).getVald() + ";";

                                    }

                                } else if (reportDatasGenerated2.get(j).getValt() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getValt())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += ";";

                                    } else {
                                        lineFile += reportDatasGenerated2.get(j).getValt() + ";";

                                    }

                                } else {

                                    System.out.println("TYPE ERROR .........");
 lineFile +=  ";";
                                }

                                CONST++;

                            } else {

                                if (reportDatasGenerated2.get(j).getValc() != null) {

                                    if (reportDatasGenerated2.get(j).getValc()
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += "";

                                    } else {
                                        if (typefile.get("result").equals("duplicateNoPost") && reportDatasGenerated2.get(j).getCol() == 2) {
                                            lineFile += reportDatasGenerated2.get(j).getPost() + ";";
                                        }
                                        lineFile += reportDatasGenerated2.get(j).getValc().replaceAll("[\\n\\t]", "");

                                    }

                                } else if (reportDatasGenerated2.get(j).getValm() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getValm())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += "";

                                    } else {
                                        srre = (int) Math.round(reportDatasGenerated2.get(j).getValm());
                                        lineFile += (srre >= 0 ? srre : -srre) + ";";
//                                        lineFile += (int) Math.round(reportDatasGenerated2.get(j).getValm());

                                    }

                                } else if (reportDatasGenerated2.get(j).getVald() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getVald())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += "";

                                    } else {
                                        lineFile += reportDatasGenerated2.get(j).getVald();

                                    }

                                } else if (reportDatasGenerated2.get(j).getValt() != null) {

                                    if (String.valueOf(reportDatasGenerated2.get(j).getValt())
                                            .equalsIgnoreCase("9.99999999999999E14")) {

                                        lineFile += "";

                                    } else {
                                        lineFile += reportDatasGenerated2.get(j).getValt();

                                    }

                                } else {

                                    System.out.println("TYPE ERROR .........");

                                }

                                try {
                                    writer.write(lineFile);
                                } catch (IOException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                try {
                                    writer.newLine();
                                } catch (IOException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                System.out.println("line" + j + "= :" + lineFile);
                                System.out.println("end of line" + j);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        Long.valueOf(nombreTotal / nbreLigne), Long.valueOf(count), quotien, minimum);
                                CONST = 0;
                                lineFile = "";
                            }
                        }
                        defineFileToSave = 0;
                        writer.close();
                        defineFileToSave = 0;
                        statut = Long.valueOf(1);
                        liveReportingService.endDetailsReportingToTheVue(idTrait, codeFichier, statut,
                                Long.valueOf(count));
                        System.out.println("AFTERENDING DETAILS" + idTrait);
                        nombreOpeTraite++;
                        quotien = Long.valueOf(1);
                        count = Long.valueOf(0);
                    } catch (IOException ex) {
                        Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                            fic.getCodeFichier().get(i).getCode(), Long.valueOf(0));
                    liveReportingService.endDetailsReportingToTheVue(idTrait, fic.getCodeFichier().get(i).getCode(),
                            Long.valueOf(1), Long.valueOf(0));
                }
            } else if (typefile.get("result").equals("sql")) {
                System.out.println("GET DATA FILE FROM THE DATABASE");
                System.out.println("iNSIDE FIRST date value" + fic.getDate());
                reportDatasGenerated3 = sqlFileTypeRepository.findSqlFileTypeSByDarAndFichi(fic.getDate(),
                        fic.getCodeFichier().get(i).getCode());
                System.out.println("iNSIDE FIRST reportDatasGenerated3.size()" + reportDatasGenerated3.size());
                if (reportDatasGenerated3.size() > 0) {
                    CONST = 0;
                    try {
                        for (int j = 0; j < reportDatasGenerated3.size(); j++) {
                            System.out.println("UNSIDE SECOND FOREACH");
                            System.out.println("UNSIDE SECOND FOREACH code " + fic.getCodeFichier().get(i).getCode());
                            System.out.println(
                                    "UNSIDE SECOND reportDatasGenerated3 " + reportDatasGenerated3.get(j).getFichi());
                            // if(fic.getCodeFichier().get(i).getCode().equalsIgnoreCase(reportDatasGenerated2.get(j).getFichier())){
                            System.out.println("READYY TO WRITE");
                            // preparation du fichier
                            if (defineFileToSave == 0) {
                                try {
                                    System.out.println("FILE NAME " + getYearMonth2(fic.getDate())
                                            + reportDatasGenerated3.get(j).getFichi() + parameters.get("idetab") + "."
                                            + parameters.get("extention"));
                                    // structure du nom de fichier yyyyMM+typede
                                    // fichier(5positions)+idetab(5positions)+extension(TXT)
                                    fileName = getYearMonth2(fic.getDate()) + reportDatasGenerated3.get(j).getFichi()
                                            + parameters.get("idetab") + "." + parameters.get("extention");
                                } catch (ParseException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                OutputStream out = null;
                                try {
                                    out = channelSftp.put(parameters.get("chemin") + fileName);
                                } catch (SftpException ex) {
                                    Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                writer = new BufferedWriter(new OutputStreamWriter(out));
                                defineFileToSave = 1;
                                idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                        fic.getCodeFichier().get(i).getCode(), Long.valueOf(nombreTotal / nbreLigne));
                                String ActualDate = "";
                                ActualDate = getDateAtTheGoodformat1(fic.getDate());
                                lineFile1 = parameters.get("idetab") + parameters.get("delimiteur")
                                        + parameters.get("codePays") + parameters.get("delimiteur")
                                        + parameters.get("status") + parameters.get("delimiteur") + ActualDate
                                        + parameters.get("delimiteur") + reportDatasGenerated3.get(j).getFichi();
                                writer.write(lineFile1);
                                writer.newLine();
                                lineFile1 = "";
                            }
                            codeFichier = fic.getCodeFichier().get(i).getCode();

                            // contruction de la ligne
                            int t = Integer.parseInt(typefile.get("size"));
                            System.out.println("the lenth is:" + t);
                            lineFile = "";
                            for (int p = 1; p <= t; p++) {
                                lineFile += reportDatasGenerated3.get(j).cellExtra(p) + (p == t ? "" : ";");
                            }
                            System.out.println(lineFile);
                            try {
                                writer.write(lineFile);
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("line" + j + "= :" + lineFile);
                            System.out.println("end of line" + j);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                    Long.valueOf(nombreTotal / nbreLigne), Long.valueOf(count), quotien, minimum);
                            CONST = 0;
                            lineFile = "";
                        }
                        defineFileToSave = 0;
                        writer.close();
                        defineFileToSave = 0;
                        statut = Long.valueOf(1);
                        liveReportingService.endDetailsReportingToTheVue(idTrait, codeFichier, statut,
                                Long.valueOf(count));
                        System.out.println("AFTERENDING DETAILS" + idTrait);
                        nombreOpeTraite++;
                        quotien = Long.valueOf(1);
                        count = Long.valueOf(0);
                    } catch (IOException ex) {
                        Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                            fic.getCodeFichier().get(i).getCode(), Long.valueOf(0));
                    liveReportingService.endDetailsReportingToTheVue(idTrait, fic.getCodeFichier().get(i).getCode(),
                            Long.valueOf(1), Long.valueOf(0));

                }
            }

        }
        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

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

        System.out.println("AFETR PREPARING DATE FORMAT");

        // Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(d);
        String newstring = new SimpleDateFormat("yyyyMM").format(date);

        System.out.println("AFETR PARSING DAYE TO STRING");

        // DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        // Date date = new Date();
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
        System.out.println("before parsing date");
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
            Logger.getLogger(GenererFichierServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DATE VALUE RETURNED " + date1);
        return date1;

    }

}

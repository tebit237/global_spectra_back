/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.ServiceInterface.ChargerDonneesService;

/**
 *
 * @author fabri
 */

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import iwomi.base.ServiceInterface.ChargerDonneesServiceLocal;
import iwomi.base.ServiceInterface.GenererFichierServices;
import iwomi.base.ServiceInterface.LiveReportingService;
import iwomi.base.form.ClientToSystemForm;
import iwomi.base.form.Consql;
import iwomi.base.form.DataIntegrationForm;
import iwomi.base.form.GenererFichierForm;
import iwomi.base.form.InventaireListForm;
import iwomi.base.form.LiveReportForm;
import iwomi.base.form.ReportResultFom;
import iwomi.base.form.invFileToIntegrate;
import iwomi.base.objects.InAutorisationDecouvert;
import iwomi.base.objects.InBalance;
import iwomi.base.objects.InCautions;
import iwomi.base.objects.InClients;
import iwomi.base.objects.InComptes;
import iwomi.base.objects.InGaranties;
import iwomi.base.objects.InOib;
import iwomi.base.objects.InRapartriements;
import iwomi.base.objects.InSoldes;
import iwomi.base.objects.Inbdc;
import iwomi.base.objects.Incredit;
import iwomi.base.objects.Indat;
import iwomi.base.objects.Inech;
import iwomi.base.objects.Intitre;
import iwomi.base.objects.Intran;
import iwomi.base.objects.Invent;
import iwomi.base.objects.Invgenr;
import iwomi.base.objects.LiveOperation;
import iwomi.base.objects.LiveTraitement;
import iwomi.base.objects.ReportDatasGenerated;
import iwomi.base.repositories.InAutorisationDecouvertRepository;
import iwomi.base.repositories.InBalanceRepository;
import iwomi.base.repositories.InCautionsRepository;
import iwomi.base.repositories.InClientsRepository;
import iwomi.base.repositories.InComptesRepository;
import iwomi.base.repositories.InGarantiesRepository;
import iwomi.base.repositories.InOibRepository;
import iwomi.base.repositories.InSoldesRepository;
import iwomi.base.repositories.InbdcRepository;
import iwomi.base.repositories.IncreditRepository;
import iwomi.base.repositories.IndatRepository;
import iwomi.base.repositories.InechRepository;
import iwomi.base.repositories.InrapatRepository;
import iwomi.base.repositories.IntitreRepository;
import iwomi.base.repositories.IntransRepository;
import iwomi.base.repositories.InvgenrRepository;
import iwomi.base.repositories.LiveOperationRepository;
import iwomi.base.repositories.LiveTraitementRepository;
import iwomi.base.repositories.ReportDatasGeneratedRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javassist.CtMethod.ConstParameter.integer;
import javax.transaction.Transactional;
//import static org.codehaus.groovy.syntax.Token.newString;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabri
 */
public class ChargerDonneesServiceImpl1 implements ChargerDonneesService {

    Connection connection, con = null;
    public static final String CONN_STRING = "jdbc:mysql://10.1.1.26:3306/ares?autoReconnect=true&useSSL=false";
//    public static final String CONN_STRING = "jdbc:mysql://localhost:3306/spectra28052021?autoReconnect=true&useSSL=false";

    public static final String USERNAME = "root";

    public static final String PASSWORD = "ares";
    String NOMENGABTABLE_SYS = "0012";
    String NOMMENCLATURE_FICH = "3009";
    String NOMMENCLATURE_INVENTAIRES = "2001";
    Map<String, String> PARAM = new HashMap<>();
    ReportDatasGeneratedRepository reportDatasGeneratedRepository;
    List<InAutorisationDecouvert> inAutorisationDecouvertList = new ArrayList<InAutorisationDecouvert>();
    List<invFileToIntegrate> allFileNamList = new ArrayList<invFileToIntegrate>();
    List<Incredit> increditList = new ArrayList<Incredit>();
    List<InBalance> inBalanceList = new ArrayList<InBalance>();
    List<InCautions> inCautionsList = new ArrayList<InCautions>();
    List<InClients> inClientsList = new ArrayList<InClients>();
    List<InComptes> inComptesList = new ArrayList<InComptes>();
    List<Indat> indatList = new ArrayList<Indat>();
    List<Inbdc> inbdcList = new ArrayList<Inbdc>();
    List<InOib> inOibList = new ArrayList<InOib>();
    List<Inech> inechList = new ArrayList<Inech>();
    List<Invent> inventList = new ArrayList<Invent>();
    List<InRapartriements> inrapaList = new ArrayList<InRapartriements>();
    List<Intitre> intitreList = new ArrayList<Intitre>();
    List<Intran> intranList = new ArrayList<Intran>();
    List<InGaranties> inGarantiesList = new ArrayList<InGaranties>();
    List<InSoldes> inSoldesList = new ArrayList<InSoldes>();
    List<Invgenr> invgenrList = new ArrayList<Invgenr>();
    Long idOpe, minimum;
    String idTrait, codeFichier;
    Long quotien = Long.valueOf(1);
    Long nombreTotal = Long.valueOf(1);
    Long count = Long.valueOf(0);
    Long statut = Long.valueOf(3);
    Long statutope = Long.valueOf(3);
    Long nombreOpeTraite = Long.valueOf(0);
    ClientToSystemForm fic32 = null;

    @Autowired
    private ChargerDonneesService chargerDonneesService;
    @Autowired
    private ChargerDonneesServiceLocal chargerDonneesServiceLocal;
    @Autowired
    InClientsRepository inClientsRepository;
    @Autowired
    InAutorisationDecouvertRepository inAutorisationDecouvertRepository;
    @Autowired
    InBalanceRepository inBalanceRepository, inBalanceRepository2;
    @Autowired
    InCautionsRepository inCautionsRepository;
    @Autowired
    InComptesRepository inComptesRepository;
    @Autowired
    InGarantiesRepository inGarantiesRepository;
    @Autowired
    InSoldesRepository inSoldesRepository;
    @Autowired
    InbdcRepository inbdcRepository;
    @Autowired
    IndatRepository indatRepository;
    @Autowired
    LiveTraitementRepository liveTraitementRepository;
    @Autowired
    LiveOperationRepository liveOperationRepository;
    @Autowired
    IncreditRepository increditRepository;
    @Autowired
    InOibRepository inOibRepository;
    @Autowired
    InechRepository inechRepository;
    @Autowired
    InrapatRepository inrapatRepository;
    @Autowired
    IntitreRepository intitreRepository;
    @Autowired
    IntransRepository intransRepository;
    @Autowired
    LiveReportingService liveReportingService;
    @Autowired
    InvgenrRepository invgenrRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    Map<String, String> parameters = new HashMap<>();
    Map<String, String> dropdown = new HashMap<>();
    int CONST = 0, defineFileToSave = 0;
    String fileName;
    String lineFile = "";
    Session session = null;
    JSch jsch = new JSch();
    Channel channel = null;
    ChannelSftp channelSftp;
    List<InventaireListForm> inventaireList = new ArrayList<InventaireListForm>();

    public Map<String, String> getGenerationAndSavingParam()
            throws SQLException, ClassNotFoundException, JSONException {

        System.out.println("START GETTING PÄRAMS");
        String select = "";
        Connection connmdection = null;
        // JSONObject obj = new JSONObject();
        // boolean val = false;3
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
                // System.out.println("pass :"+result.getString("lib2"));

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
                System.out.println("invEncours :" + result.getString("lib2"));

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
                PARAM.put("minimumNumber", result.getString("lib2")); // 1e nombre minimum d element a traiter avant
                // insertion dans la bd
            }
            if (result.getString("acscd").equalsIgnoreCase("0030")) {
                PARAM.put("ext", result.getString("lib2")); // 1e nombre minimum d element a traiter avant insertion
                // dans la bd
            }
            /*
			 * if(result.getString("acscd").equalsIgnoreCase("0022")){
			 * PARAM.put("portOracle", result.getString("lib2")); }
             */
            // System.out.println("END DISTRIBUTION");
        }
        System.out.println("END GETGENARATION AND SAVING PARAMS");

        return PARAM;
    }

    public List<InventaireListForm> getInventairelist() throws SQLException, ClassNotFoundException, JSONException {

        System.out.println("START  getInventairelist");
        String select = "";
        // boolean val = false;
//         USERNAME="root";
//        PASSWORD="";
//        CONN_STRING="jdbc:mysql://localhost:3306/spectra";  

        Class.forName("com.mysql.jdbc.Driver");
        try {
            System.out.println("BEFORE  CONNECTION");
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            // connection =
            // DriverManager.getConnection("jdbc:mysql://localhost/ares",USERNAME,PASSWORD);
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("AFTER CONEXION AND BEFORE STATEMENT");
        Statement stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMMENCLATURE_INVENTAIRES + "' AND dele='0'";
        System.out.println("AFTER STATEMENT BEFORE EXECUTING QUERRY");

        ResultSet result = stmt.executeQuery(select);
        System.out.println("AFTER EXECUTING QUERRY");
        while (result.next()) {
            // System.out.println(result.getString("acscd"));
            InventaireListForm inv = new InventaireListForm(result.getString("acscd"), result.getString("lib1"),
                    result.getString("lib2"), result.getString("lib3"), result.getString("lib4"), Integer.valueOf(0),
                    result.getString("lib5"), result.getString("cetab"));
            inventaireList.add(inv);
        }
        System.out.println("END  inventaireList");

        return inventaireList;

    }

    public void deleteDatas(String table, String dar) {

        String sql1 = "delete from " + table + " where dar =to_date('" + dar + "','YYYY-MM-DD')";
        jdbcTemplate.execute(sql1);

    }

    public String extrairesFromClientDatabase(ClientToSystemForm fic) {

        System.out.println("START FUNCTION");
        System.out.println("START INITIATE REPORTING");
        // getCon();
        try {
            // recuperation Les inventaire paramétrées
            parameters = getGenerationAndSavingParam();
            /*
			 * if(parameters.get("sameServer").equalsIgnoreCase("1")){ // we store the file
			 * in the same server. as the web service return
			 * chargerDonneesServiceLocal.extrairesFromClientDatabase(fic); }
             */// the file is been stored in a distant server.
            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeInventaires().size()));
            inventaireList = getInventairelist();
            minimum = Long.valueOf(parameters.get("minimumNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        // acces au fichier pour ecriture
        BufferedWriter writer = null;
        JSch jsch2 = new JSch();
        try {
            System.out.println("BEGINING FTP CONNEXION");
            System.out.println(parameters.get("user"));
            System.out.println(parameters.get("ip"));
            System.out.println(parameters.get("port"));
            System.out.println("PENDING REPOSITORY: " + parameters.get("invEncours"));
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            System.out.println("BEFORE FTP CONNEXION");
            session.connect();
            System.out.println("AFTER FTP CONNEXION");

        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("BEFORE CHANNEL CONNEXION");
            channel.connect();
            System.out.println("AFTER CHANNEL CONNEXION");
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;
        try {
            channelSftp.cd(parameters.get("invEncours"));
            System.out.println("PENDING REPOSITORY: " + parameters.get("invEncours"));

        } catch (SftpException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("BEFORE FOREEACH TOGENERATE THE INVENTORY FILES");
        System.out.println("CODE INVENTAIRE SISE :" + fic.getCodeInventaires().size());
        System.out.println("LINE ALL INVENTORY CODE: " + fic.getCodeInventaires().toString());
        for (int i = 0; i < fic.getCodeInventaires().size(); i++) {
            System.out.println("INSIDE FIRST FOREACH");
            for (int j = 0; j < inventaireList.size(); j++) {
                System.out.println("INSIDE FIRST FOREACH");

                if (fic.getCodeInventaires().get(i).getCode().equalsIgnoreCase(inventaireList.get(j).getCodeInv())) {

                    System.out.println("INSIDE SECOND FOREACH");
                    System.out.println("INSIDE SECOND FOREACH THE TREATMENT FOR CODE :"
                            + fic.getCodeInventaires().get(i).getCode());
                    try {
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                    } catch (ClassNotFoundException e) {
                        System.out.println("Where is your Oracle JDBC Driver?");
                        e.printStackTrace();
                    }
                    try {
                        // connection = DriverManager.getConnection(url, login, password);
                        System.out.println("BEFORE ORACLE CONNEXION");
                        connection = DriverManager.getConnection(parameters.get("oracleUrl"),
                                parameters.get("loginUrl"), parameters.get("passwordUrl"));
                        System.out.println("AFTER ORACLE CONNEXION");
                    } catch (SQLException e) {
                        System.out.println("Connection Failed! Check output console");
                        e.printStackTrace();
                    }
                    Statement stmt = null;
                    String query = "";
                    System.out.println("BEFORE ORACLE QUERRY");
                    System.out.println("CodeInventaire:" + fic.getCodeInventaires().get(i).getCode());

                    if (fic.getCodeInventaires().get(i).getCode().equals("BAL")) {
                        // query = "select 'A'dar,'A'age, 'A'com,'A' cle,'A' dev,'A'cli,'A'chap,'A'
                        // sldd, 'A' sldcvd,'A' cumc,'A'cumd,'A'sldf,'A'
                        // sldcvf,'A'txb,'A'dcre,'A'dmod,'A' uticre,'A' utimod from inbal";
                        query = "select 'A1'cetab,'A2'age, 'A3'com,'A4' cle,'A5' dev,'A6'cli,'A7'chap,'A8' sldd, 'A9' sldcvd,'A10' cumc,'A11'cumd,'A12'sldf,'A13' sldcvf,'A14'txb,'A15'dcre,'A16'dmod,'A17' uticre,'A18' utimod from dual";
                        /*
						 * query =
						 * "select '31082019'dar, age age,  ncp com,clc cle, dev dev, cli cli, cha chap, sde sldd, sde sldcvd, 0 cumc, 0 cumd, 0 sldf, 0 sldcvf, 0 txb, sysdate dcre, sysdate dmod,'AUTO' uticre,'AUTO' utimod "
						 * + "from econnect.bkcom where cfe!='O'";
                         */
                    } else if (fic.getCodeInventaires().get(i).getCode().equals("SOLDE")) {
                        // query = "select 'A'dar,'A'age, 'A'com,'A' cle,'A' dev,'A'cli,'A'chap,'A'
                        // sldd, 'A' sldcvd,'A' cumc,'A'cumd,'A'sldf,'A'
                        // sldcvf,'A'txb,'A'dcre,'A'dmod,'A' uticre,'A' utimod from dual";
                        query = "select '" + fic.getDate()
                                + "' dar,'A2'age, 'A3'com,'A4' cle,'A5' dev,'A6'cli,'A7'chap,'10' sldd, '10' sldcvd,'10' sldcvc,'10' cumc,'10'cumd,'10' sldf,'10' sldcvf,'10'txb,'"
                                + fic.getDate() + "'dcre,'" + fic.getDate()
                                + "'dmod,'A17' uticre,'A18' utimod from dual";

                        // query = "";
                    } else if (fic.getCodeInventaires().get(i).getCode().equals("AUTO")) {

                        query = "select 'A1'age, 'A2' cetab, 'A3' chap, 'A4' cle, 'A5' cli, 'A6' com, 'A7' dcre,'A8' ddep, '0' dele, 'A9' dev, 'A11' dfin,'A12 'dmep,'A13' dmod,'A14' eta,'A15' mont,'A16' ref,'A17' typ,'A18' uticre,'A19' utimod from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("CLI")) {

                        query = "select 'A1'age, 'A2' cli, 'A3' nom, 'A4' pre, 'A5' sig, 'A6' rso, 'A7' nomc, 'A8' typ, 'A9' nper, 'A10' preper, 'A11' nmer, 'A12' premer, 'A13' dnai, 'A14' lnai, 'A15' typpie, 'A16' numpie, 'A17' ddel, 'A18' ldel, 'A19' dexp, 'A20' ncc, 'A21' nrc, 'A22' dnrc, 'A23' sec , 'A24' ctn , 'A25' naema,'0000' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("COM")) {

                        query = "select 'A1'age, 'A2' cfer, 'A3' chap, 'A4' chl1, 'A5' chl2, 'A6' chl3, 'A7' chl4, 'A8' chl5, 'A9' chl6, 'A10' cle, 'A11' cli, 'A12' com, 'A13' cumc, 'A14' cumd, 'A15' dcre, 'A16' ddc, 'A17' ddd, 'A18' ddm, 'A19' dev, 'A20' dfe, 'A21' dodb, 'A22' dou, 'A23' lib , 'A24' sde , 'A25' shi , '0000' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("CAU")) {

                        query = "select '0000' cetab, A1'age, 'A2' cfer, 'A3' chap, 'A4' chl1, 'A5' chl2, 'A6' chl3, 'A7' chl4, 'A8' chl5, 'A9' chl6, 'A10' cle, 'A11' cli, 'A12' com, 'A13' cumc, 'A14' cumd, 'A15' dcre, 'A16' ddc, 'A17' ddd, 'A18' ddm, 'A19' dev, 'A20' dfe, 'A21' dodb, 'A22' dou, 'A23' lib , 'A24' sde , 'A25' shi  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("GAR")) {

                        query = "select 'A1'age, 'A2' com, 'A3' cle, 'A4' dev, 'A5' cli, 'A6' chap, 'A7' ref, 'A8' typ, 'A9' dmep, 'A10' ddeb, 'A11' dfin, 'A12' mont, 'A13' maf, 'A14' eng, 'A15' teng, 'A16' taff, 'A17' eta, 'A18' dlev, 'A19' dcre, 'A20' dmod, 'A21' uticre, 'A22' utimod, '0000' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("BDC")) {

                        query = "select 'A1'age, 'A2' com, 'A3' cle, 'A4' dev, 'A5' cli, 'A6' numsou, 'A7' ref, 'A8' typ, 'A9' dmep, 'A10' ddeb, 'A11' dfin, '0' mont, 'A13' nan, 'A14' per, 'A15' tau, 'A16' dcre, 'A17' dmod, 'A18' uticre, 'A19' utimod,'0'dele ,'0000'cetab from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("DAT")) {

                        query = "select 'A1' age, 'A2' com, 'A3' cle, 'A4' dev, 'A5' cli, 'A6' numsou, 'A7' ref, 'A8' typ, 'A9' dmep, 'A10' ddeb, 'A11' dfin, '10' mont, 'A13' nan, 'A14' per, 'A15' tau, 'A16' dcre, 'A17' dmod, 'A18' uticre, 'A19' utimod, '0' dele, '0000' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("CREDOC")) {

                        query = "";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("REMDOC")) {
                        query = "";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("TRANS")) {
                        query = "select 'A1' age, 'A2' cle, 'A3' cli, 'A4' com, 'A5' dcre, 'A6' dev, 'A7' dmep, 'A8' dmod, 'A9' moncv, 'A10' mont, 'A11' mot, 'A12' pdes, 'A13' ref, 'A14' tau, 'A15' typ, 'A16' uticcre, 'A17' utimod , '0' dele , 'A19' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("RAPAT")) {

                        query = "select 'A1' age, 'A2' cle, 'A3' cli, 'A4' com, 'A5' dcre, 'A6' dev, 'A7' dmep, 'A8' dmod, 'A9' moncv, 'A10' mot, 'A11' pdes, 'A12' ref, 'A13' tau, 'A14' typ, 'A15' uticre, 'A16' utimod, '10' mont, '0' dele, '10' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("ECH")) {

                        query = "select 'A1' age, 'A2' chac,'A2_' chae, 'A3' cli, 'A4' comc, 'A5' come, 'A6' dcre, 'A7' deb, 'A8' dev, 'A9' dmep, 'A10' dmod, 'A11' fin, 'A12' moncv, '10' mont, 'A14' nat, 'A15' nbe, '10' per, 'A17' ref , 'A18' tau , 'A17' tdev , 'A17' teg , 'A17' typ,'A17' uticre , 'A17' utimod , '0' dele , '0000' cetab   from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("OPCH")) {

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("MARCH")) {

                        query = "";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("TITRE")) {

                        query = "select 'A1' age, 'A2' fin, 'A3' cha, 'A4' cle, 'A51' cli, 'A5' com, 'A6' dcre, 'A7' deb, 'A8' dev, 'A9' dmep, 'A10' dmod, 'A11' dod, 'A12' issu, 'A13' mob, 'A14' moncv, '10' mont, 'A16' nat, 'A17' ref , 'A18' sen , 'A19' tau  , 'A20' typ , 'A21' uticre,'A22' utimod,'0' dele,'0000' cetab from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("OPIB")) {

                        query = "select 'A1' age, 'A2' cha, 'A3' cle, 'A4' cli, 'A51' com, 'A5' dcre, 'A6' deb, 'A7' dev, 'A8' dmep, 'A9' dmod, 'A10' dod, 'A11' fin, 'A12' issu, 'A13' mob, 'A14' moncv, '10' mont, 'A17' nat , 'A18' ref , 'A19' sen  , 'A20' tau , 'A21' typ,'A22' uticre,'A23' utimod,'0' dele,'A24' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("WALLET")) {

                        query = "";
                    } else if (fic.getCodeInventaires().get(i).getCode().equals("IMP")) {

                        query = "";
                    } else if (fic.getCodeInventaires().get(i).getCode().equals("CRED")) {

                        query = "select 'A1' age, 'A2' come, 'A3' comc, 'A4' dev, 'A51' chae, 'A5' chac, 'A6' cli, 'A7' ref, 'A8' typ, 'A9' nat, 'A10' dmep, 'A11' deb, 'A12' fin, '10' mont, 'A14' moncv, 'A15' tdev, 'A16' tau, 'A17' teg , 'A18' per , 'A19' nbe  , 'A20' dcre , 'A21' dmod , 'A21' uticre , 'A21' utimod, '0' dele, '0000' cetab  from dual";

                    } else if (fic.getCodeInventaires().get(i).getCode().equals("MONET")) {
                        query = "";
                    } else {

                        System.out.println("SHOULD NOT COUNT");
                        query = "";
                        // ecrire dans le repertoire des erreurs.pour le type de fichier
                    }

                    try {
                        System.out.println("BEFORE ORACLE QUERRY EXECUTION BFORE STATEMENT ");
                        stmt = connection.createStatement();
                        // System.out.println("BEFORE ORACLE QUERRY EXECUTION AFTER STATEMENT IT MAY
                        // TAKE TIME.............");
                        System.out.println("QUERRY:" + query);
                        ResultSet rs = stmt.executeQuery(query);
                        System.out.println("AFTER ORACLE QUERRY EXECUTION AFTER STATEMENT");
                        // rs.last();
                        // nombreTotal = Long.valueOf(rs.getRow());
                        // rs.beforeFirst();
                        System.out.println("Nombre de données." + nombreTotal);
                        if (nombreTotal > 0) {

                            while (rs.next()) {
                                System.out.println("INSIDE RESULTS");
                                // preparation du fichier
                                if (defineFileToSave == 0) {
                                    System.out.println(
                                            "INSIDE RESULTS1111111111111111111111111111111111111111111111111111111111111111111");
                                    idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                            inventaireList.get(j).getCodeInv(), nombreTotal);
                                    // fileName= inventaireList.get(j).getNomFic();
                                    fileName = inventaireList.get(j).getCodeInv() + getYearMonthDayp(fic.getDate())
                                            + parameters.get("ext");

                                    final File file = new File(fileName);
                                    OutputStream out = null;
                                    try {
                                        out = channelSftp.put(parameters.get("invEncours") + fileName);
                                    } catch (SftpException ex) {
                                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE,
                                                null, ex);
                                    }
                                    writer = new BufferedWriter(new OutputStreamWriter(out));
                                    defineFileToSave = 1;
                                }
                                // contruction de la ligne
                                // if(CONST<inventaireList.get(j).getNbrColoneTable()) {
/////// A revoir                          
                                System.out
                                        .println("LINE " + fic.getCodeInventaires().get(i).getCode() + " :" + lineFile);

                                if (fic.getCodeInventaires().get(i).getCode().equals("BAL")) {

                                    lineFile = rs.getString("cetab") + "|" + rs.getString("age") + "|"
                                            + rs.getString("com") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("dev") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("chap") + "|" + rs.getString("sldd") + "|"
                                            + rs.getString("sldcvd") + "|" + rs.getString("cumc") + "|"
                                            + rs.getString("cumd") + "|" + rs.getString("sldf") + "|"
                                            + rs.getString("sldcvf") + "|" + rs.getString("txb") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("dmod") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod");
                                    System.out.println("LINE BAL: " + lineFile);
                                } else if (fic.getCodeInventaires().get(i).getCode().equals("SOLDE")) {
                                    System.out.println("LINE SOLDE: " + lineFile);
                                    lineFile = rs.getString("dar") + "|" + rs.getString("age") + "|"
                                            + rs.getString("com") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("dev") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("chap") + "|" + rs.getString("sldd") + "|"
                                            + rs.getString("sldcvd") + "|" + rs.getString("sldcvc") + "|"
                                            + rs.getString("cumc") + "|" + rs.getString("cumd") + "|"
                                            + rs.getString("sldf") + "|" + rs.getString("sldcvf") + "|"
                                            + rs.getString("txb") + "|" + rs.getString("dcre") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("AUTO")) {

//                                            lineFile = rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("ref")+"|"+rs.getString("typ")+"|"+rs.getString("dmep")+"|"+rs.getString("ddep")+"|"+rs.getString("dfin")+"|"+rs.getString("mont")+"|"+rs.getString("eta")+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");
                                    lineFile = rs.getString("age") + "|" + rs.getString("cetab") + "|"
                                            + rs.getString("chap") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("com") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("ddep") + "|"
                                            + rs.getString("dele") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("dfin") + "|" + rs.getString("dmep") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("eta") + "|"
                                            + rs.getString("mont") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("typ") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("CLI")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("nom") + "|" + rs.getString("pre") + "|"
                                            + rs.getString("sig") + "|" + rs.getString("rso") + "|"
                                            + rs.getString("nomc") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("nper") + "|" + rs.getString("preper") + "|"
                                            + rs.getString("nmer") + "|" + rs.getString("premer") + "|"
                                            + rs.getString("dnai") + "|" + rs.getString("lnai") + "|"
                                            + rs.getString("typpie") + "|" + rs.getString("numpie") + "|"
                                            + rs.getString("ddel") + "|" + rs.getString("ldel") + "|"
                                            + rs.getString("dexp") + "|" + rs.getString("ncc") + "|"
                                            + rs.getString("nrc") + "|" + rs.getString("dnrc") + "|"
                                            + rs.getString("sec") + "|" + rs.getString("catn") + "|"
                                            + rs.getString("naema") + "|" + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("COM")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("cfer") + "|"
                                            + rs.getString("chap") + "|" + rs.getString("chl1") + "|"
                                            + rs.getString("chl2") + "|" + rs.getString("chl3") + "|"
                                            + rs.getString("chl4") + "|" + rs.getString("chl5") + "|"
                                            + rs.getString("chl6") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("com") + "|"
                                            + rs.getString("cumc") + "|" + rs.getString("cumd") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("ddc") + "|"
                                            + rs.getString("ddd") + "|" + rs.getString("ddm") + "|"
                                            + rs.getString("dev") + "|" + rs.getString("dfe") + "|"
                                            + rs.getString("dodb") + "|" + rs.getString("dou") + "|"
                                            + rs.getString("lib") + "|" + rs.getString("sde") + "|"
                                            + rs.getString("shi") + "|" + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("CAU")) {

                                    lineFile = rs.getString("cetab") + "|" + rs.getString("age") + "|"
                                            + rs.getString("cfer") + "|" + rs.getString("chap") + "|"
                                            + rs.getString("chl1") + "|" + rs.getString("chl2") + "|"
                                            + rs.getString("chl3") + "|" + rs.getString("chl4") + "|"
                                            + rs.getString("chl5") + "|" + rs.getString("chl6") + "|"
                                            + rs.getString("cle") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("com") + "|" + rs.getString("cumc") + "|"
                                            + rs.getString("cumd") + "|" + rs.getString("dcre") + "|"
                                            + rs.getString("ddc") + "|" + rs.getString("ddd") + "|"
                                            + rs.getString("ddm") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("dfe") + "|" + rs.getString("dodb") + "|"
                                            + rs.getString("dou") + "|" + rs.getString("lib") + "|"
                                            + rs.getString("sde") + "|" + rs.getString("shi");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("GAR")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("com") + "|"
                                            + rs.getString("cle") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("chap") + "|"
                                            + rs.getString("ref") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("ddeb") + "|"
                                            + rs.getString("dfin") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("maf") + "|" + rs.getString("eng") + "|"
                                            + rs.getString("teng") + "|" + rs.getString("taff") + "|"
                                            + rs.getString("eta") + "|" + rs.getString("dlev") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("dmod") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("BDC")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("com") + "|"
                                            + rs.getString("cle") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("numsou") + "|"
                                            + rs.getString("ref") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("ddeb") + "|"
                                            + rs.getString("dfin") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("nan") + "|" + rs.getString("per") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("dcre") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod") + "|" + rs.getString("dele") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("DAT")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("com") + "|"
                                            + rs.getString("cle") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("numsou") + "|"
                                            + rs.getString("ref") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("ddeb") + "|"
                                            + rs.getString("dfin") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("nan") + "|" + rs.getString("per") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("dcre") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod") + "|" + rs.getString("dele") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("CREDOC")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("REMDOC")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("TRANS")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("com") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("dmep") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("moncv") + "|"
                                            + rs.getString("mont") + "|" + rs.getString("mot") + "|"
                                            + rs.getString("pdes") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod") + "|"
                                            + rs.getString("dele") + "|" + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("RAPAT")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("com") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("dmod") + "|"
                                            + rs.getString("moncv") + "|" + rs.getString("mot") + "|"
                                            + rs.getString("pdes") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod") + "|"
                                            + rs.getString("mont") + "|" + rs.getLong("dele") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("OPCH")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("ECH")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("chac") + "|"
                                            + rs.getString("chae") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("comc") + "|" + rs.getString("come") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("deb") + "|"
                                            + rs.getString("dev") + "|" + rs.getString("dmep") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("fin") + "|"
                                            + rs.getString("moncv") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("nat") + "|" + rs.getString("nbe") + "|"
                                            + rs.getString("per") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("tdev") + "|"
                                            + rs.getString("teg") + "|" + rs.getString("typ") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod") + "|"
                                            + rs.getString("dele") + "|" + rs.getString("cetab");
                                } else if (fic.getCodeInventaires().get(i).getCode().equals("MARCH")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("TITRE")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("fin") + "|"
                                            + rs.getString("cha") + "|" + rs.getString("cle") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("com") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("deb") + "|"
                                            + rs.getString("dev") + "|" + rs.getString("dmep") + "|"
                                            + rs.getString("dmod") + "|" + rs.getString("dod") + "|"
                                            + rs.getString("issu") + "|" + rs.getString("mob") + "|"
                                            + rs.getString("moncv") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("nat") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("sen") + "|" + rs.getString("tau") + "|"
                                            + rs.getString("typ") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod") + "|" + rs.getString("dele") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("OPIB")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("cha") + "|"
                                            + rs.getString("cle") + "|" + rs.getString("cli") + "|"
                                            + rs.getString("com") + "|" + rs.getString("dcre") + "|"
                                            + rs.getString("deb") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("dmod") + "|"
                                            + rs.getString("dod") + "|" + rs.getString("fin") + "|"
                                            + rs.getString("issu") + "|" + rs.getString("mob") + "|"
                                            + rs.getString("moncv") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("nat") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("sen") + "|" + rs.getString("tau") + "|"
                                            + rs.getString("typ") + "|" + rs.getString("uticre") + "|"
                                            + rs.getString("utimod") + "|" + rs.getString("dele") + "|"
                                            + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("WALLET")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("IMP")) {

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("CRED")) {

                                    lineFile = rs.getString("age") + "|" + rs.getString("come") + "|"
                                            + rs.getString("comc") + "|" + rs.getString("dev") + "|"
                                            + rs.getString("chae") + "|" + rs.getString("chac") + "|"
                                            + rs.getString("cli") + "|" + rs.getString("ref") + "|"
                                            + rs.getString("typ") + "|" + rs.getString("nat") + "|"
                                            + rs.getString("dmep") + "|" + rs.getString("deb") + "|"
                                            + rs.getString("fin") + "|" + rs.getString("mont") + "|"
                                            + rs.getString("moncv") + "|" + rs.getString("tdev") + "|"
                                            + rs.getString("tau") + "|" + rs.getString("teg") + "|"
                                            + rs.getString("per") + "|" + rs.getString("nbe") + "|"
                                            + rs.getString("dcre") + "|" + rs.getString("dmod") + "|"
                                            + rs.getString("uticre") + "|" + rs.getString("utimod") + "|"
                                            + rs.getString("dele") + "|" + rs.getString("cetab");

                                } else if (fic.getCodeInventaires().get(i).getCode().equals("MONET")) {

                                } else {

                                    // ecrire dans le repertoire des erreurs.pour le type de fichier
                                }
                                // CONST++;
                                // } else {

                                try {
                                    System.out.println("ECRIRE DANS FICHIER");
                                    System.out.println("here 1");
//                                            writer.write("TEST|test|TEST|test");
                                    writer.write(lineFile);
                                    count++;
                                    quotien = liveReportingService.detailsReportingToTheVue(idTrait,
                                            fic.getCodeInventaires().get(i).getCode(), nombreTotal, Long.valueOf(count),
                                            quotien, minimum);
                                } catch (IOException ex) {
                                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                try {
                                    writer.newLine();

                                } catch (IOException ex) {
                                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null,
                                            ex);
                                }
                                System.out.println("line" + count + "= :" + lineFile);
                                System.out.println("end of line" + count);
                                // CONST=0;
                                // }
                            }
                            writer.close();
                            statut = Long.valueOf(1);
                            defineFileToSave = 0;

                            liveReportingService.endDetailsReportingToTheVue(idTrait,
                                    fic.getCodeInventaires().get(i).getCode(), statut, Long.valueOf(count));

                            nombreOpeTraite++;
                            count = Long.valueOf(0);
                            quotien = Long.valueOf(1);
                            // end of file treatment
                        } else {

                            idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                                    fic.getCodeInventaires().get(i).getCode(), Long.valueOf(0));
                            liveReportingService.endDetailsReportingToTheVue(idTrait,
                                    fic.getCodeInventaires().get(i).getCode(), Long.valueOf(1), Long.valueOf(0));

                        }
                    } catch (SQLException e) {
                        // JDBCTutorialUtilities.printSQLException(e);
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {

                        if (stmt != null) {

                            try {
                                stmt.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    }

                }

            }
        }
        // End reporting status
        statutope = Long.valueOf(1);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

    }
//               
//public String writeInDataBaseSystem(DataIntegrationForm fic) {
//   
//   // recuperation des données a ecrire sur le fichier 
//        try {
//            // recuperation des paramètres
//            parameters=getGenerationAndSavingParam();
//            /*if(parameters.get("sameServer").equalsIgnoreCase("1")){
//                  return chargerDonneesServiceLocal.writeInDataBaseSystem(fic);
//            }*/
//            inventaireList=getInventairelist();
//            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(),fic.getCetab(), fic.getUsid(),fic.getOperation(),Long.valueOf(fic.getCodeInventaires().size()));
//            minimum=Long.valueOf(parameters.get("minimumNumber"));
//
//        } catch (SQLException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JSONException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }     
//
//   // acces au fichier pour ecriture
//        BufferedReader read = null;
//        JSch jsch2 = new JSch();
//        
//        try {
//            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"), Integer.parseInt(parameters.get("port")));
//        } catch (JSchException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        session.setPassword(parameters.get("pass"));
//        java.util.Properties config = new java.util.Properties(); 
//        config.put("StrictHostKeyChecking", "no");
//        session.setConfig(config);
//        System.out.println("Config set");
//        try {
//            session.connect();
//        } catch (JSchException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("Session connected");
//        try {
//            channel = session.openChannel("sftp");
//        } catch (JSchException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            channel.connect();
//        } catch (JSchException ex) {
//
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//       System.out.println("Connection Opened\n");
//       channelSftp = (ChannelSftp) channel;
//        try {
//            //invEncours   invArchives  invErreurs
//            channelSftp.cd(parameters.get("invEncours"));
//
//        } catch (SftpException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    for(int i =0; i< fic.getCodeInventaires().size();i++){
//
//       for (int j = 0; j<inventaireList.size();j++){
//           
//            if(fic.getCodeInventaires().get(i).getCode().equalsIgnoreCase(inventaireList.get(j).getCodeInv())){
//                   codeFichier=inventaireList.get(j).getCodeInv();
//
//                try {
//                    fileName=inventaireList.get(j).getCodeInv()+getYearMonthDayp(fic.getDate())+getYearMonthDay();
//                } catch (ParseException ex) {
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                   //  final File file = new File(fileName);
//                     InputStream in = null;
//                    try {
//                      //  out = channelSftp.put(parameters.get("invEncours")+fileName);
//                      String n=parameters.get("invEncours")+fileName;
//                        in = channelSftp.get(n);
//                    } catch (SftpException ex) {
//                        Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                     //writer = new BufferedWriter(new OutputStreamWriter(out));
//                    read = new BufferedReader(new InputStreamReader(in));
//                    String line = null;
//                try {
//                    // contituer une  collection de données a partir de chaque fichier a traiter 
//                    Vector fileList =channelSftp.ls(parameters.get("invEncours"));
//                    System.out.println("COUNT THE NUMBER OF INVENTORIES OF CODE: "+fic.getCodeInventaires().get(i).getCode());
//                    System.out.println("COUNT THE NUMBER OF FILES TO CONSUME: BEGIN");
//                     for(int z=0; z<fileList.size();z++){
//
//                               String[] data = fileList.get(z).toString().split(" ", -1);
//                               for (String filename : data){
//
//                                   if(filename.contains(fic.getCodeInventaires().get(i).getCode())){
//                                       
//                                        invFileToIntegrate allFileNam= new invFileToIntegrate(filename);
//                                        allFileNamList.add(allFileNam);
//                                      
//                                   }
//                                 
//                               }
//                       }
//                    System.out.println("NUMBER : "+allFileNamList.size());
//                    
//        if(allFileNamList.size()>0){
//         
//                    
//                    if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
//                            
//                        while ((line = read.readLine()) != null) {
//                            if(line.length()==0){
//                                System.out.println("eeeeeeeeeeeeeeeeee :");
//                                line=null;
//                            }else{
//                            // placer les elements dans le string
//                            String[] data = line.split("\\|", -1);
//                            System.out.println("DATdddddddddA :"+line);
//                           /* System.out.println("DATA"+data[0]);
//                            System.out.println("DATA"+data[1]);
//                            System.out.println("DATA"+data[2]);
//                            System.out.println("DATA"+data[3]);
//                            System.out.println("DATA"+data[4]);
//                            System.out.println("DATA"+data[5]);
//                            System.out.println("DATA"+data[6]);
//                            System.out.println("DATA"+data[7]);
//                            System.out.println("DATA"+data[8]);
//                            System.out.println("DATA"+data[9]);
//                            System.out.println("DATA"+data[10]);
//                            System.out.println("DATA"+data[11]);
//                            System.out.println("DATA"+data[12]);
//                            System.out.println("DATA"+data[13]);
//                            System.out.println("DATA"+data[14]);
//                            System.out.println("DATA"+data[15]);
//                            System.out.println("DATA"+data[16]);
//                            System.out.println("DATA"+data[17]);
//                            System.out.println("DATA"+Long.valueOf(0));
//*/
//                            InBalance inbal= new InBalance(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf(0)
//                            );
//                            inBalanceList.add(inbal);
//                            count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                            System.out.println(line);
//                            }
//                        }          
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){
//
//                        while ((line = read.readLine()) != null) {
//                            
//                            String[] data = line.split("\\|", -1);
//                            InSoldes insold= new InSoldes(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf(0)
//                            );
//                            inSoldesList.add(insold);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);
//                        }                              
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){
//
//                        while ((line = read.readLine()) != null) {
//                            
//                            String[] data = line.split("\\|", -1);
//                            InAutorisationDecouvert inAut= new InAutorisationDecouvert(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],
//                                    Long.valueOf(0)
//                            );
//                            inAutorisationDecouvertList.add(inAut);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);
//                            
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){
//
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            String[] data = line.split("\\|", -1);
//                            InClients incli= new InClients(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[24],
//                                    Long.valueOf(0)
//                            );
//                            inClientsList.add(incli);
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){
//                             
//                        while ((line = read.readLine()) != null) {
//
//                            String[] data = line.split("\\|", -1);
//                            InComptes incom= new InComptes(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[24],
//                                    Long.valueOf(0)
//                            );
//                            inComptesList.add(incom);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);
//
//                        }                              
//
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            String[] data = line.split("\\|", -1);
//                            InCautions incau= new InCautions(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf(0)
//                            );
//                            inCautionsList.add(incau);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);                            
//                        }
//                    
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){ 
//                           int k =0;  
//                        while ((line = read.readLine()) != null) {
//                            
//                            String[] data = line.split("\\|", -1);
//                           
//                            InGaranties inGar= new InGaranties(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],
//                                    Long.valueOf(0)
//                            );
//                            inGarantiesList.add(inGar);
//                             count++;
//                             quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                            System.out.println(line);
//                        }  
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                             quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        }   
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){
//
//                            
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        }                               
//
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }    
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){
//
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }                              
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        } 
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){
//
//                         
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        } 
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){
//                            
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }                              
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        } 
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){
//
//                        while ((line = read.readLine()) != null) {
//                            
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                        }    
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            
//                            
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        }                               
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){
//                             
//                        while ((line = read.readLine()) != null) {
//                            System.out.println(line);
//                            
//                             count++;
//                             quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                        }
//                    }else{
// 
//                        while ((line = read.readLine()) != null) {
//                            System.out.println(line);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//
//                            
//                        }                        
//                    }
//                    
//                }
//                    
//                } catch (IOException ex) {
//                    
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (SftpException ex) {
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try {
//                    read.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//               
//                // faire les saves dans la base de données oracle:
//
//                    if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
//                        
//                        System.out.println("BEFORE SAVING LIST BAL");                                                                           
//                        inBalanceRepository.save(inBalanceList);
//                        System.out.println("ttttttttttttttttttttttttt"+inBalanceList.size());
//                        inBalanceRepository.save(inBalanceList);
//                        System.out.println("AFTER SAVING LIST BAL"); 
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){
//                        
//                        System.out.println("BEFORE SAVING LIST SOLDE");                                                                           
//                        inSoldesRepository.save(inSoldesList);
//                        System.out.println("AFTER SAVING LIST SOLDE");          
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){
//                        
//                       inAutorisationDecouvertRepository.save(inAutorisationDecouvertList);
//                                                       
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){
//                     
//                      inClientsRepository.save(inClientsList);                             
//                                                     
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){
//                             
//                         inComptesRepository.save(inComptesList);                        
//
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){
//                        
//                        inCautionsRepository.save(inCautionsList);
//                    
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){
//                         
//                        inGarantiesRepository.save(inGarantiesList);
//                         
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){
//                        
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){
//
//                                                      
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){
//                             
//                                                  
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){
//                             
//                                                   
//
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){
//                             
//                          
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){
//
//                             
//                                                       
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){
//                             
//                        
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){                       
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){                        
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){
//                            
//                                                      
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){
//                             
//                         
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){
//
//                           
//                        
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){
//                             
//                                                     
//                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){
//                             
//                       
//                    }else{
// 
//                    }
//                     // deplacer et renommer le fichier dans les archieves ou les erreurs 
//                try {
//                    channelSftp.rename(parameters.get("invEncours")+fileName,parameters.get("invArchives")+fileName+getYearMonthTimeStamp());
//                } catch (SftpException ex) {
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try {
//                     channelSftp.cd(parameters.get("invEncours"));
//                     liveReportingService.endDetailsReportingToTheVue(idTrait,codeFichier, statut, Long.valueOf(count));
//                     System.out.println("AFTERENDING DETAILS"+idTrait);
//
//                     nombreOpeTraite++;
//                     quotien=Long.valueOf(1);
//                     count=Long.valueOf(0);   
//                } catch (SftpException ex) {
//                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                }
//               // j++;
//            }       
//        }
//    
//        statutope=Long.valueOf(1);
//        System.out.println("BEFORE GLOBALENDING"+idOpe);
//        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
//        nombreOpeTraite=Long.valueOf(0);
//        System.out.println("end of process-----------");// 
//        return "1";
//
//}
//  

    public String[] trimString(String[] r) {
        for (int i = 0; i < r.length; i++) {
            r[i] = r[i].trim();
        }
        return r;
    }

    public String writeInDataBaseSystem(DataIntegrationForm fic) {

        List<invFileToIntegrate> allFileNamList = new ArrayList<invFileToIntegrate>();
        // recuperation des données a ecrire sur le fichier
        try {
            // recuperation des paramètres
            parameters = getGenerationAndSavingParam();
            /*
			 * if(parameters.get("sameServer").equalsIgnoreCase("1")){ return
			 * chargerDonneesServiceLocal.writeInDataBaseSystem(fic); }
             */
            inventaireList = getInventairelist();
            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeInventaires().size()));
            minimum = Long.valueOf(parameters.get("minimumNumber"));

        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }

        // acces au fichier pour ecriture
        BufferedReader read = null;
        JSch jsch2 = new JSch();

        try {
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }

        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            session.connect();
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            channel.connect();
        } catch (JSchException ex) {

            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;
//		try {
//			// invEncours invArchives invErreurs
//			channelSftp.cd(parameters.get("invEncours"));
//
//		} catch (SftpException ex) {
//			Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//		}

        // controler le nombre d inventaires envoyé.
        System.out.println("CODE INVENTAIRE SIZE/////////////" + fic.getCodeInventaires().size());
        System.out.println(fic.getCodeInventaires());

        for (int z = 0; z < fic.getCodeInventaires().size(); z++) {// LIST OF CODES FOR TAKEN FROM THE VIEW

            String ispresent = "NO";
            try {
                // System.out.println("DANS LE REPERTOIRE TEMPORAIRE///////////////");
                Vector fileList = channelSftp.ls(parameters.get("invEncours"));
                // System.out.println("DANS LE REPERTOIRE TEMPORAIRE///////////////");

                for (int ll = 0; ll < fileList.size(); ll++) {
                    String[] data = fileList.get(ll).toString().split(" ", -1);
                    for (String filename : data) {
                        System.out.println("NAME OF THE FILE TO SEARCH: " + String
                                .valueOf(fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())));
                        System.out.println("NAME OF THE FILE TO SEARCHAA: " + filename);
//                        if(filename.contains(String.valueOf(fic.getCodeInventaires().get(z).getCode()+getYearMonthDayp(fic.getDate())))){
                        if (filename.contains(String.valueOf(
                                fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())))) {
                            System.out.println("NAME OF THE FILE TO SEARCH INSIDE: " + String.valueOf(
                                    fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())));
                            ispresent = "YES";
                            invFileToIntegrate allFileNam = new invFileToIntegrate(filename,
                                    String.valueOf(fic.getCodeInventaires().get(z).getCode()), ispresent);
                            if (allFileNamList.contains(allFileNam)) {
                            } else {
                                allFileNamList.add(allFileNam);
                            }
                        }

                    }
                }
                if (ispresent.equalsIgnoreCase("NO")) {

                    System.out.println(
                            " NO FILES FOUND FOR INVETORY: " + String.valueOf(fic.getCodeInventaires().get(z).getCode())
                            + " AT THE DATE " + fic.getDate());

                    idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                            fic.getCodeInventaires().get(z).getCode(), Long.valueOf(0));
                    liveReportingService.endDetailsReportingToTheVue(idTrait, fic.getCodeInventaires().get(z).getCode(),
                            Long.valueOf(1), Long.valueOf(0));
                    // invFileToIntegrate allFileNam= new invFileToIntegrate("NO
                    // FILE",String.valueOf(fic.getCodeInventaires().get(i)),ispresent);
                    // allFileNamList.add(allFileNam);
                }
                // }
            } catch (SftpException ex) {
                Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("STATUT allFileNamList OK ///////////////////////////////////" + allFileNamList.size());
        if (allFileNamList.size() > 0) {
            for (int j = 0; j < allFileNamList.size(); j++) {
                System.out.println("CODE FICHIER " + allFileNamList.get(j).getFilecode());
                System.out.println("FILE NAME TO BE TREATED " + allFileNamList.get(j).getFilename());

            }

            for (int j = 0; j < allFileNamList.size(); j++) {

                codeFichier = allFileNamList.get(j).getFilecode();
                System.out.println("CODE FICHIER " + codeFichier);
                System.out.println("FILE NAME " + allFileNamList.get(j).getFilecode());
                InputStream in = null;
                try {
                    String n = parameters.get("invEncours") + allFileNamList.get(j).getFilename();
                    System.out.println("CHEMIN PENDING " + n);
                    in = channelSftp.get(n);
                } catch (SftpException ex) {
                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                }
                // writer = new BufferedWriter(new OutputStreamWriter(out));
                // writer = new BufferedWriter(new OutputStreamWriter(out));

                BufferedReader readr = null;
                try {
                    readr = new BufferedReader(new InputStreamReader(
                            channelSftp.get(parameters.get("invEncours") + allFileNamList.get(j).getFilename())));
                } catch (SftpException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                int pf = 0;
                String line1 = null;
                try {
                    while ((line1 = readr.readLine()) != null) {
                        pf++;
                    }
                    nombreTotal = (long) pf;
                    System.out.println("---------------------------------------------------");
                    System.out.println(nombreTotal);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                read = new BufferedReader(new InputStreamReader(in));
                String line = null;
                idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe, codeFichier, Long.valueOf(1));
                if (codeFichier.equals("BAL")) {
                    System.out.println("inside BAL :");

                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                // placer les elements dans le string
                                String[] data = line.split("\\|", -1);
                                System.out.println("DATdddddddddA :" + line);
                                /*
								 * System.out.println("DATA"+data[0]); System.out.println("DATA"+data[1]);
								 * System.out.println("DATA"+data[2]); System.out.println("DATA"+data[3]);
								 * System.out.println("DATA"+data[4]); System.out.println("DATA"+data[5]);
								 * System.out.println("DATA"+data[6]); System.out.println("DATA"+data[7]);
								 * System.out.println("DATA"+data[8]); System.out.println("DATA"+data[9]);
								 * System.out.println("DATA"+data[10]); System.out.println("DATA"+data[11]);
								 * System.out.println("DATA"+data[12]); System.out.println("DATA"+data[13]);
								 * System.out.println("DATA"+data[14]); System.out.println("DATA"+data[15]);
								 * System.out.println("DATA"+data[16]); System.out.println("DATA"+data[17]);
								 * System.out.println("DATA"+Long.valueOf(0));
                                 */
                                InBalance inbal = new InBalance(data[0], java.sql.Date.valueOf(fic.getDate()), data[1],
                                        data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
                                        data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17],
                                        Long.valueOf(0));
                                inBalanceList.add(inbal);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // String sql1 = "delete from inbal where dar ='"+fic.getDate()+"'";
                    deleteDatas("inbal", fic.getDate());
//                    String sql1 = "delete from inbal where dar ='A1'";
//                    jdbcTemplate.execute(sql1);
//                    
                    // inBalanceRepository2.deleteInBalanceByDar("A1");
                    System.out.println("BEFORE SAVING LIST BAL");
                    inBalanceRepository.save(inBalanceList);
                    System.out.println("ttttttttttttttttttttttttt" + inBalanceList.size());
                    inBalanceList.clear();
                    System.out.println("AFTER SAVING LIST BAL");

                } else if (codeFichier.equals("SOLDE")) {
                    inSoldesList.clear();
                    deleteDatas("insld", fic.getDate());
                    try {
                        int ppp = 0;
                        int counter = 0;
                        while ((line = read.readLine()) != null) {
                            counter++;
                            ppp++;
                            if (ppp == 1) {
                                continue;
                            }
                            if (line.length() == 0) {
                                line = null;
                            } else {

                                String[] data = line.split("\\|", -1);

                                System.out.println("element in line with length" + data.length);
                                int i = 0;
                                for (String s : data) {
//                                    System.out.println(s);
//                                    System.out.println(data[i] + " empty space :" + i);
//                                    System.out.println((data[i].toString() == null) ? "is null" : "");
//                                    System.out.println((data[i].toString() == "") ? "is empty" : "");
//                                    System.out.println((data[i].toString() == " ") ? "is a space" : "");
//                                    System.out.println((data[i].toString() == "  ") ? "is two spaces" : "");
                                    i++;

                                }
                                System.out.print("data content");
//                                System.out.print(data);
//								InSoldes insold = new InSoldes(fic.getCetab(), java.sql.Date.valueOf(fic.getDate()), data[1],
//										data[2], data[3], data[4], data[5], data[6], 
//										Double.valueOf((data[7]+"0" == null || data[7]+"0" == "" || data[7]+"0" == " ") ? "0" : data[7]+"0")/10,
//										Double.valueOf((data[8]+"0" == null || data[8]+"0" == "" || data[8]+"0" == " ") ? "0" : data[8]+"0")/10,
//										data[9], data[10],
//										// Double.valueOf(data[11]),
//										1.0,
//										Double.valueOf((data[12]+"0" == null || data[12]+"0" == "" || data[12]+"0"
//										== " ") ? "0"
//												: data[12]+"0")/10,
//										Double.valueOf((data[13]+"0" == null || data[13]+"0" == "" || data[13]+"0" == " ") ? "0"
//												: data[13]+"0")/10,
//										data[14], data[15], data[16], data[17], data[18], data[19], data[20], data[21],
//										data[22], data[23], data[24], data[25], data[26], data[27], data[28], data[29],
//										data[30], data[31], data[32], data[33],
//										((data[34]+"	 " == null || data[34]+"" == "" || data[34]+"" == " ")
//												? java.sql.Date.valueOf(fic.getDate())
//														: StringtoDate(data[34])),
//										((data[35]+"" == null || data[35]+"" == "" || data[35]+"" == " ")
//												? java.sql.Date.valueOf(fic.getDate())
//														: StringtoDate(data[35]+""))
//										, data[36], data[37]);
                                data = trimString(data);
                                System.out.println(line);
                                InSoldes insold = new InSoldes(fic.getCetab(), java.sql.Date.valueOf(fic.getDate()), data[1],
                                        data[2], data[3], data[4], data[5], data[6],
                                        Double.valueOf(data[7].equals("") ? "0" : data[7]),
                                        Double.valueOf(data[8].equals("") ? "0" : data[8]),
                                        data[9], data[10],
                                        1.0,
                                        Double.valueOf(data[12].equals("") ? "0"
                                                : data[12]),
                                        Double.valueOf(data[13].equals("") ? "0"
                                                : data[13]),
                                        data[14], data[15], data[16], data[17], data[18], data[19], data[20], data[21],
                                        data[22], data[23], data[24], data[25], data[26], data[27], data[28], data[29],
                                        data[30], data[31], data[32], data[33],
                                        (data[34].equals("")
                                        ? java.sql.Date.valueOf(fic.getDate())
                                        : StringtoDate(data[34])),
                                        ((data[35].equals(""))
                                        ? java.sql.Date.valueOf(fic.getDate())
                                        : StringtoDate(data[35] + "")),
                                        data[36], data[37], data[38], data[39], data[40], data[41], data[42], data[43], data[44], data[45], data[46], data[47]);
                                inSoldesList.add(insold);
                                if (counter == 1000) {
                                    inSoldesRepository.save(inSoldesList);
                                    System.out.println("counter value : " + inSoldesList.size() + " --  " + counter);
                                    inSoldesList.clear();
                                    counter = 0;
                                }

                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                            }
                        }

                        System.out.println("BEFORE SAVING LIST SOLDE");
                        System.out.println(ppp);
                        inSoldesRepository.save(inSoldesList);
                        System.out.println("ttttttttttttttttttttttttt" + inSoldesList.size());
                        inSoldesList.clear();
                        // inBalanceRepository.save(inBalanceList);
                        System.out.println("AFTER SAVING LIST SOLDE");
                    } catch (IOException | NumberFormatException | ParseException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("IVG")) {
                    invgenrList.clear();
                    deleteDatas("invg", fic.getDate());

                    int ppp = 0;
                    int counter = 0;
                    try {
                        while ((line = read.readLine()) != null) {
                            counter++;
                            ppp++;
                            if (ppp == 1) {
                                continue;
                            }
                            if (line.length() == 0) {
                                line = null;
                            } else {

                                String[] data = line.split("\\|", -1);

                                System.out.println("element in line with length" + data.length);
                                int i = 0;
                                Invgenr invgenr = new Invgenr();
                                invgenr.setDar(convertStringDate(data[0], fic));//convertStringDate(data[46], fic)
                                invgenr.setCinv(toSting(data[1]));
                                invgenr.setAge(toSting(data[2]));
                                invgenr.setCom(toSting(data[3]));
                                invgenr.setDev(toSting(data[4]));
                                invgenr.setCle(toSting(data[5]));
                                invgenr.setChap(toSting(data[6]));
                                invgenr.setCha1(toSting(data[7]));
                                invgenr.setCha2(toSting(data[8]));
                                invgenr.setNsou(toSting(data[9]));
                                invgenr.setNcp1(toSting(data[10]));
                                invgenr.setNcp2(toSting(data[11]));
                                invgenr.setMaut(convertStringDouble(data[12]));
                                invgenr.setDou(convertStringDate(data[13], fic));
                                invgenr.setDeb(convertStringDate(data[14], fic));
//                                invgenr.setFin(convertStringDate(data[15], fic));
                                invgenr.setMond(convertStringDouble(data[16]));
                                invgenr.setMoncv(convertStringDouble(data[17]));
                                invgenr.setNat(toSting(data[18]));
                                invgenr.setSen(toSting(data[19]));
                                invgenr.setTau(convertStringDouble(data[20]));
                                invgenr.setMob(convertStringDouble(data[21]));
                                invgenr.setMmob(convertStringDouble(data[22]));
                                invgenr.setIssut(toSting(data[23]));
                                invgenr.setMon1(convertStringDouble(data[24]));
                                invgenr.setMon2(convertStringDouble(data[25]));
                                invgenr.setMon3(convertStringDouble(data[26]));
                                invgenr.setMon4(convertStringDouble(data[27]));
                                invgenr.setMon5(convertStringDouble(data[28]));
                                invgenr.setMon6(convertStringDouble(data[29]));
                                invgenr.setLib1(toSting(data[30]));
                                invgenr.setLib2(toSting(data[31]));
                                invgenr.setLib3(toSting(data[32]));
                                invgenr.setLib4(toSting(data[33]));
                                invgenr.setLib5(toSting(data[34]));
                                invgenr.setLib6(toSting(data[35]));
                                invgenr.setTau1(convertStringDouble(data[36]));
                                invgenr.setTau2(convertStringDouble(data[37]));
                                invgenr.setTau3(convertStringDouble(data[38]));
                                invgenr.setTau4(convertStringDouble(data[39]));
                                invgenr.setTau5(convertStringDouble(data[40]));
                                invgenr.setTau6(convertStringDouble(data[41]));
                                invgenr.setDat1(convertStringDate(data[42], fic));
                                invgenr.setDat2(convertStringDate(data[43], fic));
                                invgenr.setDat3(convertStringDate(data[44], fic));
                                invgenr.setDat4(convertStringDate(data[45], fic));
                                invgenr.setDat5(convertStringDate(data[46], fic));
                                invgenr.setDat6(convertStringDate(data[47], fic));
                                invgenr.setDcre(new Date());
                                invgenr.setDmod(new Date());
                                invgenr.setUticre("");
                                invgenr.setUtimod("");
                                invgenrList.add(invgenr);
                                if (counter == 1000) {
                                    invgenrRepository.save(invgenrList);
                                    System.out.println("counter value : " + invgenrList.size() + " --  " + counter);
                                    invgenrList.clear();
                                    counter = 0;
                                }

                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);
                                System.out.println(line);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("BEFORE SAVING LIST SOLDE");
                    System.out.println(ppp);
                    invgenrRepository.save(invgenrList);
                    System.out.println("ttttttttttttttttttttttttt" + invgenrList.size());
                    invgenrList.clear();
                    // inBalanceRepository.save(inBalanceList);
                    System.out.println("AFTER SAVING LIST SOLDE");

                } else if (codeFichier.equals("AUTO")) {
                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);
//								InAutorisationDecouvert inAut = new InAutorisationDecouvert(data[0], data[1], data[2],
//										data[3], data[4], data[5], data[6], data[7], Long.valueOf(data[8]), data[9],
//										data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17],
//										data[18], java.sql.Date.valueOf(fic.getDate()));
                                InAutorisationDecouvert inAut = new InAutorisationDecouvert();
                                try {
                                    inAut.setAge(data[0]);
                                    inAut.setCom(data[1]);
                                    inAut.setCle(data[2]);
                                    inAut.setDev(data[3]);
                                    inAut.setCli(data[4]);
                                    inAut.setChadr(data[5]);
                                    inAut.setChacr(data[6]);
                                    inAut.setRef(data[7]);
                                    inAut.setTyp(data[8]);

                                    inAut.setDmep(data[9].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[9]));//date

                                    inAut.setDdeb((data[10].equals("")) ? java.sql.Date.valueOf(fic.getDate()) : StringtoDate(data[10]));//date
                                    inAut.setDfin(data[11].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[11]));//date
                                    inAut.setMont(Double.valueOf(data[12].equals("") ? "0" : data[12]));//amount
                                    inAut.setUtil(Double.valueOf(data[13].equals("") ? "0" : data[13]));//amount
                                    inAut.setEta(data[14]);//
                                } catch (ParseException ex) {
                                    System.out.println("error of collection");
                                    System.out.println(ex.getMessage());
                                }
                                inAutorisationDecouvertList.add(inAut);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);
                                System.out.println(line);
                            }
                        }
                        deleteDatas("inaut", fic.getDate());
                        System.out.println("BEFORE SAVING LIST AutorisationDecouvert");
                        inAutorisationDecouvertRepository.save(inAutorisationDecouvertList);
                        System.out.println("Length" + inAutorisationDecouvertList.size());
                        inAutorisationDecouvertList.clear();
                        // inBalanceRepository.save(inBalanceList);
                        System.out.println("AFTER SAVING LIST AUTORISATION DECOUVERT");

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("CLI")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);

                                InClients incli = null;
                                System.out.println("sdfsdf  sdfsdf");
                                System.out.println(data);
                                try {
                                    incli = new InClients(data[0], data[1], data[2], data[3], data[4], data[5], data[6],
                                            data[7], data[8], data[9], data[10], data[11], StringtoDate(data[12]),
                                            data[13], data[14], data[15], data[16], data[17], data[18], data[19],
                                            data[20], data[21], data[22], data[23], data[24], data[25], data[26],
                                            data[27], data[28], data[29], data[30], data[31], data[32], data[33],
                                            data[34], data[35], data[36], data[37], data[38], data[39], data[40],
                                            data[41], data[42], data[43], data[44], data[45], data[46], data[47],
                                            data[48], data[49], data[50], data[51], data[52], data[53], data[54],
                                            data[55], data[56], data[57], StringtoDate(data[58]),
                                            StringtoDate(data[59]), data[60], data[61], fic.getCetab(),
                                            java.sql.Date.valueOf(fic.getDate()));
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                System.out.println(incli);
                                inClientsList.add(incli);
                                System.out.println(line);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);
                                System.out.println(line);
                                System.out.println(quotien);
                                System.out.println(nombreTotal);
                                System.out.println(minimum);
                            }
                        }
                        deleteDatas("incli", fic.getDate());
                        inClientsRepository.save(inClientsList);
                        inClientsList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("COM")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);
                                InComptes incom = new InComptes(data[25], data[0], data[1], data[2], data[3], data[4],
                                        data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                                        data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20],
                                        data[21], data[22], data[23], data[24], Long.valueOf(0),
                                        java.sql.Date.valueOf(fic.getDate()));
                                inComptesList.add(incom);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);
                                System.out.println(line);

                            }
                        }
                        deleteDatas("incom", fic.getDate());
                        inComptesRepository.save(inComptesList);
                        inComptesList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("CAU")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);
//                                InCautions incau = new InCautions(data[0], data[1], data[2], data[3], data[4], data[5],
//                                        data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13],
//                                        data[14], data[15], data[16], data[17], data[18], Long.valueOf(0),
//                                        java.sql.Date.valueOf(fic.getDate()));
//                                inCautionsList.add(incau);
                                try {
                                    InCautions incau = new InCautions();
                                    incau.setAge(data[0]);
                                    incau.setCom(data[1]);
                                    incau.setCle(data[2]);
                                    incau.setDev(data[3]);
                                    incau.setCli(data[4]);
                                    incau.setNom(data[5]);
                                    incau.setChap(data[6]);
                                    incau.setRef(data[7]);
                                    incau.setTyp(data[8]);
                                    incau.setDmep(data[9].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[9]));
                                    incau.setDdeb(data[10].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[10]));
                                    incau.setDfin(data[11].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[11]));
                                    incau.setMont(Double.valueOf(data[12].equals("") ? "0" : data[12]));//montant
                                    incau.setEta(data[13]);
                                    incau.setDlev(data[14].equals("")
                                            ? java.sql.Date.valueOf(fic.getDate())
                                            : StringtoDate(data[14]));
                                    incau.setCetab(fic.getCetab());
                                    incau.setDele(new Long("0"));
                                    incau.setDar(java.sql.Date.valueOf(fic.getDate()));
                                    inCautionsList.add(incau);
                                } catch (ParseException ex) {
                                    System.out.println("error of collection");
                                    System.out.println(ex.getMessage());
                                }
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);
                                System.out.println(line);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    deleteDatas("incau", fic.getDate());
                    inCautionsRepository.save(inCautionsList);
                    inCautionsList.clear();

                } else if (codeFichier.equals("GAR")) {
                    int k = 0;
                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);

//                                InGaranties inGar = new InGaranties(data[22], data[0], data[1], data[2], data[3],
//                                        data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11],
//                                        data[12], data[13], data[14], data[15], data[16], data[17], data[18], data[19],
//                                        data[20], data[21], Long.valueOf(0), java.sql.Date.valueOf(fic.getDate()));
                                InGaranties inGar = new InGaranties();
                                inGar.setAge(data[0]);
                                inGar.setCom(data[1]);
                                inGar.setCle(data[2]);
                                inGar.setDev(data[3]);
                                inGar.setCli(data[4]);
                                inGar.setChap(data[5]);
                                inGar.setRef(data[6]);
                                inGar.setTyp(data[7]);
                                inGar.setDmep(convertStringDate(data[8], fic));
                                inGar.setDdeb(convertStringDate(data[9], fic));
                                inGar.setDfin(convertStringDate(data[10], fic));
                                inGar.setMon(convertStringDouble(data[11]));
                                inGar.setMaf(convertStringDouble(data[12]));
                                inGar.setValneg(convertStringDouble(data[13]));
                                inGar.setValest(convertStringDouble(data[14]));
                                inGar.setValg(convertStringDouble(data[15]));
                                inGar.setValcpt(convertStringDouble(data[16]));
                                inGar.setValacq(convertStringDouble(data[17]));
                                inGar.setEng(data[18]);
                                inGar.setTeng(data[19]);
                                inGar.setTaff(data[20]);
                                inGar.setEta(data[21]);
                                inGar.setDlev(convertStringDate(data[22], fic));
                                inGar.setCetab(fic.getCetab());
                                inGar.setDele(new Long("0"));
                                inGar.setDar(java.sql.Date.valueOf(fic.getDate()));
                                inGarantiesList.add(inGar);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("ingar", fic.getDate());
                        inGarantiesRepository.save(inGarantiesList);
                        inGarantiesList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("ECH")) {
                    int k = 0;
                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);

//                                Inech inech = new Inech(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        data[17], data[18], data[19], data[20], data[21], data[22], data[23],
//                                        Long.valueOf(data[24]), data[25], java.sql.Date.valueOf(fic.getDate()));
                                Inech inech = new Inech();
                                inech.setAge(toSting(data[0]));
                                inech.setRef(toSting(data[1]));
                                inech.setEch(toSting(data[2]));
                                inech.setTyp(toSting(data[3]));
                                inech.setNum(convertStringDouble(data[4]));
                                inech.setDech(convertStringDate(data[5], fic));
                                inech.setMon(convertStringDouble(data[6]));
                                inech.setMonp(convertStringDouble(data[7]));
                                inech.setMimp(convertStringDouble(data[8]));
                                inech.setDcre(convertStringDate(data[9], fic));
                                inech.setDmod(convertStringDate(data[10], fic));
                                inech.setUticre(toSting(data[11]));
                                inech.setUtimod(toSting(data[12]));
                                inech.setCetab(fic.getCetab());
                                inech.setDele(new Long("0"));
                                inech.setDar(java.sql.Date.valueOf(fic.getDate()));
                                inechList.add(inech);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inech", fic.getDate());
                        inechRepository.save(inechList);
                        inechList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("BDC")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            if (line.length() == 0) {
                                line = null;
                            } else {
                                String[] data = line.split("\\|", -1);

//                                Inbdc inbdc = new Inbdc(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        data[17], data[18], Long.valueOf(data[19]), data[20],
//                                        java.sql.Date.valueOf(fic.getDate())
//                                );
                                Inbdc inbdc = new Inbdc();
                                inbdc.setAge(toSting(data[0]));
                                inbdc.setCom(toSting(data[1]));
                                inbdc.setCle(toSting(data[2]));
                                inbdc.setDev(toSting(data[3]));
                                inbdc.setCha(toSting(data[4]));
                                inbdc.setCli(toSting(data[5]));
                                inbdc.setNumsou(toSting(data[6]));
                                inbdc.setRef(toSting(data[7]));
                                inbdc.setTyp(toSting(data[8]));
                                inbdc.setDmep(convertStringDate(data[9], fic));
                                inbdc.setDdeb(convertStringDate(data[10], fic));
                                inbdc.setDfin(convertStringDate(data[11], fic));
                                inbdc.setMont(convertStringDouble(data[12]));
                                inbdc.setNan(toSting(data[13]));
                                inbdc.setPer(toSting(data[14]));
                                inbdc.setTau(convertStringDouble(data[15]));
                                inbdc.setCetab(fic.getCetab());
                                inbdc.setDele(new Long("0"));
                                inbdc.setDar(java.sql.Date.valueOf(fic.getDate()));
                                inbdcList.add(inbdc);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inbdc", fic.getDate());
                        inbdcRepository.save(inbdcList);
                        inbdcList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("DAT")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                Indat indat = new Indat(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        data[17], data[18], Long.valueOf(data[19]), data[20],
//                                        java.sql.Date.valueOf(fic.getDate())
//                                );
                                Indat indat = new Indat();
                                indat.setAge(toSting(data[0]));
                                indat.setCom(toSting(data[1]));
                                indat.setCle(toSting(data[2]));
                                indat.setDev(toSting(data[3]));
                                indat.setCha(toSting(data[4]));
                                indat.setCli(toSting(data[5]));
                                indat.setNumsou(toSting(data[6]));
                                indat.setRef(toSting(data[7]));
                                indat.setTyp(toSting(data[8]));
                                indat.setDmep(convertStringDate(data[9], fic));
                                indat.setDdeb(convertStringDate(data[10], fic));
                                indat.setDfin(convertStringDate(data[11], fic));
                                indat.setMont(convertStringDouble(data[12]));
                                indat.setNan(toSting(data[13]));
                                indat.setPer(toSting(data[14]));
                                indat.setTau(convertStringDouble(data[15]));
                                indat.setDcre(convertStringDate(data[16], fic));
                                indat.setDmod(convertStringDate(data[17], fic));
                                indat.setUticre(toSting(data[18]));
                                indat.setUtimod(toSting(data[19]));
                                indat.setCetab(fic.getCetab());
                                indat.setDele(new Long("0"));
                                indat.setDar(java.sql.Date.valueOf(fic.getDate()));
                                indatList.add(indat);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }

                        deleteDatas("indat", fic.getDate());
                        indatRepository.save(indatList);
                        indatList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("CREDOC")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("REMDOC")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("TRANS")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                Intran intran = new Intran(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        Long.valueOf(data[17]), data[18], java.sql.Date.valueOf(fic.getDate())
//                                );
                                Intran intran = new Intran();
                                intran.setAge(toSting(data[0]));
                                intran.setCom(toSting(data[1]));
                                intran.setCle(toSting(data[2]));
                                intran.setDev(toSting(data[3]));
                                intran.setCli(toSting(data[4]));
                                intran.setRef(toSting(data[5]));
                                intran.setTyp(toSting(data[6]));
                                intran.setDmep(convertStringDate(data[7], fic));
                                intran.setPdes(toSting(data[8]));
                                intran.setMot(toSting(data[9]));
                                intran.setMont(convertStringDouble(data[10]));
                                intran.setMoncv(convertStringDouble(data[11]));
                                intran.setTau(convertStringDouble(data[12]));
                                intran.setCetab(fic.getCetab());
                                intran.setDele(new Long("0"));
                                intran.setDar(java.sql.Date.valueOf(fic.getDate()));
                                intranList.add(intran);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("intran", fic.getDate());
                        intransRepository.save(intranList);
                        intranList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("RAPAT")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                InRapartriements inrapa = new InRapartriements(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        Long.valueOf(data[17]), data[18], java.sql.Date.valueOf(fic.getDate())
//                                );
                                InRapartriements inrapa = new InRapartriements();
                                inrapa.setAge(toSting(data[0]));
                                inrapa.setCom(toSting(data[1]));
                                inrapa.setCle(toSting(data[2]));
                                inrapa.setDev(toSting(data[3]));
                                inrapa.setCli(toSting(data[4]));
                                inrapa.setRef(toSting(data[5]));
                                inrapa.setTyp(toSting(data[6]));
                                inrapa.setDmep(convertStringDate(data[7], fic));
                                inrapa.setPdes(toSting(data[8]));
                                inrapa.setMot(toSting(data[9]));
                                inrapa.setMont(convertStringDouble(data[10]));
                                inrapa.setMoncv(convertStringDouble(data[11]));
                                inrapa.setTau(convertStringDouble(data[12]));
                                inrapa.setDcre(convertStringDate(data[13], fic));
                                inrapa.setDmod(convertStringDate(data[14], fic));
                                inrapa.setUticre(toSting(data[15]));
                                inrapa.setUtimod(toSting(data[16]));

                                inrapa.setCetab(fic.getCetab());
                                inrapa.setDele(new Long("0"));
                                inrapa.setDar(java.sql.Date.valueOf(fic.getDate()));
                                inrapaList.add(inrapa);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inrapa", fic.getDate());
                        inrapatRepository.save(inrapaList);
                        inrapaList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("OPCH")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("MARCH")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("TITRE")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {

                                String[] data = line.split("\\|", -1);

                                Intitre intitre = new Intitre(
                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
                                        data[17], data[18], data[19], data[20], data[21], data[22],
                                        Long.valueOf(data[23]), data[24], java.sql.Date.valueOf(fic.getDate())
                                );
                                intitreList.add(intitre);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);

                            }
                        }
                        deleteDatas("intitre", fic.getDate());
                        intitreRepository.save(intitreList);
                        intitreList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("OPIB")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

                                InOib inOib = new InOib(
                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
                                        data[17], data[18], data[19], data[20], data[21], data[22],
                                        Long.valueOf(data[23]), data[24], java.sql.Date.valueOf(fic.getDate())
                                );
                                inOibList.add(inOib);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inoib", fic.getDate());
                        inOibRepository.save(inOibList);
                        inOibList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("WALLET")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("IMP")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("CRED")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                Incredit incredit = new Incredit(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], Double.valueOf(data[13]), data[14],
//                                        data[15], data[16], data[17], data[18], data[19], data[20], data[21], data[22],
//                                        data[23], Long.valueOf(data[24]), data[25], java.sql.Date.valueOf(fic.getDate())
//                                );
                                Incredit incredit = new Incredit();
                                incredit.setAge(toSting(data[0]));
                                incredit.setCome(toSting(data[1]));
                                incredit.setComc(toSting(data[2]));
                                incredit.setDev(toSting(data[3]));
                                incredit.setChae(toSting(data[4]));
                                incredit.setChac(toSting(data[5]));
                                incredit.setCli(toSting(data[6]));
                                incredit.setRef(toSting(data[7]));
                                incredit.setTyp(toSting(data[8]));
                                incredit.setNat(toSting(data[9]));
                                incredit.setDmep(convertStringDate(data[10], fic));
                                incredit.setDeb(convertStringDate(data[11], fic));
                                incredit.setFin(convertStringDate(data[12], fic));
                                incredit.setMont(convertStringDouble(data[13]));
                                incredit.setMoncv(convertStringDouble(data[14]));
                                incredit.setTdev(convertStringDouble(data[15]));
                                incredit.setTau(convertStringDouble(data[16]));
                                incredit.setTeg(convertStringDouble(data[17]));
                                incredit.setAnt(toSting(data[18]));
                                incredit.setPer(toSting(data[19]));
                                incredit.setNbe(convertStringDouble(data[20]));
                                incredit.setCetab(fic.getCetab());
                                incredit.setDele(new Long("0"));
                                incredit.setDar(java.sql.Date.valueOf(fic.getDate()));
                                increditList.add(incredit);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("incre", fic.getDate());
                        increditRepository.save(increditList);
                        increditList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("MONET")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);

                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    try {
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    read.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                }

//				try {
                System.out.println("SPLIT FILE NAME............................");
                String[] data2 = allFileNamList.get(j).getFilename().split("\\.", -1);
                System.out.println("WAY OF THE FILE TO RENAME............................"
                        + parameters.get("invEncours") + allFileNamList.get(j).getFilename());
                System.out.println(parameters.get("invArchives"));
                System.out.println(data2[0]);
                System.out.println(getYearMonthTimeStamp1());
                System.out.println(data2[1]);
                System.out.println(parameters.get("invArchives") + data2[0] + '.' + data2[1]);
                System.out.println(
                        parameters.get("invArchives") + data2[0] + "_" + getYearMonthTimeStamp1() + '.' + data2[1]);
//					channelSftp.rename(parameters.get("invEncours") + allFileNamList.get(j).getFilename(),
//							parameters.get("invArchives") + data2[0] + "_" + getYearMonthTimeStamp() + '.' + data2[1]);
//					channelSftp.rename(parameters.get("invEncours") + allFileNamList.get(j).getFilename(),
//							parameters.get("invArchives") + data2[0] + '.' + data2[1]);
//				} catch (SftpException ex) {
//					Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//				}
//				try {
//					channelSftp.cd(parameters.get("invEncours"));
                // liveReportingService.endDetailsReportingToTheVue(idTrait,codeFichier, statut,
                // Long.valueOf(count));
                System.out.println("AFTERENDING DETAILS" + idTrait);

                // count=Long.valueOf(0);
//				} catch (SftpException ex) {
//					Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//				}
                liveReportingService.endDetailsReportingToTheVue1(idTrait, codeFichier, statut, Long.valueOf(count),
                        nombreTotal);
                nombreOpeTraite++;
                quotien = Long.valueOf(1);
                count = Long.valueOf(0);

            }

        } else {

            statutope = Long.valueOf(1);
            System.out.println("NO DATA TREATED" + idOpe);
            System.out.println("BEFORE GLOBALENDING" + idOpe);
            liveReportingService.endGobalReportingToTheVue(idOpe, statutope, Long.valueOf(0));
            nombreOpeTraite = Long.valueOf(0);
            System.out.println("end of process-----------");//
            return "1";

        }
        // }

        statutope = Long.valueOf(1);
        System.out.println("BEFORE GLOBALENDING" + idOpe);
        liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
        nombreOpeTraite = Long.valueOf(0);
        System.out.println("end of process-----------");//
        return "1";

    }

    private Date convertStringDate(String e, DataIntegrationForm f) {
        try {
            return e.equals("")
                    ? java.sql.Date.valueOf(f.getDate())
                    : StringtoDate(e);
        } catch (ParseException ex) {
            Logger.getLogger(ChargerDonneesServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error of collection");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private Double convertStringDouble(String e) {
        return Double.valueOf(e.equals("") ? "0" : e);
    }

    private String toSting(String e) {
        return e;
    }

    public String DuplicatePreviousData(DataIntegrationForm fic) {

        List<invFileToIntegrate> allFileNamList = new ArrayList<invFileToIntegrate>();
        // recuperation des données a ecrire sur le fichier
        try {
            // recuperation des paramètres
            parameters = getGenerationAndSavingParam();
            /*
			 * if(parameters.get("sameServer").equalsIgnoreCase("1")){ return
			 * chargerDonneesServiceLocal.writeInDataBaseSystem(fic); }
             */
            inventaireList = getInventairelist();
            idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(), fic.getCetab(), fic.getUsid(),
                    fic.getOperation(), Long.valueOf(fic.getCodeInventaires().size()));
            minimum = Long.valueOf(parameters.get("minimumNumber"));

        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }

        // acces au fichier pour ecriture
        BufferedReader read = null;
        JSch jsch2 = new JSch();

        try {
            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"),
                    Integer.parseInt(parameters.get("port")));
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }

        session.setPassword(parameters.get("pass"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
        try {
            session.connect();
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Session connected");
        try {
            channel = session.openChannel("sftp");
        } catch (JSchException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            channel.connect();
        } catch (JSchException ex) {

            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connection Opened\n");
        channelSftp = (ChannelSftp) channel;
//		try {
//			// invEncours invArchives invErreurs
//			channelSftp.cd(parameters.get("invEncours"));
//
//		} catch (SftpException ex) {
//			Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//		}

        // controler le nombre d inventaires envoyé.
        System.out.println("CODE INVENTAIRE SIZE/////////////" + fic.getCodeInventaires().size());
        System.out.println(fic.getCodeInventaires());

        for (int z = 0; z < fic.getCodeInventaires().size(); z++) {// LIST OF CODES FOR TAKEN FROM THE VIEW

            String ispresent = "NO";
            try {
                // System.out.println("DANS LE REPERTOIRE TEMPORAIRE///////////////");
                Vector fileList = channelSftp.ls(parameters.get("invEncours"));
                // System.out.println("DANS LE REPERTOIRE TEMPORAIRE///////////////");

                for (int ll = 0; ll < fileList.size(); ll++) {
                    String[] data = fileList.get(ll).toString().split(" ", -1);
                    for (String filename : data) {
                        System.out.println("NAME OF THE FILE TO SEARCH: " + String
                                .valueOf(fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())));
                        System.out.println("NAME OF THE FILE TO SEARCHAA: " + filename);
//                        if(filename.contains(String.valueOf(fic.getCodeInventaires().get(z).getCode()+getYearMonthDayp(fic.getDate())))){
                        if (filename.contains(String.valueOf(
                                fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())))) {
                            System.out.println("NAME OF THE FILE TO SEARCH INSIDE: " + String.valueOf(
                                    fic.getCodeInventaires().get(z).getCode() + getYearMonthDayp(fic.getDate())));
                            ispresent = "YES";
                            invFileToIntegrate allFileNam = new invFileToIntegrate(filename,
                                    String.valueOf(fic.getCodeInventaires().get(z).getCode()), ispresent);
                            if (allFileNamList.contains(allFileNam)) {
                            } else {
                                allFileNamList.add(allFileNam);
                            }
                        }

                    }
                }
                if (ispresent.equalsIgnoreCase("NO")) {

                    System.out.println(
                            " NO FILES FOUND FOR INVETORY: " + String.valueOf(fic.getCodeInventaires().get(z).getCode())
                            + " AT THE DATE " + fic.getDate());

                    idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,
                            fic.getCodeInventaires().get(z).getCode(), Long.valueOf(0));
                    liveReportingService.endDetailsReportingToTheVue(idTrait, fic.getCodeInventaires().get(z).getCode(),
                            Long.valueOf(1), Long.valueOf(0));
                    // invFileToIntegrate allFileNam= new invFileToIntegrate("NO
                    // FILE",String.valueOf(fic.getCodeInventaires().get(i)),ispresent);
                    // allFileNamList.add(allFileNam);
                }
                // }
            } catch (SftpException ex) {
                Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("STATUT allFileNamList OK ///////////////////////////////////" + allFileNamList.size());
        if (allFileNamList.size() > 0) {
            for (int j = 0; j < allFileNamList.size(); j++) {
                System.out.println("CODE FICHIER " + allFileNamList.get(j).getFilecode());
                System.out.println("FILE NAME TO BE TREATED " + allFileNamList.get(j).getFilename());

            }

            for (int j = 0; j < allFileNamList.size(); j++) {

                codeFichier = allFileNamList.get(j).getFilecode();
                System.out.println("CODE FICHIER " + codeFichier);
                System.out.println("FILE NAME " + allFileNamList.get(j).getFilecode());
                InputStream in = null;
                try {
                    String n = parameters.get("invEncours") + allFileNamList.get(j).getFilename();
                    System.out.println("CHEMIN PENDING " + n);
                    in = channelSftp.get(n);
                } catch (SftpException ex) {
                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                }
                // writer = new BufferedWriter(new OutputStreamWriter(out));
                // writer = new BufferedWriter(new OutputStreamWriter(out));

                BufferedReader readr = null;
                try {
                    readr = new BufferedReader(new InputStreamReader(
                            channelSftp.get(parameters.get("invEncours") + allFileNamList.get(j).getFilename())));
                } catch (SftpException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                int pf = 0;
                String line1 = null;
                try {
                    while ((line1 = readr.readLine()) != null) {
                        pf++;
                    }
                    nombreTotal = (long) pf;
                    System.out.println("---------------------------------------------------");
                    System.out.println(nombreTotal);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                read = new BufferedReader(new InputStreamReader(in));
                String line = null;
                idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe, codeFichier, Long.valueOf(1));
                if (codeFichier.equals("BAL")) {
                    System.out.println("inside BAL :");

                    // String sql1 = "delete from inbal where dar ='"+fic.getDate()+"'";
                    deleteDatas("inbal", fic.getDate());
//                    String sql1 = "delete from inbal where dar ='A1'";
//                    jdbcTemplate.execute(sql1);
//                    
                    // inBalanceRepository2.deleteInBalanceByDar("A1");
                    System.out.println("BEFORE SAVING LIST BAL");
                    // save inbal duplicate here
                    System.out.println("AFTER SAVING LIST BAL");
                } else if (codeFichier.equals("SOLDE")) {

                } else if (codeFichier.equals("AUTO")) {

                } else if (codeFichier.equals("CLI")) {

                } else if (codeFichier.equals("COM")) {

                } else if (codeFichier.equals("CAU")) {

                } else if (codeFichier.equals("GAR")) {

                } else if (codeFichier.equals("ECH")) {

                } else if (codeFichier.equals("BDC")) {

                } else if (codeFichier.equals("DAT")) {

                } else if (codeFichier.equals("CREDOC")) {

                } else if (codeFichier.equals("REMDOC")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("TRANS")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                Intran intran = new Intran(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        Long.valueOf(data[17]), data[18], java.sql.Date.valueOf(fic.getDate())
//                                );
                                Intran intran = new Intran();
                                intran.setAge(toSting(data[0]));
                                intran.setCom(toSting(data[1]));
                                intran.setCle(toSting(data[2]));
                                intran.setDev(toSting(data[3]));
                                intran.setCli(toSting(data[4]));
                                intran.setRef(toSting(data[5]));
                                intran.setTyp(toSting(data[6]));
                                intran.setDmep(convertStringDate(data[7], fic));
                                intran.setPdes(toSting(data[8]));
                                intran.setMot(toSting(data[9]));
                                intran.setMont(convertStringDouble(data[10]));
                                intran.setMoncv(convertStringDouble(data[11]));
                                intran.setTau(convertStringDouble(data[12]));
                                intran.setCetab("10025");
                                intran.setDele(new Long("0"));
                                intran.setDar(java.sql.Date.valueOf(fic.getDate()));
                                intranList.add(intran);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("intran", fic.getDate());
                        intransRepository.save(intranList);
                        intranList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("RAPAT")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                InRapartriements inrapa = new InRapartriements(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
//                                        Long.valueOf(data[17]), data[18], java.sql.Date.valueOf(fic.getDate())
//                                );
                                InRapartriements inrapa = new InRapartriements();
                                inrapa.setAge(toSting(data[0]));
                                inrapa.setCom(toSting(data[1]));
                                inrapa.setCle(toSting(data[2]));
                                inrapa.setDev(toSting(data[3]));
                                inrapa.setCli(toSting(data[4]));
                                inrapa.setRef(toSting(data[5]));
                                inrapa.setTyp(toSting(data[6]));
                                inrapa.setDmep(convertStringDate(data[7], fic));
                                inrapa.setPdes(toSting(data[8]));
                                inrapa.setMot(toSting(data[9]));
                                inrapa.setMont(convertStringDouble(data[10]));
                                inrapa.setMoncv(convertStringDouble(data[11]));
                                inrapa.setTau(convertStringDouble(data[12]));
                                inrapa.setDcre(convertStringDate(data[13], fic));
                                inrapa.setDmod(convertStringDate(data[14], fic));
                                inrapa.setUticre(toSting(data[15]));
                                inrapa.setUtimod(toSting(data[16]));

                                inrapa.setCetab("10025");
                                inrapa.setDele(new Long("0"));
                                inrapa.setDar(java.sql.Date.valueOf(fic.getDate()));
                                inrapaList.add(inrapa);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inrapa", fic.getDate());
                        inrapatRepository.save(inrapaList);
                        inrapaList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("OPCH")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("MARCH")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("TITRE")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {

                                String[] data = line.split("\\|", -1);

                                Intitre intitre = new Intitre(
                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
                                        data[17], data[18], data[19], data[20], data[21], data[22],
                                        Long.valueOf(data[23]), data[24], java.sql.Date.valueOf(fic.getDate())
                                );
                                intitreList.add(intitre);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);

                            }
                        }
                        deleteDatas("intitre", fic.getDate());
                        intitreRepository.save(intitreList);
                        intitreList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("OPIB")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

                                InOib inOib = new InOib(
                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
                                        data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16],
                                        data[17], data[18], data[19], data[20], data[21], data[22],
                                        Long.valueOf(data[23]), data[24], java.sql.Date.valueOf(fic.getDate())
                                );
                                inOibList.add(inOib);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("inoib", fic.getDate());
                        inOibRepository.save(inOibList);
                        inOibList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (codeFichier.equals("WALLET")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("IMP")) {

                    try {
                        while ((line = read.readLine()) != null) {

                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("CRED")) {

                    try {

                        while ((line = read.readLine()) != null) {

                            if (line.length() == 0) {
                                line = null;

                            } else {
                                String[] data = line.split("\\|", -1);

//                                Incredit incredit = new Incredit(
//                                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8],
//                                        data[9], data[10], data[11], data[12], Double.valueOf(data[13]), data[14],
//                                        data[15], data[16], data[17], data[18], data[19], data[20], data[21], data[22],
//                                        data[23], Long.valueOf(data[24]), data[25], java.sql.Date.valueOf(fic.getDate())
//                                );
                                Incredit incredit = new Incredit();
                                incredit.setAge(toSting(data[0]));
                                incredit.setCome(toSting(data[1]));
                                incredit.setComc(toSting(data[2]));
                                incredit.setDev(toSting(data[3]));
                                incredit.setChae(toSting(data[4]));
                                incredit.setChac(toSting(data[5]));
                                incredit.setCli(toSting(data[6]));
                                incredit.setRef(toSting(data[7]));
                                incredit.setTyp(toSting(data[8]));
                                incredit.setNat(toSting(data[9]));
                                incredit.setDmep(convertStringDate(data[10], fic));
                                incredit.setDeb(convertStringDate(data[11], fic));
                                incredit.setFin(convertStringDate(data[12], fic));
                                incredit.setMont(convertStringDouble(data[13]));
                                incredit.setMoncv(convertStringDouble(data[14]));
                                incredit.setTdev(convertStringDouble(data[15]));
                                incredit.setTau(convertStringDouble(data[16]));
                                incredit.setTeg(convertStringDouble(data[17]));
                                incredit.setAnt(toSting(data[18]));
                                incredit.setPer(toSting(data[19]));
                                incredit.setNbe(convertStringDouble(data[20]));
                                incredit.setCetab("10025");
                                incredit.setDele(new Long("0"));
                                incredit.setDar(java.sql.Date.valueOf(fic.getDate()));
                                increditList.add(incredit);
                                count++;
                                quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier,
                                        nombreTotal, Long.valueOf(count), quotien, minimum);

                                System.out.println(line);
                            }
                        }
                        deleteDatas("incre", fic.getDate());
                        increditRepository.save(increditList);
                        increditList.clear();

                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (codeFichier.equals("MONET")) {

                    try {
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);

                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    try {
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                            count++;
                            quotien = liveReportingService.detailsReportingToTheVue(idTrait, codeFichier, nombreTotal,
                                    Long.valueOf(count), quotien, minimum);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    read.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
                }

//				try {
                System.out.println("SPLIT FILE NAME............................");
                String[] data2 = allFileNamList.get(j).getFilename().split("\\.", -1);
                System.out.println("WAY OF THE FILE TO RENAME............................"
                        + parameters.get("invEncours") + allFileNamList.get(j).getFilename());
                System.out.println(parameters.get("invArchives"));
                System.out.println(data2[0]);
                System.out.println(getYearMonthTimeStamp1());
                System.out.println(data2[1]);
                System.out.println(parameters.get("invArchives") + data2[0] + '.' + data2[1]);
                System.out.println(
                        parameters.get("invArchives") + data2[0] + "_" + getYearMonthTimeStamp1() + '.' + data2[1]);
//					channelSftp.rename(parameters.get("invEncours") + allFileNamList.get(j).getFilename(),
//							parameters.get("invArchives") + data2[0] + "_" + getYearMonthTimeStamp() + '.' + data2[1]);
//					channelSftp.rename(parameters.get("invEncours") + allFileNamList.get(j).getFilename(),
//							parameters.get("invArchives") + data2[0] + '.' + data2[1]);
//				} catch (SftpException ex) {
//					Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//				}
//				try {
//					channelSftp.cd(parameters.get("invEncours"));
                // liveReportingService.endDetailsReportingToTheVue(idTrait,codeFichier, statut,
                // Long.valueOf(count));
                System.out.println("AFTERENDING DETAILS" + idTrait);

                // count=Long.valueOf(0);
//				} catch (SftpException ex) {
//					Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//				}
                liveReportingService.endDetailsReportingToTheVue1(idTrait, codeFichier, statut, Long.valueOf(count),
                        nombreTotal);
                nombreOpeTraite++;
                quotien = Long.valueOf(1);
                count = Long.valueOf(0);

            }

        } else {

            statutope = Long.valueOf(1);
            System.out.println("NO DATA TREATED" + idOpe);
            System.out.println("BEFORE GLOBALENDING" + idOpe);
            liveReportingService.endGobalReportingToTheVue(idOpe, statutope, Long.valueOf(0));
            nombreOpeTraite = Long.valueOf(0);
            System.out.println("end of process-----------");//
            return "1";

        }
        // }

        statutope = Long.valueOf(1);
        System.out.println("BEFORE GLOBALENDING" + idOpe);
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

    public String getYearMonthTimeStamp() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String newstring = new SimpleDateFormat("yyyyMM").format(date) + "_" + (new Timestamp(date.getTime()));
        return newstring;

    }

    public String getYearMonthTimeStamp1() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String newstring = new SimpleDateFormat("yyyyMMdd").format(date);
        return newstring;

    }

    public String getYearMonthDay() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String newstring = new SimpleDateFormat("yyyyMMdd").format(date);
        return newstring;

    }

    public Date getdate(String d) {
        System.out.println("date date date " + d);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(d);
        } catch (ParseException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date1;

    }

    public String getYearMonthDayp(String d) throws ParseException {
        // System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz: "+d);
        if (d != null) {
            // Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
//        	Date date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(d));
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
            // Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(d);
            String newstring = new SimpleDateFormat("yyyyMMdd").format(date);
            // DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
            // Date date = new Date();
            return newstring;
        }
        return "";

    }
//     

    public String getYearMonth2(Date d) throws ParseException {

        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(d));
        // Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
        // Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(d);
        String newstring = new SimpleDateFormat("yyyyMM").format(date);
        // DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        // Date date = new Date();
        return newstring;

    }

////     
//   public static void main (String[] args) {
//        
//        String date2 ="31/10/2019";
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
//        Date date = new Date();
//        System.out.println("START date");
//        System.out.println(dateFormat.format(date));
//        
//	 System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        System.out.println("START fic");
////        List<String> nums = new ArrayList<String>() {{ add("BAL"); add("SOLDE");}};
// //       System.out.println("END fic");
////        ClientToSystemForm fic=new ClientToSystemForm(date,"O","M",nums);
////        DataIntegrationForm fic2=new DataIntegrationForm(date,"O","M",nums);
////        ChargerDonneesServiceImpl chr=new ChargerDonneesServiceImpl();
//        System.out.println("START TESTING");
//        //chr.extrairesFromClientDatabase(fic);
//    //    chr.writeInDataBaseSystem(fic2);
//        System.out.println("END TESTING"); 
//        System.out.println("END TESTING"); 
//    }
//   
//   
    @Transactional
    public void saveInBalance(List<InBalance> inBalance) {
        int size = inBalance.size();
        int counter = 0;

        List<InBalance> temp = new ArrayList<>();

        for (InBalance emp : inBalance) {
            temp.add(emp);

            if ((counter + 1) % 500 == 0 || (counter + 1) == size) {
                System.out.println("dddddddddddddddddddddddddddddddddddddd" + emp);
                System.out.println(counter);
                inBalanceRepository.save(temp);
                temp.clear();
            }
            counter++;
        }
    }

    /*
	 * public ChargerDonneesServiceImpl(ClientToSystemForm fic) { // store parameter
	 * for later user this.fic32 = fic;
	 * 
	 * }
	 * 
	 * @Override public void run() { Thread.sleep(15000);
	 * chargerDonneesService.extrairesFromClientDatabase(fic32); throw new
	 * UnsupportedOperationException("Not supported yet."); //To change body of
	 * generated methods, choose Tools | Templates. }
     */
    public Consql getCon() {

        System.out.println("get connexion param: ");
        // String FILENAME = "/xch/IWOMICORE/con.txt";
        // String FILENAME = "/xch/IWOMI/conIWOMI.txt";
        String FILENAME = "c:\\xch\\iwomiCon.txt";
        Consql conn = new Consql();
        BufferedReader bufferedreader = null;
        FileReader filereader = null;
        System.out.println("get connexion param : ");
        try {
            filereader = new FileReader(FILENAME);
            bufferedreader = new BufferedReader(filereader);
            String strCurrentLine;
            int i = 0;
            System.out.println("get connexion param : ");
            while ((strCurrentLine = bufferedreader.readLine()) != null) {
                System.out.println("get connexion param : " + 1);
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
                System.out.println(strCurrentLine);
                System.out.println("connexion param: " + tab[1]);
                System.out.println("connexion param: " + tab[1]);
                System.out.println("connexion param: " + tab[1]);
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

    public LocalDate formatDate(String d) {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld2 = LocalDate.parse(d, f);
        System.out.println("ggg " + ld2);
        return ld2;

    }

    public String getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            System.out.println("BEFORE CONNEXION");
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("AFTER CONNEXION");
        } catch (SQLException ex) {
//                    Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = (Statement) connection.createStatement();
        return "";
    }

//	public Date StringtoDate(String s) throws ParseException {
//		return new SimpleDateFormat("dd/MM/yyyy").parse(s);
//	}
    public Date StringtoDate(String s) throws ParseException {
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return new SimpleDateFormat("dd/MM/yyyy").parse(s);
        } else {
            return null;
        }
    }

    public static int countLines(File aFile) throws IOException {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(aFile));
            while ((reader.readLine()) != null)
				;
            return reader.getLineNumber();
        } catch (Exception ex) {
            return -1;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String getDiffDate(Date d1) {

        // String dateStart = "01/14/2012 09:29:58";
        String valtime = "";
        Date d2 = Calendar.getInstance().getTime();
//      DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");  
//      String dateStop = dateFormat.format(date);  
        // String dateStop = "01/15/2012 10:31:48";
        // HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // Date d1 = null;
        // Date d2 = new Date();

        try {
//d1 = format.parse(dateStart);
//d2 = format.parse(dateStop);
//in milliseconds

            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            valtime = diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
                    + " seconds.";

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return valtime;

    }

    public ReportResultFom returnLivereport(LiveReportForm liveReport) {

        String timeSpend = "";
        System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
        List<LiveTraitement> trait = liveTraitementRepository
                .findLiveTraitementByCodeUniqueAndCodefichier(liveReport.getCodeUnique(), liveReport.getCodeFichier());
        System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
        LiveOperation op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
        String filevalue = "";
        String opvalue = "";
        System.out.println("AFTER OPERATION LiveReportingServiceImpl returnLivereport 47 ");

        if (trait.get(0).getNbtotal().equalsIgnoreCase("0")) {

            filevalue = "EMPTY";

        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            filevalue = String.valueOf(
                    (Long.valueOf(trait.get(0).getNbtraite()) * 100) / Long.valueOf(trait.get(0).getNbtotal()));
        }

        if (op.getNbtotal() == 0) {

            opvalue = "EMPTY";

        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());
            timeSpend = getDiffDate(trait.get(0).getDateDebut());
        }
        System.out.println("AFTER filevalue " + filevalue);// percentage done
        System.out.println("AFTER getNbtraite " + trait.get(0).getNbtraite());
        System.out.println("AFTER getNbtotal " + trait.get(0).getNbtotal());
        System.out.println("AFTER opvalue " + opvalue);
        System.out.println(" Before returning response LiveReportingServiceImpl returnLivereport 56");
        return new ReportResultFom("1", liveReport.getCodeFichier(), String.valueOf(trait.get(0).getStatut()),
                filevalue, String.valueOf(op.getStatut()), opvalue, timeSpend);

    }

}

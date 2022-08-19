/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import iwomi.base.ServiceInterface.ChargerDonneesServiceLocalS;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.form.ClientToSystemFormS;
import iwomi.base.form.DataIntegrationFormS;
import iwomi.base.form.InventaireListFormS;
import iwomi.base.objects.InAutorisationDecouvert;
import iwomi.base.objects.InBalance;
import iwomi.base.objects.InCautions;
import iwomi.base.objects.InClients;
import iwomi.base.objects.InComptes;
import iwomi.base.objects.InGaranties;
import iwomi.base.objects.InSoldes;
import iwomi.base.repositories.InAutorisationDecouvertRepository;
import iwomi.base.repositories.InBalanceRepository;
import iwomi.base.repositories.InCautionsRepository;
import iwomi.base.repositories.InClientsRepository;
import iwomi.base.repositories.InComptesRepository;
import iwomi.base.repositories.InGarantiesRepository;
import iwomi.base.repositories.InSoldesRepository;
import iwomi.base.repositories.ReportDatasGeneratedRepositoryS;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabri
 */
@Service
@Component
public class ChargerDonneesServiceLocalImplS implements ChargerDonneesServiceLocalS{

    Connection connection,con = null;
//    public static final String USERNAME="root";
//    public static final String PASSWORD="ares";
//    public static final String CONN_STRING="jdbc:mysql://localhost/ares";  
    public static final String USERNAME="root";
    public static final String PASSWORD="root";
    public static final String CONN_STRING="jdbc:mysql://localhost/spectra";  
    String NOMENGABTABLE_SYS ="0012";
    String NOMMENCLATURE_FICH ="3020";
    String NOMMENCLATURE_INVENTAIRES ="2001";
    Map<String, String> PARAM = new HashMap<>();
    ReportDatasGeneratedRepositoryS reportDatasGeneratedRepository;
    List<InAutorisationDecouvert> inAutorisationDecouvertList= new ArrayList<InAutorisationDecouvert>();
    List<InBalance> inBalanceList= new ArrayList<InBalance>();
    List<InCautions> inCautionsList= new ArrayList<InCautions>();
    List<InClients> inClientsList= new ArrayList<InClients>();
    List<InComptes> inComptesList = new ArrayList<InComptes>();
    List<InGaranties> inGarantiesList= new ArrayList<InGaranties>();
    List<InSoldes> inSoldesList= new ArrayList<InSoldes>();
    Long idOpe,minimum;
    String idTrait,codeFichier;
    Long quotien = Long.valueOf(1);
    Long nombreTotal =Long.valueOf(1);
    Long count = Long.valueOf(0);
    Long statut =Long.valueOf(3);
    Long statutope =Long.valueOf(3);
    Long nombreOpeTraite =Long.valueOf(0);
    JdbcTemplate jdbcTemplate; 
    
    @Autowired
    InClientsRepository inClientsRepository;
    @Autowired
    InAutorisationDecouvertRepository inAutorisationDecouvertRepository;
    @Autowired
    InBalanceRepository inBalanceRepository;
    @Autowired
    InCautionsRepository inCautionsRepository;
    @Autowired
    InComptesRepository inComptesRepository;
    @Autowired
    InGarantiesRepository inGarantiesRepository;
    @Autowired
    InSoldesRepository inSoldesRepository;
    @Autowired
    LiveReportingServiceS  liveReportingService;   

     
    Map<String, String> parameters= new HashMap<>(); 
    Map<String, String> dropdown= new HashMap<>(); 
    int CONST=0,defineFileToSave=0;
    String fileName;
    String lineFile="";
    Session session = null;
    JSch jsch = new JSch();
    Channel channel=null;
    ChannelSftp channelSftp; 
    List<InventaireListFormS> inventaireList= new ArrayList<InventaireListFormS>();

    /**
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws JSONException
     */
    @Override
    public Map<String, String>  getGenerationAndSavingParam(){
       
        System.out.println("START GETTING PÃ„RAMS");
        String select = "";
        try {
            //  JSONObject obj = new JSONObject();
            //boolean val = false;
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("BEFORE CONNEXION");
            connection = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("AFTER CONNEXION");
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = null;
        try {
            stmt = (Statement) connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        select = "SELECT * FROM sanm  WHERE tabcd='"+NOMENGABTABLE_SYS+"' AND dele='0'";
        System.out.println("BEFORE QUERRY");
        ResultSet result = null;
        try {
            result = stmt.executeQuery(select);
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("AFTER QUERRY");

        try {
            while(result.next()) {
                
                if(result.getString("acscd").equalsIgnoreCase("0009")){
                    PARAM.put("idetab", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0010")){
                    PARAM.put("extention", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0011")){
                    PARAM.put("ip", result.getString("lib2"));
                    System.out.println("ip :"+result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0012")){
                    PARAM.put("port", result.getString("lib2"));
                    System.out.println("port :"+result.getString("lib2"));
                    
                }
                if(result.getString("acscd").equalsIgnoreCase("0013")){
                    PARAM.put("pass", result.getString("lib2"));
                    System.out.println("pass :"+result.getString("lib2"));
                    
                }
                if(result.getString("acscd").equalsIgnoreCase("0014")){
                    PARAM.put("user", result.getString("lib2"));
                    System.out.println("user :"+result.getString("lib2"));
                    
                }
                if(result.getString("acscd").equalsIgnoreCase("0015")){
                    PARAM.put("chemin", result.getString("lib2"));
                }
                
                if(result.getString("acscd").equalsIgnoreCase("0016")){
                    PARAM.put("oracleUrl", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0017")){
                    PARAM.put("loginUrl", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0018")){
                    PARAM.put("passwordUrl", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0019")){
                    PARAM.put("invEncours", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0020")){
                    PARAM.put("invArchives", result.getString("lib2"));
                }
                if(result.getString("acscd").equalsIgnoreCase("0021")){
                    PARAM.put("invErreurs", result.getString("lib2"));
                }
                
                if(result.getString("acscd").equalsIgnoreCase("0029")){
                    PARAM.put("minimumNumber", result.getString("lib2"));  // 1e nombre minimum d element a traiter avant insertion dans la bd
                }
                
                /* if(result.getString("acscd").equalsIgnoreCase("0022")){
                PARAM.put("portOracle", result.getString("lib2"));
                } */
                System.out.println("END DISTRIBUTION");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println("END GETGENARATION AND SAVING PARAMS");

        return PARAM;

    }
                
    public  List<InventaireListFormS> getInventairelist() throws SQLException, ClassNotFoundException, JSONException{
        
        System.out.println("START  getInventairelist");
        String select = "";
        //boolean val = false;
        Class.forName("com.mysql.jdbc.Driver");
        try {
           System.out.println("BEFORE  CONNECTION");
            connection = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("AFTER CONEXION AND BEFORE STATEMENT");
        Statement stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='"+NOMMENCLATURE_INVENTAIRES+"' AND dele='0'";
        System.out.println("AFTER STATEMENT BEFORE EXECUTING QUERRY");

        ResultSet result = stmt.executeQuery(select);
        System.out.println("AFTER EXECUTING QUERRY");
        while(result.next()) { 
           //  System.out.println(result.getString("acscd"));
             InventaireListFormS inv = new InventaireListFormS(result.getString("acscd"),result.getString("lib1"),result.getString("lib2"),result.getString("lib3"),result.getString("lib4"),Integer.valueOf(result.getString("taux1")),result.getString("lib5"),result.getString("cetab"));
             inventaireList.add(inv);
        }
        System.out.println("END  inventaireList");

        return inventaireList;

    }
     
     
                  
public String extrairesFromClientDatabase(ClientToSystemFormS fic) {
    
     System.out.println("START FUNCTION");
     idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(),fic.getCetab(), fic.getUsid(),fic.getOperation(),Long.valueOf(fic.getCodeInventaires().size()));

     try {  
            // recuperation Les inventaire paramÃ©trÃ©es
            inventaireList = getInventairelist();
            System.out.println("START INITIATE REPORTING");
            parameters= getGenerationAndSavingParam();
            minimum=Long.valueOf(parameters.get("minimumNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        // acces au fichier pour ecriture
     BufferedWriter writer = null;
//      Writer writer = null;
//      File file = new File("C:\\Users\\fabri\\Documents\\NetBeansProjects\\testSoft\\test4.txt");
//            writer = new BufferedWriter(new FileWriter(file));
//           // for(int i=0; i<fileDatas.length(); i++){
//               //CODE TO FETCH RESULTS AND WRITE FILE
//                writer.write(fileDatas); 
    
    System.out.println("BEFORE FOREEACH TOGENERATE THE INVENTORY FILES");
    System.out.println("LINE ALL INVENTORY CODE: "+fic.getCodeInventaires());
    for(int i =0; i< fic.getCodeInventaires().size();i++){
    System.out.println("INSIDE FIRST FOREACH");
       for (int j = 0; j<inventaireList.size();j++){
        System.out.println("INSIDE FIRST FOREACH");
            if(fic.getCodeInventaires().get(i).getCode().equalsIgnoreCase(inventaireList.get(j).getCodeInv())){
                
                System.out.println("INSIDE SECOND FOREACH");
                System.out.println("INSIDE SECOND FOREACH THE TREATMENT FOR CODE :"+fic.getCodeInventaires().get(i).getCode());
                 try {
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                    } catch (ClassNotFoundException e) {
                        System.out.println("Where is your Oracle JDBC Driver?");
                        e.printStackTrace();
                    }
                    try {
                       // connection = DriverManager.getConnection(url, login, password);
                       System.out.println("BEFORE ORACLE CONNEXION");
                       connection = DriverManager.getConnection(parameters.get("oracleUrl"), parameters.get("loginUrl"), parameters.get("passwordUrl"));
                       System.out.println("AFTER ORACLE CONNEXION");
                    } catch (SQLException e) {
                        System.out.println("Connection Failed! Check output console");
                        e.printStackTrace();
                    }
                    Statement stmt = null;
                    String query = "";
                    System.out.println("BEFORE ORACLE QUERRY");
                    System.out.println("CodeInventaire:"+fic.getCodeInventaires().get(i).getCode());

                    if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
                            //query = "select 'A'dar,'A'age, 'A'com,'A' cle,'A' dev,'A'cli,'A'chap,'A' sldd, 'A' sldcvd,'A' cumc,'A'cumd,'A'sldf,'A' sldcvf,'A'txb,'A'dcre,'A'dmod,'A' uticre,'A' utimod from inbal";                               
                            query = "select 'A1'dar,'A2'age, 'A3'com,'A4' cle,'A5' dev,'A6'cli,'A7'chap,'A8' sldd, 'A9' sldcvd,'A10' cumc,'A11'cumd,'A12'sldf,'A13' sldcvf,'A14'txb,'A15'dcre,'A16'dmod,'A17' uticre,'A18' utimod from dual";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){
                           // query = "select 'A'dar,'A'age, 'A'com,'A' cle,'A' dev,'A'cli,'A'chap,'A' sldd, 'A' sldcvd,'A' cumc,'A'cumd,'A'sldf,'A' sldcvf,'A'txb,'A'dcre,'A'dmod,'A' uticre,'A' utimod from dual";                               
                            query = "select 'A1'dar,'A2'age, 'A3'com,'A4' cle,'A5' dev,'A6'cli,'A7'chap,'A8' sldd, 'A9' sldcvd,'A10' cumc,'A11'cumd,'A12'sldf,'A13' sldcvf,'A14'txb,'A15'dcre,'A16'dmod,'A17' uticre,'A18' utimod from dual";                               

                            //  query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){

                            query = ""; 
                            
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){

                            query = "";                               

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){
                            query = "";                               

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){

                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){
                            query = "";                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){
                            query = "";
                    }else{
                        System.out.println("SHOULD NOT COUNT");
                            query = "";                               
                        // ecrire dans le repertoire des erreurs.pour le type de fichier
                    }
     
                try {
                        System.out.println("BEFORE ORACLE QUERRY EXECUTION BFORE STATEMENT ");
                        stmt = connection.createStatement();
                        System.out.println("BEFORE ORACLE QUERRY EXECUTION AFTER STATEMENT");
                        System.out.println("QUERRY:"+query);
                        ResultSet rs = stmt.executeQuery(query);
                        System.out.println("AFTER ORACLE QUERRY EXECUTION AFTER STATEMENT");
                 //       if(rs.next()){
                 int k =0;
                            while (rs.next()) {
                                k++;
                              System.out.println("INSIDE RESULTS" );
                          // preparation du fichier
                                    if(defineFileToSave==0){
                                         codeFichier=inventaireList.get(j).getCodeInv();
                                         System.out.println("AFTEREBEGINING DETAILS_"+j);
                                         //idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,inventaireList.get(j).getCodeInv(), nombreTotal);
                                         idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,codeFichier, nombreTotal);
                                         System.out.println("ID traitement"+idTrait);
                                         fileName= inventaireList.get(j).getNomFic();
                                         final File file = new File(fileName);
                                         OutputStream out = null;
                                      
                                         writer = new BufferedWriter(new FileWriter(parameters.get("invEncours")+file));
                                         defineFileToSave=1;
                                    }   
                                    // contruction de la ligne
                               //     if(CONST<inventaireList.get(j).getNbrColoneTable()) {
/////// A revoir         
                                        System.out.println("LINE "+fic.getCodeInventaires().get(i).getCode()+" :"+lineFile);

                                        if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
                                            
                                            lineFile=rs.getString("dar")+"|"+rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("sldd")+"|"+rs.getString("sldcvd")+"|"+rs.getString("cumc")+"|"+rs.getString("cumd")+"|"+rs.getString("sldf")+"|"+rs.getString("sldcvf")+"|"+rs.getString("txb")+"|"+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");
                                            System.out.println("LINE BAL: "+lineFile);
                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){
                                            System.out.println("LINE SOLDE: "+lineFile);
                                            lineFile=rs.getString("dar")+"|"+rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("sldd")+"|"+rs.getString("sldcvd")+"|"+rs.getString("cumc")+"|"+rs.getString("cumd")+"|"+rs.getString("sldf")+"|"+rs.getString("sldcvf")+"|"+rs.getString("txb")+"|"+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){

                                            lineFile=rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("ref")+"|"+rs.getString("typ")+"|"+rs.getString("dmep")+"|"+rs.getString("ddeb")+"|"+rs.getString("dfin")+"|"+rs.getString("mont")+"|"+rs.getString("eta")+"|"+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){

                                            lineFile=rs.getString("age")+"|"+rs.getString("cli")+"|"+rs.getString("nom")+"|"+rs.getString("pre")+"|"+rs.getString("sig")+"|"+rs.getString("rso")+"|"+rs.getString("nomc")+"|"+rs.getString("typ")+"|"+rs.getString("nper")+"|"+rs.getString("preper")+"|"+rs.getString("nmer")+"|"+rs.getString("premer")+"|"+rs.getString("dnai")+"|"+rs.getString("lnai")+"|"+rs.getString("typpie")+"|"+rs.getString("numpie")+"|"+rs.getString("ddel")+"|"+rs.getString("ldel")+"|"+rs.getString("dexp")+"|"+rs.getString("ncc")+"|"+rs.getString("nrc")+"|"+rs.getString("dnrc")+"|"+rs.getString("sec")+"|"+rs.getString("catn")+"|"+rs.getString("naema");

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){
                             
                                            lineFile=rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("lib")+"|"+rs.getString("dou")+"|"+rs.getString("dfe")+"|"+rs.getString("cfer")+"|"+rs.getString("shi")+"|"+rs.getString("sde")+"|"+rs.getString("dodb")+"|"+rs.getString("ddm")+"|"+rs.getString("ddc")+"|"+rs.getString("ddd")+"|"+rs.getString("cumc")+"|"+rs.getString("cumd")+"|"+rs.getString("chl1")+"|"+rs.getString("chl2")+"|"+rs.getString("chl3")+"|"+rs.getString("chl4")+"|"+rs.getString("chl5")+"|"+rs.getString("chl6")+"|"+rs.getString("dcre");


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){

                                            lineFile=rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("ref")+"|"+rs.getString("typ")+"|"+rs.getString("dmep")+"|"+rs.getString("ddeb")+"|"+rs.getString("dfin")+"|"+rs.getString("mont")+"|"+rs.getString("eta")+"|"+rs.getString("dlev")+"|"+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){

                                            lineFile=rs.getString("age")+"|"+rs.getString("com")+"|"+rs.getString("cle")+"|"+rs.getString("dev")+"|"+rs.getString("cli")+"|"+rs.getString("chap")+"|"+rs.getString("ref")+"|"+rs.getString("typ")+"|"+rs.getString("dmep")+"|"+rs.getString("ddeb")+"|"+rs.getString("dfin")+"|"+rs.getString("mont")+"|"+rs.getString("maf")+"|"+rs.getString("eng")+"|"+rs.getString("teng")+"|"+rs.getString("taff")+"|"+rs.getString("eta")+"|"+rs.getString("dlev")+"|"+rs.getString("dcre")+"|"+rs.getString("dmod")+"|"+rs.getString("uticre")+"|"+rs.getString("utimod");

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){



                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){
                                            

                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){


                                        }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){


                                        }else{

                                             // ecrire dans le repertoire des erreurs.pour le type de fichier
                                        }
                                       // CONST++;
                                 //   } else {
                                 
                                        try {
                                        	System.out.println("ECRIRE DANS FICHIER");
                                            System.out.println("Here 3");
                                         //    writer.write("TEST|test|TEST|test");
                                         //    writer.write("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                                             writer.write(lineFile);
                                             count++;
                                             quotien = liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                                        } catch (IOException ex) {
                                            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            writer.newLine();
//                                            writer.flush();
//                                            writer.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        System.out.println("line"+j+"= :"+lineFile);
                                        System.out.println("end of line"+j);
                                   //     CONST=0;
                                   //}
                            }
                             writer.flush();
                             writer.close();
                             statut=Long.valueOf(1);
                            defineFileToSave=0;
                 //       }else{
                        
                            // ecrire dans le repertoire des erreurs.
                        
                        
                    //    }
                    } catch (SQLException e ) {
            //                JDBCTutorialUtilities.printSQLException(e);
                    } catch (IOException ex) {
                Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                   
                        if (stmt != null) { 
                            
                            try {
                                stmt.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }

                    }
                
                liveReportingService.endDetailsReportingToTheVue(idTrait,codeFichier, statut, Long.valueOf(count));
                System.out.println("AFTERENDING DETAILS"+idTrait);

                nombreOpeTraite++;
                quotien = Long.valueOf(1);
                count=Long.valueOf(0);
            }               


        }
    }
    
       //End reporting status 
    statutope=Long.valueOf(1);
    
    System.out.println("BEFORE GLOBALENDING"+idOpe);
    liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
    nombreOpeTraite=Long.valueOf(0);
    System.out.println("end of process-----------");// 
    
    return "1";

}
               
public String writeInDataBaseSystem(DataIntegrationFormS fic) {
   
   // recuperation des donnÃ©es a ecrire sur le fichier 
     idOpe = liveReportingService.beginGobalReportingToTheVue(fic.getCodeUnique(),fic.getCetab(), fic.getUsid(),fic.getOperation(),Long.valueOf(fic.getCodeInventaires().size()));
        try {
            // recuperation des paramÃ¨tres
            parameters=getGenerationAndSavingParam();
            inventaireList=getInventairelist();
            minimum=Long.valueOf(parameters.get("minimumNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }     
   // acces au fichier pour ecriture
        BufferedReader read = null;
        JSch jsch2 = new JSch();
//        try {
//            session = jsch2.getSession(parameters.get("user"), parameters.get("ip"), Integer.parseInt(parameters.get("port")));
//        } catch (JSchException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
//        } catch (SftpException ex) {
//            Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    for(int i =0; i< fic.getCodeInventaires().size();i++){

       for (int j = 0; j<inventaireList.size();j++){
            if(fic.getCodeInventaires().get(i).getCode().equalsIgnoreCase(inventaireList.get(j).getCodeInv())){

                    // preparation du fichier
                   // if(defineFileToSave==0){
                     fileName=inventaireList.get(j).getNomFic();
                     final File file = new File(fileName);
                     InputStream in = null;
                     String n=parameters.get("invEncours")+fileName;
                     codeFichier=inventaireList.get(j).getCodeInv();
                     System.out.println("AFTEREBEGINING DETAILS_"+j);
                     idTrait = liveReportingService.beginDetailsReportingToTheVue(idOpe,codeFichier, nombreTotal);
                     try {
                    //  try {
//                      //  out = channelSftp.put(parameters.get("invEncours")+fileName);
//                        in = channelSftp.get(n);
//                    } catch (SftpException ex) {
//                        Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                    }

//writer = new BufferedWriter(new OutputStreamWriter(out));
                     read = new BufferedReader(new FileReader(n));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ChargerDonneesServiceLocalImplS.class.getName()).log(Level.SEVERE, null, ex);
                }
                    String line = null;
                try {
                    // contituer une  collection de donnÃ©es a partir de chaque fichier a traiter 
                    
                    if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
                            
                        while ((line = read.readLine()) != null) {
                            if(line.length()==0){
                                System.out.println("eeeeeeeeeeeeeeeeee :");
                                line=null;
                            }else{
                            // placer les elements dans le string
                            String[] data = line.split("\\|", -1);
                            System.out.println("DATdddddddddA :"+line);
                            System.out.println("DATA"+data[0]);
                            System.out.println("DATA"+data[1]);
                            System.out.println("DATA"+data[2]);
                            System.out.println("DATA"+data[3]);
                            System.out.println("DATA"+data[4]);
                            System.out.println("DATA"+data[5]);
                            System.out.println("DATA"+data[6]);
                            System.out.println("DATA"+data[7]);
                            System.out.println("DATA"+data[8]);
                            System.out.println("DATA"+data[9]);
                            System.out.println("DATA"+data[10]);
                            System.out.println("DATA"+data[11]);
                            System.out.println("DATA"+data[12]);
                            System.out.println("DATA"+data[13]);
                            System.out.println("DATA"+data[14]);
                            System.out.println("DATA"+data[15]);
                            System.out.println("DATA"+data[16]);
                            System.out.println("DATA"+data[17]);
                            System.out.println("DATA"+Long.valueOf(0));
//                           InBalance inbal= new InBalance(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf("0")
//                            );
                            
                            /* InBalance inbal= new InBalance(
                                   "l",
                                    "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19",
                                    Long.valueOf("45")
                            );*/
//                            inBalanceList.add(inbal);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                            System.out.println(line);
                            }
                        }          
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){

                        while ((line = read.readLine()) != null) {
                            
//                            String[] data = line.split("\\|", -1);
//                            InSoldes insold= new InSoldes(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf("0")
//                            );
//                            inSoldesList.add(insold);
//                             count++;
//                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
//                            System.out.println(line);
                        }                              
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){

                        while ((line = read.readLine()) != null) {
                            
                            String[] data = line.split("\\|", -1);
//                            InAutorisationDecouvert inAut= new InAutorisationDecouvert(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],
//                                    Long.valueOf(0)
//                            );
//                            inAutorisationDecouvertList.add(inAut);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                            System.out.println(line);
                            
                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){

                             
                        while ((line = read.readLine()) != null) {
                            
                            String[] data = line.split("\\|", -1);
//                            InClients incli= new InClients(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[24],
//                                    Long.valueOf(0)
//                            );
//                            inClientsList.add(incli);                   
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                            System.out.println(line);
                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){
                             
                        while ((line = read.readLine()) != null) {

                            String[] data = line.split("\\|", -1);
//                            InComptes incom= new InComptes(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[24],
//                                    Long.valueOf(0)
//                            );
//                            inComptesList.add(incom);
                            System.out.println(line);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
                        }                              

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            String[] data = line.split("\\|", -1);
//                            InCautions incau= new InCautions(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],
//                                    Long.valueOf(0)
//                            );
//                            inCautionsList.add(incau);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
                            System.out.println(line);                            
                        }
                    
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){ 
                           int k =0;  
                        while ((line = read.readLine()) != null) {
                            
                            String[] data = line.split("\\|", -1);
//                            InGaranties inGar= new InGaranties(
//                                   "etabtest",
//                                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],
//                                    Long.valueOf(0)
//                            );
//                            inGarantiesList.add(inGar);
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);
                            System.out.println(line);
                        }  
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){
                             
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }   
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){

                            
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){
                             
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){
                             
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                               

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }    
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){

                             
                        while ((line = read.readLine()) != null) {
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                              
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){
                             
                        while ((line = read.readLine()) != null) {
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        } 
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){

                         
                        while ((line = read.readLine()) != null) {
                            
                            
                            System.out.println(line);
                            count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        } 
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){
                            
                        while ((line = read.readLine()) != null) {
                            
                            
                            System.out.println(line);
                             quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                              
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        } 
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){

                        while ((line = read.readLine()) != null) {
                            
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }    
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                            
                        }                               
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){
                             
                        while ((line = read.readLine()) != null) {
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }
                    }else{
 
                        while ((line = read.readLine()) != null) {
                            
                            System.out.println(line);
                             count++;
                            quotien= liveReportingService.detailsReportingToTheVue(idTrait,codeFichier, nombreTotal, Long.valueOf(count), quotien, minimum);

                        }                        
                    }
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    read.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChargerDonneesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                // faire les saves dans la base de donnÃ©es oracle:

                    if(fic.getCodeInventaires().get(i).getCode().equals("BAL")){
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.size());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDele());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCetab());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getAge());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getChap());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCle());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCli());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCom());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCumc());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getCumd());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDar());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDcre());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDev());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDev());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getDmod());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getSldcvd());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getSldcvf());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getSldd());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getSldf());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getTxb());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getUticre());                                                                           
                        System.out.println("BEFORE SAVING LIST BAL: "+inBalanceList.get(0).getUtimod());                                                                           
                        //inBalanceRepository.save(inBalanceList);
                        inBalanceRepository.save(inBalanceList);
                       // inb.save(inBalanceList);
                        System.out.println("AFTER SAVING LIST BAL"); 
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("SOLDE")){
                        System.out.println("BEFORE SAVING LIST SOLDE");                                                                           
                        inSoldesRepository.save(inSoldesList);
                        System.out.println("AFTER SAVING LIST SOLDE");                                                                           

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("AUTO")){
                    
                       inAutorisationDecouvertRepository.save(inAutorisationDecouvertList);
                                                       
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CLI")){
                     
                      inClientsRepository.save(inClientsList);                             
                                                     
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("COM")){
                             
                         inComptesRepository.save(inComptesList);                        

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CAU")){
                        
                        inCautionsRepository.save(inCautionsList);
                    
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("GAR")){
                         
                        inGarantiesRepository.save(inGarantiesList);
                         
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("BDC")){
                        
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("DAT")){

                                                      
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CREDOC")){
                             
                                                  
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("REMDOC")){
                             
                                                   

                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TRANS")){
                             
                          
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("RAPAT")){

                             
                                                       
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPCH")){
                             
                        
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MARCH")){
                             
                       
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("TITRE")){

                         
                        
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("OPIB")){
                            
                                                      
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("WALLET")){
                             
                         
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("IMP")){

                           
                        
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("CRED")){
                             
                                                     
                    }else if(fic.getCodeInventaires().get(i).getCode().equals("MONET")){
                             
                       
                    }else{
 
                    }
                     // deplacer et renommer le fichier dans les archieves ou les erreurs 
               // try {
                    
                     File file2 = new File(parameters.get("invEncours")+fileName);
                     File newFile = new File(parameters.get("invArchives")+fileName+getYearMonthTimeStamp());
                    // File newFile = new File("C:\\xch\\inv\\archives\\"+fileName+getYearMonthTimeStamp());
                     System.out.println("BEFORE MOVING FILE *****************");
                     file2.renameTo(newFile);
                     System.out.println("BEFORE After MOVING FILE *****************");
                     System.out.println("BEFORE After MOVING FILE *****************");
                     System.out.println("C:\\xch\\inv\\archives\\"+fileName+getYearMonthTimeStamp());
                     System.out.println("C:\\xch\\inv\\pending\\"+fileName);
                     liveReportingService.endDetailsReportingToTheVue(idTrait,codeFichier, statut, Long.valueOf(count));
                     System.out.println("AFTERENDING DETAILS"+idTrait);

                     nombreOpeTraite++;
                     quotien=Long.valueOf(1);
                     count=Long.valueOf(0);    
       
                   //  Path result2 = null;
                   //  result2 =  Files.move(Paths.get("C:\\xch\\inv\\pending\\"), Paths.get("C:\\xch\\inv\\archives\\"));
                    // Files.move(Paths.get("/foo.txt"), Paths.get("bar.txt"));
                    // Files.move(fileToMovePath, targetPath.resolve(fileToMovePath.getFileName()));
                }
                
            }               
                
        }
    
    statutope=Long.valueOf(1);
    System.out.println("BEFORE GLOBALENDING"+idOpe);
    liveReportingService.endGobalReportingToTheVue(idOpe, statutope, nombreOpeTraite);
    nombreOpeTraite=Long.valueOf(0);
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
        for(int i = 0; i < 9; i++) {
          int k = rand.nextInt(longueur);
          tezst = tezst+alphabet.charAt(k);
        }
        String code = current+"IWARES"+tezst;
        return  code;
    }
    
    public String getYearMonthTimeStamp(){
    
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        //String newstring = new SimpleDateFormat("yyyyMM").format(date)+"_"+(new Timestamp(date.getTime()));
        String newstring = new SimpleDateFormat("yyyyMM").format(date)+"_"+randoms();
        return newstring;
        
    }
    
     public String getYearMonth2(Date d) throws ParseException{
    
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
     //   Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(d);
        String newstring = new SimpleDateFormat("yyyyMM").format(date);
       // DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
       // Date date = new Date();
        return newstring;
    
    }
//     
//   public static void main (String[] args) {
//         File file2 = new File("C:\\xch\\inv\\pending\\SOLDE.txt");
//         File newFile = new File("C:\\xch\\inv\\archives\\SOLDENEW.txt");
//         System.out.println("BEFORE MOVING FILE *****************");
//         file2.renameTo(newFile);
//   }
//        
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        //System.out.println("START date");
//      //  System.out.println(dateFormat.format(date));
//        
//	//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        //System.out.println("START fic");
//         List<String> nums = new ArrayList<String>() {{ add("BAL"); add("SOLDE");}};
//       // System.out.println("END fic");
//        //  ClientToSystemForm fic=new ClientToSystemForm(date,"O","M",nums);
//          DataIntegrationForm fic2=new DataIntegrationForm(date,"O","M",nums);
//       // ChargerDonneesServiceImpl chr=new ChargerDonneesServiceImpl();
//        ChargerDonneesServiceLocalImpl chr=new ChargerDonneesServiceLocalImpl();
//        System.out.println("START TESTING");
//       // chr.extrairesFromClientDatabase(fic);
//      /*  BufferedWriter writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter("C:\\xch\\inv\\pending\\test.txt"));
//            writer.write("ggggggggggggggggggggggg");
//           // writer.newLine();
//            writer.flush();
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }*/
//    //    chr.saveBatch("40","te","te","te");
//        chr.writeInDataBaseSystem(fic2);
//        //chr.writeInDataBaseSystem(fic2);
//        System.out.println("END TESTING"); 
//        System.out.println("END TESTING"); 
//    }
//   
   /*
       @Transactional
	public void saveInBalance(List<InBalance> inBalance) {
		int size = inBalance.size();
		int counter = 0;
		
		List<InBalance> temp = new ArrayList<>();
		
		for (InBalance emp : inBalance) {
			temp.add(emp);
			
			if ((counter + 1) % 500 == 0 || (counter + 1) == size) {
                            System.out.println(emp);
                            System.out.println(counter);
				inBalanceRepository.save(temp);
				temp.clear();
			}
			counter++;
		}
	}
        
        
        public void saveBatch (String etab,String post,String col,String solde) {
            int etabInt = Integer.parseInt(etab);
            System.out.println(etab);
            System.out.println(post);
            System.out.println(col);
            System.out.println(solde);
            String sql = "INSERT INTO rprep (etab,post, col, val) VALUES (?,?,?,?)";
            BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String[] cols = new String[4];
                    ps.setInt(1, etabInt);
                    ps.setString(2, post);
                    ps.setString(3, col);
                    ps.setString(4, solde);
                }

                @Override
                public int getBatchSize() {
                    return 4;
                }
            };
            
             jdbcTemplate.batchUpdate(sql, batchArgs);
        }
        

        public void setDataSource(DataSource dataSource) {

         this.dataSource = dataSource;

         this.jdbcTemplate = new JdbcTemplate((javax.sql.DataSource) dataSource);

        }
        
        
        public void saveP(){
        
        try {

            try {
                parameters= getGenerationAndSavingParam();
            } catch (SQLException ex) {
                Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your Oracle JDBC Driver?");
                e.printStackTrace();
            }
            try {
                // connection = DriverManager.getConnection(url, login, password);
                System.out.println("BEFORE ORACLE CONNEXION");
                connection = DriverManager.getConnection(parameters.get("oracleUrl"), parameters.get("loginUrl"), parameters.get("passwordUrl"));
                System.out.println("AFTER ORACLE CONNEXION");
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
            }
            Statement stmt = null;
            String query = "INSERT INTO rprep (etab,post, col, val) VALUES ('23','hj','ghhhh','gghjj')";
            stmt = connection.createStatement();
            System.out.println("BEFORE ORACLE QUERRY EXECUTION AFTER STATEMENT");
            System.out.println("QUERRY:"+query);
            ResultSet rs = stmt.executeQuery(query);
              
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        }
        
 /*       
        public void send(List<InBalance> inbal) throws RollbackException{
        
        
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            for ( int i=0; i<inbal.size(); i++ ) {
              InBalance inb= inbal.get(i);
              session.save(inbal.get(i));
              if ( i % 20 == 0 ) { // 20, same as the JDBC batch size
                // flush a batch of inserts and release memory
                session.flush();
                session.clear();
              }
            } 
            tx.commit();
            session.close();
        
        
        
        
        
        }

   */    
}

    
    
    
    


package iwomi.base.services;

import static iwomi.base.services.ChargerDonneesServiceImpl.CONN_STRING;
import static iwomi.base.services.ChargerDonneesServiceImpl.PASSWORD;
import static iwomi.base.services.ChargerDonneesServiceImpl.USERNAME;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;

import iwomi.base.ServiceInterface.LiveReportingService;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.ServiceInterface.ReportCalculateService;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.ReportControleQuality;
import iwomi.base.objects.ReportRep;
import iwomi.base.objects.ReportRepTmp;
import iwomi.base.objects.ReportResultTmp;
import iwomi.base.objects.ReportResultTmpS;
import iwomi.base.repositories.LiveTraitementRepositoryS;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportAnomalyRepository;
import iwomi.base.repositories.ReportAttributeRepository;
//import iwomi.base.repositories.FichierSpectraRepository;
import iwomi.base.repositories.ReportCalculateRepository;
import iwomi.base.repositories.ReportControleComplexRepository;
import iwomi.base.repositories.ReportControleInterRepository;
import iwomi.base.repositories.ReportControleIntraRepository;
import iwomi.base.repositories.ReportControleQualityRepository;
import iwomi.base.repositories.ReportFileRepository;
import iwomi.base.repositories.ReportRepRepository;
import iwomi.base.repositories.ReportRepTempRepository;
import iwomi.base.repositories.ReportResultRepository;
import iwomi.base.repositories.ReportResultTmpRepository;
import static iwomi.base.services.ReportCalculateServiceImplS.RPNtoDouble;
import static iwomi.base.services.ReportCalculateServiceImplS.eval;
import static iwomi.base.services.ReportCalculateServiceImplS.infixToRPN;
import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

//@Service
@Component
public class ReportCalculateServiceImpl extends GlobalService implements ReportCalculateService {

    @Autowired
    private ReportCalculateRepository reportCalculateRepository;
    @Autowired
    private ReportRepRepository reportRepRepository;
    @Autowired
    private ReportAttributeRepository reportAttributeRepository;
    @Autowired
    private ReportRepTempRepository reportRepTempRepository;
    @Autowired
    private ReportControleIntraRepository reportControleIntraRepository;
    @Autowired
    private ReportControleInterRepository reportControleInterRepository;
    @Autowired
    private ReportControleQualityRepository reportControleQualityRepository;
    @Autowired
    private ReportControleComplexRepository reportControleComplexRepository;

//    Connection connection, con = null;
//    Connection connection1, con1 = null;
    Map<String, String> PARAM = new HashMap<>();
    String NOMENGABTABLE_SYS = "0012";
    private String[] ORACLE_CON_PARAM;
    private static Map<String, Object> val = new HashMap<>();
    Map<String, String> parameters = new HashMap<>();
    @Autowired
    private ReportFileRepository ReportFileRepository;
    @Autowired
    ReportCalculateServiceImpl reportCalculateServiceImpl;
    @Autowired
    private ReportResultTmpRepository ReportResultTmpRepository;
    Long idOpe, minimum;
    private Statement connect;
    private Statement connect1;

    public Statement conac() {
        return connect;
    }

    public Statement conac1() {
        return connect1;
    }
    @Autowired
    JdbcTemplate jdbcTemplate;
//	@Autowired
//	LiveReportingService liveReportingService;
    String idTrait, codeFichier;
    Long quotien = Long.valueOf(1);
    Long count = Long.valueOf(0);
    Long statut = Long.valueOf(3);
    Long statutope = Long.valueOf(3);
    Long nombreOpeTraite = Long.valueOf(0);
    private static final BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(0);
    String duplicate = "1";
    String sql = "2";
    String uniquecode = "";
    private String URL_ORACLE = "";
    private String LOGIN_ORACLE = "";
    private String PASSWORD_ORACLE = "";
    // Associativity constants for operators
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;
    // Operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();

    static {
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
    }

    // Test if token is an operator
    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    public List<ReportCalculate> listAll() {
        List<ReportCalculate> reportCalculates = new ArrayList<>();
        return reportCalculates;
    }

    public Map<String, Object> ReportGetCalculate(String json) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        val = parser.parseMap(json);
        return val;
    }

    public Map<String, String> CalculData1(String json, Model m) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    CalculDataTraitementv1(json);
                } catch (SQLException ex) {
                    Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportCalculateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        ;
        });

		t.start();
        return getAveragtimeFiles(json, m);
    }
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

    public void CalculDataTraitementv1(String json) throws SQLException, ClassNotFoundException, ParseException {
        Map<String, Object> fich = ReportGetCalculate(json);
        Map<String, String> arr = (HashMap) fich.get("arrete");
        Connection r1 = connectDB();
        Statement r = r1.createStatement();
        Map<String, String> parameters = chargerDonneesServiceImpl.getGenerationAndSavingParamv2(r);
        ExecutorService service = Executors.newFixedThreadPool(5);
        List<String> fichier = (List<String>) fich.get("fichier");
        System.out.println("Start traitement");
        String sql1 = "delete from rpreptmp where dar =to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql1);
        Long idOpe = liveReportingService.beginGobalReportingToTheVue(fich.get("codeUnique").toString(),
                fich.get("etab").toString(), fich.get("cuser").toString(), fich.get("operation").toString(),
                Long.valueOf(fichier.size()));
        if (r != null) {
            r.close();
        }
        if (r1 != null) {
            r1.close();
        }
        Thread t = new Thread(new Runnable() {
            public void run() {
                do {
                    try {
                        Map<String, String> type = new HashMap<>();
                        System.out.println("Treatment start with processing id :" + idOpe);
                        for (int i = 0; i < fichier.size(); i++) {
                            type = getType1(fichier.get(i));
                            fich.put("date", arr.get(fichier.get(i)));//fichier.get(i));
                            service.execute(new ReportCalculateServiceImpl.Treatement(fichier.get(i), fich, type, idOpe));
                        }
                        // this will get blocked until all task finish
                        service.shutdown();
                        try {
                            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            System.out.println("Tebit service did not terminate");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("An error occured during the treatment " + e.getLocalizedMessage());
                    }
                    System.out.println("Setting the status");
                    liveReportingService.endGobalReportingToTheVue1(idOpe, 1L);
                } while (!service.isShutdown());
            }
        });
        t.start();

    }

    @Override
    public void CalculData(String json) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connec() throws SQLException, ClassNotFoundException, JSONException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connec1() throws SQLException, ClassNotFoundException, JSONException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insertPeriodsValiables(String e, Statement r) throws SQLException, ClassNotFoundException, JSONException {
        ResultSet result = r.executeQuery("SELECT * FROM sanm  WHERE tabcd='3018' AND dele='0' and acscd = '001'");
        while (result.next()) {
            e = e.replaceAll("//cm//", "to_date('" + result.getString("lib1") + "','ddMMyyyy')").
                    replaceAll("//cq//", "to_date('" + result.getString("lib2") + "','ddMMyyyy')").
                    replaceAll("//cs//", "to_date('" + result.getString("lib3") + "','ddMMyyyy')").
                    replaceAll("//cy//", "to_date('" + result.getString("lib4") + "','ddMMyyyy')").
                    replaceAll("//pm//", "to_date('" + result.getString("lib5") + "','ddMMyyyy')").
                    replaceAll("//pq//", "to_date('" + result.getString("taux1") + "','ddMMyyyy')").
                    replaceAll("//ps//", "to_date('" + result.getString("taux2") + "','ddMMyyyy')").
                    replaceAll("//py//", "to_date('" + result.getString("taux3") + "','ddMMyyyy')");
        }
        if (result != null) {
            result = null;
        }
        System.gc();
        return e;
    }

    public class Treatement implements Runnable {

        String fichier;
        Map<String, Object> fich;
        Map<String, String> typ;
        Long idOpe;

        public Treatement(String fichier, Map<String, Object> fich, Map<String, String> typ, Long idOpe) {
            this.fichier = fichier;
            this.fich = fich;
            this.typ = typ;
            this.idOpe = idOpe;
        }

        @Override
        public void run() {
            try {
                tsdsfsdfs(fichier, fich, typ);
            } catch (ParseException ex) {
                Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public String insertPeriodsValiables(String e, Statement r) throws SQLException, ClassNotFoundException, JSONException {
            ResultSet result = r.executeQuery("SELECT * FROM sanm  WHERE tabcd='3018' AND dele='0' and acscd = '001'");
            while (result.next()) {
                e = e.replaceAll("//cm//", "to_date('" + result.getString("lib1") + "','ddMMyyyy')").
                        replaceAll("//cq//", "to_date('" + result.getString("lib2") + "','ddMMyyyy')").
                        replaceAll("//cs//", "to_date('" + result.getString("lib3") + "','ddMMyyyy')").
                        replaceAll("//cy//", "to_date('" + result.getString("lib4") + "','ddMMyyyy')").
                        replaceAll("//pm//", "to_date('" + result.getString("lib5") + "','ddMMyyyy')").
                        replaceAll("//pq//", "to_date('" + result.getString("taux1") + "','ddMMyyyy')").
                        replaceAll("//ps//", "to_date('" + result.getString("taux2") + "','ddMMyyyy')").
                        replaceAll("//py//", "to_date('" + result.getString("taux3") + "','ddMMyyyy')");
            }
            if (result != null) {
                result = null;
            }
            System.gc();
            return e;
        }

        private void tsdsfsdfs(String efi, Map<String, Object> fich, Map<String, String> type) throws ParseException, ClassNotFoundException, SQLException, JSONException {
            Connection r1 = connectDB();
            Statement r = r1.createStatement();
            Map<String, String> cloture = (Map<String, String>) fich.get("cloture");
            Long Total = null;
            int Friqunce;
            Total = getTotal(efi, type);
            Friqunce = (int) Math.ceil(Total / 25.0);
            Long minim = 0L;
            if (type.get("result").toString().equalsIgnoreCase("duplicate")) {
                List<Map<String, Object>> dup = findByDup1v1(fich.get("date").toString(), efi, cloture.get(efi));
                int e = dup.size();
                Friqunce = (int) Math.ceil(e / 100.0);//(int) Math.ceil(e / 25.0);
                Total = new Long(e != 0 ? e : 1);
                minim = new Long(Friqunce);
                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
                System.out.println("Fichier type Duplicate");
                for (int t = 0; t < dup.size(); t++) {
                    saveCalcTmp1v1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), 0L);
                    if (t % minim == 0) {
                        liveReportingService.detailsReportingToTheVue3(fich.get("codeUnique").toString(), efi, minim);
                    }

                }
            } else if (type.get("result").toString().equalsIgnoreCase("duplicatepost")) {
                List<Map<String, Object>> dup = findByDup1v1(fich.get("date").toString(), efi, cloture.get(efi));
                int e = dup.size();
                Friqunce = (int) Math.ceil(e / 100.0);//(1;//e ;
                Total = new Long(e != 0 ? e : 1);
                minim = new Long(Friqunce);
                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
                System.out.println("Fichier type Duplicate POST");
                for (int t = 0; t < dup.size(); t++) {
                    saveCalcTmp1v1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), 0L);
                    if (t % minim == 0) {
                        liveReportingService.detailsReportingToTheVue3(fich.get("codeUnique").toString(), efi, minim);
                    }
                }
            } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
                System.out.println("the value of fichier:" + efi);
                if (type.get("sql") != null && type.get("sql") != "") {
                    String formule = type.get("sql").toString();
                    System.out.println("Fichier type SQL");
                    formule = insertPeriodsValiables(formule.replaceAll("//dar//", "to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')"), r);
                    System.out.println(formule);
                    if (type.get("link") != null) {// link link
                        findBySql3(formule, fich.get("date").toString(), efi, type.get("col_number"), type.get("link").toString(), r, fich.get("codeUnique").toString(), idOpe, fich.get("etab").toString());
                    } else {// internal database
                        liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, 2L);
                        findBySql2v1(formule, fich.get("date").toString(), efi, fich.get("codeUnique").toString());
                    }
                }
            } else {
                System.out.println("Fichier type calculation");
                if (efi.equalsIgnoreCase("F1000") || efi.equalsIgnoreCase("FM1000")) {
                    List<Map<String, Object>> FICSQLr = findCalcByPoint1(efi);// all *** cells
                    List<Map<String, Object>> FICPOST = findCalcByPost(efi);
                    List<Map<String, Object>> FICSQLe = findCalcByPoint2(efi);// all *** cells
                    int e = FICSQLr.size() + FICPOST.size() + FICSQLe.size();
                    System.out.println("total elm to treat : " + e);
                    Friqunce = (int) Math.ceil(e / 100.0);
                    Total = new Long(e);
                    minim = new Long(Friqunce);
                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
                    savePostF1000v2(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICPOST, cloture.get(efi), minim);
                    savePoint250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQLr, minim);
                    savecol250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQLe, minim);
                } else if (efi.equalsIgnoreCase("F1139") && false) {
                    List<Map<String, Object>> FICPOST = findCalcByPost_F1139(efi, parameters.get("F1139"));
                    List<Map<String, Object>> FICSQL = findCalcBySql(efi);
                    List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
                    List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
                    List<Map<String, Object>> FICSQLr = findCalcByPoint1_F1139(efi, parameters.get("F1139"));// all *** cells
                    List<Map<String, Object>> FICSQLrr = findCalcByPoint2_F1139(efi, parameters.get("F1139"));// all *** cells
                    int e = FICPOST.size() + FICSQL.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQLr.size() + FICSQLrr.size();
                    System.out.println("total elm to treat : " + e);
                    Friqunce = (int) Math.ceil(e / 25.0);
                    Total = new Long(e);
                    minim = new Long(Friqunce);
                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
                    switch (parameters.get("F1139")) {
                        case "CMR":
                            savePost250202019_F1139v1(efi, fich.get("etab").toString(),
                                    fich.get("date").toString(), fich.get("cuser").toString(),
                                    fich.get("codeUnique").toString(), parameters.get("F1139"), FICPOST);
                            saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL, minim);
                            savePoint250202019_F1139v1(efi, fich.get("etab").toString(),
                                    fich.get("date").toString(), fich.get("cuser").toString(),
                                    fich.get("codeUnique").toString(), parameters.get("F1139"), FICSQLr);
                            saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT, minim);
                            saveTraitement250202019v1(efi, fich.get("etab").toString(),
                                    fich.get("date").toString(), fich.get("cuser").toString(),
                                    fich.get("codeUnique").toString(), FICTRAITEMENT1, minim);
                            savecol250202019_F1139v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(),
                                    parameters.get("F1139"), FICSQLrr);
                            savePoint250202019_F1139v1(efi, fich.get("etab").toString(),
                                    fich.get("date").toString(), fich.get("cuser").toString(),
                                    fich.get("codeUnique").toString(), parameters.get("F1139"), FICSQLr);
                            break;
                    }
                } else {

                    List<Map<String, Object>> FICPOST = findCalcByPost(efi);
                    List<Map<String, Object>> FICSQL = findCalcBySql(efi);
                    List<Map<String, Object>> FICSQL1 = findCalcByPoint1(efi);// all *** cells
                    List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
                    List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
                    List<Map<String, Object>> FICSQL2 = findCalcByPoint2(efi);// all *** cells
                    List<Map<String, Object>> FICSQL3 = findCalcByPoint1(efi);// all *** cells
                    int e = FICPOST.size() + FICSQL.size() + FICSQL1.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQL2.size() + FICSQL3.size();
                    Friqunce = (int) Math.ceil(e / 100.0);
                    Total = new Long(e);
                    minim = new Long(Friqunce);
                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
                    savePost250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICPOST, minim, cloture.get(efi));
                    saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL, minim);
                    savePoint30032020v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL1, minim);
                    saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT, minim);
                    saveTraitement250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT1, minim);
                    savecol250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL2, minim);
                    savePoint250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL3, minim);
                }
            }

            saveCalcv1(fich.get("date").toString(), efi);
            liveReportingService.endDetailsReportingToTheVue2(fich.get("codeUnique").toString(), efi, 1L, Total, Total);
            if (r != null) {
                r.close();
            }
            if (r1 != null) {
                r1.close();
            }
            System.gc();
        }

    }

    public void saveCalcTmp1v1(String etab, Map<String, Object> obj, String dar, String cuser, String idtrait, long l)
            throws ParseException {
        ReportResultTmp save = new ReportResultTmp(new SimpleDateFormat("yyyy-MM-dd").parse(dar), etab,
                (obj.get("field") == null ? "" : obj.get("field").toString()), (obj.get("post") == null ? "" : obj.get("post").toString()), obj.get("col").toString(), cuser, cuser,
                obj.get("valc") == null ? "" : obj.get("valc").toString(), Long.parseLong(obj.get("rang").toString()), obj.get("fichier").toString(),
                Integer.parseInt(obj.get("status").toString()));
        ReportResultTmpRepository.save(save);
    }

    @Transactional
    public int findBySql2v1(String sql, String d, String f, String r) {
        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
        liveReportingService.detailsReportingToTheVue2(r, f, 1L);
        return jdbcTemplate.update(sql);
    }

    private void savePost250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
            String s, List<Map<String, Object>> FICPOST) {
        minimum = minim.get(fichier);
        String p = "";
        List<Map<String, Object>> post1 = findByPostv1();
        int re = 0;
        List<String> et = new ArrayList<>();
        if (FICPOST.size() > 0) {
            String globalpostqury = "";
            for (int t = 0; t < FICPOST.size(); t++) {
                String result = "";
                List<String> ret1 = new ArrayList<String>();
                String formule = FICPOST.get(t).get("CALC").toString();
                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
                System.out.println("FormulePost: " + formule);
                String condition = null;
                if (formule.equals("")) {
                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
                    p = "";
                } else {

                    List<String> st = separeteDataPost(formule);
                    List<String> ret = new ArrayList<String>();

                    if (st.size() != 1) {
                        for (int f = 1; f < st.size(); f++) {
                            ret.add(st.get(f));
                        }
                        condition = findByAtt(ret);
                        ret1 = ret;
                    }
                    p = st.get(0).toString();
                }
                List<Map<String, Object>> post = postCollect(post1, p);
//                List<Map<String, Object>> post = findByPost(p);
                if (formule.isEmpty()) {
                    result = "0";
                } else {
                    if (!post.isEmpty()) {
                        result = calcBySoldv2(post, condition, date, p, ret1);
                    } else {
                        result = "0";
                    }
                }
                globalpostqury += (re != 0 ? " union " : "") + "select '" + FICPOST.get(t).get("DIVD") + "' divd, '" + FICPOST.get(t).get("STATUS") + "' status,'" + FICPOST.get(t).get("TYPEVAL") + "'typeval,'" + FICPOST.get(t).get("FIELD") + "'field,'" + FICPOST.get(t).get("RANG") + "'rang,'" + fichier + "' fichi,'" + FICPOST.get(t).get("POST").toString() + "' post ,'" + FICPOST.get(t).get("COL").toString() + "' col, (" + result + ") result from dual";
                if (re == 50) {//50queries
                    et.add(globalpostqury);
                    globalpostqury = "";
                    re = 0;
                } else {
                    re++;
                }
                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minimum);
            }
            if (globalpostqury != "") {
                et.add(globalpostqury);
            }
            lotSavingCalcule(et, date, idtrait, fichier, 50L);
        }
    }

    public String findBySqlv1(String sql) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result == null ? "0" : (result.get(0).get("result") == null ? "0" : result.get(0).get("result").toString());
    }

    private void saveSql250202019V1(String fichier, String etab, String date, String cuser, String sss, List<Map<String, Object>> FICSQL, Long m) {

        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
                String formule = FICSQL.get(t).get("CALC").toString();
                System.out.println("formule sql :" + formule);
                formule = formule.replaceAll(":dar", "to_date('" + date.trim() + "','yyyy-mm-dd')");
//                System.out.println(FICSQL.get(t).get("RANG"));
                String ret = findBySqlv1(formule);
                if (FICSQL.get(t).get("TYPEVAL").toString().equalsIgnoreCase("M")) {
                    Double dre = Double.parseDouble(ret);
                    Double divd = Double.parseDouble(FICSQL.get(t).get("DIVD").toString());
                    long soldet = Math.round(dre / divd);
                    ret = soldet + "";
                }
                saveCalcTmpv1(etab, FICSQL.get(t).get("POST").toString(), FICSQL.get(t).get("COL").toString(),
                        ret, fichier, date,
                        Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser,
                        Integer.parseInt(FICSQL.get(t).get("STATUS").toString()), FICSQL.get(t).get("FIELD").toString(),
                        FICSQL.get(t).get("TYPEVAL").toString(), ret);
                if (t % m == 0) {
                    liveReportingService.detailsReportingToTheVue3(sss, fichier, m);
                }

            }
        }
    }

    private void saveCondition250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICTRAITEMENT, Long m)
            throws ParseException {

        if (FICTRAITEMENT.size() > 0) {
            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
                System.out.println("FormuleCondition: " + formule);
                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
                cd = separeteDataCond(formule, etab, fichier, date);
                cd = cd.setScale(0, RoundingMode.HALF_UP);
                String sold = cd.toString();
                saveCalcTmpv1(etab, (FICTRAITEMENT.get(t).get("POST") == null ? "" : FICTRAITEMENT.get(t).get("POST").toString()),
                        (FICTRAITEMENT.get(t).get("COL") == null ? "-1" : FICTRAITEMENT.get(t).get("COL").toString()), sold, fichier, date,
                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG") == null ? "0" : FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString(), sold);
                if (t % m == 0) {
                    liveReportingService.detailsReportingToTheVue3(idtrait, fichier, m);
                }

            }
        }
    }

    private void saveTraitement250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICTRAITEMENT, Long m)
            throws ParseException {

        if (FICTRAITEMENT.size() > 0) {
            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
                System.out.println("FormuleTraitement: " + formule);
                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
                cd = separeteDataTraitv1(formule, etab, fichier, date);
                String sold = cd.toString();
                saveCalcTmpv1(etab, FICTRAITEMENT.get(t).get("POST").toString(),
                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString(), cd.toString());
                if (t % m == 0) {
                    liveReportingService.detailsReportingToTheVue3(idtrait, fichier, m);
                }
            }
        }
    }

    private void savePoint250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
            String source, List<Map<String, Object>> FICSQL) {
        System.out.println(FICSQL);
        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
//				String formule = FICSQL.get(t).get("CALC").toString();
                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
                String val = "999999999999999";
                String qu = "select col,fichier,post,rang from rpreptmp where " + "fichier = '"
                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and post = '"
                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and col = '"
                        + FICSQL.get(t).get("COL").toString() + "'" + "  and rang = '"
                        + FICSQL.get(t).get("RANG").toString() + "' ";
                System.out.println(qu);
                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);

                if (PPP.size() > 0) {// modify existing
                    updateTemp(FICSQL, val, t);
                } else {// save new
                    saveCalcTmpv1(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
                            FICSQL.get(t).get("FIELD").toString(), "M", val);

                }
                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
            }
        }
    }

    private void savecol250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
            String source, List<Map<String, Object>> FICSQL) {
        System.out.println(FICSQL);
        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
            }
        }
    }

    public Map<String, Object> calcBySoldtables(List<Map<String, Object>> post, Map<String, Object> condition, String date) {
        String bt = "";
        String bt1 = "";
        String rt = "";
        List<String> e = (List<String>) condition.get("cmdt");
        String fromelm = "";
        String dateWher = "";
        for (int k = 0; k < e.size(); k++) {
            fromelm += (fromelm == "" ? "" : ",") + e.get(k);
            dateWher += (dateWher == "" ? "" : " and ") + e.get(k) + ".dar =to_date('" + date + "','yyyy-mm-dd')";
            for (int j = 0; j < post.size(); j++) {

//                if (j != post.size() - 1) {
//                    rt = " or ";
//                }
                if (e.get(k).trim().equalsIgnoreCase("INSEC") || e.get(k).trim().equalsIgnoreCase("inech") || e.get(k).trim().equalsIgnoreCase("inauto")) {
                } else {
                    bt += (bt == "" ? "" : " or ") + e.get(k).trim() + ".chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' ";
                }
            }
        }
        String sql = "select coalesce(sum(" + e.get(0) + "." + condition.get("sel") + "),0) as result from " + fromelm + " where " + dateWher + " and (" + bt + ")" + condition.get("cmd");
        System.out.println("-------------------------------------------------------------- modifierd");
        System.out.println(sql);
        if (condition == null) {
            sql = "select coalesce(sum(" + e.get(0) + "." + condition.get("sel") + "),0) as result from insld where " + dateWher + " and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public String calcBySoldtablesv1(List<Map<String, Object>> post, Map<String, Object> condition, String date) {
        String bt = "";
        List<String> e = (List<String>) condition.get("cmdt");
        String fromelm = "";
        String dateWher = "";
        for (int k = 0; k < e.size(); k++) {
            fromelm += (fromelm == "" ? "" : ",") + e.get(k);
            dateWher += (dateWher == "" ? "" : " and ") + e.get(k) + ".dar =to_date('" + date + "','yyyy-mm-dd')";
            for (int j = 0; j < post.size(); j++) {
                if (e.get(k).trim().equalsIgnoreCase("INSEC") || e.get(k).trim().equalsIgnoreCase("inech") || e.get(k).trim().equalsIgnoreCase("inauto")) {
                } else {
                    bt += (bt == "" ? "" : " or ") + e.get(k).trim() + ".chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' ";
                }
            }
        }
        String sql = "select coalesce(sum(" + e.get(0) + "." + condition.get("sel") + "),0) as result from " + fromelm + " where " + dateWher + " and (" + bt + ")" + condition.get("cmd");
//        System.out.println(sql);
        if (condition == null) {
            sql = "select coalesce(sum(" + e.get(0) + "." + condition.get("sel") + "),0) as result from insld where " + dateWher + " and (" + bt + ")";
        }
//        System.out.println("requÃªte sql post: " + sql);
        return (sql);
    }

    public Map<String, Object> findByAttv1(List<String> att) {
        String values = "";
        String value = "'" + att.get(0) + "'";
        for (int i = 1; i < att.size(); i++) {
            values += " or att='" + att.get(i) + "'";
        }
        values = value + values;
        String sql = "Select val,tab,sel from rpatt " + "where att=" + values;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<String, Object> p = new HashMap<>();
        String cmd = "";
        List<String> cmdt = new ArrayList<>();
        String selc = "";
        for (int j = 0; j < result.size(); j++) {
            if (result.get(j).get("VAL") != null) {
                if (result.get(j).get("VAL").toString().isEmpty()) {
                    cmd += " and 1=0";
                } else {
                    cmd += " and " + result.get(j).get("VAL").toString();
                }
                if (result.get(j).get("TAB") != null) {
                    cmdt.add(result.get(j).get("TAB").toString().trim());
                    if (result.get(j).get("SEL") != null && result.get(j).get("SEL") != "") {
                        selc = result.get(j).get("SEL").toString().trim();
                    }
                }
            }
        }
        p.put("cmd", cmd);
        p.put("cmdt", cmdt);
        p.put("sel", selc);
        return p;
    }

    public Map<String, Object> calcBySoldv1(List<Map<String, Object>> post, String condition, String date, String p, List<String> attr) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }
            bt += "insld.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;

        }
        String sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        System.out.println("-------------------------------------------------------------- modifierd");
        if (attr.contains("CREDITEURC")) {
            sql = "select coalesce(sum(sld),0) as result from (select chap, (case when sum(insld.sldcvd)<0 then 0 else sum(insld.sldcvd) end) sld\n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2) " + condition + " group by chap)T";
        }

        if (attr.contains("DEBITEURC")) {
            sql = "select coalesce(sum(sld),0) as result from (select chap, (case when sum(insld.sldcvd)>0 then 0 else sum(insld.sldcvd) end) sld\n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2)" + condition + " group by chap)T";
        }
        if (attr.contains("CREDITEURT")) {
            sql = "select coalesce((case when sum(insld.sldcvd) < 0 then 0 else sum(insld.sldcvd) end),0) as result \n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2) " + condition + " ";
        }

        if (attr.contains("DEBITEURT")) {
            sql = "select coalesce((case when sum(insld.sldcvd) > 0 then 0 else sum(insld.sldcvd) end),0) as result \n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2)" + condition + " ";
        }
        if (condition == null) {
            sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                    + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public String calcBySoldv2(List<Map<String, Object>> post, String condition, String date, String p, List<String> attr) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }
            bt += "insld.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;

        }
        String sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (attr.contains("CREDITEURC")) {
            sql = "select coalesce(sum(sld),0) as result from (select chap, (case when sum(insld.sldcvd)<0 then 0 else sum(insld.sldcvd) end) sld\n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2) " + condition + " group by chap)T";
        }

        if (attr.contains("DEBITEURC")) {
            sql = "select coalesce(sum(sld),0) as result from (select chap, (case when sum(insld.sldcvd)>0 then 0 else sum(insld.sldcvd) end) sld\n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2)" + condition + " group by chap)T";
        }
        if (attr.contains("CREDITEURT")) {
            sql = "select coalesce((case when sum(insld.sldcvd) < 0 then 0 else sum(insld.sldcvd) end),0) as result \n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2) " + condition + " ";
        }

        if (attr.contains("DEBITEURT")) {
            sql = "select coalesce((case when sum(insld.sldcvd) > 0 then 0 else sum(insld.sldcvd) end),0) as result \n"
                    + "from insld, rppost b  where insld.dar=to_date('" + date
                    + "','yyyy-mm-dd') and b.codep='" + p.trim() + "' and (insld.chap between b.char1 and b.char2)" + condition + " ";
        }
        if (condition == null) {
            sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                    + "','yyyy-mm-dd') and (" + bt + ")";
        }
//        System.out.println("requÃªte sql post: " + sql);
        return (sql);
    }

    private void savePost250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICPOST, Long m, String cloture) {
        String p = "";
        List<Map<String, Object>> post = findByPostv1();
        int re = 0;
        List<String> et = new ArrayList<>();
        if (FICPOST.size() > 0) {
            String globalpostqury = "";
            for (int t = 0; t < FICPOST.size(); t++) {
                String result = "";
                List<String> ret1 = new ArrayList<String>();
                String formule = FICPOST.get(t).get("CALC").toString();
                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
                String condition = null;
                Map<String, Object> pr = new HashMap<>();
                if (formule.equals("")) {
                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
                    p = "";
                } else {
                    List<String> ret = new ArrayList<String>();
                    List<String> st = separeteDataPost(formule);
                    if (st.size() != 1) {
                        for (int f = 1; f < st.size(); f++) {
                            if (st.get(f) != null) {
                                ret.add(st.get(f).trim());
                            }
                        }
                        ret1 = ret;
                        pr = findByAttv1(ret);
                        condition = (String) pr.get("cmd");
                    }
                    p = st.get(0).toString();
                }
                List<Map<String, Object>> rr = postCollect(post, p);
//                System.out.println("get Chapter of post " + p + " : " + rr);
                if (formule.isEmpty()) {
                    result = "0";
                } else {
                    if (!rr.isEmpty()) {//when the post exist in rppost table
                        String d = date;
                        if (ret1.contains("Date-1")) {
                            d = cloture;
                        }
                        if (pr.containsKey("cmdt") && ((List<String>) pr.get("cmdt")).size() > 0) {
                            System.out.println("Treatment in External table ");
                            result = calcBySoldtablesv1(rr, pr, d);
                        } else {
                            result = calcBySoldv2(rr, condition, d, p, ret1);
                        }

                    } else {
                        result = "0";
                    }
                }
                System.out.println(formule+" : "+result);
                globalpostqury += (re != 0 ? " union " : "") + "select '" + FICPOST.get(t).get("DIVD") + "' divd, '" + FICPOST.get(t).get("STATUS") + "' status,'" + FICPOST.get(t).get("TYPEVAL") + "'typeval,'" + FICPOST.get(t).get("FIELD") + "'field,'" + FICPOST.get(t).get("RANG") + "'rang,'" + fichier + "' fichi,'" + FICPOST.get(t).get("POST").toString() + "' post ,'" + FICPOST.get(t).get("COL").toString() + "' col, (" + result + ") result from dual";
                if (re == 50) {//50 queries
                    et.add(globalpostqury);
                    globalpostqury = "";
                    re = 0;
                } else {
                    re++;
                }
            }
            if (globalpostqury != "") {
                et.add(globalpostqury);
            }
            lotSavingCalcule(et, date, idtrait, fichier, 50L);
        }
    }

    private List<Map<String, Object>> postCollect(List<Map<String, Object>> post, String p) {
        List<Map<String, Object>> rr = new ArrayList<>();
        for (Map<String, Object> e : post) {
            if (e.get("codep").toString().equalsIgnoreCase(p)) {
                rr.add(e);
            }
        }
        return rr;
    }

    @Autowired
    LiveTraitementRepositoryS liveTraitementRepositoryS;

    private void lotSavingCalcule(List<String> tttt, String date, String prog, String fichier, Long min) throws NumberFormatException, DataAccessException {
        ExecutorService service = Executors.newFixedThreadPool(10);//eg 15 dat and divid 4 
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
        String gg = t;
        for (String globalpostqury : tttt) {
            service.execute(new Runnable() {
                @Transactional
                public void run() {
                    Long e = 0L;
                    try {
                        List<Map<String, Object>> PPP = jdbcTemplate.queryForList(globalpostqury);
                        List<ReportResultTmp> rr = new ArrayList<>();

                        for (Map<String, Object> y : PPP) {
                            rr.add(new ReportResultTmp(
                                    new Long(y.get("rang").toString()),
                                    y.get("field").toString(),
                                    y.get("post").toString(),
                                    gg, 0L, y.get("col").toString(),
                                    y.get("typeval").toString(),
                                    y.get("result"),
                                    y.get("divd"),
                                    date,
                                    y.get("fichi").toString())
                            );
                            e = e + 1;
                        }
                        System.out.println("Start Saving :" + rr.size());
                        rr = reportResultTmpRepository.save(rr);
                        liveTraitementRepositoryS.updatelivetreatm(prog, fichier, e);
                    } catch (Exception r) {
                        System.out.println(r.getMessage());
                        System.out.println(e);
                        System.out.println(globalpostqury);
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Yann service did not terminate");
            e.printStackTrace();
        }

    }
    @Autowired
    ReportResultTmpRepository reportResultTmpRepository;

    private void savePoint30032020v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL, Long m) {
        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
                String val = "999999999999999";
                String qu = "select col,fichier,post,rang from rpreptmp where " + "(fichier) = '"
                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and (post) = '"
                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and (col) = '"
                        + FICSQL.get(t).get("COL").toString() + "'" + "  and (rang) = '"
                        + FICSQL.get(t).get("RANG").toString() + "' ";
                System.out.println(qu);
                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
                if (PPP.size() > 0) {// modify existing
                    updateTemp(FICSQL, val, t);
                }
                if (t % m == 0) {
                    liveReportingService.detailsReportingToTheVue3(idtrait, fichier, m);
                }
            }
        }
    }

    private void savecol250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL, Long min) {
        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
                if (t % min == 0) {
                    liveReportingService.detailsReportingToTheVue3(idtrait, fichier, min);
                }
            }
        }
    }

    private void savePoint250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL, Long min) {
        if (FICSQL.size() > 0) {
            for (int t = 0; t < FICSQL.size(); t++) {
                String val = "999999999999999";
                String qu = "select col,fichier,post,rang from rpreptmp where " + "(fichier) = '"
                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and (post) = '"
                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and (col) = '"
                        + FICSQL.get(t).get("COL").toString() + "'" + "  and (rang) = '"
                        + FICSQL.get(t).get("RANG").toString() + "' ";
                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
                if (PPP.size() > 0) {// modify existing
                    updateTemp(FICSQL, val, t);
                } else {// save new
                    saveCalcTmpv1(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
                            FICSQL.get(t).get("FIELD").toString(), "M", val);

                }
                if (t % min == 0) {
                    liveReportingService.detailsReportingToTheVue3(idtrait, fichier, min);
                }
            }
        }
    }

    public void saveCalcv1(String dar, String fichier) throws ParseException {
//        ReportResultRepository.deleteByDar(dar);
        System.out.println("Date :" + dar);
//        for (int r = 0; r < fichier.size(); r++) {
        String sql = "delete from rprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
                + fichier + "'";
        jdbcTemplate.execute(sql);
        System.out.println("Sql :" + sql);
//        }
        sql = "insert into rprep (col,CRDT,CUSER,DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select col,CRDT,CUSER,DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from rpreptmp where dar =to_date('" + dar
                + "','yyyy-mm-dd') AND FICHIER = '" + fichier + "'";
        jdbcTemplate.execute(sql);
        String sql1 = "delete from rpreptmp where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
                + fichier + "'";
        jdbcTemplate.execute(sql1);
        System.out.println("Sql :" + sql1);
    }
    @Autowired
    LiveReportingServiceS liveReportingService;
    Map<String, Long> minim = new HashMap<String, Long>();

    @SuppressWarnings("null")
    public Map<String, String> getAveragtimeControle(String st, Model p) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> fich = parser.parseMap(st);
        List<String> fichier = (List<String>) fich.get("controle");
        Map<String, String> sp = new HashMap<String, String>();
        Double s = 0.0;
        try {
            final double dd = Double.parseDouble(getTimes());
//			System.out.println("dd value");
//			System.out.println(dd);

            fichier.forEach(e -> {
                // time for a treatment
//				System.out.println("type getting "+e);
                long startTime = System.nanoTime();
                double ys = 0L;// ReportFileRepository.countByFich(e).doubleValue();
                switch (e) {
                    case "inter":
//					System.out.println("count inter "+reportControleIntraRepository.countInter().get(0));
                        ys = reportControleIntraRepository.countInter().get(0).doubleValue();
                        break;

                    case "intra":
                        ys = reportControleInterRepository.countIntra().get(0).doubleValue();
                        break;
                    case "QLITE":
                        ys = reportControleQualityRepository.countQuality().get(0).doubleValue();
                        break;
                    case "CMPX":
                        ys = reportControleComplexRepository.countComplexe().get(0).doubleValue();
                        break;
                }
//				System.out.println("value is "+ys);
                long endTime = System.nanoTime();
                // get difference of two nanoTime values
                long timeElapsed = endTime - startTime;
                Double ss = ys * dd * timeElapsed * 0.000000152886;
//				System.out.println("the grobal time calculated");
//				System.out.println(ss);
//				sp.put(e, millisecond	stotime(Long.parseLong(ss.toString())));
                sp.put(e, millisecondstotime(ss.longValue()));
            });
        } catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("---------------------------------------------------");
        System.out.println(sp);
        return sp;
    }

    @SuppressWarnings("null")
    public Map<String, String> getAveragtimeFiles(String st, Model p) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> fich = parser.parseMap(st);
        List<String> fichier = (List<String>) fich.get("fichier");
        Map<String, String> sp = new HashMap<String, String>();
        Double s = 0.0;
        System.out.println("files to be treated");
        System.out.println(fichier);
        try {
            final double dd = Double.parseDouble(getTimes());
            System.out.println(dd);
            fichier.forEach(e -> {
                // time for a treatment
                long startTime = System.nanoTime();
                double ys = ReportFileRepository.countByFich(e).doubleValue();
                long endTime = System.nanoTime();

                // get difference of two nanoTime values
                long timeElapsed = endTime - startTime;
                Double ss = ys * dd * timeElapsed * 0.000000152886;
                System.out.println(ss);
//				sp.put(e, millisecondstotime(Long.parseLong(ss.toString())));
                sp.put(e, millisecondstotime(ss.longValue()));
            });
        } catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println(sp);
        return sp;
    }

    public String millisecondstotime(Long diff) {
        String valtime = "";
        try {
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            valtime = diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
                    + " seconds.";
            valtime = diffHours + " Hrs, " + diffMinutes + " Min, " + diffSeconds + " Sec";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valtime;
    }

    public String getTimes() throws ClassNotFoundException, JSONException, SQLException {
        return "0";//getGenerationAndSavingParam().get("AvergeTimeCoef");

    }

    private Long getTotal(String fichier, Map<String, String> type) {
        if (type.get("result").toString().equalsIgnoreCase(duplicate)) {
            String sql2 = "select count(id) as p from rppfich where fich = '" + fichier + "' ";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
            return Long.valueOf(result.get(0).get("p").toString());
        } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
            return 1L;
        } else {
            String sql2 = "select count(id) as p from rppfich where fich = '" + fichier + "' ";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
            return Long.valueOf(result.get(0).get("p").toString());
        }

    }

    public Map<String, Object> findByFich(String fic) {
        String sql = "Select u.calc,u.fichi,u.col,u.post.u.typeval,u.divd, a.rang from rppcalc.u,rppfich.a "
                + "where u.fich = ?";
        return jdbcTemplate.queryForMap(sql, new Object[]{fic});
    }

    public String findByAtt(List<String> att) {
        String values = "";
        String value = "'" + att.get(0) + "'";
        for (int i = 1; i < att.size(); i++) {
            values += " or att='" + att.get(i) + "'";
        }
        System.out.println(values);
        values = value + values;
        String sql = "Select val from rpatt " + "where att=" + values;
        System.out.println("the value of attri :" + sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        String cmd = "";
        for (int j = 0; j < result.size(); j++) {
            System.out.println("result is elm :" + result.get(j));
            if (result.get(j).get("VAL") != null) {
                if (result.get(j).get("VAL").toString().isEmpty()) {
                    cmd += " and 1=0";
                } else {
                    cmd += " and " + result.get(j).get("VAL").toString();
                }
            }
        }
        System.out.println("cmd value :" + cmd);
        return cmd;
    }

    public Map<String, Object> calcBySold(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        System.out.println("--------------------------------------------------------------");
        System.out.println(sql);
        if (condition == null) {
            sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
                    + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalCUMC(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "inbal.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum (REPLACE(inbal.cumc,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(inbal.cumc,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalCUMD(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "inbal.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select  coalesce(sum (REPLACE(inbal.cumd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(inbal.cumd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalSLDD(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "inbal.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum (REPLACE(inbal.sldd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(inbal.sldd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalSLDC(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum (REPLACE(insld.sldf,',' , '.')),0) as result from insld where insld.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        System.out.println(sql);
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(insld.sldf,',' , '.')),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalSLDCVD(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "inbal.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum (REPLACE(inbal.sldcvd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(inbal.sldcvd,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> calcByBalSLDCVC(List<Map<String, Object>> post, String condition, String date) {
        String bt = "";
        for (int j = 0; j < post.size(); j++) {
            String rt = "";
            if (j != post.size() - 1) {
                rt = " or ";
            }

            bt += "inbal.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

        }
        String sql = "select coalesce(sum (REPLACE(inbal.sldcvf,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
        if (condition == null) {
            sql = "select coalesce(sum (REPLACE(inbal.sldcvf,',' , '.')),0) as result from inbal where inbal.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")";
        }
        System.out.println("requÃªte sql post: " + sql);
        return jdbcTemplate.queryForMap(sql);
    }

    public List<Map<String, Object>> findByPost(String post) {
        String sql = "Select char1,char2 from rppost where codep = ? and dele = 0";
        return jdbcTemplate.queryForList(sql, new Object[]{post});
    }

    public List<Map<String, Object>> findByPostv1() {
        String sql = "Select char1,char2,codep from rppost where dele = 0";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findCalcByPoint(String fichier) {
        String sql = "((select col,fich,poste,rang from rppfich WHERE fich ='" + fichier
                + "' ) MINUS (select col,fich,poste , rang from rppfich a where fich ='" + fichier
                + "' and EXISTS(select * from rpcalc u where u.fichi = a.fich  and u.post =a.poste  and u.col = a.col and u.fichi ='"
                + fichier + "')))";

        System.out.println(sql);
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional
    public List<Map<String, Object>> findCalcByPoint1(String fichier) {
        String sql = "select col,fich,poste,rang,CONCAT('CH',CONCAT(poste,CONCAT('C',col))) field from rppfich where fich = '"
                + fichier + "' and gen ='***'";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findCalcByPoint1_F1139(String fichier, String source) {
        String sql = "select col,fich,poste,rang,CONCAT('CH',CONCAT(poste,CONCAT('C',col))) field from rppfich where fich = '"
                + fichier + "' and gen ='***' and source  = '" + source + "'";
        System.out.println(sql);
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional
    public List<Map<String, Object>> findCalcByPoint2(String fichier) {
        String sql = "select rang,poste from rppfich where fich = '" + fichier + "' group by poste,rang";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findCalcByPoint2_F1139(String fichier, String source) {
        String sql = "select rang,poste from rppfich where fich = '" + fichier + "' and source = '" + source
                + "'  group by poste,rang";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findByCalc(String fic) {
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd, a.rang ,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =?";
        return jdbcTemplate.queryForList(sql, new Object[]{fic});
    }

    @Transactional
    public List<Map<String, Object>> findCalcByPost(String fic) {
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.field ,u.source ,(case when a.rang is not NULL then a.rang else 0 end) rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='P' and a.gen is null ORDER BY u.COL asc,u.POST";
        return jdbcTemplate.queryForList(sql, new Object[]{fic});
    }

    public List<Map<String, Object>> findCalcByPostPlus(String fic) {//get all post, many
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.field ,u.source ,(case when a.rang is not NULL then a.rang else 0 end) rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='A' ORDER BY u.POST,u.COL asc";
        return jdbcTemplate.queryForList(sql, new Object[]{fic});
    }

    public List<Map<String, Object>> findCalcByPost_F1139(String fic, String s) {
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.field ,u.source ,(case when a.rang is not NULL then a.rang else 0 end) rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='P' and a.source = '"
                + s + "'ORDER BY u.POST,u.COL asc";
        return jdbcTemplate.queryForList(sql, new Object[]{fic});
    }

    public List<String> separeteDataPost1(String formule) {
        formule = formule.replaceAll(" ", "");
        List<String> ret = new ArrayList<String>();
        int firstIndex = formule.indexOf('(');
        int lasttIndex = formule.indexOf(')');
        String post = formule.substring(0, firstIndex);
        String at = "";
        System.out.println("le post:" + post);
        ret.add(post);
        if (lasttIndex != firstIndex + 1) {
            int count = StringUtils.countOccurrencesOf(formule, ",");
            int att = formule.indexOf(',');
            System.out.println(count);
            if (count > 0) {
                for (int i = 0; i <= count; i++) {
                    at = formule.substring(firstIndex + 1, att);
                    firstIndex = att;
                    if (i < count - 1) {
                        att = formule.indexOf(',', att + 1);
                    } else {
                        att = lasttIndex;
                    }
                    ret.add(at);
                }

            } else {
                at = formule.substring(firstIndex + 1, lasttIndex);
                System.out.println(at);
                ret.add(at);
            }
        }

        return ret;

    }

    public List<String> separeteDataPostpls(String formule) {
        formule = formule.replaceAll(" ", "");
        List<String> ret = new ArrayList<String>();
        int firstIndex = formule.indexOf('(');
        int lasttIndex = formule.indexOf(')');
        String post = formule.substring(0, firstIndex);
        String at = "";
        System.out.println("le post:" + post);
        ret.add(post);
        if (lasttIndex != firstIndex + 1) {
            int count = StringUtils.countOccurrencesOf(formule, ",");
            int att = formule.indexOf(',');
            System.out.println(count);
            if (count > 0) {
                for (int i = 0; i <= count; i++) {
                    at = formule.substring(firstIndex + 1, att);
                    firstIndex = att;
                    if (i < count - 1) {
                        att = formule.indexOf(',', att + 1);
                    } else {
                        att = lasttIndex;
                    }
                    ret.add(at);
                }

            } else {
                at = formule.substring(firstIndex + 1, lasttIndex);
                System.out.println(at);
                ret.add(at);
            }
        }

        return ret;

    }

    public List<String> separeteDataPost(String formule) {
        formule = formule.replaceAll(" ", "");
        List<String> ret = new ArrayList<String>();
        int firstIndex = formule.indexOf('(');
        int lasttIndex = formule.indexOf(')');
        String post = formule.substring(0, firstIndex);
        String at = "";
        System.out.println("le post:" + post);
        ret.add(post);
        if (lasttIndex != firstIndex + 1) {
            int count = StringUtils.countOccurrencesOf(formule, ",");
            int att = formule.indexOf(',');
            if (count > 0) {
                for (int i = 0; i <= count; i++) {
                    at = formule.substring(firstIndex + 1, att);
                    firstIndex = att;
                    if (i < count - 1) {
                        att = formule.indexOf(',', att + 1);
                        System.out.println("attribute seen:" + at);
                    } else {
                        att = lasttIndex;
                        System.out.println("attribute seen:" + at);
                    }
                    ret.add(at);
                }
            } else {
                at = formule.substring(firstIndex + 1, lasttIndex);
                System.out.println("attribute seen:" + at);
                ret.add(at);
            }
        }

        return ret;

    }

    public void saveCalcTmp(String etab, String post, String col, String solde, String fich, String dar, int rang,
            int d, String cuser, int status, String field, String typeval) {
        System.out.println(etab);
        int etabInt = Integer.parseInt(etab);
        String sql = "INSERT INTO rpreptmp (etab,post, col, VAL" + typeval
                + ",fichier,dar,rang,cuser,muser,status,field) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                System.out.println("Solde to tmp: " + solde);
                ps.setInt(1, etabInt);
                ps.setString(2, post);
                ps.setString(3, col);
                if (typeval.equalsIgnoreCase("M")) {
                    System.out.println("Solde to before trans: " + solde);
                    System.out.println("Solde transformed: " + Double.parseDouble(solde));
                    ps.setDouble(4, Double.parseDouble(solde));
                } else if (typeval.equalsIgnoreCase("D")) {
                    ps.setString(4, solde);
                } else if (typeval.equalsIgnoreCase("C")) {
                    ps.setString(4, solde);
                } else if (typeval.equalsIgnoreCase("T")) {
                    ps.setDouble(4, Double.parseDouble(solde));
                }
                ps.setString(5, fich);
                ps.setDate(6, Date.valueOf(dar));
                ps.setInt(7, rang);
                ps.setString(8, cuser);
                ps.setString(9, cuser);
                ps.setInt(10, status);
                ps.setString(11, field);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        };

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void saveCalcTmpv1(String etab, String post, String col, String solde, String fich, String dar, int rang,
            int d, String cuser, int status, String field, String typeval, String g) {
        int etabInt = Integer.parseInt(etab);
        String sql = "INSERT INTO rpreptmp (etab,post, col, VAL" + typeval
                + ",fichier,dar,rang,cuser,muser,status,field,rawres) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, etabInt);
                ps.setString(2, post);
                ps.setString(3, col);
                if (typeval.equalsIgnoreCase("M")) {
                    ps.setDouble(4, Double.parseDouble(solde));
                } else if (typeval.equalsIgnoreCase("D")) {
                    ps.setString(4, solde);
                } else if (typeval.equalsIgnoreCase("C")) {
                    ps.setString(4, solde);
                } else if (typeval.equalsIgnoreCase("T")) {
                    ps.setDouble(4, Double.parseDouble(solde));
                }
                ps.setString(5, fich);
                ps.setDate(6, Date.valueOf(dar));
                ps.setInt(7, rang);
                ps.setString(8, cuser);
                ps.setString(9, cuser);
                ps.setInt(10, status);
                ps.setString(11, field);
                ps.setString(12, g);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        };

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void saveCalc1(String dar, List<String> fichier) throws ParseException {
//        ReportResultRepository.deleteByDar(dar);
        System.out.println("Date :" + dar);
        for (int r = 0; r < fichier.size(); r++) {
            String sql = "delete from rprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
                    + fichier.get(r) + "'";
            jdbcTemplate.execute(sql);
            System.out.println("Sql :" + sql);
        }

//        List<ReportResultTmp> tmp = ReportResultTmpRepository.findByDar(dar);
//        sql = "select * from rpreptmp where dar =?";
//        List<Map<String, Object>> tmp =jdbcTemplate.queryForList(sql, new Object[] { dar });
//        
//        sql = "insert into rprep select * from rpreptmp where dar =to_date('"+dar+"','dd-mm-yyyy')";
        sql = "insert into rprep (\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select ((CASE WHEN(SELECT \"MAX\"(\"ID\") FROM RPREP) IS NULL THEN 1 ELSE (SELECT \"MAX\"(\"ID\") FROM RPREP) end)+ROWNUM)\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from rpreptmp where dar =to_date('" + dar
                + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);
//        for (ReportResultTmp s : tmp) {
//        for (int t = 0; t<tmp.size(); t++){
////            ReportResult save = new ReportResult(s.getDar(),s.getEtab(),s.getFeuille(),s.getFeild(),s.getPost(),s.getDoc(),
////                    s.getCol(),s.getCuser(),s.getMuser(),s.getVal(),s.getRang(),s.getFichier());
//            ReportResult save = new ReportResult(new SimpleDateFormat("dd-MM-yyyy").parse(tmp.get(t).get("dar").toString()),tmp.get(t).get("etab").toString(),isNullOrEmpty(tmp.get(t).get("feuille")),isNullOrEmpty(tmp.get(t).get("feild")),isNullOrEmpty(tmp.get(t).get("post")),isNullOrEmpty(tmp.get(t).get("doc")),
//                    isNullOrEmpty(tmp.get(t).get("col")),isNullOrEmpty(tmp.get(t).get("cuser")),isNullOrEmpty(tmp.get(t).get("muser")),isNullOrEmpty(tmp.get(t).get("val")),Long.parseLong(tmp.get(t).get("rang").toString()),isNullOrEmpty(tmp.get(t).get("fichier")),Integer.parseInt(tmp.get(t).get("status").toString()));
//            ReportResultRepository.save(save);
//        }
//        ReportResultTmpRepository.deleteByDar(dar);
        String sql1 = "delete from rpreptmp where dar =to_date('" + dar + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql1);
//        String sql = "Select* from rpreptmp where dar =?";
//        List<Map<String, Object>> findByCalc = jdbcTemplate.queryForList(sql, new Object[] { dar });
//        String sql = "INSERT INTO rpreptmp (etab,post, col, val,fichier,dar,rang,id) VALUES (?,?,?,?,?,?,?,?)";
//        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setInt(1, etabInt);
//                ps.setString(2, post);
//                ps.setString(3, col);
//                ps.setString(4, solde);
//                ps.setString(5, fich);
//                ps.setString(6, dar);
//                ps.setInt(7, rang);
//                ps.setInt(8, d);
//            }
//
//                @Override
//                public int getBatchSize() {
//                        return 1;
//                }
//        };
//
//        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void deleteRep(String dar, List<String> fichier) throws ParseException {
//        ReportResultRepository.deleteByDar(dar);
        System.out.println("Date :" + dar);
        for (int r = 0; r < fichier.size(); r++) {
            String sql = "delete from rprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
                    + fichier.get(r) + "'";
            jdbcTemplate.execute(sql);
            System.out.println("Sql :" + sql);
        }
    }

    public void saveCalc(String dar, List<String> fichier) throws ParseException {
//        ReportResultRepository.deleteByDar(dar);
        System.out.println("Date :" + dar);
        for (int r = 0; r < fichier.size(); r++) {
            String sql = "delete from rprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
                    + fichier.get(r) + "'";
            jdbcTemplate.execute(sql);
            System.out.println("Sql :" + sql);
        }

        sql = "insert into rprep (\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select ((CASE WHEN(SELECT \"MAX\"(\"ID\") FROM RPREP) IS NULL THEN 1 ELSE (SELECT \"MAX\"(\"ID\") FROM RPREP) end)+ROWNUM)\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from rpreptmp where dar =to_date('" + dar
                + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);

    }

    public String isNullOrEmpty(Object str) {
        if (str != null && !str.toString().isEmpty()) {
            return str.toString();
        }
        return null;
    }

    public void saveCalcTmp(String etab, Map<String, Object> obj, String dar, String cuser) throws ParseException {
        for (int t = 0; t < obj.size(); t++) {
            ReportResultTmp save = new ReportResultTmp(new SimpleDateFormat("dd-MM-yyyy").parse(dar), etab,
                    obj.get("feuille").toString(), obj.get("feild").toString(), obj.get("post").toString(),
                    obj.get("doc").toString(), obj.get("col").toString(), cuser, cuser,
                    Double.parseDouble(obj.get("val").toString()), Long.parseLong(obj.get("rang").toString()),
                    obj.get("fichier").toString(), Integer.parseInt(obj.get("status").toString()));
            ReportResultTmpRepository.save(save);

        }
    }
    @Autowired
    NomenclatureRepository nomenclatureRepository;

    public void saveCalcTmp1(String etab, Map<String, Object> obj, String dar, String cuser, String idtrait, long l)
            throws ParseException {
        System.out.println(obj);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(dar));
        ReportResultTmp save = new ReportResultTmp(new SimpleDateFormat("yyyy-MM-dd").parse(dar), etab,
                obj.get("field").toString(), obj.get("post").toString(), obj.get("col").toString(), cuser, cuser,
                obj.get("valc").toString(), Long.parseLong(obj.get("rang").toString()), obj.get("fichier").toString(),
                Integer.parseInt(obj.get("status").toString()));
        ReportResultTmpRepository.save(save);
        liveReportingService.detailsReportingToTheVue1(idtrait, obj.get("fichier").toString(), l);
    }

    public Map<String, Dynamic> getCalculatedFields(String fich) {

        return null;
    }

    public List<Long> dismantlePostCalculate(String s) {
        List<Long> n = new ArrayList<Long>();
        s = s.trim().substring(5, s.trim().length() - 1);
        String[] split = s.split(",");

        for (String e : split) {
            n.add(reportAttributeRepository.findByDeleAndAttContaining(false, e).get(0).getId());
        }
        return n;
    }

    public Map<String, String> getGenerationAndSavingParam()
            throws SQLException, ClassNotFoundException, JSONException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        Statement stmt = null;
        ResultSet result = null;
        try {
            Nomenclature s = nomenclatureRepository.findBytabcdAndDeleAndAcscd("4002", 0, "0001");
            connection = DriverManager.getConnection(s.getLib1(), s.getLib2(), s.getLib3());

            String select = "";
            stmt = connection.createStatement();//(Statement) connection.createStatement();
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
                    System.out.println("ip :" + result.getString("lib2"));
                }
                if (result.getString("acscd").equalsIgnoreCase("0012")) {
                    PARAM.put("port", result.getString("lib2"));
                    System.out.println("port :" + result.getString("lib2"));

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
                    PARAM.put("sameServer", result.getString("lib2")); // 1 le fichier est stoquÃ© sur le meme serveur que
                    // le
                    // webservice 2 il est sur un serveur distant.
                }
                if (result.getString("acscd").equalsIgnoreCase("0029")) {
                    PARAM.put("minimumNumber", result.getString("lib2")); // 1e nombre minimum d element a traiter avant
                    // insertion dans la bd
                }

                if (result.getString("acscd").equalsIgnoreCase("0031")) {
                    PARAM.put("F1139", result.getString("lib2").trim()); // statut du fichier
                }
                if (result.getString("acscd").equalsIgnoreCase("0032")) {
                    PARAM.put("AvergeTimeCoef", result.getString("lib2").trim()); // statut du fichier
                }
//                System.out.println("END DISTRIBUTION");
            }
            System.out.println("END GETGENARATION AND SAVING PARAMS");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return PARAM;

    }

    public Map<String, String> getType1(String fichier) throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        Map<String, String> PARAM = new HashMap<>();
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        Statement stmt = null;
        ResultSet result = null;
        try {
            Nomenclature s = nomenclatureRepository.findBytabcdAndDeleAndAcscd("4002", 0, "0001");
            connection = DriverManager.getConnection(s.getLib1(), s.getLib2(), s.getLib3());

            stmt = connection.createStatement();//(Statement) connection.createStatement();
            select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0' and lib2='" + fichier + "'";
            System.out.println(select);
            result = stmt.executeQuery(select);
            String ppp = "";
            while (result.next()) {
                switch (result.getString("taux1")) {
                    case "0":
                        ppp = "calculate";
                        break;
                    case "2":
                        ppp = "sql";
                        break;
                    case "1":
                        ppp = "duplicate";
                        break;
                    case "3":
                        ppp = "duplicatepost";
                        break;
                    default:
                        ppp = "not define";
                }

                PARAM.put("result", ppp);
                PARAM.put("link", result.getString("lib4"));
                PARAM.put("sql", result.getString("lib5"));
                PARAM.put("col_number", result.getString("taux4"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return PARAM;
    }

    @Override
    public String getmin() throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        select = "SELECT * FROM sanm  WHERE tabcd='0012' AND dele='0' and acscd='0029'";
        System.out.println(select);
        ResultSet result = connect.executeQuery(select);
        while (result.next()) {
            return result.getString("lib2");
        }
        return "0";
    }

    @Override
    public List<ReportControleIntra> QUERYSET(Statement s) throws SQLException, ClassNotFoundException, JSONException {
        List<ReportControleIntra> sdf = new ArrayList<>();
        String select = "";
        select = "SELECT * FROM sanm  WHERE tabcd='3019' AND dele='0' ";
        System.out.println(select);
        ResultSet result = s.executeQuery(select);
        while (result.next()) {
            ReportControleIntra e = new ReportControleIntra();
            e.setSign(result.getString("lib5"));
            e.setCd(result.getString("lib4"));
            e.setPer(result.getString("lib2"));
            e.setDele(result.getString("acscd"));
            e.setCg(result.getString("lib3"));
//            e.setPer(result.getString("lib1"));
            sdf.add(e);
        }
        return sdf;
    }

    public Map<String, String> getType12(String fichier) throws SQLException, ClassNotFoundException, JSONException {
        Map<String, String> PARAM = new HashMap<>();
        String select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0' and lib2='" + fichier + "'";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        ResultSet result = null;
        try {
            Nomenclature s = nomenclatureRepository.findBytabcdAndDeleAndAcscd("4002", 0, "0001");
            connection = DriverManager.getConnection(s.getLib1(), s.getLib2(), s.getLib3());
            result = connection.createStatement().executeQuery(select);
            String ppp, gg = "";
            int ee = 0;
            while (result.next()) {
                ee++;
                gg = result.getString("taux4");
                switch (result.getString("taux1")) {
                    case "0":
                        ppp = "calculate";
                        break;
                    case "1":
                        ppp = "duplicate";
                        break;
                    case "2":
                        ppp = "sql";
                        break;
                    case "3":
                        ppp = "duplicateNoPost";
                        break;
                    default:
                        ppp = "not define";
                }

                PARAM.put("result", ppp);
                PARAM.put("size", gg);
                PARAM.put("sql", result.getString("lib5"));
                System.out.println("directGenerate :" + result.getString("taux5"));
                PARAM.put("directGenerate", result.getString("taux5"));
            }
            if (ee == 0) {
                System.out.println("noting found for this request :" + select);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    /* Ignored */
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    /* Ignored */
                }
            }
            return PARAM;
        }
    }

    public Map<String, String> getType11(String fichier) throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0' and lib2='" + fichier + "'";
        System.out.println("BEFORE QUERRY");
        System.out.println(select);
        ResultSet result = connect.executeQuery(select);
        System.out.println("AFTER QUERRY");
        String ppp = "";
        int ee = 0;
        while (result.next()) {
            ee++;
            switch (result.getString("taux1")) {
                case "0":
                    ppp = "calculate";
                    break;
                case "1":
                    ppp = "duplicate";
                    break;
                case "2":
                    ppp = "sql";
                    break;
                case "3":
                    ppp = "duplicateNoPost";
                    break;
                default:
                    ppp = "not define";
            }

            PARAM.put("ncol", result.getString("taux4"));
            PARAM.put("result", ppp);
            PARAM.put("sql", result.getString("lib5"));
        }
        if (ee == 0) {
            System.out.println("noting found for this request");
        }
        return PARAM;
    }

    public Map<String, Map<String, String>> getType13() throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        Map<String, Map<String, String>> er = new HashMap<>();
        select = "SELECT taux1,lib2,taux4 FROM sanm  WHERE tabcd='3009' AND dele='0'";
        System.out.println("BEFORE QUERRY");
        System.out.println(select);
        ResultSet result = connect.executeQuery(select);
        System.out.println("AFTER QUERRY");
        String ppp = "";
        int ee = 0;
        while (result.next()) {
            ee++;
            Map<String, String> elm = new HashMap<String, String>();
            switch (result.getString("taux1")) {
                case "0":
                    ppp = "calculate";
                    break;
                case "1":
                    ppp = "duplicate";
                    break;
                case "2":
                    ppp = "sql";
                    break;
                case "3":
                    ppp = "duplicateNoPost";
                    break;
                default:
                    ppp = "not define";
            }

            elm.put("ncol", result.getString("taux4"));
            elm.put("typ", ppp);
            er.put(result.getString("lib2"), elm);
        }
        result.close();
        return er;
    }

    public Map<String, Object> findBySql(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    public int findBySql1(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int findBySql2(String sql, String d, String f) {
        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
        return jdbcTemplate.update(sql);
    }

    @Transactional
    public int findBySql3(String sql, String d, String f, String p, String se, Statement r, String ryy, Long idOpe, String etab) throws SQLException, ClassNotFoundException {
        ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement rtt = null;
        ExecutorService service = Executors.newFixedThreadPool(10);//eg 15 dat and divid 4 

        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
        int o = 0;
        Long total = 0L;
        List<String[]> list1 = new ArrayList<String[]>();
        try {
            while (rsp.next()) {
                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
                String v = new String(decoder);
                Class.forName(rsp.getString("lib2"));
                rtt = DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
                rs1 = rtt.executeQuery("select count(*) r from (" + sql + ")");
                rs1.next();
                total = new Long(rs1.getString("r"));
                rs = rtt.executeQuery(sql);
            }
            int Friqunce = (int) Math.ceil(total / 100.0);
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, f, new Long(total));
            int y = 0;
            int y1 = 0;
            String te = "";
            int s = Integer.parseInt(p);
            List<String> list = new ArrayList<String>();

            while (rs.next()) {
                y++;
                y1++;
                String sqlput = "";
                String sqlput1 = "";
                for (int i = 1; i <= s; i++) {
                    sqlput += (i == 1 ? "" : ",") + "col" + i;
                    sqlput1 += (i == 1 ? "" : ",") + "'" + (rs.getString(i) == null ? "" : rs.getString(i).replaceAll("\\'", "\\''")) + "'";
                }
                list.add("INSERT INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d + "','yyyy-mm-dd'),'" + f + "','" + etab + "')");
                if (y1 % 5000 == 0) {
                    String[] yyy = list.toArray(new String[0]);
                    service.execute(new Runnable() {
                        @Transactional
                        public void run() {
                            try {
                                System.out.println("Starting save ");
                                jdbcTemplate.batchUpdate(yyy);
                                liveReportingService.detailsReportingToTheVue3(ryy, f, 5000L);
                                System.out.println("ending Saving :");
                            } catch (Exception r) {
                                System.out.println(r.getMessage());
                            }
                        }
                    });
//                    jdbcTemplate.batchUpdate(list.toArray(new String[0]));
                    System.out.println("the quantity alread in " + y1);
//                    list1.add(list.toArray(new String[0]));
                    list.clear();
                }
            }
            if (list.size() > 0) {
                System.out.println(" get the rest left");
//                jdbcTemplate.batchUpdate(list.toArray(new String[0]));
                System.out.println("its oke :" + list.size());
                String[] yyy = list.toArray(new String[0]);
                service.execute(new Runnable() {
                    @Transactional
                    public void run() {
                        try {
                            System.out.println("starting save :");
                            jdbcTemplate.batchUpdate(yyy);
                            liveReportingService.detailsReportingToTheVue3(ryy, f, new Long(yyy.length));
                            System.out.println("ending Saving :");
                        } catch (Exception r) {
                            System.out.println(r.getMessage());
                        }
                    }
                });
                list.clear();
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("Yann service did not terminate");
                e.printStackTrace();
            }

            if (rs != null) {
                rs.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
            if (rtt != null) {
                rtt.close();
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("inserted query Error in File " + f + " : " + e.getMessage());
        }
        lotsavequery(list1, f, ryy, 5000);
        return o;

    }

    private void lotsavequery(List<String[]> tttt, String f, String ryy, Integer t) throws NumberFormatException, DataAccessException {
        ExecutorService service = Executors.newFixedThreadPool(10);//eg 15 dat and divid 4 
        for (String[] globalpostqury : tttt) {
            service.execute(new Runnable() {
                @Transactional
                public void run() {
                    try {
                        jdbcTemplate.batchUpdate(globalpostqury);
                        liveReportingService.detailsReportingToTheVue3(ryy, f, 5000L);
                        System.out.println("Start Saving :");
                    } catch (Exception r) {
                        System.out.println(r.getMessage());
                        System.out.println(globalpostqury);
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Yann service did not terminate");
            e.printStackTrace();
        }

    }

//1201
    @Transactional
    public int findBySql5(String sql, String d, String f, String p, String se, Statement r, String ryy, Long idOpe, String etab) throws SQLException, ClassNotFoundException {
        ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement rtt = null;
        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
        int o = 0;
        Long total = 0L;
        try {
            while (rsp.next()) {//only one connection
                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
                String v = new String(decoder);
                Class.forName(rsp.getString("lib2"));
                rtt = DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
                rs1 = rtt.executeQuery("select count(*) r from (" + sql + ")");
                rs1.next();
                total = new Long(rs1.getString("r"));
                rsp = r.executeQuery("select * from sanm where tabcd = '' and dele =''");
                while (rsp.next()) {//for the different agence
                    rs = rtt.executeQuery(sql.replaceAll("key", rsp.getString("key")));
                    int Friqunce = (int) Math.ceil(total / 100.0);
                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, f, new Long(total));
                    int y = 0;
                    int y1 = 0;
                    String te = "";
                    int s = Integer.parseInt(p);
                    List<String> list = new ArrayList<String>();
                    while (rs.next()) {
                        y++;
                        y1++;
                        String sqlput = "";
                        String sqlput1 = "";
                        for (int i = 1; i <= s; i++) {
                            sqlput += (i == 1 ? "" : ",") + "col" + i;
                            sqlput1 += (i == 1 ? "" : ",") + "'" + (rs.getString(i) == null ? "" : rs.getString(i).replaceAll("\\'", "\\''")) + "'";
                        }

//                jdbcTemplate.execute("INSERT INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d
//                        + "','yyyy-mm-dd'),'" + f + "','"+etab+"')");
                        list.add("INSERT INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d + "','yyyy-mm-dd'),'" + f + "','" + etab + "')");
                        if (y % Friqunce == 0) {

//                    System.out.println(Friqunce + " autity in " + (System.na - Starttime));
//                    Starttime = System.nanoTime();
                            liveReportingService.detailsReportingToTheVue3(ryy, f, new Long(Friqunce));
                        }

                        if (y1 % 1000 == 0) {
                            jdbcTemplate.batchUpdate(list.toArray(new String[0]));
                            list.clear();
                        }
                    }
                    if (te != "") {
                        System.out.println(" get the rest left");
                        jdbcTemplate.batchUpdate(list.toArray(new String[0]));
                        list.clear();
                    }

                }
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rtt != null) {
                    rtt.close();
                }
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("inserted query Error in File " + f + " : " + e.getMessage());
        }
        return o;

    }

    @Transactional
    public int findBySql4(String sql, String d, String f, String p, String se, Statement r, String ryy, Long idOpe, String etab) throws SQLException, ClassNotFoundException {
        ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement rtt = null;
        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
        int o = 0;
        Long total = 0L;
        List<String> roo = new ArrayList<>();
        try {
            while (rsp.next()) {
                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
                String v = new String(decoder);
                Class.forName(rsp.getString("lib2"));
                rtt = DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
                rs1 = rtt.executeQuery("select count(*) r from (" + sql + ")");
                rs1.next();
                total = new Long(rs1.getString("r"));
                rs = rtt.executeQuery(sql);
            }
            int Friqunce = (int) Math.ceil(total / 100.0);
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, f, new Long(total));
            int y = 0;
            int y1 = 0;
            String te = "";
            int s = Integer.parseInt(p);
//            Long Starttime = System.nanoTime();
            while (rs.next()) {
                y++;
                y1++;
                String sqlput = "";
                String sqlput1 = "";
                for (int i = 1; i <= s; i++) {
                    sqlput += (i == 1 ? "" : ",") + "col" + i;
                    sqlput1 += (i == 1 ? "" : ",") + "'" + (rs.getString(i) == null ? "" : rs.getString(i).replaceAll("\\'", "\\''")) + "'";
                }

//                jdbcTemplate.execute("INSERT INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d
//                        + "','yyyy-mm-dd'),'" + f + "','"+etab+"')");
                te += " INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d + "','yyyy-mm-dd'),'" + f + "','" + etab + "')";
                if (y % Friqunce == 0) {

//                    System.out.println(Friqunce + " autity in " + (System.na - Starttime));
//                    Starttime = System.nanoTime();
                    liveReportingService.detailsReportingToTheVue3(ryy, f, new Long(Friqunce));
                }

                if (y1 % 300 == 0) {
                    te = "INSERT All " + te + " SELECT 1 FROM DUAL";
//                    jdbcTemplate.execute(te);
                    roo.add(te);
                    te = "";
                }
            }
            if (te != "") {
                System.out.println(" get the rest left");
                te = "INSERT All " + te + "SELECT 1 FROM DUAL";
//                jdbcTemplate.execute(te);
                roo.add(te);
            }

            lotsave(roo);
            if (rs != null) {
                rs.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
            if (rtt != null) {
                rtt.close();
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("inserted query Error in File " + f + " : " + e.getMessage());
        }
        return o;

    }

    private void lotsave(List<String> tttt) throws NumberFormatException, DataAccessException {
        ExecutorService service = Executors.newFixedThreadPool(10);//eg 15 dat and divid 4 
        for (String globalpostqury : tttt) {
            service.execute(new Runnable() {
                @Transactional
                public void run() {
                    try {
                        jdbcTemplate.execute(globalpostqury);
                        System.out.println("Start Saving :");
                    } catch (Exception r) {
                        System.out.println(r.getMessage());
                        System.out.println(globalpostqury);
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Yann service did not terminate");
            e.printStackTrace();
        }

    }

//    public int findBySql3(String sql, String d, String f, String p, String se, Statement r, String ryy, Long idOpe,String etab) throws SQLException, ClassNotFoundException {
//        ResultSet rsp = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + se.trim() + "'");
//        ResultSet rs = null;
//        ResultSet rs1 = null;
//        Statement rtt = null;
//        jdbcTemplate.update("DELETE from sqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
//        int o = 0;
//        Long total = 0L;
//        try {
//            while (rsp.next()) {
//                byte[] decoder = Base64.getDecoder().decode(rsp.getString("pass"));
//                String v = new String(decoder);
//                Class.forName(rsp.getString("lib2"));
//                rtt = DriverManager.getConnection(rsp.getString("lib1"), rsp.getString("login"), v).createStatement();
//                rs1 = rtt.executeQuery("select count(*) r from (" + sql + ")");
//                rs1.next();
//                total = new Long(rs1.getString("r"));
//                rs = rtt.executeQuery(sql);
//            }
//            int Friqunce = (int) Math.ceil(total / 100.0);
//            liveReportingService.beginDetailsReportingToTheVue2(idOpe, f, new Long(total));
//            int y = 0;
//            int s = Integer.parseInt(p);
//            while (rs.next()) {
//                y++;
//                String sqlput = "";
//                String sqlput1 = "";
//                for (int i = 1; i <= s; i++) {
//                    sqlput += (i == 1 ? "" : ",") + "col" + i;
//                    sqlput1 += (i == 1 ? "" : ",") + "'" + (rs.getString("col" + i) == null ? "" : rs.getString("col" + i).replaceAll("\\'", "\\''")) + "'";
//                }
//                jdbcTemplate.execute("INSERT INTO sqltype (" + sqlput + ",dar,fichi,etab)values(" + sqlput1 + ",to_date('" + d
//                        + "','yyyy-mm-dd'),'" + f + "','"+etab+"')");
//                if (y % Friqunce == 0) {
//                    liveReportingService.detailsReportingToTheVue3(ryy, f, new Long(Friqunce));
//                }
//            }
//            if (rs != null) {
//                rs.close();
//            }
//            if (rs1 != null) {
//                rs1.close();
//            }
//            if (rtt != null) {
//                rtt.close();
//            }
//        } catch (SQLSyntaxErrorException e) {
//            System.out.println("inserted query Error in File " + f + " : " + e.getMessage());
//        }
//        return o;
//
//    }
    public BigDecimal separeteDataTrait(String formule, String etab, String fichier, String date)
            throws ParseException {
        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        formule = formule.replaceAll(" ", "");
        String frm = formule.replaceAll("-", "+");
        frm = formule.replaceAll("/", "+");
        frm = formule.replaceAll("\\*", "+");
        String[] str = frm.split("\\+");
        String val = "0";
        int i = 0;
        int j = 1;
        System.out.println("Date sql:" + date);
        for (String s : str) {// each formular in the + operator
            List<String> string = separeteData(s);
            sql = "select * from rpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                    + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
                    + string.get(1).trim() + "'";
            List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//            System.out.println("test sql :" + sql);
            if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
                System.out.println("this is s value (first if): " + s);
                if (s.substring(0, 1).equalsIgnoreCase("M")) {
                    val = s.substring(1, s.length());
                } else {
                    System.out.println("Element size found here:" + tmp.size());
                    if (tmp.size() == 1) {
                        val = tmp.get(0).get("valm").toString();
                    } else {
                        val = "0";
                    }
                }
//                System.out.println("passe1 :" + val);
//                System.out.println("formule :" + formule);
//                System.out.println("expression :" + s.trim());
//                System.out.println("value :" + val + "");
                formule = formule.replaceAll(s.trim(), val + "");
//				formule = formule.replaceAll(s, " " + val + " ");

                i = 1;
            } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
                System.out.println("this is s value (second if):" + s);
                if (tmp.size() == 1) {
                    val = tmp.get(0).get("valM").toString();
                } else {
                    val = "0";
                }
                System.out.println("passe2 :" + val);
                formule = formule.replaceAll(s, " " + val + " ");

                i = 1;
            }
        }
        System.out.println("its the value return by " + formule + " is : " + eval(formule));
        if (i == 1) {
            System.out.println("result formule : " + formule);
            String[] input = formule.split(" ");
            String[] output = infixToRPN(input);

            // Build output RPN string minus the commas
            for (String token : output) {
                System.out.print(token + " ");
            }

            System.out.println(output);
            rt = RPNtoDouble(output);
            System.out.println("result after :  " + rt);
        }

        return rt;
    }

    public BigDecimal separeteDataTrait1(String formule, String etab, String fichier, String date)
            throws ParseException {
        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        formule = " " + formule + " ";
        String frm = formule.replaceAll("-", "+");
        frm = formule.replaceAll("/", "+");
        frm = formule.replaceAll("\\*", "+");
        String[] str = frm.split("\\+");
        String val = "0";
        int i = 0;
        int j = 1;
        System.out.println("Date sql:" + date);
        for (String s : str) {// each formular in the + operator
            List<String> string = separeteData(s);
            sql = "select * from rpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                    + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
                    + string.get(1).trim() + "'";
            List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//            System.out.println("test sql :" + sql);
            if (tmp.isEmpty()) {
                sql = "select * from rpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichier='" + fichier + "' and field ='" + s.trim() + "'";
                tmp = jdbcTemplate.queryForList(sql);
            }
            if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
                System.out.println("this is s value (first if): " + s);
                if (s.substring(0, 1).equalsIgnoreCase("M")) {
                    val = s.substring(1, s.length());
                } else {
                    System.out.println("Element size found here:" + tmp.size());
                    if (tmp.size() == 1) {
                        val = tmp.get(0).get("valm").toString();
                    } else {
                        val = "0";
                    }
                }
                System.out.println("passe1 :" + val);
                System.out.println("formule :" + formule);
                System.out.println("expression :" + s.trim());
                val = (val.equals("999999999999999")) ? "0" : val;
                System.out.println("value :" + val + "");
                formule = formule.replaceAll(s, " " + val + " ");
//				formule = formule.replaceAll(s, " " + val + " ");

                i = 1;
            } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
                System.out.println("this is s value (second if):" + s);
                if (tmp.size() == 1) {
                    val = tmp.get(0).get("valM").toString();
                } else {
                    val = "0";
                }
                val = (val.equals("999999999999999")) ? "0" : val;
                System.out.println("passe2 :" + val);

                formule = formule.replaceAll(s, " " + val + " ");

                i = 1;
            }
        }
        System.out.println("its the value return by " + formule + " is : " + eval(formule));
        if (i == 1) {
            System.out.println("result formule : " + formule);
            String[] input = formule.split(" ");
            String[] output = infixToRPN(input);

            // Build output RPN string minus the commas
            for (String token : output) {
                System.out.print(token + " ");
            }

            // Feed the RPN string to RPNtoDouble to give result
            System.out.println(output);
            rt = RPNtoDouble(output);
            System.out.println("result after :  " + rt);
        }

        return rt;
    }

    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') {
                    nextChar();
                }
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        x += parseTerm(); // addition
                    } else if (eat('-')) {
                        x -= parseTerm(); // subtraction
                    } else {
                        return x;
                    }
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) {
                        x *= parseFactor(); // multiplication
                    } else if (eat('/')) {
                        x /= parseFactor(); // division
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) {
                    return parseFactor(); // unary plus
                }
                if (eat('-')) {
                    return -parseFactor(); // unary minus
                }
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') {
                        nextChar();
                    }
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) {
                        x = Math.sqrt(x);
                    } else if (func.equals("sin")) {
                        x = Math.sin(Math.toRadians(x));
                    } else if (func.equals("cos")) {
                        x = Math.cos(Math.toRadians(x));
                    } else if (func.equals("tan")) {
                        x = Math.tan(Math.toRadians(x));
                    } else {
                        throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) {
                    x = Math.pow(x, parseFactor()); // exponentiation
                }
                return x;
            }
        }.parse();
    }

    public BigDecimal separeteDataCond(String formule, String etab, String fichier, String date) throws ParseException {
        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        formule = formule.replaceAll(" ", "");
        List<String> ret = new ArrayList<String>();
        int firstIndex = formule.indexOf('(');
        int lasttIndex = formule.indexOf(')');
        String bool = formule.substring(firstIndex + 1, lasttIndex);
        System.out.println(bool);
        Boolean val = false;
        if (bool.contains("<=")) {
            String[] str = bool.split("<=");
            int res;//-1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            System.out.println("the value of res is :" + res);
            val = (res <= 0);
        } else if (bool.contains(">=")) {
            String[] str = bool.split(">=");
            int res;
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            val = (res >= 0);
        } else if (bool.contains("<")) {
            String[] str = bool.split("<");
            int res;
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            val = (res < 0);
        } else if (bool.contains(">")) {
            String[] str = bool.split(">");
            int res;
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            val = (res > 0);
        } else if (bool.contains("!=")) {
            String[] str = bool.split("!=");
            int res;
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            val = (res != 0);
        } else {
            String[] str = bool.split("=");
            int res;
            res = separeteDataTraitv1(str[0], etab, fichier, date)
                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
            val = (res == 0);
        }
        String frm = formule.substring(lasttIndex + 1, formule.length());
        String[] strd = frm.split(";");
        System.out.println("tebit check here: " + strd[1]);
        System.out.println(strd[0]);
        if (val) {
            return separeteDataTraitv1(strd[0], etab, fichier, date);
        } else {
            System.out.println(separeteDataTraitv1(strd[1].substring(4), etab, fichier, date));

            return separeteDataTraitv1(strd[1].substring(4), etab, fichier, date);
        }

    }

    @Transactional
    public BigDecimal separeteDataTraitv1(String formule, String etab, String fichier, String date)
            throws ParseException {
        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        try {
            if (formule == null) {
                return new BigDecimal("0");
            }
            return new BigDecimal(formule.trim());
        } catch (NumberFormatException nfe) {
            if (formule.trim().isEmpty()) {
                return new BigDecimal("0");
            }
            formule = formule.replaceAll(" ", "");

            System.out.println("tebit formular:" + formule);
            String frm = formule.replaceAll("\\-", "+");
            frm = frm.replaceAll("\\/", "+");
            frm = frm.replaceAll("\\*", "+");
            String[] str = frm.split("\\+");
            formule = " " + formule.replaceAll("\\+", " + ").replaceAll("\\-", " - ") + " ";
            String val = "0";
            String val1 = "0";
            int i = 0;
            int j = 1;
            for (String s : str) {// each formular in the + operator

                sql = "select * from rpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichier='" + fichier + "' and field='" + s.trim() + "'";

                List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
                if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
                    System.out.println("this is s value (first if): " + s);
                    if (s.substring(0, 1).equalsIgnoreCase("M")) {
                        val = s.substring(1, s.length());
                    } else {
                        if (tmp.size() == 1) {

                            val = Double.valueOf(tmp.get(0).get("valm").toString()).longValue() + "";
                            val1 = Double.valueOf(tmp.get(0).get("rawres").toString()).longValue() + "";
                        } else {
                            val1 = "0";
                            val = "0";
                        }
                    }
                    formule = formule.replaceAll(" " + s.trim() + " ", val + "");
//				formule = formule.replaceAll(s, " " + val + " ");
                    i = 1;
                } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
                    if (tmp.size() == 1) {
                        val = Double.valueOf(tmp.get(0).get("valM").toString()).longValue() + "";
                        val1 = Double.valueOf(tmp.get(0).get("rawres").toString()).longValue() + "";
                    } else {;
                        val = "0";
                        val1 = "0";
                    }
                    formule = formule.replaceAll(" " + s.trim() + " ", " " + val + " ");

                    i = 1;
                }
                formule = formule.replaceAll("999999999999999", "0");
                System.out.println("value is :" + formule);
            }
            if (i == 1) {
                rt = new BigDecimal(eval(formule));
            }

            return rt;
        }
    }

    public List<String> separeteData(String formule) {
        formule = formule.trim();
        List<String> ret = new ArrayList<String>();
        System.out.println("this is the formule : " + formule);
        formule = formule.substring(2, formule.length());
        System.out.println("this is the formule : " + formule);
        System.out.println("this is the formule : " + formule);
        int lasttIndex = formule.lastIndexOf('C');
        String post = formule.substring(0, lasttIndex);
        System.out.println("this is the start index : " + (lasttIndex + 1));
        System.out.println("this is the length : " + formule.length());
        String colone = formule.substring(lasttIndex + 1, formule.length());
        System.out.println("this is the start index : " + post);
        System.out.println("this is the column : " + colone);
        ret.add(post);
        ret.add(colone);
        return ret;

    }

    @Transactional
    public List<Map<String, Object>> findByDup(String toString, String fichier) {
        String sql = "Select * from ARES.RPREP where to_date(dar,'yyyy-mm-dd') < to_date(?,'yyyy-mm-dd') "
                + "and to_date(dar,'yyyy-mm-dd') = (Select MAX(to_date(dar,'yyyy-mm-dd')) from ARES.RPREP) and fichier='"
                + fichier + "'";
        return jdbcTemplate.queryForList(sql, new Object[]{toString});
    }

    @Transactional
    public List<Map<String, Object>> findByDup1(String toString, String fichier) {
        String sql = "Select * from rprep where dar in (select max(dar) from rprep where fichier = '" + fichier
                + "' and col > 1) and fichier='" + fichier + "'";
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional
    public List<Map<String, Object>> findByDup1v1(String toString, String fichier, String dar) {
        String sql = "Select * from rprep where dar=to_date('" + dar + "','yyyy-mm-dd') and fichier='" + fichier + "'";
        String hasdata = "Select * from rprep where valc is not null and dar=to_date('" + toString + "','yyyy-mm-dd') and fichier='" + fichier + "'";
        List<Map<String, Object>> o0 = jdbcTemplate.queryForList(hasdata);
        if (o0.size() == 0) {
            String hasdatad = "delete from rprep where dar=to_date('" + toString + "','yyyy-mm-dd') and fichier='" + fichier + "'";
            jdbcTemplate.update(hasdatad);
            return jdbcTemplate.queryForList(sql);
        } else {
            return jdbcTemplate.queryForList("Select * from rprep where dar=to_date('" + toString + "','yyyy-mm-dd') and fichier='" + fichier + "'");
        }
    }

    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    // Compare precedence of operators.
    private static final int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalid tokens: " + token1 + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    // Convert infix expression format into reverse Polish notation
    public static String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        // For each token
        for (String token : inputTokens) {
            // If token is an operator
            if (isOperator(token)) {
                // While stack not empty AND stack top element
                // is an operator
                while (!stack.empty() && isOperator(stack.peek())) {
                    if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0)) {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack
                stack.push(token);
            } // If token is a left bracket '('
            else if (token.equals("(")) {
                stack.push(token); //
            } // If token is a right bracket ')'
            else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } // If token is a number
            else {
                out.add(token);
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static BigDecimal RPNtoDouble(String[] tokens) {
        Stack<String> stack = new Stack<String>();

        // For each token
        for (String token : tokens) {
            // If the token is a value push it onto the stack
            if (!isOperator(token)) {
                stack.push(token);
            } else {
                // Token is an operator: pop top two entries
                Double d2 = Double.valueOf(stack.pop());
                Double d1 = Double.valueOf(stack.pop());

                // Get the result
                Double result = token.compareTo("+") == 0 ? d1 + d2
                        : token.compareTo("-") == 0 ? d1 - d2 : token.compareTo("*") == 0 ? d1 * d2 : d1 / d2;

                // Push result onto stack
                stack.push(String.valueOf(result));
            }
        }
//        System.out.println(stack.pop());
        BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(stack.pop());
//        return Double.valueOf(stack.pop());
        return DEFAULT_FOOBAR_VALUE;
    }

    private class MonRunnable implements Runnable {

        String fichier;
        String etab;
        String date;
        String cuser;
        String idtrait;
        List<Map<String, Object>> FICPOST;
        String dar_cloture;
        Long minimum;

        public MonRunnable(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICPOST, String dar_cloture, Long minimum) {
            this.fichier = fichier;
            this.etab = etab;
            this.date = date;
            this.cuser = cuser;
            this.idtrait = idtrait;
            this.FICPOST = FICPOST;
            this.dar_cloture = dar_cloture;
            this.minimum = minimum;
        }

        @Override
        public void run() {
            postF100v3(FICPOST, dar_cloture, date, etab, fichier, cuser, idtrait, minimum);
        }

        private void postF100v3(List<Map<String, Object>> fpost1, String dar_cloture, String date, String etab, String fichier, String cuser, String idtrait, Long minimum) throws NumberFormatException {
            String globalpostqury = "";
            int re = 0;
            List<Map<String, Object>> post1 = findByPostv1();
            List<String> et = new ArrayList<>();

            for (Map<String, Object> fpost : fpost1) {
                String p = "";
                List<String> ret1 = new ArrayList<String>();
                String formule = fpost.get("CALC").toString();
                Double divd = Double.parseDouble(fpost.get("DIVD").toString());
                System.out.println("FormulePost: " + formule);
                String condition = null;
                Map<String, Object> pr = new HashMap<>();
                String result = "";
                if (formule.equals("")) {
                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
                    p = "";
                } else {
                    List<String> ret = new ArrayList<String>();
                    List<String> st = separeteDataPost(formule);
                    if (st.size() != 1) {
                        for (int f = 1; f < st.size(); f++) {
                            ret.add(st.get(f));
                        }
                        ret1 = ret;
                        pr = findByAttv1(ret);
                        condition = (String) pr.get("cmd");
                    }
                    p = st.get(0).toString();
                }
                List<Map<String, Object>> post = postCollect(post1, p);
                if (!post.isEmpty()) {
                    if (fpost.get("COL").toString().equalsIgnoreCase("2")) {
                        result = calcByBalSLDDv2(post, condition, dar_cloture);
                    } else if (fpost.get("COL").toString().equalsIgnoreCase("3")) {
                        result = calcByBalSLDCv2(post, condition, dar_cloture);
                    } else if (fpost.get("COL").toString().equalsIgnoreCase("4")) {
                        result = calcByBalCUMDv2(post, condition, date);
                    } else if (fpost.get("COL").toString().equalsIgnoreCase("5")) {
                        result = calcByBalCUMCv2(post, condition, date);
                    } else if (fpost.get("COL").toString().equalsIgnoreCase("6")) {
                        result = calcByBalSLDCVDv2(post, condition, date);
                    } else if (fpost.get("COL").toString().equalsIgnoreCase("7")) {
                        result = calcByBalSLDCVCv2(post, condition, date);
                    }
                } else {
                    result = "0";
                }
                globalpostqury += (re != 0 ? " union " : "") + "select '" + fpost.get("DIVD") + "' divd, '" + fpost.get("STATUS") + "' status,'" + fpost.get("TYPEVAL") + "'typeval,'" + fpost.get("FIELD") + "'field,'" + fpost.get("RANG") + "'rang,'" + fichier + "' fichi,'" + fpost.get("POST").toString() + "' post ,'" + fpost.get("COL").toString() + "' col, (" + result + ") result from dual";
                if (re == 50) {//50 queries
                    et.add(globalpostqury);
                    globalpostqury = "";
                    re = 0;
                } else {
                    re++;
                }
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minimum);
            }
            if (globalpostqury != "") {
                et.add(globalpostqury);
            }
            lotSavingCalcule(et, date, idtrait, fichier, 50L);
        }

        public Map<String, Object> calcByBalSLDCVCv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            System.out.println("requÃªte sql post: " + sql);
//            return jdbcTemplate.queryForMap(sql);
            Map<String, Object> E = jdbcTemplate.queryForMap(sql);
            System.out.println("requÃªte sql post: " + E);
            return E;
        }

        public Map<String, Object> calcByBalSLDCVDv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }

            System.out.println("requÃªte sql post: " + sql);
            Map<String, Object> E = jdbcTemplate.queryForMap(sql);
            System.out.println("requÃªte sql post: " + E);
            return E;
        }

        public String calcByBalSLDCVCv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
//            System.out.println("requÃªte sql post: " + sql);
//            return jdbcTemplate.queryForMap(sql);
//            Map<String, Object> E = jdbcTemplate.queryForMap(sql);
//            System.out.println("requÃªte sql post: " + E);
            return sql;
        }

        public String calcByBalSLDCVDv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }

            return sql;
        }

        public void saveCalcTmpv2(String etab, String post, String col, String solde, String fich, String dar, int rang,
                String cuser, int status, String field, String typeval) {
//            System.out.println(etab);
            int etabInt = Integer.parseInt(etab);
            String sql = "INSERT INTO rpreptmp (etab,post, col, VAL" + typeval
                    + ",fichier,dar,rang,cuser,muser,status,field) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    System.out.println("Solde to tmp: " + solde);
                    ps.setInt(1, etabInt);
                    ps.setString(2, post);
                    ps.setString(3, col);
                    if (typeval.equalsIgnoreCase("M")) {
                        System.out.println("Solde to before trans: " + solde);
                        System.out.println("Solde transformed: " + Double.parseDouble(solde));
                        ps.setDouble(4, Double.parseDouble(solde));
                    } else if (typeval.equalsIgnoreCase("D")) {
                        ps.setString(4, solde);
                    } else if (typeval.equalsIgnoreCase("C")) {
                        ps.setString(4, solde);
                    } else if (typeval.equalsIgnoreCase("T")) {
                        ps.setDouble(4, Double.parseDouble(solde));
                    }
                    ps.setString(5, fich);
                    ps.setDate(6, Date.valueOf(dar));
                    ps.setInt(7, rang);
                    ps.setString(8, cuser);
                    ps.setString(9, cuser);
                    ps.setInt(10, status);
                    ps.setString(11, field);
                }

                @Override
                public int getBatchSize() {
                    return 1;
                }
            };

            jdbcTemplate.batchUpdate(sql, batchArgs);
        }

        public Map<String, Object> calcByBalCUMCv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.chl27),0) as result from insld where insld.dar =to_date('"
                    //        String sql = "select coalesce(sum (insld.cumc),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.chl27),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            System.out.println("requÃªte sql post: " + sql);
            return jdbcTemplate.queryForMap(sql);
        }

        public Map<String, Object> calcByBalCUMDv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
//        String sql = "select  coalesce(sum (insld.cumd),0) as result from insld where insld.dar =to_date('"
            String sql = "select  coalesce(sum (insld.chl26),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
//            sql = "select coalesce(sum (insld.cumd),0) as result from insld where insld.dar =to_date('"
                sql = "select coalesce(sum (insld.chl26),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            System.out.println("requÃªte sql post: " + sql);
//            return jdbcTemplate.queryForMap(sql);
//            return jdbcTemplate.queryForMap(sql);
            Map<String, Object> E = jdbcTemplate.queryForMap(sql);
            System.out.println("requÃªte sql post: " + E);
            return E;
        }

        public Map<String, Object> calcByBalSLDDv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            return jdbcTemplate.queryForMap(sql);
        }

        public Map<String, Object> calcByBalSLDCv1(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            System.out.println(sql);
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            System.out.println("requÃªte sql post: " + sql);
            return jdbcTemplate.queryForMap(sql);
        }

        public String calcByBalCUMCv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.chl27),0) as result from insld where insld.dar =to_date('"
                    //        String sql = "select coalesce(sum (insld.cumc),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.chl27),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
//            System.out.println("requÃªte sql post: " + sql);
            return (sql);
        }

        public String calcByBalCUMDv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select  coalesce(sum (insld.chl26),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.chl26),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
//            System.out.println("requÃªte sql post: " + sql);
//            return jdbcTemplate.queryForMap(sql);
//            return jdbcTemplate.queryForMap(sql);
//            Map<String, Object> E = jdbcTemplate.queryForMap(sql);
//            System.out.println("requÃªte sql post: " + E);
            return sql;
        }

        public String calcByBalSLDDv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
            return (sql);
        }

        public String calcByBalSLDCv2(List<Map<String, Object>> post, String condition, String date) {
            String bt = "";
            for (int j = 0; j < post.size(); j++) {
                String rt = "";
                if (j != post.size() - 1) {
                    rt = " or ";
                }

                bt += "insld.chap between " + post.get(j).get("CHAR1") + " and " + post.get(j).get("CHAR2") + " " + rt;

            }
            String sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                    + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
            System.out.println(sql);
            if (condition == null) {
                sql = "select coalesce(sum (insld.sldcvd),0) as result from insld where insld.dar =to_date('"
                        + date + "','yyyy-mm-dd') and (" + bt + ")";
            }
//            System.out.println("requÃªte sql post: " + sql);
            return (sql);
        }

    }

    private void savePostF1000v2(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICPOST, String dar_cloture, Long mini) {
        ExecutorService service = Executors.newFixedThreadPool(3);//eg 15 dat and divid 4 
        if (FICPOST.size() > 0) {
            service.execute(new MonRunnable(fichier, etab, date, cuser, idtrait,
                    FICPOST,
                    dar_cloture, mini));
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("Yann service did not terminate");
                e.printStackTrace();
            }
        }
    }

    private void updateTemp(List<Map<String, Object>> FICSQL, String val, int t) {
        String sss = "UPDATE rpreptmp SET valm = " + val + ",status = 1,rawres ='" + val + "' where fichier = '"
                + FICSQL.get(t).get("FICH").toString() + "' " + "and post = '" + FICSQL.get(t).get("POSTE").toString()
                + "' " + "and col = " + FICSQL.get(t).get("COL").toString() + " " + "and rang = "
                + FICSQL.get(t).get("RANG").toString() + " ";
        jdbcTemplate.update(sss);
    }

    @Transactional
    public List<Map<String, Object>> findCalcBySql(String fichier) {
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='S' ORDER BY u.POST,u.COL asc";
        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
    }

    @Transactional
    public List<Map<String, Object>> findCalcByTraitement(String fichier) {
//		String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
//				+ "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY u.POST,u.COL asc";
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, (case when( a.rang is null )then -1 else a.rang end) as rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status "
                + "from rpcalc u LEFT JOIN rppfich a on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY  u.COL Asc,u.POST";
        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
    }

    public List<Map<String, Object>> findCalcByTraitement_F1139(String fichier, String ctry) {
//		String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
//				+ "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY u.POST,u.COL asc";
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, (case when( a.rang is null )then -1 else a.rang end) as rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status "
                + "from rpcalc u LEFT JOIN rppfich a on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' and a.source = '"
                + ctry + "' ORDER BY u.POST,u.COL asc";
        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
    }

    @Transactional
    public List<Map<String, Object>> findCalcByCondition(String fichier) {
        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.source,u.field, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='C' ORDER BY u.POST,u.COL asc";
        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
    }

    @Override
    public List<Long> dismantlePostCalculate1(String s) {
//		String[] n = new String[]{};
        ArrayList<Long> n = new ArrayList<Long>();
        s = s.trim().substring(5, s.trim().length() - 1);

        String[] split = s.split(",");
        List<ReportAttribute> f = null;
        for (String e : split) {
            f = reportAttributeRepository.findByDeleAndAttContaining(false, e);
            if (!f.isEmpty()) {
                n.add(f.get(0).getId());
            }
        }
        return n;
    }

    @Override
    public List<Long> dismantlePostCalculate2(String s) {
//		String[] n = new String[]{};
        ArrayList<Long> n = new ArrayList<Long>();
//		s = s.trim().substring(5, s.trim().length() - 1);
        s = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        System.out.print(s + " klllllllllllllllllllllllllllllllllllllllllllllll ");
        String[] split = s.split(",");
        List<ReportAttribute> f = null;
        for (String e : split) {
            f = reportAttributeRepository.findByDeleAndAttContaining(false, e.trim());
            if (!f.isEmpty()) {
                n.add(f.get(0).getId());
            }
        }
        return n;
    }

    @SuppressWarnings("unchecked")
    public List<ReportCalculate> constructfilterquery1(ReportCalculate filter) {

        return reportCalculateRepository.findAll(new Specification<ReportCalculate>() {

            @Override
            public Predicate toPredicate(Root<ReportCalculate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(root.get("fichi"), filter.getFichi()));

                // If attribute is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getField() != null) {
                    predicates.add(cb.like(cb.lower(root.get("field")), "%" + filter.getField().toLowerCase() + "%"));
                }

                // If lastName is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getPost() != null) {
                    predicates.add(cb.like(cb.lower(root.get("post")), "%" + filter.getPost().toLowerCase() + "%"));
                }
                if (filter.getTysorce() != null) {
                    predicates
                            .add(cb.like(cb.lower(root.get("tysorce")), "%" + filter.getTysorce().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }

        });
    }

    @SuppressWarnings("null")
    public Model get_cells(ReportRep s) {
        List<ReportCalculate> sd = reportCalculateRepository.findbyelm(s.getFichier(), s.getCol(), s.getPost());
        if (sd.size() > 1) {
            return null;
        }
        if (sd.get(0).getSource() == "T") {
            String p = sd.get(0).getCalc();
            String g = "CH";
            int i = p.indexOf(g);
            Model e = null, j = null;
            System.out.println("index:${i}" + i);
            while (i >= 0) {
//		     to collect Caracter
                int h = p.indexOf(" ", i + 1);
                // GET POST
                String post = p.substring(i + 2, h - 2);
                System.out.println("post:" + post);

                // get column
                String col = p.substring(h - 1, h);
                System.out.println("col:" + col);

                // file
                String f = sd.get(0).getFichi();
                // closing date
                System.out.println("file:" + f);

                java.util.Date d = s.getDar();
                System.out.println("Date: " + d.toString());

                List<ReportRep> p1 = reportRepRepository.findByFichierAndPostAndColAndDar(f, post, col, d);
                if (p1.size() != 1) {
                    return null;
                }
                // get cell value
                String r = null;
                if (p1.get(0).getValc() != null) {
                    r = p1.get(0).getValc();
                } else if (p1.get(0).getValm() != null) {
                    r = p1.get(0).getValm().toString();
                } else if (p1.get(0).getVald() != null) {
                    r = p1.get(0).getVald().toString();
                } else if (p1.get(0).getValt() != null) {
                    r = p1.get(0).getValt().toString();
                }
                // add informations
                e.addAttribute("post", post);
                e.addAttribute("col", col);
                e.addAttribute("val", r);
                j.addAttribute(i);
                i = p.indexOf(g, i + 1);
            }
            return j;

        } else {// if its not TYPEVAL T
            return null;
        }

    }

    public Map<String, Object> getFormularAndSubFields(String fichi, String post, String col, String date) {
        Map<String, Object> r = new HashMap<String, Object>();
        Map<String, Object> f = new HashMap<String, Object>();
        Map<String, Object> g = new HashMap<String, Object>();
        List<ReportCalculate> s = reportCalculateRepository.findByFichiAndPostAndCol(fichi, post, col);
        int i = 0;
        r.put("obj", s.get(0));
        // get formular if its sum type
        if (s.get(0).getSource().equals("T")) {
            String[] sf = s.get(0).getCalc().split(" ");
            System.out.println("checking return value");
            for (String yg : sf) {// cut at points of space
                if (yg.length() > 2 && yg.substring(0, 2).equals("CH")) {// select only those starting with CH
                    List<ReportRep> o = reportRepRepository.fld(fichi, yg, date);
                    if (!o.isEmpty()) {
                        g.put("post", o.get(0).getPost());
                        g.put("col", o.get(0).getCol());
                        g.put("vald", o.get(0).getVald());
                        g.put("valm", o.get(0).getValm());
                        g.put("valt", o.get(0).getValt());
                        g.put("valc", o.get(0).getValc());
                        f.put(yg, ((HashMap) g).clone());
                        i++;
                        g.clear();
                    } else {
                    }
                }
            }
        }
        r.put("obj1", f);
        return r;
    }
    @Autowired
    ReportAnomalyRepository reportAnomalyRepository;

    public Map<String, Object> getFormularAndSubFieldsControl(Long idOpe, String key, String date) {
        Map<String, Object> r = new HashMap<String, Object>();
        Map<String, Object> f = new HashMap<String, Object>();
        Map<String, Object> g = new HashMap<String, Object>();
        ReportAnomaly s = reportAnomalyRepository.findOne(idOpe);
        int i = 0;
        r.put("obj", s);
        System.out.println("checking return value");
        if (s.getType().equalsIgnoreCase("intra")) {
            String[] sf = s.getKey(key).split("[-+*/]");
            String fichi = s.getFichier();
            for (String yg : sf) {// cut at points of space
                yg = yg.trim();
                if (yg.length() > 2 && yg.substring(0, 2).equals("CH")) {// select only those starting with CH
                    System.out.println(fichi + ' ' + yg + ' ' + date);
                    List<ReportRep> o = reportRepRepository.fld(fichi, yg, date);
                    if (!o.isEmpty()) {
                        g.put("post", o.get(0).getPost());
                        g.put("col", o.get(0).getCol());
                        g.put("vald", o.get(0).getVald());
                        g.put("valm", o.get(0).getValm());
                        g.put("valt", o.get(0).getValt());
                        g.put("valc", o.get(0).getValc());
                        g.put("fichier", o.get(0).getFichier());
                        f.put(yg, ((HashMap) g).clone());
                        i++;
                        g.clear();
                    } else {
                    }
                }
            }
        } else if (s.getType().equalsIgnoreCase("inter")) {
            String[] sf = s.getKey(key).split("[-+*/]");
            String fichi = s.getFichier();
            for (String yg1 : sf) {// cut at points of space
                yg1 = yg1.trim();
                String[] y = yg1.split(":");
                String yg = y[1];
                if (yg.length() > 2 && yg.substring(0, 2).equals("CH")) {// select only those starting with CH
                    System.out.println(fichi + ' ' + yg + ' ' + date);
                    List<ReportRep> o = reportRepRepository.fld(y[0], yg, date);
                    if (!o.isEmpty()) {
                        g.put("post", o.get(0).getPost());
                        g.put("col", o.get(0).getCol());
                        g.put("vald", o.get(0).getVald());
                        g.put("valm", o.get(0).getValm());
                        g.put("valt", o.get(0).getValt());
                        g.put("valc", o.get(0).getValc());
                        g.put("fichier", o.get(0).getFichier());
                        f.put(yg, ((HashMap) g).clone());
                        i++;
                        g.clear();
                    } else {
                    }
                }
            }
        }
        r.put("obj1", f);
        return r;
    }

    public List<Object> getAttributeNotExist(Model re) {
//		Model re = null;
        re.addAttribute("test4");
        Map<String, Object> sr = null;

        Map<String, Object> sr2 = new HashMap<String, Object>();
        List<Object> sr1 = null;
        List<Object> sr3 = new ArrayList<Object>();
        List<String> sp = new ArrayList<String>();
        List<ReportCalculate> sd = reportCalculateRepository.findallPost();
        System.out.println("entring size");
        System.out.println(sd.size());

        for (int m = 0; m < sd.size(); m++) {
            if (sd.get(m).getCalc().contains("CREDNR")) {
                continue;
            }
            System.out.println("we are at formular number :" + m);
            System.out.println(sd.get(m).getCalc());
            String[] s = sd.get(m).getCalc().trim()
                    .substring(sd.get(m).getPost().trim().length() + 1, sd.get(m).getCalc().trim().indexOf(')'))
                    .split(",");
            for (int i = 0; i < s.length; i++) {
                System.out.println("we are at attribute : " + s[i]);
                System.out.println("we are at attribute number: " + i);
                List<ReportAttribute> ra = reportAttributeRepository.findByAttAndDele(s[i].trim(), false);
                if (ra.size() == 1 || ra.size() == 0) {// normal
                    if (ra.size() == 1) {// one
                        System.out.println(s[i] + "  is existing and is unique");
                    } else {// not existing
                        System.out.println(s[i] + "  is not existing");
                        if (!sp.contains(s[i])) {
                            System.out.println("first time detecting :" + s[i]);
                            sp.add(s[i]);
                            System.out.println("first time detecting :" + s[i]);
                            sr2.put("fichier", sd.get(m).getFichi());
                            sr2.put("post", sd.get(m).getPost());
                            sr2.put("col", sd.get(m).getCol());
                            sr2.put("formular", sd.get(m).getCalc());
                            sr2.put("att", s[i]);
                            sr3.add(((HashMap) sr2).clone());
                            sr2.clear();
                        }
                    }
                } else {// abnormal : duplicate attribute
                    System.out.println(s[i] + "  is more than one");
                    ra.forEach(t -> {
                        sr.put("att", t.getAtt());
                        sr.put("id", t.getId());
                        sr1.add(sr);
                        sr.clear();
                    });

                }
                System.out.println("attribute search complited : " + s[i]);
            }
            System.out.println("attribute finished");
            System.out.println("formular search complited : " + sd.get(m).getCalc());
        }
        System.out.println("formular search complitely finished");
//		re.addAttribute("notExiting", sp);
//		re.addAttribute("Duplicate", sr1);
//		re.addAttribute("detailNotExisting", sr3);
//		return re;
//		return sp;
        return sr3;
    }

}

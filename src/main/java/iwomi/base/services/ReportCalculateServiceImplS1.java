//package iwomi.base.services;
//
//import static iwomi.base.services.ChargerDonneesServiceImpl.CONN_STRING;
//import static iwomi.base.services.ChargerDonneesServiceImpl.PASSWORD;
//import static iwomi.base.services.ChargerDonneesServiceImpl.USERNAME;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Stack;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.json.JsonParser;
//import org.springframework.boot.json.JsonParserFactory;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//
//import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
//
//import iwomi.base.ServiceInterface.LiveReportingServiceS;
//import iwomi.base.ServiceInterface.ReportCalculateServiceS;
//import iwomi.base.objects.ReportAttributeS;
//import iwomi.base.objects.ReportCalculateS;
//import iwomi.base.objects.ReportRepSS;
//import iwomi.base.objects.ReportRepTmpS;
//import iwomi.base.objects.ReportResultTmpS;
//import iwomi.base.repositories.LiveOperationRepository;
//import iwomi.base.repositories.ReportAttributeRepositoryS;
////import iwomi.base.repositories.FichierSpectraRepository;
//import iwomi.base.repositories.ReportCalculateRepositoryS;
//import iwomi.base.repositories.ReportControleComplexRepositoryS;
//import iwomi.base.repositories.ReportControleInterRepositoryS;
//import iwomi.base.repositories.ReportControleIntraRepositoryS;
//import iwomi.base.repositories.ReportControleQualityRepositoryS;
//import iwomi.base.repositories.ReportFileRepositoryS;
//import iwomi.base.repositories.ReportRepRepositoryS;
//import iwomi.base.repositories.ReportRepTempRepositoryS;
//import iwomi.base.repositories.ReportResultRepositoryS;
//import iwomi.base.repositories.ReportResultTmpRepositoryS;
//import java.util.concurrent.ExecutorService;
//import org.springframework.stereotype.Service;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class ReportCalculateServiceImplS1 {
//
//    public ReportCalculateServiceImplS1() {
//    }
//    @Autowired
//    private ReportCalculateRepositoryS reportCalculateRepository;
//    @Autowired
//    private LiveOperationRepository liveOperationRepository;
//    @Autowired
//    private ReportRepRepositoryS reportRepRepository;
//    @Autowired
//    private ReportAttributeRepositoryS reportAttributeRepository;
//    @Autowired
//    private ReportRepTempRepositoryS reportRepTempRepository;
//    @Autowired
//    private ReportControleIntraRepositoryS reportControleIntraRepository;
//    @Autowired
//    private ReportControleInterRepositoryS reportControleInterRepository;
//    @Autowired
//    private ReportControleQualityRepositoryS reportControleQualityRepository;
//    @Autowired
//    private ReportControleComplexRepositoryS reportControleComplexRepository;
//    Map<String, Long> minim = new HashMap<String, Long>();
//    Connection connection, con = null;
//    Map<String, String> PARAM = new HashMap<>();
//    String NOMENGABTABLE_SYS = "0012";
//    String uniquecode = "";
//    private String[] ORACLE_CON_PARAM;
//    private static Map<String, Object> val = new HashMap<>();
//    Map<String, String> parameters = new HashMap<>();
//    @Autowired
//    private ReportCalculateRepositoryS ReportCalculateRepository;
//    @Autowired
//    private ReportFileRepositoryS ReportFileRepository;
//    @Autowired
//    private ReportResultRepositoryS ReportResultRepository;
//    @Autowired
//    private ReportResultTmpRepositoryS ReportResultTmpRepository;
//    Long idOpe, minimum;
//    private Statement connect;
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @Autowired
//    LiveReportingServiceS liveReportingService;
//    String idTrait, codeFichier;
//    Long quotien = Long.valueOf(1);
//    Long count = Long.valueOf(0);
//    Long statut = Long.valueOf(3);
//    Long statutope = Long.valueOf(3);
//    Long nombreOpeTraite = Long.valueOf(0);
//    private static final BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(0);
//    String duplicate = "1";
//    String sql = "2";
//    private String URL_ORACLE = "";
//    private String LOGIN_ORACLE = "";
//    private String PASSWORD_ORACLE = "";
//    // Associativity constants for operators
//    private static final int LEFT_ASSOC = 0;
//    private static final int RIGHT_ASSOC = 1;
//    // Operators
//    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
//
//    static {
//        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
//        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
//        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
//        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
//    }
//
//    // Test if token is an operator
//    private static boolean isOperator(String token) {
//        return OPERATORS.containsKey(token);
//    }
//
////    private ReportCalculateServiceImplS(List<String> fichier, int i, Map<String, Object> fich) {
////    ReportCalculateServiceImplS(List<String> fichier, int i, Map<String, Object> fich) {
////                    System.out.println("file i started");
////
//////        try {
//////            tsdsfsdfs(fichier, i, fich);
//////        } catch (ParseException ex) {
//////            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//////        } catch (ClassNotFoundException ex) {
//////            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//////        } catch (SQLException ex) {
//////            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//////        } catch (JSONException ex) {
//////            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//////        }
////    }
//    private void tsdsfsdfs(List<String> fichier, int i, Map<String, Object> fich) throws ParseException, ClassNotFoundException, SQLException, JSONException {
//        Long Total;
//        int Friqunce;
//        String efi = fichier.get(i);
//        String sse;
//        List<Map<String, Object>> FIC = findByCalc(efi);
//        Map<String, String> type = new HashMap<>();
//        type = getType1(efi);
//        Total = getTotal(efi, type);
//        Friqunce = (int) Math.ceil(Total / 100.0);
////        sse = liveReportingService.beginDetailsReportingToTheVue(idOpe, efi, Total);
//        System.out.println("the result element is :" + type.get("result"));
//        if (type.get("result").toString().equalsIgnoreCase("duplicate")) {
//            System.out.println("this is the date: " + fich.get("date").toString());
//            List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), efi);
//            int e = dup.size();
//            Friqunce = (int) Math.ceil(e / 100.0);
//            Total = new Long(e);
//            if (!minim.containsKey(efi)) {
//                minim.put(efi, new Long(Friqunce));
//            }
//            liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//            for (int t = 0; t < dup.size(); t++) {
//                System.out.println("Fichier type Duplicate");
//                saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                liveReportingService.detailsReportingToTheVue2(uniquecode, efi, minim.get(efi));
//            }
////            liveReportingService.endDetailsReportingToTheVue1(sse, efi, 1L, Total, Total);
//        } else if (type.get("result").toString().equalsIgnoreCase("duplicatepost")) {
//            System.out.println("this is the date: " + fich.get("date").toString());
//            List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), efi);
//            int e = dup.size();
//            Friqunce = (int) Math.ceil(e / 100.0);
//            Total = new Long(e);
//            if (!minim.containsKey(efi)) {
//                minim.put(efi, new Long(Friqunce));
//            }
//            liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//            for (int t = 0; t < dup.size(); t++) {
//                System.out.println("Fichier type Duplicate");
//                saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                liveReportingService.detailsReportingToTheVue2(uniquecode, efi, minim.get(efi));
//            }
////            liveReportingService.endDetailsReportingToTheVue1(sse, efi, 1L, Total, Total);
//        } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
//            liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, 2L);
//            System.out.println("the value of result:" + type.get("result"));
//            System.out.println("the value of sql:" + type.get("sql"));
//            String formule = type.get("sql").toString();
//            System.out.println("Fichier type SQL");
//            System.out.println("to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//            formule = formule.replaceAll("//dar//", "to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//            if (type.get("result").toString().equalsIgnoreCase("1")) {// link
//                int ret = findBySql3(formule, fich.get("date").toString(), efi,
//                        type.get("result").toString());
//                System.out.println("entered the first");
//            } else {// internal database
//                System.out.println("entered the second");
//                int ret = findBySql2v1(formule, fich.get("date").toString(), efi);
//            }
//        } else {
//            System.out.println("Fichier type calculation");
//            if (efi.equalsIgnoreCase("F1000")) {
//                savePostF1000_1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                System.out.println("entering for *** values ");
//                savePoint250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                savecol250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                liveReportingService.endDetailsReportingToTheVue1(uniquecode, efi, 1L, Total, Total);
//            } else if (efi.equalsIgnoreCase("F1139")) {
//                sse = uniquecode;//liveOperationRepository.findOne(idOpe).getCodeUnique();//
//                List<Map<String, Object>> FICPOST = findCalcByPost_F1139(efi, parameters.get("F1139"));
//                List<Map<String, Object>> FICSQL = findCalcBySql(efi);
//                List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
//                List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
//                List<Map<String, Object>> FICSQLr = findCalcByPoint1_F1139(efi, parameters.get("F1139"));// all *** cells
//                List<Map<String, Object>> FICSQLrr = findCalcByPoint2_F1139(efi, parameters.get("F1139"));// all *** cells
//                int e = FICPOST.size() + FICSQL.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQLr.size() + FICSQLrr.size();
//                Friqunce = (int) Math.ceil(e / 100.0);
//                Total = new Long(e);
//                if (!minim.containsKey(efi)) {
//                    minim.put(efi, new Long(Friqunce));
//                }
//                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                switch (parameters.get("F1139")) {
//                    case "CMR":
//                        savePost250202019_F1139v1(efi, fich.get("etab").toString(),
//                                fich.get("date").toString(), fich.get("cuser").toString(),
//                                fich.get("codeUnique").toString(), parameters.get("F1139"), FICPOST);
//                        saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL);
//                        saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT);
//                        saveTraitement250202019v1(efi, fich.get("etab").toString(),
//                                fich.get("date").toString(), fich.get("cuser").toString(),
//                                fich.get("codeUnique").toString(), FICTRAITEMENT1);
//                        savePoint250202019_F1139v1(efi, fich.get("etab").toString(),
//                                fich.get("date").toString(), fich.get("cuser").toString(),
//                                fich.get("codeUnique").toString(), parameters.get("F1139"), FICSQLr);
//                        savecol250202019_F1139v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                fich.get("cuser").toString(), fich.get("codeUnique").toString(),
//                                parameters.get("F1139"), FICSQLrr);
//                        break;
//                }
//
//            } else {
//
//                sse = uniquecode;//liveReportingService.beginDetailsReportingToTheVue(idOpe, efi, Total);
//                System.out.println("the value of sse:" + sse);
//                List<Map<String, Object>> FICPOST = findCalcByPost(efi);
//                List<Map<String, Object>> FICSQL = findCalcBySql(efi);
//                List<Map<String, Object>> FICSQL1 = findCalcByPoint1(efi);// all *** cells
//                List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
//                List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
//                List<Map<String, Object>> FICSQL2 = findCalcByPoint2(efi);// all *** cells
//                List<Map<String, Object>> FICSQL3 = findCalcByPoint1(efi);// all *** cells
//                int e = FICPOST.size() + FICSQL.size() + FICSQL1.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQL2.size() + FICSQL3.size();
//                Friqunce = (int) Math.ceil(e / 100.0);
//                Total = new Long(e);
//                if (!minim.containsKey(efi)) {
//                    minim.put(efi, new Long(Friqunce));
//                }
//                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                savePost250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICPOST);
//                saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL);
//                savePoint30032020v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL1);
//                saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT);
////                    saveIntermidiate250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
////                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                saveTraitement250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT1);
//                savecol250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL2);
//                savePoint250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                        fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL3);
//            }
//        }
//
//        saveCalcv1(fich.get("date").toString(), efi);
//        liveReportingService.endDetailsReportingToTheVue2(uniquecode, efi, 1L, Total, Total);
//
//    }
//
//    public List<ReportCalculateS> listAll() {
//        List<ReportCalculateS> reportCalculates = new ArrayList<>();
//        return reportCalculates;
//    }
//
//    public Map<String, Object> ReportGetCalculate(String json) {
//        JsonParser parser = JsonParserFactory.getJsonParser();
//        val = parser.parseMap(json);
//        return val;
//    }
//
//    public void CalculData(String json) {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    CalculDataTraitement(json);
//                } catch (SQLException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ParseException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        ;
//        });
//		t.start();
//    }
//
//    public Map<String, String> CalculData1(String json, Model m) {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    CalculDataTraitementv1(json);
////                    CalculDataTraitement(json);
//                } catch (SQLException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ParseException ex) {
//                    Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        ;
//        });
//
//		t.start();
//        return getAveragtimeFiles(json, m);
//    }
//
//    private void savecol250202019_F1139(String fichier, String etab, String date, String cuser, String idtrait,
//            String source) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint2_F1139(fichier, source);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    @SuppressWarnings("null")
//    public Map<String, String> getAveragtimeControle(String st, Model p) {
//        JsonParser parser = JsonParserFactory.getJsonParser();
//        Map<String, Object> fich = parser.parseMap(st);
//        List<String> fichier = (List<String>) fich.get("controle");
//        Map<String, String> sp = new HashMap<String, String>();
//        Double s = 0.0;
////		System.out.println("files to be treated");
////		System.out.println(fichier);
//        try {
//            final double dd = Double.parseDouble(getTimes());
////			System.out.println("dd value");
////			System.out.println(dd);
//
//            fichier.forEach(e -> {
//                // time for a treatment
////				System.out.println("type getting "+e);
//                long startTime = System.nanoTime();
//                double ys = 0L;// ReportFileRepository.countByFich(e).doubleValue();
//                switch (e) {
//                    case "inter":
////					System.out.println("count inter "+reportControleIntraRepository.countInter().get(0));
//                        ys = reportControleIntraRepository.countInter().get(0).doubleValue();
//                        break;
//
//                    case "intra":
//                        ys = reportControleInterRepository.countIntra().get(0).doubleValue();
//                        break;
//                    case "QLITE":
//                        ys = reportControleQualityRepository.countQuality().get(0).doubleValue();
//                        break;
//                    case "CMPX":
//                        ys = reportControleComplexRepository.countComplexe().get(0).doubleValue();
//                        break;
//                }
////				System.out.println("value is "+ys);
//                long endTime = System.nanoTime();
//                // get difference of two nanoTime values
//                long timeElapsed = endTime - startTime;
//                Double ss = ys * dd * timeElapsed * 0.000000152886;
////				System.out.println("the grobal time calculated");
////				System.out.println(ss);
////				sp.put(e, millisecond	stotime(Long.parseLong(ss.toString())));
//                sp.put(e, millisecondstotime(ss.longValue()));
//            });
//        } catch (NumberFormatException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (ClassNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (JSONException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (SQLException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        System.out.println("---------------------------------------------------");
//        System.out.println(sp);
//        return sp;
//    }
//
//    @SuppressWarnings("null")
//    public Map<String, String> getAveragtimeFiles(String st, Model p) {
//        JsonParser parser = JsonParserFactory.getJsonParser();
//        Map<String, Object> fich = parser.parseMap(st);
//        List<String> fichier = (List<String>) fich.get("fichier");
//        Map<String, String> sp = new HashMap<String, String>();
//        Double s = 0.0;
//        System.out.println("files to be treated");
//        System.out.println(fichier);
//        try {
//            final double dd = Double.parseDouble(getTimes());
//            System.out.println("dd value");
//            System.out.println(dd);
//            fichier.forEach(e -> {
//                // time for a treatment
//                long startTime = System.nanoTime();
//                double ys = ReportFileRepository.countByFich(e).doubleValue();
//                long endTime = System.nanoTime();
//
//                // get difference of two nanoTime values
//                long timeElapsed = endTime - startTime;
//                Double ss = ys * dd * timeElapsed * 0.000000152886;
//                System.out.println(ss);
////				sp.put(e, millisecondstotime(Long.parseLong(ss.toString())));
//                sp.put(e, millisecondstotime(ss.longValue()));
//            });
//        } catch (NumberFormatException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (ClassNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (JSONException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (SQLException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        System.out.println(sp);
//        return sp;
//    }
//
//    public String millisecondstotime(Long diff) {
//        String valtime = "";
//        try {
//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);
//            valtime = diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
//                    + " seconds.";
//            valtime = diffHours + " Hrs, " + diffMinutes + " Min, " + diffSeconds + " Sec";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return valtime;
//    }
//
//    public String getTimes() throws ClassNotFoundException, JSONException, SQLException {
//        return getGenerationAndSavingParam().get("AvergeTimeCoef");
//
//    }
//
//    public void CalculDataTraitement(String json) throws SQLException, ClassNotFoundException, ParseException {
//        Map<String, Object> fich = ReportGetCalculate(json);
//        parameters = getGenerationAndSavingParam();
//
//        List<String> fichier = (List<String>) fich.get("fichier");
////            parameters = getGenerationAndSavingParam();
////            idOpe = liveReportingService.beginGobalReportingToTheVue(fich.get("CodeUnique").toString(),fich.get("cetab").toString(), fich.get("usid").toString(),fich.get("Operation").toString(),Long.valueOf(fichier.size()));       
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        System.out.println("Start traitement");
//        String sql1 = "delete from srpreptmp where dar =to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')";
//        jdbcTemplate.execute(sql1);
//
//        idOpe = liveReportingService.beginGobalReportingToTheVue(fich.get("codeUnique").toString(),
//                fich.get("etab").toString(), fich.get("cuser").toString(), fich.get("operation").toString(),
//                Long.valueOf(fichier.size()));
//        Long Total = 0L;
//        String sse = "";
//        for (int i = 0; i < fichier.size(); i++) {
//            List<Map<String, Object>> FIC = findByCalc(fichier.get(i));
//            Map<String, String> type = getType1(fichier.get(i));
//            Total = getTotal(fichier.get(i), type);
//            sse = liveReportingService.beginDetailsReportingToTheVue1(idOpe, fichier.get(i), Total);
//            if (type.get("result").toString().equalsIgnoreCase("duplicate")) {
//                System.out.println("this is the date: " + fich.get("date").toString());
//                List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), fichier.get(i));
//                for (int t = 0; t < dup.size(); t++) {
//                    System.out.println("Fichier type Duplicate");
//                    saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                }
//                liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//            } else if (type.get("result").toString().equalsIgnoreCase("duplicatepost")) {
//                List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), fichier.get(i));
//                for (int t = 0; t < dup.size(); t++) {
//                    System.out.println("Fichier type Duplicate");
//                    saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                }
//                liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//            } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
//                String formule = type.get("sql").toString();
//                System.out.println("Fichier type SQL");
//                System.out.println("to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//                formule = formule.replaceAll("//dar//", "to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//                if (type.get("result").toString().equalsIgnoreCase("1")) {// link
//                    int ret = findBySql3(formule, fich.get("date").toString(), fichier.get(i),
//                            type.get("result").toString());
//                } else {// internal database
//                    int ret = findBySql2(formule, fich.get("date").toString(), fichier.get(i));
//                }
//                // to do sql to their system
//
//                liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//            } else {
//                System.out.println("Fichier type calculation 02032021");
//                if (fichier.get(i).equalsIgnoreCase("F1000")) {
//                    savePostF1000_1(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    System.out.println("entering for *** values ");
//                    savePoint250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    savecol250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//                } else if (fichier.get(i).equalsIgnoreCase("F1139")) {
//                    switch (parameters.get("F1139")) {
//                        case "CMR":
//                            savePost250202019_F1139(fichier.get(i), fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString(), parameters.get("F1139"));
//                            saveSql250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                            saveCondition250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                            saveTraitement250202019(fichier.get(i), fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString());
//                            savePoint250202019_F1139(fichier.get(i), fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString(), parameters.get("F1139"));
//                            savecol250202019_F1139(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(),
//                                    parameters.get("F1139"));
//                            System.out.println("total in file : " + Total);
//                            liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//                            break;
//                    }
//
//                } else {
//                    savePost250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    saveSql250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    savePoint30032020(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    saveCondition250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
////                    saveIntermidiate250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
////                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    saveTraitement250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    savecol250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    savePoint250202019(fichier.get(i), fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    System.out.println("total in file : " + Total);
//                    liveReportingService.endDetailsReportingToTheVue1(sse, fichier.get(i), 1L, Total, Total);
//                }
//            }
//            saveCalcv1(fich.get("date").toString(), fichier.get(i));
//        }
////        saveCalc(fich.get("date").toString(), fichier);
//        statutope = Long.valueOf(1);
//        liveReportingService.endGobalReportingToTheVue1(idOpe, statutope);
//    }
//
//    public class Treatement implements Runnable {
//
//        List<String> fichier;
//        int i;
//        Map<String, Object> fich;
//
//        public Treatement(List<String> fichier, int i, Map<String, Object> fich) {
//            this.fichier = fichier;
//            this.i = i;
//            this.fich = fich;
//        }
//
//        @Override
//        public void run() {
//            try {
//                tsdsfsdfs(fichier, i, fich);
//            } catch (ParseException ex) {
//                Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SQLException ex) {
//                Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (JSONException ex) {
//                Logger.getLogger(ReportCalculateServiceImplS1.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    public void CalculDataTraitementv1(String json) throws SQLException, ClassNotFoundException, ParseException {
//        Map<String, Object> fich = ReportGetCalculate(json);
//        parameters = getGenerationAndSavingParam();
//        ExecutorService service = Executors.newCachedThreadPool();
//        List<String> fichier = (List<String>) fich.get("fichier");
////            parameters = getGenerationAndSavingParam();
////            idOpe = liveReportingService.beginGobalReportingToTheVue(fich.get("CodeUnique").toString(),fich.get("cetab").toString(), fich.get("usid").toString(),fich.get("Operation").toString(),Long.valueOf(fichier.size()));       
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        System.out.println("Start traitement");
//        String sql1 = "delete from srpreptmp where dar =to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')";
//        jdbcTemplate.execute(sql1);
//
//        idOpe = liveReportingService.beginGobalReportingToTheVue(fich.get("codeUnique").toString(),
//                fich.get("etab").toString(), fich.get("cuser").toString(), fich.get("operation").toString(),
//                Long.valueOf(fichier.size()));
//        uniquecode = fich.get("codeUnique").toString();
//        Long Total = 0L;
//        String sse = "";
//
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                // we do the treatment
//                do {
//                    //method to call or code fragment
//                    /**
//                     * ********
//                     */
//                    try {
//
//                        for (int i = 0; i < fichier.size(); i++) {
//                            service.execute(new Treatement(fichier, i, fich));
//                        }
//
//                        // shutdown
//                        // this will get blocked until all task finish
//                        service.shutdown();
//                        try {
//                            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//
//                        } catch (InterruptedException e) {
//                            System.out.println("Yann service did not terminate");
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        System.out.println("An error occured during the treatment " + e.getLocalizedMessage());
//                    }
//                    System.out.println("Setting the status");
//                    statutope = Long.valueOf(1);
//                    liveReportingService.endGobalReportingToTheVue1(idOpe, statutope);
//                } while (!service.isShutdown());
//
//            }
//        ;
//        });
//        t.start();
//
//    }
//
//    private Long getTotal(String fichier, Map<String, String> type) {
//        if (type.get("result").toString().equalsIgnoreCase(duplicate)) {
//            String sql2 = "select count(id) as p from srppfich where fich = '" + fichier + "' ";
//            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
//            return Long.valueOf(result.get(0).get("p").toString());
//        } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
//            return 1L;
//        } else {
//            String sql2 = "select count(id) as p from srpcalc where fichi = '" + fichier + "' ";
//            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
//            return Long.valueOf(result.get(0).get("p").toString());
//        }
//
//    }
//
//    public Map<String, Object> findByFich(String fic) {
//        String sql = "Select u.calc,u.fichi,u.col,u.post.u.typeval,u.divd, a.rang from srppcalc.u,srppfich.a "
//                + "where u.fich = ?";
//        return jdbcTemplate.queryForMap(sql, new Object[]{fic});
//    }
//
//    public String findByAtt(List<String> att) {
//        String values = "";
//        String value = "'" + att.get(0) + "'";
//        for (int i = 1; i < att.size(); i++) {
//            values += " or att='" + att.get(i) + "'";
//        }
//        System.out.println(values);
//        values = value + values;
//        String sql = "Select val from srpatt " + "where att=" + values;
//        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
//        String cmd = "";
//        for (int j = 0; j < result.size(); j++) {
//            System.out.println(result.get(j));
//            if (result.get(j).get("VAL") != null) {
//                if (result.get(j).get("VAL").toString().isEmpty()) {
//                    cmd += " and 1=0";
//                } else {
//                    cmd += " and " + result.get(j).get("VAL").toString();
//                }
//            }
//        }
//        return cmd;
//    }
//
//    public Map<String, Object> calcBySold(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "insld.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
//                + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        System.out.println("-------------------------------------------------------------- modifierd");
//        System.out.println(sql);
//        if (condition == null) {
//            sql = "select coalesce(sum(insld.sldcvd),0) as result from insld where insld.dar =to_date('" + date
//                    + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalCUMC(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum (REPLACE(inbal.cumc,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.cumc,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalCUMD(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select  coalesce(sum (REPLACE(inbal.cumd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.cumd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalSLDD(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum (REPLACE(inbal.sldd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.sldd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalSLDC(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum (REPLACE(inbal.sldf,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        System.out.println(sql);
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.sldf,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalSLDCVD(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum (REPLACE(inbal.sldcvd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.sldcvd,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public Map<String, Object> calcByBalSLDCVC(List<Map<String, Object>> post, String condition, String date) {
//        String bt = "";
//        for (int j = 0; j < post.size(); j++) {
//            String rt = "";
//            if (j != post.size() - 1) {
//                rt = " or ";
//            }
//
//            bt += "inbal.chap between '" + post.get(j).get("CHAR1") + "' and '" + post.get(j).get("CHAR2") + "' " + rt;
//
//        }
//        String sql = "select coalesce(sum (REPLACE(inbal.sldcvf,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                + date + "','yyyy-mm-dd') and (" + bt + ")" + condition;
//        if (condition == null) {
//            sql = "select coalesce(sum (REPLACE(inbal.sldcvf,',' , '.')),0) as result from sinbal where inbal.dar =to_date('"
//                    + date + "','yyyy-mm-dd') and (" + bt + ")";
//        }
//        System.out.println("requÃªte sql post: " + sql);
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public List<Map<String, Object>> findByPost(String post) {
//        String sql = "Select char1,char2 from srppost where codep = ? and dele = 0";
//        return jdbcTemplate.queryForList(sql, new Object[]{post});
//    }
//
//    public List<Map<String, Object>> findCalcByPoint(String fichier) {
//        String sql = "((select col,fich,poste,rang from srppfich WHERE fich ='" + fichier
//                + "' ) MINUS (select col,fich,poste , rang from srppfich a where fich ='" + fichier
//                + "' and EXISTS(select * from srpcalc u where u.fichi = a.fich  and u.post =a.poste  and u.col = a.col and u.fichi ='"
//                + fichier + "')))";
//
//        System.out.println(sql);
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public List<Map<String, Object>> findCalcByPoint1(String fichier) {
//        String sql = "select col,fich,poste,rang,CONCAT('CH',CONCAT(poste,CONCAT('C',col))) field from srppfich where fich = '"
//                + fichier + "' and gen ='***'";
//        System.out.println(sql);
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public List<Map<String, Object>> findCalcByPoint1_F1139(String fichier, String source) {
//        String sql = "select col,fich,poste,rang,CONCAT('CH',CONCAT(poste,CONCAT('C',col))) field from srppfich where fich = '"
//                + fichier + "' and gen ='***' and source  = '" + source + "'";
//        System.out.println(sql);
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public List<Map<String, Object>> findCalcByPoint2(String fichier) {
//        String sql = "select rang,poste from srppfich where fich = '" + fichier + "' group by poste,rang";
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public List<Map<String, Object>> findCalcByPoint2_F1139(String fichier, String source) {
//        String sql = "select rang,poste from srppfich where fich = '" + fichier + "' and source = '" + source
//                + "'  group by poste,rang";
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public List<Map<String, Object>> findByCalc(String fic) {
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd, a.rang ,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =?";
//        return jdbcTemplate.queryForList(sql, new Object[]{fic});
//    }
//
//    public List<Map<String, Object>> findCalcByPost(String fic) {
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.field ,u.source ,(case when a.rang is not NULL then a.rang else 0 end) rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status,u.tysorce from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='P' ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fic});
//    }
//
//    public List<Map<String, Object>> findCalcByPost_F1139(String fic, String s) {
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.field ,u.source ,(case when a.rang is not NULL then a.rang else 0 end) rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='P' and a.source = '"
//                + s + "'ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fic});
//    }
//
//    public List<String> separeteDataPost1(String formule) {
//        formule = formule.replaceAll(" ", "");
//        List<String> ret = new ArrayList<String>();
//        int firstIndex = formule.indexOf('(');
//        int lasttIndex = formule.indexOf(')');
//        String post = formule.substring(0, firstIndex);
//        String at = "";
//        System.out.println("le post:" + post);
//        ret.add(post);
//        if (lasttIndex != firstIndex + 1) {
//            int count = StringUtils.countOccurrencesOf(formule, ",");
//            int att = formule.indexOf(',');
//            System.out.println(count);
//            if (count > 0) {
//                for (int i = 0; i <= count; i++) {
//                    at = formule.substring(firstIndex + 1, att);
//                    firstIndex = att;
//                    if (i < count - 1) {
//                        att = formule.indexOf(',', att + 1);
//                    } else {
//                        att = lasttIndex;
//                    }
//                    ret.add(at);
//                }
//
//            } else {
//                at = formule.substring(firstIndex + 1, lasttIndex);
//                System.out.println(at);
//                ret.add(at);
//            }
//        }
//
//        return ret;
//
//    }
//
//    public List<String> separeteDataPost(String formule) {
//        formule = formule.replaceAll(" ", "");
//        List<String> ret = new ArrayList<String>();
//        int firstIndex = formule.indexOf('(');
//        int lasttIndex = formule.indexOf(')');
//        String post = formule.substring(0, firstIndex);
//        String at = "";
//        System.out.println("le post:" + post);
//        ret.add(post);
//        if (lasttIndex != firstIndex + 1) {
//            int count = StringUtils.countOccurrencesOf(formule, ",");
//            int att = formule.indexOf(',');
//            System.out.println(count);
//            if (count > 0) {
//                for (int i = 0; i <= count; i++) {
//                    at = formule.substring(firstIndex + 1, att);
//                    firstIndex = att;
//                    if (i < count - 1) {
//                        att = formule.indexOf(',', att + 1);
//                    } else {
//                        att = lasttIndex;
//                    }
//                    ret.add(at);
//                }
//
//            } else {
//                at = formule.substring(firstIndex + 1, lasttIndex);
//                System.out.println(at);
//                ret.add(at);
//            }
//        }
//
//        return ret;
//
//    }
//
//    public void saveCalcTmp(String etab, String post, String col, String solde, String fich, String dar, int rang,
//            int d, String cuser, int status, String field, String typeval) {
//        System.out.println(etab);
//        int etabInt = Integer.parseInt(etab);
//        String sql = "INSERT INTO srpreptmp (etab,post, col, VAL" + typeval
//                + ",fichier,dar,rang,cuser,muser,status,field) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                System.out.println("Solde to tmp: " + solde);
//                ps.setInt(1, etabInt);
//                ps.setString(2, post);
//                ps.setString(3, col);
//                if (typeval.equalsIgnoreCase("M")) {
//                    System.out.println("Solde to before trans: " + solde);
//                    System.out.println("Solde transformed: " + Double.parseDouble(solde));
//                    ps.setDouble(4, Double.parseDouble(solde));
//                } else if (typeval.equalsIgnoreCase("D")) {
//                    ps.setString(4, solde);
//                } else if (typeval.equalsIgnoreCase("C")) {
//                    ps.setString(4, solde);
//                } else if (typeval.equalsIgnoreCase("T")) {
//                    ps.setDouble(4, Double.parseDouble(solde));
//                }
//                ps.setString(5, fich);
//                ps.setDate(6, Date.valueOf(dar));
//                ps.setInt(7, rang);
//                ps.setString(8, cuser);
//                ps.setString(9, cuser);
//                ps.setInt(10, status);
//                ps.setString(11, field);
//            }
//
//            @Override
//            public int getBatchSize() {
//                return 1;
//            }
//        };
//
//        jdbcTemplate.batchUpdate(sql, batchArgs);
//    }
//
//    public void saveCalc1(String dar, List<String> fichier) throws ParseException {
////        ReportResultRepository.deleteByDar(dar);
//        System.out.println("Date :" + dar);
//        for (int r = 0; r < fichier.size(); r++) {
//            String sql = "delete from srprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
//                    + fichier.get(r) + "'";
//            jdbcTemplate.execute(sql);
//            System.out.println("Sql :" + sql);
//        }
//
////        List<ReportResultTmp> tmp = ReportResultTmpRepository.findByDar(dar);
////        sql = "select * from rpreptmp where dar =?";
////        List<Map<String, Object>> tmp =jdbcTemplate.queryForList(sql, new Object[] { dar });
////        
////        sql = "insert into rprep select * from rpreptmp where dar =to_date('"+dar+"','dd-mm-yyyy')";
//        sql = "insert into rprep (\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
//                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select ((CASE WHEN(SELECT \"MAX\"(\"ID\") FROM RPREP) IS NULL THEN 1 ELSE (SELECT \"MAX\"(\"ID\") FROM RPREP) end)+ROWNUM)\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
//                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from srpreptmp where dar =to_date('" + dar
//                + "','yyyy-mm-dd')";
//        jdbcTemplate.execute(sql);
////        for (ReportResultTmp s : tmp) {
////        for (int t = 0; t<tmp.size(); t++){
//////            ReportResult save = new ReportResult(s.getDar(),s.getEtab(),s.getFeuille(),s.getFeild(),s.getPost(),s.getDoc(),
//////                    s.getCol(),s.getCuser(),s.getMuser(),s.getVal(),s.getRang(),s.getFichier());
////            ReportResult save = new ReportResult(new SimpleDateFormat("dd-MM-yyyy").parse(tmp.get(t).get("dar").toString()),tmp.get(t).get("etab").toString(),isNullOrEmpty(tmp.get(t).get("feuille")),isNullOrEmpty(tmp.get(t).get("feild")),isNullOrEmpty(tmp.get(t).get("post")),isNullOrEmpty(tmp.get(t).get("doc")),
////                    isNullOrEmpty(tmp.get(t).get("col")),isNullOrEmpty(tmp.get(t).get("cuser")),isNullOrEmpty(tmp.get(t).get("muser")),isNullOrEmpty(tmp.get(t).get("val")),Long.parseLong(tmp.get(t).get("rang").toString()),isNullOrEmpty(tmp.get(t).get("fichier")),Integer.parseInt(tmp.get(t).get("status").toString()));
////            ReportResultRepository.save(save);
////        }
////        ReportResultTmpRepository.deleteByDar(dar);
//        String sql1 = "delete from srpreptmp where dar =to_date('" + dar + "','yyyy-mm-dd')";
//        jdbcTemplate.execute(sql1);
////        String sql = "Select* from rpreptmp where dar =?";
////        List<Map<String, Object>> findByCalc = jdbcTemplate.queryForList(sql, new Object[] { dar });
////        String sql = "INSERT INTO rpreptmp (etab,post, col, val,fichier,dar,rang,id) VALUES (?,?,?,?,?,?,?,?)";
////        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
////            @Override
////            public void setValues(PreparedStatement ps, int i) throws SQLException {
////                ps.setInt(1, etabInt);
////                ps.setString(2, post);
////                ps.setString(3, col);
////                ps.setString(4, solde);
////                ps.setString(5, fich);
////                ps.setString(6, dar);
////                ps.setInt(7, rang);
////                ps.setInt(8, d);
////            }
////
////                @Override
////                public int getBatchSize() {
////                        return 1;
////                }
////        };
////
////        jdbcTemplate.batchUpdate(sql, batchArgs);
//    }
//
//    public void saveCalc(String dar, List<String> fichier) throws ParseException {
////        ReportResultRepository.deleteByDar(dar);
//        System.out.println("Date :" + dar);
//        for (int r = 0; r < fichier.size(); r++) {
//            String sql = "delete from srprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
//                    + fichier.get(r) + "'";
//            jdbcTemplate.execute(sql);
//            System.out.println("Sql :" + sql);
//        }
//
////        List<ReportResultTmp> tmp = ReportResultTmpRepository.findByDar(dar);
////        sql = "select * from rpreptmp where dar =?";
////        List<Map<String, Object>> tmp =jdbcTemplate.queryForList(sql, new Object[] { dar });
////        
////        sql = "insert into rprep select * from rpreptmp where dar =to_date('"+dar+"','dd-mm-yyyy')";
//        sql = "insert into srprep (\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
//                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select ((CASE WHEN(SELECT \"MAX\"(\"ID\") FROM sRPREP) IS NULL THEN 1 ELSE (SELECT \"MAX\"(\"ID\") FROM sRPREP) end)+ROWNUM)\"ID\",col,CRDT,CUSER,\"DATE\",DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
//                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from srpreptmp where dar =to_date('" + dar
//                + "','yyyy-mm-dd')";
//        jdbcTemplate.execute(sql);
////        for (ReportResultTmp s : tmp) {
////        for (int t = 0; t<tmp.size(); t++){
//////            ReportResult save = new ReportResult(s.getDar(),s.getEtab(),s.getFeuille(),s.getFeild(),s.getPost(),s.getDoc(),
//////                    s.getCol(),s.getCuser(),s.getMuser(),s.getVal(),s.getRang(),s.getFichier());
////            ReportResult save = new ReportResult(new SimpleDateFormat("dd-MM-yyyy").parse(tmp.get(t).get("dar").toString()),tmp.get(t).get("etab").toString(),isNullOrEmpty(tmp.get(t).get("feuille")),isNullOrEmpty(tmp.get(t).get("feild")),isNullOrEmpty(tmp.get(t).get("post")),isNullOrEmpty(tmp.get(t).get("doc")),
////                    isNullOrEmpty(tmp.get(t).get("col")),isNullOrEmpty(tmp.get(t).get("cuser")),isNullOrEmpty(tmp.get(t).get("muser")),isNullOrEmpty(tmp.get(t).get("val")),Long.parseLong(tmp.get(t).get("rang").toString()),isNullOrEmpty(tmp.get(t).get("fichier")),Integer.parseInt(tmp.get(t).get("status").toString()));
////            ReportResultRepository.save(save);
////        }
////        ReportResultTmpRepository.deleteByDar(dar);
////		String sql1 = "delete from rpreptmp where dar =to_date('" + dar + "','yyyy-mm-dd')";
////		jdbcTemplate.execute(sql1);
////        String sql = "Select* from rpreptmp where dar =?";
////        List<Map<String, Object>> findByCalc = jdbcTemplate.queryForList(sql, new Object[] { dar });
////        String sql = "INSERT INTO rpreptmp (etab,post, col, val,fichier,dar,rang,id) VALUES (?,?,?,?,?,?,?,?)";
////        BatchPreparedStatementSetter batchArgs = new BatchPreparedStatementSetter() {
////            @Override
////            public void setValues(PreparedStatement ps, int i) throws SQLException {
////                ps.setInt(1, etabInt);
////                ps.setString(2, post);
////                ps.setString(3, col);
////                ps.setString(4, solde);
////                ps.setString(5, fich);
////                ps.setString(6, dar);
////                ps.setInt(7, rang);
////                ps.setInt(8, d);
////            }
////
////                @Override
////                public int getBatchSize() {
////                        return 1;
////                }
////        };
////
////        jdbcTemplate.batchUpdate(sql, batchArgs);
//    }
////
//
//    public void saveCalcv1(String dar, String fichier) throws ParseException {
////        ReportResultRepository.deleteByDar(dar);
//        System.out.println("Date :" + dar);
////        for (int r = 0; r < fichier.size(); r++) {
//        String sql = "delete from srprep where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
//                + fichier + "'";
//        jdbcTemplate.execute(sql);
//        System.out.println("Sql :" + sql);
////        }
//        sql = "insert into srprep (\"ID\",col,CRDT,CUSER,DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR\n"
//                + ",\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD) select ((CASE WHEN(SELECT \"MAX\"(\"ID\") FROM sRPREP) IS NULL THEN 1 ELSE (SELECT \"MAX\"(\"ID\") FROM sRPREP) end)+ROWNUM)\"ID\",col,CRDT,CUSER,DELE,DOC,ETAB,FEUILLE,FICHIER,FIELD,MDFI,MUSER,POST,VALM,CRTD,DAR,"
//                + "\"FILTER\",SENS,RANG,STATUS,VALC,VALD,VALT,FEILD from srpreptmp where dar =to_date('" + dar
//                + "','yyyy-mm-dd') AND FICHIER = '" + fichier + "'";
//        jdbcTemplate.execute(sql);
//        String sql1 = "delete from srpreptmp where dar =to_date('" + dar + "','yyyy-mm-dd') and fichier='"
//                + fichier + "'";
//        jdbcTemplate.execute(sql1);
//        System.out.println("Sql :" + sql1);
//    }
////saveCalcv1
//
//    public String isNullOrEmpty(Object str) {
//        if (str != null && !str.toString().isEmpty()) {
//            return str.toString();
//        }
//        return null;
//    }
//
//    public void saveCalcTmp(String etab, Map<String, Object> obj, String dar, String cuser) throws ParseException {
//        for (int t = 0; t < obj.size(); t++) {
//            ReportResultTmpS save = new ReportResultTmpS(new SimpleDateFormat("dd-MM-yyyy").parse(dar), etab,
//                    obj.get("feuille").toString(), obj.get("feild").toString(), obj.get("post").toString(),
//                    obj.get("doc").toString(), obj.get("col").toString(), cuser, cuser,
//                    Double.parseDouble(obj.get("val").toString()), Long.parseLong(obj.get("rang").toString()),
//                    obj.get("fichier").toString(), Integer.parseInt(obj.get("status").toString()));
//            ReportResultTmpRepository.save(save);
//
//        }
//    }
//
//    public void saveCalcTmp1(String etab, Map<String, Object> obj, String dar, String cuser, String idtrait, long l)
//            throws ParseException {
//        System.out.println(obj);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(dar));
//        ReportResultTmpS save = new ReportResultTmpS(new SimpleDateFormat("yyyy-MM-dd").parse(dar), etab,
//                (obj.get("field") == null ? "" : obj.get("field").toString()), (obj.get("post") == null ? "" : obj.get("post").toString()), obj.get("col").toString(), cuser, cuser,
//                obj.get("valc").toString(), Long.parseLong(obj.get("rang").toString()), obj.get("fichier").toString(),
//                Integer.parseInt(obj.get("status").toString()));
//        ReportResultTmpRepository.save(save);
//        liveReportingService.detailsReportingToTheVue1(idtrait, obj.get("fichier").toString(), l);
//    }
//
//    public Map<String, Dynamic> getCalculatedFields(String fich) {
//
//        return null;
//    }
//
//    public List<Long> dismantlePostCalculate(String s) {
//        System.out.println(s);
//        List<Long> n = new ArrayList<Long>();
//        s = s.trim().substring(5, s.trim().length() - 1);
//        String[] split = s.split(",");
//
//        for (String e : split) {
//            n.add(reportAttributeRepository.findByDeleAndAttContaining(false, e).get(0).getId());
//        }
//        return n;
//    }
//
//    public Map<String, String> getGenerationAndSavingParam()
//            throws SQLException, ClassNotFoundException, JSONException {
//
//        System.out.println("START GETTING PÃ„RAMS");
//        String select = "";
//        // JSONObject obj = new JSONObject();
//        // boolean val = false;
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
////                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Statement stmt = (Statement) connection.createStatement();
//        select = "SELECT * FROM sanm  WHERE tabcd='" + NOMENGABTABLE_SYS + "' AND dele='0'";
//        System.out.println("BEFORE QUERRY");
//        ResultSet result = stmt.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//
//        while (result.next()) {
//
//            if (result.getString("acscd").equalsIgnoreCase("0009")) {
//                PARAM.put("idetab", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0010")) {
//                PARAM.put("extention", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0011")) {
//                PARAM.put("ip", result.getString("lib2"));
//                System.out.println("ip :" + result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0012")) {
//                PARAM.put("port", result.getString("lib2"));
//                System.out.println("port :" + result.getString("lib2"));
//
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0013")) {
//                PARAM.put("pass", result.getString("lib2"));
//                System.out.println("pass :" + result.getString("lib2"));
//
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0014")) {
//                PARAM.put("user", result.getString("lib2"));
//                System.out.println("user :" + result.getString("lib2"));
//
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0015")) {
//                PARAM.put("chemin", result.getString("lib2"));
//            }
//
//            if (result.getString("acscd").equalsIgnoreCase("0016")) {
//                PARAM.put("oracleUrl", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0017")) {
//                PARAM.put("loginUrl", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0018")) {
//                PARAM.put("passwordUrl", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0019")) {
//                PARAM.put("invEncours", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0020")) {
//                PARAM.put("invArchives", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0021")) {
//                PARAM.put("invErreurs", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0022")) {
//                PARAM.put("invErreurs", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0024")) {
//                PARAM.put("delimiteur", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0025")) {
//                PARAM.put("codePays", result.getString("lib2"));
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0026")) {
//                PARAM.put("formatDate", result.getString("lib2"));
//            }
//
//            if (result.getString("acscd").equalsIgnoreCase("0027")) {
//                PARAM.put("status", result.getString("lib2")); // statut du fichier
//            }
//
//            if (result.getString("acscd").equalsIgnoreCase("0028")) {
//                PARAM.put("sameServer", result.getString("lib2")); // 1 le fichier est stoquÃ© sur le meme serveur que
//                // le
//                // webservice 2 il est sur un serveur distant.
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0029")) {
//                PARAM.put("minimumNumber", result.getString("lib2")); // 1e nombre minimum d element a traiter avant
//                // insertion dans la bd
//            }
//
//            if (result.getString("acscd").equalsIgnoreCase("0031")) {
//                PARAM.put("F1139", result.getString("lib2").trim()); // statut du fichier
//            }
//            if (result.getString("acscd").equalsIgnoreCase("0032")) {
//                PARAM.put("AvergeTimeCoef", result.getString("lib2").trim()); // statut du fichier
//            }
//            /*
//             * if(result.getString("acscd").equalsIgnoreCase("0022")){
//             * PARAM.put("portOracle", result.getString("lib2")); }
//             */
//            System.out.println("END DISTRIBUTION");
//        }
//        System.out.println("END GETGENARATION AND SAVING PARAMS");
//
//        return PARAM;
//
//    }
//
//    public Map<String, String> getType(String fichier) throws SQLException, ClassNotFoundException, JSONException {
//
//        String select = "";
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
//        }
//        Statement stmt = (Statement) connection.createStatement();
//        select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0' and id='" + fichier + "'";
//        System.out.println("BEFORE QUERRY");
//        ResultSet result = stmt.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        System.out.println(select);
//        System.out.println(result);
//        while (result.next()) {
//            if (result.getString("lib2").equalsIgnoreCase(fichier)) {
//                PARAM.put("result", result.getString("taux1"));
//                PARAM.put("sql", result.getString("lib5"));
//            }
//        }
//        System.out.println(PARAM);
//        return PARAM;
//
//    }
//
//    public Map<String, String> getType12() throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
//        }
//        Statement stmt = (Statement) connection.createStatement();
//        select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0'";
//        System.out.println("BEFORE QUERRY");
//        System.out.println(select);
//        ResultSet result = stmt.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        String ppp = "";
//        while (result.next()) {
//            switch (result.getString("taux1")) {
//                case "0":
//                    ppp = "calculate";
//                    break;
//                case "1":
//                    ppp = "sql";
//                    break;
//                case "2":
//                    ppp = "duplicate";
//                    break;
//                default:
//                    ppp = "not define";
//            }
//
//            PARAM.put("result", ppp);
//            PARAM.put("sql", result.getString("lib5"));
//        }
//        return PARAM;
//    }
//
//    public void connec() throws SQLException, ClassNotFoundException, JSONException {
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
//        }
//        connect = (Statement) connection.createStatement();
//    }
//
//    public String[] getOracleCon() throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
//        }
//        Statement stmt = (Statement) connection.createStatement();
//        select = "SELECT * FROM pwd  WHERE tabcd='0043' AND dele='0'";
//        System.out.println("BEFORE QUERRY");
//        System.out.println(select);
//        ResultSet result = stmt.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        String ppp = "";
//        while (result.next()) {
//            byte[] decoder = Base64.getDecoder().decode(result.getString("lib1"));
//            String v = new String(decoder);
//            URL_ORACLE = result.getString("lib1");
//            LOGIN_ORACLE = result.getString("login");
//            PASSWORD_ORACLE = v;
//        }
//
//        return new String[]{URL_ORACLE, LOGIN_ORACLE, PASSWORD_ORACLE};
//
//    }
//
//    public Map<String, String> getType1(String fichier) throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            System.out.println("BEFORE CONNEXION");
//            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
//            System.out.println("AFTER CONNEXION");
//        } catch (SQLException ex) {
//        }
//        Statement stmt = (Statement) connection.createStatement();
//        select = "SELECT * FROM sanm  WHERE tabcd='3020' AND dele='0' and lib2='" + fichier + "'";
//        System.out.println("BEFORE QUERRY");
//        System.out.println(select);
//        ResultSet result = stmt.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        String ppp = "";
//        while (result.next()) {
//            switch (result.getString("taux1")) {
//                case "0":
//                    ppp = "calculate";
//                    break;
//                case "2":
//                    ppp = "sql";
//                    break;
//                case "1":
//                    ppp = "duplicate";
//                    break;
//                case "3":
//                    ppp = "duplicatepost";
//                    break;
//                default:
//                    ppp = "not define";
//            }
//
//            PARAM.put("result", ppp);
//            PARAM.put("link", result.getString("lib4"));
//            PARAM.put("sql", result.getString("lib5"));
//            PARAM.put("col_number", result.getString("lib5"));
//        }
//        return PARAM;
//    }
//
////    @Override
//    public String getmin() throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        select = "SELECT * FROM sanm  WHERE tabcd='0012' AND dele='0' and acscd='0029'";
//        System.out.println(select);
//        ResultSet result = connect.executeQuery(select);
//        while (result.next()) {
//            return result.getString("lib2");
//        }
//        return "0";
//    }
//
//    public Map<String, String> getType12(String fichier) throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        select = "SELECT * FROM sanm  WHERE tabcd='3020' AND dele='0' and lib2='" + fichier + "'";
//        System.out.println("BEFORE QUERRY");
//        System.out.println(select);
//        ResultSet result = connect.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        String ppp, gg = "";
//        int ee = 0;
//        while (result.next()) {
//            ee++;
//            gg = result.getString("taux4");
//            switch (result.getString("taux1")) {
//                case "0":
//                    ppp = "calculate";
//                    break;
//                case "1":
//                    ppp = "duplicate";
//                    break;
//                case "2":
//                    ppp = "sql";
//                    break;
//                case "3":
//                    ppp = "duplicateNoPost";
//                    break;
//                default:
//                    ppp = "not define";
//            }
//
//            PARAM.put("result", ppp);
//            PARAM.put("size", gg);
//            PARAM.put("sql", result.getString("lib5"));
//        }
//        if (ee == 0) {
//            System.out.println("noting found for this request");
//        }
//        return PARAM;
//    }
//
//    public Map<String, String> getType11(String fichier) throws SQLException, ClassNotFoundException, JSONException {
//        String select = "";
//        select = "SELECT * FROM sanm  WHERE tabcd='3009' AND dele='0' and lib2='" + fichier + "'";
//        System.out.println("BEFORE QUERRY");
//        System.out.println(select);
//        ResultSet result = connect.executeQuery(select);
//        System.out.println("AFTER QUERRY");
//        String ppp = "";
//        int ee = 0;
//        while (result.next()) {
//            ee++;
//            switch (result.getString("taux1")) {
//                case "0":
//                    ppp = "calculate";
//                    break;
//                case "1":
//                    ppp = "duplicate";
//                    break;
//                case "2":
//                    ppp = "sql";
//                    break;
//                case "3":
//                    ppp = "duplicateNoPost";
//                    break;
//                default:
//                    ppp = "not define";
//            }
//
//            PARAM.put("result", ppp);
//            PARAM.put("sql", result.getString("lib5"));
//        }
//        if (ee == 0) {
//            System.out.println("noting found for this request");
//        }
//        return PARAM;
//    }
//
//    public Map<String, Object> findBySql(String sql) {
//        return jdbcTemplate.queryForMap(sql);
//    }
//
//    public int findBySql1(String sql) {
//        return jdbcTemplate.update(sql);
//    }
//
//    public int findBySql2(String sql, String d, String f) {
//        jdbcTemplate.update("DELETE from ssqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
//        return jdbcTemplate.update(sql);
//    }
//
//    public int findBySql2v1(String sql, String d, String f) {
//        jdbcTemplate.update("DELETE from ssqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
//        liveReportingService.detailsReportingToTheVue2(uniquecode, f, 1L);
//        return jdbcTemplate.update(sql);
//    }
//
//    public int findBySql3(String sql, String d, String f, String p) throws SQLException, ClassNotFoundException {
//        jdbcTemplate.update("DELETE from ssqltype where dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'");
//        ORACLE_CON_PARAM = getOracleCon();
//        BigDecimal SOLDE_DISPONIBLE = null, SOLDE_INDICATIF = null;
//        int result = 0;
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Where is your Oracle JDBC Driver?");
//            e.printStackTrace();
//        }
//
//        try {
//            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
//        } catch (SQLException e) {
//            System.out.println("Connection Failed! Check output console");
//            e.printStackTrace();
//        }
//
//        Statement stmt = null;
//        ResultSet rs = null;
//        stmt = connection.createStatement();
//        rs = stmt.executeQuery(sql);
//        int s = Integer.parseInt(p);
//        String sqlput = "";
//        String sqlput1 = "";
//        // what is gotten from the database, its type
//        while (rs.next()) {
//            for (int i = 1; i <= s; i++) {
//                sqlput += (i == 1 ? "" : ",") + "col" + i;
//                sqlput1 += (i == 1 ? "" : ",") + "'" + rs.getString("col" + i) + "'";
//            }
//            // dar = to_date('" + d + "','yyyy-mm-dd') and fichi ='" + f + "'"
//            String fq = "INSERT INTO sql (" + sqlput + ",dar,fichi)values(" + sqlput1 + ",to_date('" + d
//                    + "','yyyy-mm-dd')," + f + ")";
//            System.out.println("tebit this is my query to request on bank system: " + fq);
//            jdbcTemplate.execute(sql);
//        }
//        return 1;
//
//    }
//
//    public BigDecimal separeteDataTrait(String formule, String etab, String fichier, String date)
//            throws ParseException {
//        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
//        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
//        formule = formule.replaceAll(" ", "");
//        String frm = formule.replaceAll("-", "+");
//        frm = formule.replaceAll("/", "+");
//        frm = formule.replaceAll("\\*", "+");
//        String[] str = frm.split("\\+");
//        String val = "0";
//        int i = 0;
//        int j = 1;
//        System.out.println("Date sql:" + date);
//        for (String s : str) {// each formular in the + operator
//            List<String> string = separeteData(s);
//            sql = "select * from srpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
//                    + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
//                    + string.get(1).trim() + "'";
//            List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//            System.out.println("test sql :" + sql);
//            if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
//                System.out.println("this is s value (first if): " + s);
//                if (s.substring(0, 1).equalsIgnoreCase("M")) {
//                    val = s.substring(1, s.length());
//                } else {
//                    System.out.println("Element size found here:" + tmp.size());
//                    if (tmp.size() == 1) {
//                        val = tmp.get(0).get("valm").toString();
//                    } else {
//                        val = "0";
//                    }
//                }
//                System.out.println("passe1 :" + val);
//                System.out.println("formule :" + formule);
//                System.out.println("expression :" + s.trim());
//                System.out.println("value :" + val + "");
//                formule = formule.replaceAll(s.trim(), val + "");
//                i = 1;
//            } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
//                System.out.println("this is s value (second if):" + s);
//                if (tmp.size() == 1) {
//                    val = tmp.get(0).get("valM").toString();
//                } else {
//                    val = "0";
//                }
//                System.out.println("passe2 :" + val);
//                formule = formule.replaceAll(s, " " + val + " ");
//
//                i = 1;
//            }
//        }
//        System.out.println("its the value return by " + formule + " is : " + eval(formule));
//        if (i == 1) {
//            System.out.println("result formule : " + formule);
//            String[] input = formule.split(" ");
//            String[] output = infixToRPN(input);
//
//            // Build output RPN string minus the commas
//            for (String token : output) {
//                System.out.print(token + " ");
//            }
//
//            // Feed the RPN string to RPNtoDouble to give result
//            System.out.println(output);
//            rt = RPNtoDouble(output);
//            System.out.println("result after :  " + rt);
//        }
//
//        return rt;
//    }
//
//    public BigDecimal separeteDataTraitv1(String formule, String etab, String fichier, String date)
//            throws ParseException {
//        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
//        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
//        try {
//            return new BigDecimal(formule);
//        } catch (NumberFormatException nfe) {
//
//            formule = formule.replaceAll(" ", "");
//
//            System.out.println("tebit formular:" + formule);
//            String frm = formule.replaceAll("-", "+");
//            frm = formule.replaceAll("/", "+");
//            frm = formule.replaceAll("\\*", "+");
//            String[] str = frm.split("\\+");
//            String val = "0";
//            int i = 0;
//            int j = 1;
//            System.out.println("Date sql:" + date);
//            for (String s : str) {// each formular in the + operator
//                List<String> string = separeteData(s);
////            sql = "select * from srpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
////                    + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
////                    + string.get(1).trim() + "'";
//
//                sql = "select * from srpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
//                        + "' and fichier='" + fichier + "' and field='" + s.trim() + "'";
//
//                List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//                System.out.println("test sql :" + sql);
//                if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
//                    System.out.println("this is s value (first if): " + s);
//                    if (s.substring(0, 1).equalsIgnoreCase("M")) {
//                        val = s.substring(1, s.length());
//                    } else {
//                        System.out.println("Element size found here:" + tmp.size());
//                        if (tmp.size() == 1) {
//                            val = tmp.get(0).get("valm").toString();
//                        } else {
//                            val = "0";
//                        }
//                    }
//                    System.out.println("passe1 :" + val);
//                    System.out.println("formule :" + formule);
//                    System.out.println("expression :" + s.trim());
//                    System.out.println("value :" + val + "");
//                    formule = formule.replaceAll(s.trim(), val + "");
////				formule = formule.replaceAll(s, " " + val + " ");
//                    System.out.println("output formular " + formule);
//                    i = 1;
//                } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
//                    System.out.println("this is s value (second if):" + s);
//                    if (tmp.size() == 1) {
//                        val = tmp.get(0).get("valM").toString();
//                    } else {
//                        val = "0";
//                    }
//                    System.out.println("passe2 :" + val);
//                    formule = formule.replaceAll(s, " " + val + " ");
//
//                    i = 1;
//                }
//            }
//            System.out.println("its the value return by " + formule + " is : " + eval(formule));
//            if (i == 1) {
//                System.out.println("result formule : " + formule);
//                String[] input = formule.split(" ");
//                String[] output = infixToRPN(input);
//
//                // Build output RPN string minus the commas
//                for (String token : output) {
//                    System.out.print(token + " ");
//                }
//
//                // Feed the RPN string to RPNtoDouble to give result
//                System.out.println(output);
//                rt = RPNtoDouble(output);
//                System.out.println("result after :  " + rt);
//            }
//
//            return rt;
//        }
//    }
//
//    public BigDecimal separeteDataTrait1(String formule, String etab, String fichier, String date)
//            throws ParseException {
//        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
//        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
//        formule = " " + formule + " ";
//        String frm = formule.replaceAll("-", "+");
//        frm = formule.replaceAll("/", "+");
//        frm = formule.replaceAll("\\*", "+");
//        String[] str = frm.split("\\+");
//        String val = "0";
//        int i = 0;
//        int j = 1;
//        System.out.println("Date sql:" + date);
//        for (String s : str) {// each formular in the + operator
//            List<String> string = separeteData(s);
//            sql = "select * from srpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
//                    + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
//                    + string.get(1).trim() + "'";
//            List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//            //intermidiate files
//            if (tmp.isEmpty()) {
//                sql = "select * from srpreptmp where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
//                        + "' and fichier='" + fichier + "' and field ='" + s.trim() + "'";
//                tmp = jdbcTemplate.queryForList(sql);
//            }
//            System.out.println("test sql :" + sql);
//            if (!tmp.isEmpty() || s.substring(0, 1).equalsIgnoreCase("M")) {
//                System.out.println("this is s value (first if): " + s);
//                if (s.substring(0, 1).equalsIgnoreCase("M")) {
//                    val = s.substring(1, s.length());
//                } else {
//                    System.out.println("Element size found here:" + tmp.size());
//                    if (tmp.size() == 1) {
//                        val = tmp.get(0).get("valm").toString();
//                    } else {
//                        val = "0";
//                    }
//                }
//                System.out.println("passe1 :" + val);
//                System.out.println("formule :" + formule);
//                System.out.println("expression :" + s.trim());
//                val = (val.equals("999999999999999")) ? "0" : val;
//                System.out.println("value :" + val + "");
//                formule = formule.replaceAll(s, " " + val + " ");
////				formule = formule.replaceAll(s, " " + val + " ");
//
//                i = 1;
//            } else if (!tmp.isEmpty() || s.substring(0, 2).equalsIgnoreCase("CH")) {
//                System.out.println("this is s value (second if):" + s);
//                if (tmp.size() == 1) {
//                    val = tmp.get(0).get("valM").toString();
//                } else {
//                    val = "0";
//                }
//                val = (val.equals("999999999999999")) ? "0" : val;
//                System.out.println("passe2 :" + val);
//
//                formule = formule.replaceAll(s, " " + val + " ");
//
//                i = 1;
//            }
//        }
//        System.out.println("its the value return by " + formule + " is : " + eval(formule));
//        if (i == 1) {
//            System.out.println("result formule : " + formule);
//            String[] input = formule.split(" ");
//            String[] output = infixToRPN(input);
//
//            // Build output RPN string minus the commas
//            for (String token : output) {
//                System.out.print(token + " ");
//            }
//
//            // Feed the RPN string to RPNtoDouble to give result
//            System.out.println(output);
//            rt = RPNtoDouble(output);
//            System.out.println("result after :  " + rt);
//        }
//        Double res = rt.doubleValue();
//        rt = new BigDecimal((res >= 0 ? 1 : -1) * Math.round(res >= 0 ? res : -res));
//        return rt;
//    }
//
//    public static double eval(final String str) {
//        return new Object() {
//            int pos = -1, ch;
//
//            void nextChar() {
//                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
//            }
//
//            boolean eat(int charToEat) {
//                while (ch == ' ') {
//                    nextChar();
//                }
//                if (ch == charToEat) {
//                    nextChar();
//                    return true;
//                }
//                return false;
//            }
//
//            double parse() {
//                nextChar();
//                double x = parseExpression();
//                if (pos < str.length()) {
//                    throw new RuntimeException("Unexpected: " + (char) ch);
//                }
//                return x;
//            }
//
//            // Grammar:
//            // expression = term | expression `+` term | expression `-` term
//            // term = factor | term `*` factor | term `/` factor
//            // factor = `+` factor | `-` factor | `(` expression `)`
//            // | number | functionName factor | factor `^` factor
//            double parseExpression() {
//                double x = parseTerm();
//                for (;;) {
//                    if (eat('+')) {
//                        x += parseTerm(); // addition
//                    } else if (eat('-')) {
//                        x -= parseTerm(); // subtraction
//                    } else {
//                        return x;
//                    }
//                }
//            }
//
//            double parseTerm() {
//                double x = parseFactor();
//                for (;;) {
//                    if (eat('*')) {
//                        x *= parseFactor(); // multiplication
//                    } else if (eat('/')) {
//                        x /= parseFactor(); // division
//                    } else {
//                        return x;
//                    }
//                }
//            }
//
//            double parseFactor() {
//                if (eat('+')) {
//                    return parseFactor(); // unary plus
//                }
//                if (eat('-')) {
//                    return -parseFactor(); // unary minus
//                }
//                double x;
//                int startPos = this.pos;
//                if (eat('(')) { // parentheses
//                    x = parseExpression();
//                    eat(')');
//                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
//                    while ((ch >= '0' && ch <= '9') || ch == '.') {
//                        nextChar();
//                    }
//                    x = Double.parseDouble(str.substring(startPos, this.pos));
//                } else if (ch >= 'a' && ch <= 'z') { // functions
//                    while (ch >= 'a' && ch <= 'z') {
//                        nextChar();
//                    }
//                    String func = str.substring(startPos, this.pos);
//                    x = parseFactor();
//                    if (func.equals("sqrt")) {
//                        x = Math.sqrt(x);
//                    } else if (func.equals("sin")) {
//                        x = Math.sin(Math.toRadians(x));
//                    } else if (func.equals("cos")) {
//                        x = Math.cos(Math.toRadians(x));
//                    } else if (func.equals("tan")) {
//                        x = Math.tan(Math.toRadians(x));
//                    } else {
//                        throw new RuntimeException("Unknown function: " + func);
//                    }
//                } else {
//                    throw new RuntimeException("Unexpected: " + (char) ch);
//                }
//
//                if (eat('^')) {
//                    x = Math.pow(x, parseFactor()); // exponentiation
//                }
//                return x;
//            }
//        }.parse();
//    }
//
//    public BigDecimal separeteDataCond(String formule, String etab, String fichier, String date) throws ParseException {
//        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
//        formule = formule.replaceAll(" ", "");
//        List<String> ret = new ArrayList<String>();
//        int firstIndex = formule.indexOf('(');
//        int lasttIndex = formule.indexOf(')');
//        String bool = formule.substring(firstIndex + 1, lasttIndex);
//        System.out.println(bool);
//        Boolean val = false;
//        if (bool.contains("<=")) {
//            String[] str = bool.split("<=");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res <= 0);
//        } else if (bool.contains(">=")) {
//            String[] str = bool.split(">=");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res >= 0);
//        } else if (bool.contains("<")) {
//            String[] str = bool.split("<");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res < 0);
//        } else if (bool.contains(">")) {
//            String[] str = bool.split(">");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res > 0);
//        } else if (bool.contains("!=")) {
//            String[] str = bool.split("!=");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res != 0);
//        } else {
//            String[] str = bool.split("=");
//            int res;
//            res = separeteDataTraitv1(str[0], etab, fichier, date)
//                    .compareTo(separeteDataTraitv1(str[1], etab, fichier, date));
//            val = (res == 0);
//        }
//        String frm = formule.substring(lasttIndex + 1, formule.length());
//        String[] strd = frm.split(";");
//        System.out.println("tebit check here: " + strd[1]);
//        System.out.println(strd[0]);
//        if (val) {
//            return separeteDataTraitv1(strd[0], etab, fichier, date);
//        } else {
//            System.out.println(separeteDataTraitv1(strd[1].substring(4), etab, fichier, date));
//
//            return separeteDataTraitv1(strd[1].substring(4), etab, fichier, date);
//        }
//
//    }
//
//    public List<String> separeteData(String formule) {
//        formule = formule.trim();
//        List<String> ret = new ArrayList<String>();
//        formule = formule.substring(2, formule.length());
//        int lasttIndex = formule.lastIndexOf('C');
//        String post = formule.substring(0, lasttIndex);
//        String colone = formule.substring(lasttIndex + 1, formule.length());
//        ret.add(post);
//        ret.add(colone);
//        return ret;
//
//    }
//
//    public List<Map<String, Object>> findByDup(String toString, String fichier) {
//        String sql = "Select * from ARES.sRPREP where to_date(dar,'YYYY-MM-DD') < to_date(?,'YYYY-MM-DD') "
//                + "and to_date(dar,'YYYY-MM-DD') = (Select MAX(to_date(dar,'YYYY-MM-DD')) from ARES.sRPREP) and fichier='"
//                + fichier + "'";
//        return jdbcTemplate.queryForList(sql, new Object[]{toString});
//    }
//
//    public List<Map<String, Object>> findByDup1(String toString, String fichier) {
//        String sql = "Select * from srprep where dar in (select max(dar) from srprep where fichier = '" + fichier
//                + "' and col > 1) and fichier='" + fichier + "'";
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    private static boolean isAssociative(String token, int type) {
//        if (!isOperator(token)) {
//            throw new IllegalArgumentException("Invalid token: " + token);
//        }
//
//        if (OPERATORS.get(token)[1] == type) {
//            return true;
//        }
//        return false;
//    }
//
//    // Compare precedence of operators.
//    private static final int cmpPrecedence(String token1, String token2) {
//        if (!isOperator(token1) || !isOperator(token2)) {
//            throw new IllegalArgumentException("Invalid tokens: " + token1 + " " + token2);
//        }
//        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
//    }
//
//    // Convert infix expression format into reverse Polish notation
//    public static String[] infixToRPN(String[] inputTokens) {
//        ArrayList<String> out = new ArrayList<String>();
//        Stack<String> stack = new Stack<String>();
//
//        // For each token
//        for (String token : inputTokens) {
//            // If token is an operator
//            if (isOperator(token)) {
//                // While stack not empty AND stack top element
//                // is an operator
//                while (!stack.empty() && isOperator(stack.peek())) {
//                    if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(token, stack.peek()) <= 0)
//                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0)) {
//                        out.add(stack.pop());
//                        continue;
//                    }
//                    break;
//                }
//                // Push the new operator on the stack
//                stack.push(token);
//            } // If token is a left bracket '('
//            else if (token.equals("(")) {
//                stack.push(token); //
//            } // If token is a right bracket ')'
//            else if (token.equals(")")) {
//                while (!stack.empty() && !stack.peek().equals("(")) {
//                    out.add(stack.pop());
//                }
//                stack.pop();
//            } // If token is a number
//            else {
//                out.add(token);
//            }
//        }
//        while (!stack.empty()) {
//            out.add(stack.pop());
//        }
//        String[] output = new String[out.size()];
//        return out.toArray(output);
//    }
//
//    public static BigDecimal RPNtoDouble(String[] tokens) {
//        Stack<String> stack = new Stack<String>();
//
//        // For each token
//        for (String token : tokens) {
//            // If the token is a value push it onto the stack
//            if (!isOperator(token)) {
//                stack.push(token);
//            } else {
//                // Token is an operator: pop top two entries
//                Double d2 = Double.valueOf(stack.pop());
//                Double d1 = Double.valueOf(stack.pop());
//
//                // Get the result
//                Double result = token.compareTo("+") == 0 ? d1 + d2
//                        : token.compareTo("-") == 0 ? d1 - d2 : token.compareTo("*") == 0 ? d1 * d2 : d1 / d2;
//
//                // Push result onto stack
//                stack.push(String.valueOf(result));
//            }
//        }
////        System.out.println(stack.pop());
//        BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(stack.pop());
////        return Double.valueOf(stack.pop());
//        return DEFAULT_FOOBAR_VALUE;
//    }
//
//    private void savePost(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICPOST = findCalcByPost(fichier);
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//
//                String formule = FICPOST.get(t).get("CALC").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                List<String> st = separeteDataPost(formule);
//                List<String> ret = new ArrayList<String>();
//                String condition = null;
//                if (st.size() != 1) {
//                    for (int f = 1; f < st.size(); f++) {
//                        ret.add(st.get(f));
//                    }
//                    condition = findByAtt(ret);
//                }
//                List<Map<String, Object>> post = findByPost(st.get(0).toString());
//                if (!post.isEmpty()) {
//                    Map<String, Object> result = calcBySold(post, condition, date);
//                    System.out.println("diviseur :" + divd);
//                    int sold = (int) Math.round(Double.parseDouble(result.get("result").toString()) / divd);
//                    String solde = sold + "";
//                    System.out.println("Col :" + FICPOST.get(t).get("COL").toString());
//                    System.out.println("valeur calc:" + sold);
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                            solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//
//                } else {
//                    System.out.println(FICPOST.get(t));
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                }
//                liveReportingService.detailsReportingToTheVue1(idTrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePost250202019(String fichier, String etab, String date, String cuser, String idtrait) {
//        List<Map<String, Object>> FICPOST;
//        FICPOST = findCalcByPost(fichier);
//
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        String p = "";
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                String formule = FICPOST.get(t).get("CALC").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                String condition = null;
//                if (formule.equals("")) {
//                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
//                    p = "";
//                } else {
//
//                    List<String> st = separeteDataPost(formule);
//                    List<String> ret = new ArrayList<String>();
//
//                    if (st.size() != 1) {
//                        for (int f = 1; f < st.size(); f++) {
//                            ret.add(st.get(f));
//                        }
//                        condition = findByAtt(ret);
//                    }
//                    p = st.get(0).toString();
//                }
//                List<Map<String, Object>> post = findByPost(p);
//                if (formule.isEmpty()) {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                } else {
//                    if (!post.isEmpty()) {
//                        Map<String, Object> result = calcBySold(post, condition, date);
//                        System.out.println("diviseur :" + divd);
//                        String solde = "";
//                        if (FICPOST.get(t).get("TYSORCE").toString().equalsIgnoreCase("FINAL")) {
//                            Double dre = Double.parseDouble(result.get("result").toString());
//                            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / divd));
//                            solde = sold + "";
//                        } else {
//                            Double dre = Double.parseDouble(result.get("result").toString());
//                            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / divd));
//                            Double soldet = dre / divd;
//                            solde = soldet + "";
//                        }
//
//                        System.out.println("Col :" + FICPOST.get(t).get("COL").toString());
//                        System.out.println("valeur calc sold:" + solde);
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                        System.out.println("string valeur calc solde:" + solde);
//                    } else {
//                        System.out.println(FICPOST.get(t));
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePost250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICPOST) {
//
//        minimum = minim.get(fichier);//Long.valueOf(parameters.get("minimumNumber"));
//        System.out.println("Tebit this is the minimum for file :" + fichier + " which is :" + minimum);
//        String p = "";
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                String formule = FICPOST.get(t).get("CALC").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                String condition = null;
//                if (formule.equals("")) {
//                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
//                    p = "";
//                } else {
//
//                    List<String> st = separeteDataPost(formule);
//                    List<String> ret = new ArrayList<String>();
//
//                    if (st.size() != 1) {
//                        for (int f = 1; f < st.size(); f++) {
//                            ret.add(st.get(f));
//                        }
//                        condition = findByAtt(ret);
//                    }
//                    p = st.get(0).toString();
//                }
//                List<Map<String, Object>> post = findByPost(p);
//                if (formule.isEmpty()) {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                } else {
//                    if (!post.isEmpty()) {
//                        Map<String, Object> result = calcBySold(post, condition, date);
//                        System.out.println("diviseur :" + divd);
//                        String solde = "";
//                        if (FICPOST.get(t).get("TYSORCE").toString().equalsIgnoreCase("FINAL")) {
//                            Double dre = Double.parseDouble(result.get("result").toString());
//                            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / divd));
//                            solde = sold + "";
//                        } else {
//                            Double dre = Double.parseDouble(result.get("result").toString());
//                            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / divd));
//                            Double soldet = dre / divd;
//                            solde = soldet + "";
//                        }
//
//                        System.out.println("Col :" + FICPOST.get(t).get("COL").toString());
//                        System.out.println("valeur calc sold:" + solde);
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                        System.out.println("string valeur calc solde:" + solde);
//                    } else {
//                        System.out.println(FICPOST.get(t));
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                }
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePost250202019_F1139(String fichier, String etab, String date, String cuser, String idtrait,
//            String s) {
//        List<Map<String, Object>> FICPOST;
//        FICPOST = findCalcByPost_F1139(fichier, s);
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        String p = "";
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                String formule = FICPOST.get(t).get("CALC").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                String condition = null;
//                if (formule.equals("")) {
//                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
//                    p = "";
//                } else {
//
//                    List<String> st = separeteDataPost(formule);
//                    List<String> ret = new ArrayList<String>();
//
//                    if (st.size() != 1) {
//                        for (int f = 1; f < st.size(); f++) {
//                            ret.add(st.get(f));
//                        }
//                        condition = findByAtt(ret);
//                    }
//                    p = st.get(0).toString();
//                }
//                List<Map<String, Object>> post = findByPost(p);
//                if (formule.isEmpty()) {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                } else {
//                    if (!post.isEmpty()) {
//                        Map<String, Object> result = calcBySold(post, condition, date);
//                        System.out.println("diviseur :" + divd);
//                        int sold = (int) Math.round(Double.parseDouble(result.get("result").toString()) / divd);
//                        String solde = sold + "";
//                        System.out.println("Col :" + FICPOST.get(t).get("COL").toString());
//                        System.out.println("valeur calc:" + sold);
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//
//                    } else {
//                        System.out.println(FICPOST.get(t));
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePost250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
//            String s, List<Map<String, Object>> FICPOST) {
////        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        minimum = minim.get(fichier);
//        String p = "";
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                String formule = FICPOST.get(t).get("CALC").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                String condition = null;
//                if (formule.equals("")) {
//                    condition = "where insld.dar =to_date('','yyyy-mm-dd')";
//                    p = "";
//                } else {
//
//                    List<String> st = separeteDataPost(formule);
//                    List<String> ret = new ArrayList<String>();
//
//                    if (st.size() != 1) {
//                        for (int f = 1; f < st.size(); f++) {
//                            ret.add(st.get(f));
//                        }
//                        condition = findByAtt(ret);
//                    }
//                    p = st.get(0).toString();
//                }
//                List<Map<String, Object>> post = findByPost(p);
//                if (formule.isEmpty()) {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                } else {
//                    if (!post.isEmpty()) {
//                        Map<String, Object> result = calcBySold(post, condition, date);
//                        System.out.println("diviseur :" + divd);
//                        int sold = (int) Math.round(Double.parseDouble(result.get("result").toString()) / divd);
//                        String solde = sold + "";
//                        System.out.println("Col :" + FICPOST.get(t).get("COL").toString());
//                        System.out.println("valeur calc:" + sold);
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//
//                    } else {
//                        System.out.println(FICPOST.get(t));
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                }
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePostF1000_1(String fichier, String etab, String date, String cuser, String idtrait) {
//        List<Map<String, Object>> FICPOST = findCalcByPost(fichier);
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                System.out.println(FICPOST.get(t));
//                String formule = FICPOST.get(t).get("POST").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                List<String> st = new ArrayList<String>();
//                st.add(formule);
//                List<String> ret = new ArrayList<String>();
//                String condition = null;
//                if (st.size() != 1) {
//                    for (int f = 1; f < st.size(); f++) {
//                        ret.add(st.get(f));
//                    }
//                    condition = findByAtt(ret);
//                }
//                List<Map<String, Object>> post = findByPost(st.get(0).toString());
//                System.out.println("chap --" + post);
//                System.out.println("chap1 --" + st.get(0).toString());
//                System.out.println("empty --" + post.isEmpty());
//                if (!post.isEmpty()) {
//                    Map<String, Object> result = new HashMap<String, Object>();
//                    if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("2")) {
//                        result = calcByBalSLDD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("3")) {
//                        result = calcByBalSLDC(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("4")) {
//                        result = calcByBalCUMD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("5")) {
//                        result = calcByBalCUMC(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("6")) {
//                        result = calcByBalSLDCVD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("7")) {
//                        result = calcByBalSLDCVC(post, condition, date);
//                    }
//                    if (result.isEmpty()) {
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    } else {
//                        int sold = (int) Math.round(Double.parseDouble(result.get("result").toString()) / divd);
//                        String solde = sold + "";
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                } else {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePostF1000(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICPOST = findCalcByPost(fichier);
//        minimum = Long.valueOf(parameters.get("minimumNumber"));
//        if (FICPOST.size() > 0) {
//            for (int t = 0; t < FICPOST.size(); t++) {
//                System.out.println(FICPOST.get(t));
//                String formule = FICPOST.get(t).get("POST").toString();
//                Double divd = Double.parseDouble(FICPOST.get(t).get("DIVD").toString());
//                System.out.println("FormulePost: " + formule);
//                List<String> st = new ArrayList<String>();
//                st.add(formule);
//                List<String> ret = new ArrayList<String>();
//                String condition = null;
//                if (st.size() != 1) {
//                    for (int f = 1; f < st.size(); f++) {
//                        ret.add(st.get(f));
//                    }
//                    condition = findByAtt(ret);
//                }
//                List<Map<String, Object>> post = findByPost(st.get(0).toString());
//                System.out.println("chap --" + post);
//                System.out.println("chap1 --" + st.get(0).toString());
//                System.out.println("empty --" + post.isEmpty());
//                if (!post.isEmpty()) {
//                    Map<String, Object> result = new HashMap<String, Object>();
//                    if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("2")) {
//                        result = calcByBalSLDD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("3")) {
//                        result = calcByBalSLDC(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("4")) {
//                        result = calcByBalCUMD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("5")) {
//                        result = calcByBalCUMC(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("6")) {
//                        result = calcByBalSLDCVD(post, condition, date);
//                    } else if (FICPOST.get(t).get("COL").toString().equalsIgnoreCase("7")) {
//                        result = calcByBalSLDCVC(post, condition, date);
//                    }
//                    if (result.isEmpty()) {
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                "0", fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    } else {
//                        int sold = (int) Math.round(Double.parseDouble(result.get("result").toString()) / divd);
//                        String solde = sold + "";
//                        saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(),
//                                solde, fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                                Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                                FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                    }
//                } else {
//                    saveCalcTmp(etab, FICPOST.get(t).get("POST").toString(), FICPOST.get(t).get("COL").toString(), "0",
//                            fichier, date, Integer.parseInt(FICPOST.get(t).get("RANG").toString()), t, cuser,
//                            Integer.parseInt(FICPOST.get(t).get("STATUS").toString()),
//                            FICPOST.get(t).get("FIELD").toString(), FICPOST.get(t).get("TYPEVAL").toString());
//                }
//            }
//        }
//    }
//
//    private void saveSql(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICSQL = findCalcBySql(fichier);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("formule sql :" + formule);
//                System.out.println(FICSQL.get(t).get("RANG"));
//                Map<String, Object> ret = findBySql(formule);
////                saveCalcTmp(etab,ret.get(t),date,cuser);
//                saveCalcTmp(etab, FICSQL.get(t).get("POST").toString(), FICSQL.get(t).get("COL").toString(),
//                        ret.get("result").toString(), fichier, date,
//                        Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICSQL.get(t).get("STATUS").toString()), FICSQL.get(t).get("FIELD").toString(),
//                        FICSQL.get(t).get("TYPEVAL").toString());
//            }
//        }
//    }
//
//    private void saveSql250202019(String fichier, String etab, String date, String cuser, String sss) {
//        List<Map<String, Object>> FICSQL = findCalcBySql(fichier);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("formule sql :" + formule);
//                System.out.println(FICSQL.get(t).get("RANG"));
//                Map<String, Object> ret = findBySql(formule);
////                saveCalcTmp(etab,ret.get(t),date,cuser);
//                saveCalcTmp(etab, FICSQL.get(t).get("POST").toString(), FICSQL.get(t).get("COL").toString(),
//                        ret.get("result").toString(), fichier, date,
//                        Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICSQL.get(t).get("STATUS").toString()), FICSQL.get(t).get("FIELD").toString(),
//                        FICSQL.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue1(sss, fichier, minimum);
//
//            }
//        }
//    }
//
//    private void saveSql250202019V1(String fichier, String etab, String date, String cuser, String sss, List<Map<String, Object>> FICSQL) {
//
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("formule sql :" + formule);
//                System.out.println(FICSQL.get(t).get("RANG"));
//                Map<String, Object> ret = findBySql(formule);
////                saveCalcTmp(etab,ret.get(t),date,cuser);
//                saveCalcTmp(etab, FICSQL.get(t).get("POST").toString(), FICSQL.get(t).get("COL").toString(),
//                        ret.get("result").toString(), fichier, date,
//                        Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICSQL.get(t).get("STATUS").toString()), FICSQL.get(t).get("FIELD").toString(),
//                        FICSQL.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue2(sss, fichier, minim.get(fichier));
//
//            }
//        }
//    }
//
//    private void savecol1(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint2(fichier);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
//
//            }
//        }
//    }
//
//    private void savecol250202019(String fichier, String etab, String date, String cuser, String idtrait) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint2(fichier);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savecol250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL) {
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void savecol250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
//            String source, List<Map<String, Object>> FICSQL) {
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), "1", FICSQL.get(t).get("POSTE").toString(),
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        "CH" + FICSQL.get(t).get("POSTE").toString() + "C1", "C");
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void savePoint1(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint1(fichier);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "fichier = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and post = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and col = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and rang = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                } else {// save new
//                    saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
//                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                            FICSQL.get(t).get("FIELD").toString(), "M");
//
//                }
//            }
//        }
//    }
//
//    private void savePoint250202019(String fichier, String etab, String date, String cuser, String idtrait) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint1(fichier);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "trim(fichier) = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and trim(post) = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and trim(col) = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and trim(rang) = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//                System.out.println("the lenght is :");
//                System.out.println(PPP.size());
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                } else {// save new
//                    saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
//                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                            FICSQL.get(t).get("FIELD").toString(), "M");
//
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePoint250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL) {
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "trim(fichier) = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and trim(post) = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and trim(col) = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and trim(rang) = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//                System.out.println("the lenght is :");
//                System.out.println(PPP.size());
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                } else {// save new
//                    saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
//                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                            FICSQL.get(t).get("FIELD").toString(), "M");
//
//                }
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void savePoint30032020(String fichier, String etab, String date, String cuser, String idtrait) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint1(fichier);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "trim(fichier) = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and trim(post) = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and trim(col) = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and trim(rang) = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//                System.out.println("the lenght is :");
//                System.out.println(PPP.size());
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePoint30032020v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICSQL) {
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "trim(fichier) = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and trim(post) = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and trim(col) = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and trim(rang) = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//                System.out.println("the lenght is :");
//                System.out.println(PPP.size());
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                }
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void savePoint250202019_F1139(String fichier, String etab, String date, String cuser, String idtrait,
//            String source) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint1_F1139(fichier, source);// all *** cells
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "fichier = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and post = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and col = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and rang = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                } else {// save new
//                    saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
//                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                            FICSQL.get(t).get("FIELD").toString(), "M");
//
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void savePoint250202019_F1139v1(String fichier, String etab, String date, String cuser, String idtrait,
//            String source, List<Map<String, Object>> FICSQL) {
//        System.out.println(FICSQL);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
////				String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + FICSQL.get(t).get("FICH").toString());
//                System.out.println("Save Point :" + FICSQL.get(t).get("POSTE").toString());
//                System.out.println(Long.valueOf(FICSQL.get(t).get("RANG").toString()));
//                String val = "999999999999999";
//                String qu = "select col,fichier,post,rang from srpreptmp where " + "fichier = '"
//                        + FICSQL.get(t).get("FICH").toString() + "'" + "  and post = '"
//                        + FICSQL.get(t).get("POSTE").toString() + "'" + "  and col = '"
//                        + FICSQL.get(t).get("COL").toString() + "'" + "  and rang = '"
//                        + FICSQL.get(t).get("RANG").toString() + "' ";
//                System.out.println(qu);
//                List<Map<String, Object>> PPP = jdbcTemplate.queryForList(qu);
//
//                if (PPP.size() > 0) {// modify existing
//                    updateTemp(FICSQL, val, t);
//                } else {// save new
//                    saveCalcTmp(etab, FICSQL.get(t).get("POSTE").toString(), FICSQL.get(t).get("COL").toString(), val,
//                            fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                            FICSQL.get(t).get("FIELD").toString(), "M");
//
//                }
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void updateTemp(List<Map<String, Object>> FICSQL, String val, int t) {
//        String sss = "UPDATE srpreptmp SET valm = " + val + ",status = 1 where fichier = '"
//                + FICSQL.get(t).get("FICH").toString() + "' " + "and post = '" + FICSQL.get(t).get("POSTE").toString()
//                + "' " + "and col = " + FICSQL.get(t).get("COL").toString() + " " + "and rang = "
//                + FICSQL.get(t).get("RANG").toString() + " ";
//        jdbcTemplate.update(sss);
//    }
//
//    private void savePoint(String fichier, String etab, String date, String cuser) {
//        List<Map<String, Object>> FICSQL = findCalcByPoint(fichier);
//        if (FICSQL.size() > 0) {
//            for (int t = 0; t < FICSQL.size(); t++) {
//                String formule = FICSQL.get(t).get("CALC").toString();
//                System.out.println("Save Point :" + formule);
//                String val = "999999999999999";
//
//                saveCalcTmp(etab, FICSQL.get(t).get("POST").toString(), FICSQL.get(t).get("COL").toString(), val,
//                        fichier, date, Integer.parseInt(FICSQL.get(t).get("RANG").toString()), t, cuser, 1,
//                        FICSQL.get(t).get("FIELD").toString(), FICSQL.get(t).get("TYPEVAL").toString());
//
//            }
//        }
//    }
//
//    public List<Map<String, Object>> findCalcBySql(String fichier) {
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='S' ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
//    }
//
//    public List<Map<String, Object>> findCalcByTraitement(String fichier) {
////		String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
////				+ "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY u.POST,u.COL asc";
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, (case when( a.rang is null )then -1 else a.rang end) as rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status "
//                + "from srpcalc u LEFT JOIN srppfich a on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
//    }
//
//    public List<Map<String, Object>> findCalcByTraitement_F1139(String fichier, String ctry) {
////		String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, a.rang,(case when EXISTS(SELECT * from rppfich WHERE col =u.col) then 1 else 0 end)status from rpcalc u LEFT JOIN rppfich a \n"
////				+ "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' ORDER BY u.POST,u.COL asc";
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.source,u.field,u.divd, (case when( a.rang is null )then -1 else a.rang end) as rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status "
//                + "from srpcalc u LEFT JOIN srppfich a on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='T' and a.source = '"
//                + ctry + "' ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
//    }
//
//    public List<Map<String, Object>> findCalcByCondition(String fichier) {
//        System.out.println("calculate here tebit 02132021");
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd,u.source,u.field, a.rang,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =? and u.source='C' ORDER BY u.POST,u.COL asc";
//        return jdbcTemplate.queryForList(sql, new Object[]{fichier});
//    }
//
//    private void saveIntermidiate250202019(String fichier, String etab, String date, String cuser, String idtrait)
//            throws ParseException {
//        List<Map<String, Object>> FICTRAITEMENT;
//        if (fichier.equalsIgnoreCase("F1139")) {
//            FICTRAITEMENT = findCalcByTraitement_F1139(fichier, "CMR");
//        } else {
//            FICTRAITEMENT = findCalcByTraitement(fichier);
//        }
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleTraitement: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataTrait1(formule, etab, fichier, date);
//                cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, FICTRAITEMENT.get(t).get("POST").toString(),
//                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void saveTraitement250202019(String fichier, String etab, String date, String cuser, String idtrait)
//            throws ParseException {
//        List<Map<String, Object>> FICTRAITEMENT;
//        if (fichier.equalsIgnoreCase("F1139")) {
//            FICTRAITEMENT = findCalcByTraitement_F1139(fichier, "CMR");
//        } else {
//            FICTRAITEMENT = findCalcByTraitement(fichier);
//        }
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleTraitement: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataTrait1(formule, etab, fichier, date);
//                cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, FICTRAITEMENT.get(t).get("POST").toString(),
//                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//            }
//        }
//    }
//
//    private void saveTraitement250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICTRAITEMENT)
//            throws ParseException {
//
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleTraitement: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataTrait1(formule, etab, fichier, date);
//                cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, FICTRAITEMENT.get(t).get("POST").toString(),
//                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//            }
//        }
//    }
//
//    private void saveTraitement(String fichier, String etab, String date, String cuser) throws ParseException {
//        List<Map<String, Object>> FICTRAITEMENT = findCalcByTraitement(fichier);
//
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleTraitement: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataTrait(formule, etab, fichier, date);
//                cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, FICTRAITEMENT.get(t).get("POST").toString(),
//                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//            }
//        }
//    }
//
//    private void saveCondition250202019(String fichier, String etab, String date, String cuser, String idtrait)
//            throws ParseException {
//        List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(fichier);
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleCondition: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataCond(formule, etab, fichier, date);
//                cd = cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, (FICTRAITEMENT.get(t).get("POST") == null ? "" : FICTRAITEMENT.get(t).get("POST").toString()),
//                        (FICTRAITEMENT.get(t).get("COL") == null ? "-1" : FICTRAITEMENT.get(t).get("COL").toString()), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG") == null ? "0" : FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue1(idtrait, fichier, minimum);
//
//            }
//        }
//    }
//
//    private void saveCondition250202019v1(String fichier, String etab, String date, String cuser, String idtrait, List<Map<String, Object>> FICTRAITEMENT)
//            throws ParseException {
//
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleCondition: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataCond(formule, etab, fichier, date);
//                cd = cd.setScale(0, RoundingMode.HALF_UP);
//                String sold = cd.toString();
//                saveCalcTmp(etab, (FICTRAITEMENT.get(t).get("POST") == null ? "" : FICTRAITEMENT.get(t).get("POST").toString()),
//                        (FICTRAITEMENT.get(t).get("COL") == null ? "-1" : FICTRAITEMENT.get(t).get("COL").toString()), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG") == null ? "0" : FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//                liveReportingService.detailsReportingToTheVue2(idtrait, fichier, minim.get(fichier));
//
//            }
//        }
//    }
//
//    private void saveCondition(String fichier, String etab, String date, String cuser) throws ParseException {
//        List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(fichier);
//        if (FICTRAITEMENT.size() > 0) {
//            for (int t = 0; t < FICTRAITEMENT.size(); t++) {
//                String formule = FICTRAITEMENT.get(t).get("CALC").toString();
//                System.out.println("FormuleCondition: " + formule);
//                BigDecimal cd = DEFAULT_FOOBAR_VALUE;
//                cd = separeteDataCond(formule, etab, fichier, date);
//                cd = cd.setScale(0, RoundingMode.HALF_UP);
//                ;
//                String sold = cd.toString();
//                saveCalcTmp(etab, FICTRAITEMENT.get(t).get("POST").toString(),
//                        FICTRAITEMENT.get(t).get("COL").toString(), sold, fichier, date,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("RANG").toString()), t, cuser,
//                        Integer.parseInt(FICTRAITEMENT.get(t).get("STATUS").toString()),
//                        FICTRAITEMENT.get(t).get("FIELD").toString(), FICTRAITEMENT.get(t).get("TYPEVAL").toString());
//            }
//        }
//    }
//
////    @Override
//    public List<Long> dismantlePostCalculate1(String s) {
////		String[] n = new String[]{};
//        ArrayList<Long> n = new ArrayList<Long>();
//        s = s.trim().substring(5, s.trim().length() - 1);
//
//        String[] split = s.split(",");
//        List<ReportAttributeS> f = null;
//        for (String e : split) {
//            f = reportAttributeRepository.findByDeleAndAttContaining(false, e);
//            if (!f.isEmpty()) {
//                n.add(f.get(0).getId());
//            }
//        }
//        return n;
//    }
//
////    @Override
//    public List<Long> dismantlePostCalculate2(String s) {
////		String[] n = new String[]{};
//        ArrayList<Long> n = new ArrayList<Long>();
////		s = s.trim().substring(5, s.trim().length() - 1);
//        s = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
//        System.out.print(s + " klllllllllllllllllllllllllllllllllllllllllllllll ");
//        String[] split = s.split(",");
//        List<ReportAttributeS> f = null;
//        for (String e : split) {
//            f = reportAttributeRepository.findByDeleAndAttContaining(false, e.trim());
//            if (!f.isEmpty()) {
//                n.add(f.get(0).getId());
//            }
//        }
//        return n;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<ReportCalculateS> constructfilterquery1(ReportCalculateS filter) {
//
//        return reportCalculateRepository.findAll(new Specification<ReportCalculateS>() {
//
//            @Override
//            public Predicate toPredicate(Root<ReportCalculateS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> predicates = new ArrayList<>();
//
//                predicates.add(cb.equal(root.get("fichi"), filter.getFichi()));
//
//                // If attribute is specified in filter, add contains (lile)
//                // filter to where clause with ignore case
//                if (filter.getField() != null) {
//                    predicates.add(cb.like(cb.lower(root.get("field")), "%" + filter.getField().toLowerCase() + "%"));
//                }
//
//                // If lastName is specified in filter, add contains (lile)
//                // filter to where clause with ignore case
//                if (filter.getPost() != null) {
//                    predicates.add(cb.like(cb.lower(root.get("post")), "%" + filter.getPost().toLowerCase() + "%"));
//                }
//                if (filter.getTysorce() != null) {
//                    predicates
//                            .add(cb.like(cb.lower(root.get("tysorce")), "%" + filter.getTysorce().toLowerCase() + "%"));
//                }
//
//                return cb.and(predicates.toArray(new Predicate[0]));
//            }
//
//        });
//    }
//
//    @SuppressWarnings("null")
//    public Model get_cells(ReportRepSS s) {
//        List<ReportCalculateS> sd = reportCalculateRepository.findbyelm(s.getFichier(), s.getCol(), s.getPost());
//        if (sd.size() > 1) {
//            return null;
//        }
//        if (sd.get(0).getSource() == "T") {
//            String p = sd.get(0).getCalc();
//            String g = "CH";
//            int i = p.indexOf(g);
//            Model e = null, j = null;
//            System.out.println("index:${i}" + i);
//            while (i >= 0) {
////		     to collect Caracter
//                int h = p.indexOf(" ", i + 1);
//                // GET POST
//                String post = p.substring(i + 2, h - 2);
//                System.out.println("post:" + post);
//
//                // get column
//                String col = p.substring(h - 1, h);
//                System.out.println("col:" + col);
//
//                // file
//                String f = sd.get(0).getFichi();
//                // closing date
//                System.out.println("file:" + f);
//
//                java.util.Date d = s.getDar();
//                System.out.println("Date: " + d.toString());
//
//                List<ReportRepSS> p1 = reportRepRepository.findByFichierAndPostAndColAndDar(f, post, col, d);
//                if (p1.size() != 1) {
//                    return null;
//                }
//                // get cell value
//                String r = null;
//                if (p1.get(0).getValc() != null) {
//                    r = p1.get(0).getValc();
//                } else if (p1.get(0).getValm() != null) {
//                    r = p1.get(0).getValm().toString();
//                } else if (p1.get(0).getVald() != null) {
//                    r = p1.get(0).getVald().toString();
//                } else if (p1.get(0).getValt() != null) {
//                    r = p1.get(0).getValt().toString();
//                }
//                // add informations
//                e.addAttribute("post", post);
//                e.addAttribute("col", col);
//                e.addAttribute("val", r);
//                j.addAttribute(i);
//                i = p.indexOf(g, i + 1);
//            }
//            return j;
//
//        } else {// if its not TYPEVAL T
//            return null;
//        }
//
//    }
//
//    public Map<String, Object> getFormularAndSubFields(String fichi, String post, String col, String date) {
//        Map<String, Object> r = new HashMap<String, Object>();
//        Map<String, Object> f = new HashMap<String, Object>();
//        Map<String, Object> g = new HashMap<String, Object>();
//        List<ReportCalculateS> s = reportCalculateRepository.findByFichiAndPostAndCol(fichi, post, col);
//        int i = 0;
//        r.put("obj", s.get(0));
//        // get formular if its sum type
//        if (s.get(0).getSource().equals("T")) {
//            String[] sf = s.get(0).getCalc().split(" ");
//            System.out.println("checking return value");
//            for (String yg : sf) {// cut at points of space
//                if (yg.length() > 2 && yg.substring(0, 2).equals("CH")) {// select only those starting with CH
//                    List<ReportRepSS> o = reportRepRepository.fld(fichi, yg, date);
//                    if (!o.isEmpty()) {
//                        g.put("file", o.get(0).getField());
//                        g.put("post", o.get(0).getPost());
//                        g.put("col", o.get(0).getCol());
//                        g.put("vald", o.get(0).getVald());
//                        g.put("valm", o.get(0).getValm());
//                        g.put("valt", o.get(0).getValt());
//                        g.put("valc", o.get(0).getValc());
//                        f.put(yg, ((HashMap) g).clone());
//                        i++;
//                        g.clear();
//                    } else {
//                    }
//                }
//            }
//        }
//        r.put("obj1", f);
//        return r;
//    }
//
//    public List<Object> getAttributeNotExist(Model re) {
////		Model re = null;
//        re.addAttribute("test4");
//        Map<String, Object> sr = null;
//
//        Map<String, Object> sr2 = new HashMap<String, Object>();
//        List<Object> sr1 = null;
//        List<Object> sr3 = new ArrayList<Object>();
//        List<String> sp = new ArrayList<String>();
//        List<ReportCalculateS> sd = reportCalculateRepository.findallPost();
//        System.out.println("entring size");
//        System.out.println(sd.size());
//
//        for (int m = 0; m < sd.size(); m++) {
//            if (sd.get(m).getCalc().contains("CREDNR")) {
//                continue;
//            }
//            System.out.println("we are at formular number :" + m);
//            System.out.println(sd.get(m).getCalc());
//            String[] s = sd.get(m).getCalc().trim()
//                    .substring(sd.get(m).getPost().trim().length() + 1, sd.get(m).getCalc().trim().indexOf(')'))
//                    .split(",");
//            for (int i = 0; i < s.length; i++) {
//                System.out.println("we are at attribute : " + s[i]);
//                System.out.println("we are at attribute number: " + i);
//                List<ReportAttributeS> ra = reportAttributeRepository.findByAttAndDele(s[i].trim(), false);
//                if (ra.size() == 1 || ra.size() == 0) {// normal
//                    if (ra.size() == 1) {// one
//                        System.out.println(s[i] + "  is existing and is unique");
//                    } else {// not existing
//                        System.out.println(s[i] + "  is not existing");
//                        if (!sp.contains(s[i])) {
//                            System.out.println("first time detecting :" + s[i]);
//                            sp.add(s[i]);
//                            System.out.println("first time detecting :" + s[i]);
//                            sr2.put("fichier", sd.get(m).getFichi());
//                            sr2.put("post", sd.get(m).getPost());
//                            sr2.put("col", sd.get(m).getCol());
//                            sr2.put("formular", sd.get(m).getCalc());
//                            sr2.put("att", s[i]);
//                            sr3.add(((HashMap) sr2).clone());
//                            sr2.clear();
//                        }
//                    }
//                } else {// abnormal : duplicate attribute
//                    System.out.println(s[i] + "  is more than one");
//                    ra.forEach(t -> {
//                        sr.put("att", t.getAtt());
//                        sr.put("id", t.getId());
//                        sr1.add(sr);
//                        sr.clear();
//                    });
//
//                }
//                System.out.println("attribute search complited : " + s[i]);
//            }
//            System.out.println("attribute finished");
//            System.out.println("formular search complited : " + sd.get(m).getCalc());
//        }
//        System.out.println("formular search complitely finished");
////		re.addAttribute("notExiting", sp);
////		re.addAttribute("Duplicate", sr1);
////		re.addAttribute("detailNotExisting", sr3);
////		return re;
////		return sp;
//        return sr3;
//    }
//
//}

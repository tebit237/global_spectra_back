/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.ServiceInterface.ReportCalculateService;
import iwomi.base.ServiceInterface.ReportControleService;
import iwomi.base.objects.ReportAnomalyTmp;
import iwomi.base.objects.SqlFileType;
import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportControleComplex;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.ReportControleQuality;
import iwomi.base.objects.ReportRep;
import iwomi.base.repositories.ReportAnomalyRepository;
import iwomi.base.repositories.ReportAnomalyTmpRepository;
import iwomi.base.repositories.ReportCalculateRepository;
import iwomi.base.repositories.ReportControleComplexRepository;
import iwomi.base.repositories.ReportControleInterRepository;
import iwomi.base.repositories.ReportControleIntraRepository;
import iwomi.base.repositories.ReportControleQualityRepository;
import iwomi.base.repositories.ReportRepRepository;
import iwomi.base.repositories.ReportResultRepository;
import iwomi.base.repositories.SqlFileTypeRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Service
@Component
//@Transactional(readOnly = false)
public class ReportControleServiceImpl extends GlobalService implements ReportControleService {

    @Autowired
    private ReportControleInterRepository reportControleInterRepository;
    @Autowired
    private ReportControleIntraRepository reportControleIntraRepository;

    @Autowired
    private LiveReportingServiceS liveReportingService;
    @Autowired
    private ReportCalculateService reportCalculateService;
    @Autowired
    private ReportControleIntraRepository ReportControleIntraRepository;
    @Autowired
    private ReportControleInterRepository ReportControleInterRepository;
    @Autowired
    private ReportAnomalyRepository ReportAnomalyRepository;
    @Autowired
    private ReportAnomalyTmpRepository ReportAnomalyTmpRepository;
    @Autowired
    ReportCalculateServiceImpl reportCalculateServiceImpl;
    @Autowired
    private ReportResultRepository ReportResultRepository;
    @Autowired
    private ReportControleComplexRepository reportControleComplexRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private ReportControleQualityRepository reportControleQualityRepository;

    private static final BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(0);
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;

    // Operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();

    static {
        // Map<"token", []{precendence, associativity}>
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
    }

    // Test if token is an operator
    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }
    @Autowired
    ReportAnomalyRepository reportAnomalyRepository;
    @Autowired
    ReportRepRepository reportRepRepository;
    @Autowired
    SqlFileTypeRepository sqlFileTypeRepository;
    @Autowired
    ReportCalculateRepository reportCalculateRepository;
//inter correction

    public void autoCorrectInterControl(String date, String etab) {

//        System.out.println("the number of data to verify :" + date);
        List<ReportAnomaly> r = reportAnomalyRepository.findByinterCont(date, "inter");
//        System.out.println("the number of data to verify :" + r.size());
        Map<String, Map<String, String>> yy = null;
        List<String> uy = new ArrayList<>();
        try {
            Connection t = connectDB();
            yy = getType_v1(t.createStatement());
        } catch (Exception ye) {
            yy = null;
            System.out.println("count not get nomenclature 3009");
        }
        for (ReportAnomaly y : r) {
            if (y.getCtrid() == null) {
                continue;
            }
            ReportControleIntra g = reportControleIntraRepository.findById(y.getCtrid());//getting the control of the anomally
            BigDecimal oo = null;
            try {
                oo = getResultIntrav1(g.getCd(), date, etab).subtract(getResultIntrav1(g.getCg(), date, etab));
            } catch (Exception ex) {
//                System.out.println("Error : "+g.getCd() + " : " + g.getCg());
                oo = new BigDecimal(99);
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("the real difference is :" + oo);
            String ji = null;

//            oo = oo < 0 ? -oo : oo;
            if (oo.abs().intValue() <= 2) {
                //get which of the file to act on
                // left
                List<String> e = new ArrayList<>();
                List<String> uy1 = new ArrayList<>();
                for (String o : y.getCd().split("[+/*-]")) {
                    uy1.add(o.trim().replaceAll("\\s+", ""));
                    e.add(o.trim().split("\\:")[0].trim());//file
                    ji = o.trim().split("\\:")[1].trim();//post with colon
                }
                for (String u : y.getCg().split("[+/*-]")) {
                    uy1.add(u.trim().replaceAll("\\s+", ""));
                    e.add(u.trim().split("\\:")[0].trim());//file
                    ji = u.trim().split("\\:")[1].trim();//post with colon
                }
                if (checkAlreadyModifiedField(e, ji.substring(2, ji.lastIndexOf("C")), uy)) {
                    continue;
                }
                int j = 0;
//                1 - 2 = -1
//                round left  1+1 = 2
//        round right 2-1 = 1
                String fi = chooseFileSmallestInteger(e, j);//getting what would be modified with respect to the other
                String post = ji.substring(2, ji.lastIndexOf("C"));
//                getting which is to be selected, left or right
                String su = "g";
                System.out.println("droit :" + g.getCd() + " et gouch" + g.getCg());
                if (g.getCd().contains(fi + ":CH" + post + "C")) {
                    su = "d";
                }
                if (g.getCg().contains(fi + ":CH" + post + "C")) {
                    su = "g";
                }
                System.out.println("for eqn :" + y.getCg() + " " + y.getCd() + ", the file chosen is :" + fi + " and post from :" + post + "  " + date);
                Map<String, String> yo = yy.get(fi);
                if (yy != null && yy.containsKey(fi) && yy.get(fi) != null && yo.get("auto_correct") != null) {
                    switch (yo.get("result")) {
                        case "calculate":
                            updateOthersAutocorrect(fi, post, yo.get("auto_correct").trim(), date, oo, su);
                            //must be updated
                            uy.add(fi + ":" + post);
                            break;
                        case "duplicate":
                        case "duplicateNoPost":
                            ReportRep er1 = reportRepRepository.findByFichierAndPostAndColAndDar1(fi, post, yo.get("auto_correct").trim(), date);
                            er1.setValc(er1.getValc() + oo);
                            reportRepRepository.save(er1);
                            uy.add(fi + ":" + "CH" + post + "C" + yo.get("auto_correct").trim());
                            break;
                        case "sql":
                            SqlFileType s = sqlFileTypeRepository.findById(new Long(post));
                            String kk = s.cellExtra(Integer.parseInt(yo.get("auto_correct").trim()));
                            Integer uuo = Integer.parseInt(kk) + oo.intValue();
                            s = s.cellinsert(Integer.parseInt(yo.get("auto_correct").trim()), uuo.toString());
                            sqlFileTypeRepository.save(s);
                            uy.add(fi + ":" + "CH" + post + "C" + yo.get("auto_correct").trim());
                            break;
                        default:

                    }
                }

            }
        }
    }
// alogrithm to get the first file  F1001 is taken before F1004

    private String chooseFileSmallestInteger(List<String> e, int j) {
        String fi = "";
        for (String f : e) {
            int uu = 0;
            try {
                uu = Integer.parseInt(f.trim().substring(1));
            } catch (Exception rp) {
//                System.out.println("the value of f :" + f);
                uu = 0;
            }
            if (uu >= j) {
                j = uu;
                fi = f;
            }
        }
        return fi;
    }

    private Boolean checkAlreadyModifiedField(List<String> listfile, String post, List<String> used) {
        for (String t : listfile) {
//            System.out.println("checking " + t + ":" + post + " in used " + used);
            if (used.contains(t + ":" + post)) {
                return true;
            }
        }
        return false;
    }

    private void updateOthersAutocorrect(String fi, String post, String yo, String date, BigDecimal oo, String j) {
        try {
            Integer.parseInt(yo);
            ReportRep er = reportRepRepository.findByFichierAndPostAndColAndDar1(fi, post.trim(), yo, date);
            if (er != null) {
                Integer y = 0;
                if (j == "d") {
                    System.out.println("changing " + fi + ":CH" + post + "C" + yo + " from " + er.getValm() + " to " + (er.getValm() - oo.doubleValue()));
                    er.setValm(er.getValm() - oo.doubleValue());
                } else if (j == "g") {
                    System.out.println("changing " + fi + ":CH" + post + "C" + yo + " from " + er.getValm() + " to " + (er.getValm() + oo.doubleValue()));
                    er.setValm(er.getValm() + oo.doubleValue());
                }
                reportRepRepository.save(er);
                for (ReportCalculate h : reportCalculateRepository.findOthers(fi, "%CH" + post.trim() + "C" + yo + "%")) {
                    updateOthersAutocorrect(h.getFichi(), h.getPost(), h.getCol(), date, oo, j);
                }

            }
        } catch (Exception s) {
            System.out.println(fi + " file auto correct column is not set");
        }

    }

    public BigDecimal getResultComplex2(String arguments, String date, String etab, String cod, String type) {
        Map<String, String> ref = getArguments(arguments);
        String s = "";
        System.out.println("it is the code: " + cod);
        System.out.println("it is the query: " + arguments);
        switch (cod) {
            case "01":
                s = getSumBetweenPostRp(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"), date, etab);
                break;
            case "02":
                s = getColWhereCols(ref.get("scol"), ref.get("where"), ref.get("fichier"), date, etab);
                break;
            case "04":
                s = getCols(ref.get("scol"), ref.get("fichier"), date, etab);
                break;
            case "03":
                s = getSumBetweenPostwhereNLk(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"),
                        ref.get("whereNLk"), date, etab);
                break;
            case "05":
                s = getColPostLk(ref.get("fichier"), ref.get("col"), ref.get("whereLk"), date, etab);
                break;
            case "06":
                s = getColWhereColsWhereLk(ref.get("scole"), ref.get("col"), ref.get("where"), ref.get("whereLk"),
                        ref.get("fichier"), date, etab);
                break;
            default:
                s = getResultComplex2(arguments, date, etab);
        }
        System.out.println("this is query generated :" + s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        BigDecimal ee = tryCastnumner(tmp.get(0).get("cpx"));
        if (ee != null) {
            return ee;
        } else {
            return tryCastnumner(tmp.get(0).get("a"));
        }

    }

    public BigDecimal getResultComplex1(String arguments, String date, String etab, String cod) {
        Map<String, String> ref = getArguments(arguments);
        String s = "";
        System.out.println("it is the code: " + cod);
        System.out.println("it is the query: " + arguments);

        switch (cod) {
            case "01":
                s = getSumBetweenPostRp(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"), date, etab);
                break;
            case "02":
                s = getColWhereCols(ref.get("scol"), ref.get("where"), ref.get("fichier"), date, etab);
                break;
            case "04":
                s = getCols(ref.get("scol"), ref.get("fichier"), date, etab);
                break;
            case "03":
                s = getSumBetweenPostwhereNLk(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"),
                        ref.get("whereNLk"), date, etab);
                break;
            case "05":
                s = getColPostLk(ref.get("fichier"), ref.get("col"), ref.get("whereLk"), date, etab);
                break;
            case "06":
                s = getColWhereColsWhereLk(ref.get("scole"), ref.get("col"), ref.get("where"), ref.get("whereLk"),
                        ref.get("fichier"), date, etab);
                break;

            default:
                s = getResultComplex2(arguments, date, etab);
        }
        System.out.println("this is query generated :" + s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        BigDecimal ee = tryCastnumner(tmp.get(0).get("cpx"));
        if (ee != null) {
            return ee;
        } else {
            return tryCastnumner(tmp.get(0).get("a"));
        }

    }

    public BigDecimal getResultComplex3(String arguments, String date, String cod) {
        String etab = "10020";
        Map<String, Map<String, String>> yy = new HashMap<>();
        try {
            reportCalculateService.connec();
            yy = reportCalculateService.getType13();
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

        return getResultComplex4(cod, arguments, date, yy, etab);

    }

    public BigDecimal getResultComplex4(String cod, String arguments, String date, Map<String, Map<String, String>> yy, String etab) throws NumberFormatException, DataAccessException {
        String s = "";
        System.out.println("it is the code: " + cod);
        System.out.println("it is the query: " + arguments);
        Map<String, String> ref = new HashMap<>();
        switch (cod) {
            case "01":
                ref = getArguments(arguments);
                s = getSumBetweenPostRpv1(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "02":
                ref = getArguments(arguments);
                s = getColWhereColsv1(ref.get("col"), ref.get("where"), ref.get("fichier"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "04":
                ref = getArguments(arguments);
                s = getColsv1(ref.get("col"), ref.get("fichier"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "03":
                ref = getArguments(arguments);
                s = getSumBetweenPostwhereNLkv1(ref.get("fichier"), ref.get("post1"), ref.get("post2"), ref.get("col"),
                        ref.get("whereNLk"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "05":
                ref = getArguments(arguments);
                if (ref.containsKey("pr") && ref.get("pr") != null) {
                    System.out.println("initial date :" + date);
                    date = previousMonth(date);
                    System.out.println("final date :" + date);
                }
                s = getColPostLkv1(ref.get("fichier"), ref.get("col"), ref.get("whereLk"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "06":
                ref = getArguments(arguments);
                s = getColWhereColsWhereLkv1(ref.get("col"), ref.get("where"), ref.get("whereLk"),
                        ref.get("fichier"), date, yy.get(ref.get("fichier")).get("typ"), Integer.parseInt(yy.get(ref.get("fichier")).get("ncol")));
                break;
            case "08":
                try {
                    String tt = arguments;
                    Connection sr = connectDB();
                    Statement we = sr.createStatement();
                    for (String t : arguments.split("[-+*/)(]")) {
                        try {
                            ResultSet sp = we.executeQuery("select lib1,lib3 from sanm where tabcd = '3020' and acscd = '" + t.trim() + "' and dele = 0");
                            if (sp.next()) {
                                BigDecimal r = getResultComplex4(sp.getString("lib1").trim(), sp.getString("lib3").trim(), date, yy, etab);
                                sp.close();
                                tt = tt.replace(t, r.toString());
                            } else {
                                System.out.println("code: " + t.trim() + " not found");
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println(ex.getMessage());
                        }
                    }
                    we.close();
                    sr.close();
                    System.out.println("updated formular " + tt + " : value :");
                    return new BigDecimal(eval(tt));
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            case "09":
                String srr = arguments;
                for (String o : extractKey(arguments, '{', '}')) {
                    try {
                        ResultSet sp = reportCalculateService.conac().executeQuery("select lib1,lib3 from sanm where tabcd = '3020' and acscd = '" + o + "' and dele = 0");
                        if (sp.next()) {
                            BigDecimal r = getResultComplex4(sp.getString("lib1").trim(), sp.getString("lib3").trim(), date, yy, etab);
                            sp.close();
                            srr = srr.replace(o, r.toString());
                        } else {
                            System.out.println("code: " + o + " not found");
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            default:
                s = getResultComplex2(arguments, date, etab);
        }
        System.out.println("this is query generated :" + s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        BigDecimal ee = tryCastnumner(tmp.get(0).get("cpx"));
        if (ee != null) {
            return ee;
        } else {
            return tryCastnumner(tmp.get(0).get("a"));
        }
    }

    private String previousMonth(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        try {
            cal.setTime(sdf.parse(date));// all done
        } catch (ParseException ex) {
            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(cal.getTime());
    }

    private List<String> extractKey(String arguments, char openc, char closec) {
        Boolean open = false;
        List<String> r = new ArrayList<>();
        String o = "";
        for (int i = 0; i < arguments.length(); i++) {
            if (arguments.charAt(i) == openc) {
                open = true;
                continue;
            }
            if (arguments.charAt(i) == closec) {
                if (!r.contains(o)) {
                    r.add(o);
                }
                o = "";
                open = false;
                continue;
            }
            if (open) {
                o = o + arguments.charAt(i);
            }
        }
        return r;
    }

    private BigDecimal tryCastnumner(Object r) {

        try {
            return BigDecimal.valueOf((Integer) r);
        } catch (Exception e) {
            try {
                return new BigDecimal((Double) r);
            } catch (Exception g) {
                try {
                    return (BigDecimal) r;
                } catch (Exception t) {
                    return new BigDecimal("0");
                }
            }
        }
    }

    private BigDecimal tryCastnumner_v1(Object r) {
        if (r == null) {
            new BigDecimal("0");
        }
        try {
            return BigDecimal.valueOf((Integer) r);
        } catch (Exception e) {
            try {
                return new BigDecimal((Double) r);
            } catch (Exception g) {
                try {
                    return (BigDecimal) r;
                } catch (Exception t) {
                    return new BigDecimal("0");
                }
            }
        }
    }

    private String getColPostLk(String fichier, String col, String whereLk, String date, String etab) {
        String v = "";
//        System.out.println(whereLk);
        for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
            v = v + "And post like " + w + " ";
        }
//        System.out.println("final output");
//        System.out.println(v);
        return "select  coalesce(SUM(coalesce(valm,0)),0) cpx from rprep where  fichier = '" + fichier + "' " + v
                + " and col= " + col + " and dar = to_date('" + date + "','yyyy-mm-dd') and etab = '" + etab + "' ";
    }

    private String getColPostLkv1(String fichier, String col, String whereLk, String date, String typ, int d) {
        String v = "";
//        System.out.println(whereLk);
        for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
            v = v + (v == "" ? "" : "OR ") + " col1 like '" + w.trim() + "' ";
        }
//        System.out.println("final output");
//        System.out.println(v);
        return "select  coalesce(SUM(coalesce(to_number(col" + col + "),0)),0) cpx from ("
                + subquerytab(typ, date, fichier, d, "")
                + " ) where  fichi = '" + fichier + "' AND (" + v
                + ") and dar = to_date('" + date + "','yyyy-mm-dd')";
    }

    private String getSumBetweenPostwhereNLkv1(String fichier, String post1, String post2, String col, String where,
            String date, String ty, int e) {
        String v = "";
        for (String w : where.substring(1, where.length() - 1).split(",")) {
            v = v + "And col1 NOT like '" + w.trim() + "' ";
        }
        String s = getSumBetweenPostRpv1(fichier, post1, post2, col, date, ty, e) + v;
//        System.out.println(s);
        return s;
    }

    private String getSumBetweenPostwhereNLk(String fichier, String post1, String post2, String col, String where,
            String date, String etab) {
        String v = "";
        for (String w : where.substring(1, where.length() - 1).split(",")) {
            v = v + "And post NOT like " + w + " ";
        }
        return "SELECT coalesce(SUM(coalesce(valm,0)),0) cpx FROM rprep WHERE col= " + col + " " + v
                + "  and dar = to_date('" + date + "','yyyy-mm-dd') and etab = '" + etab + "' and fichier = '" + fichier
                + "' and rang between (select distinct rang from rppfich where poste ='" + post1 + "' and fich = '"
                + fichier + "') and (select distinct rang from rppfich where poste ='" + post2 + "' and fich = '"
                + fichier + "')";
    }

    private String getColWhereCols(String scol, String where, String fichier, String date, String etab) {
        String[] s = where.substring(1, where.length() - 1).split(":");// since there is only one
        return "select coalesce(sum(coalesce(to_number(" + scol + "),0)),0) cpx from sqltype where  " + s[0].trim()
                + " = '" + s[1].trim() + "' and fichi = '" + fichier + "' and dar =to_date('" + date
                + "','yyyy-mm-dd') and etab = '" + etab + "'";
    }

    private String getColWhereColsv1(String scol, String where, String fichier, String date, String typ, int d) {
        String[] s = where.trim().substring(1, where.length() - 1).split(":");

        String se = "select coalesce(sum(coalesce(to_number(col" + scol.trim() + "),0)),0) cpx "
                + "from (" + subquerytab(typ, date, fichier, d, "") + ") where  " + s[0].trim()
                + " = '" + s[1].trim() + "' and fichi = '" + fichier.trim() + "' and dar =to_date('" + date
                + "','yyyy-mm-dd') ";
//        System.out.println(se);
        return se;
    }

    private List<Integer> WeekendMonthnull(String t) {//yyyy-mm-dd
        List<Integer> str = new ArrayList<>();
        int year = Integer.parseInt(t.substring(0, 4));
        int month = Integer.parseInt(t.substring(5, 7)) - 1;
        System.out.println("the year is :" + year + " and the month is :" + month);
        Calendar cal = new GregorianCalendar(year, month, 1);

//        int day = cal.getActualMaximum(Calendar.DATE);// cal.get(Calendar.DAY_OF_WEEK);
        do {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                System.out.println("the weekend day:" + cal.get(Calendar.DAY_OF_MONTH));
                str.add(cal.get(Calendar.DAY_OF_MONTH));
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == month);
        return str;
    }

    //on sqltype table
    private String getColWhereColsWhereLk(String scole, String col, String where, String whereLk, String fichier,
            String date, String etab) {
        String v = "";
        String[] s = where.substring(1, where.length() - 1).split(":");// since there is only one
        for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
            String[] sp = w.split(":");
            v = v + "And " + sp[0].trim() + " like " + sp[1].trim() + " ";
        }
        return "select coalesce(sum(coalesce(to_number(" + scole + "),0)),0) cpx from sqltype where  " + s[0].trim()
                + " = '" + s[1].trim() + "' and fichi = '" + fichier + "' " + v + "and dar =to_date('" + date
                + "','yyyy-mm-dd') and etab = '" + etab + "'";
    }

    //on sqltype table
    private String getColWhereColsWhereLkv1(String col, String where, String whereLk, String fichier,
            String date, String ty, int d) {
        String v = "";
        String[] s = where.substring(1, where.length() - 1).split(":");// since there is only one
        for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
            String[] sp = w.split(":");
            v = v + "And " + sp[0].trim() + " like '" + sp[1].trim() + "' ";
        }
        String ss = "select coalesce(sum(coalesce(to_number(col" + col + "),0)),0) cpx from ("
                + subquerytab(ty, date, fichier, d, " ")
                + ") where  " + s[0].trim()
                + " = '" + s[1].trim() + "' and fichi = '" + fichier + "' " + v + " and dar =to_date('" + date
                + "','yyyy-mm-dd') ";
//        System.out.println(ss);
        return ss;
    }

    //on rprep table
    private String getColWhereColsWhereLkRprep(String scole, String col, String where, String whereLk, String fichier,
            String date, String etab) {
        String v = "";
        String[] s = where.substring(1, where.length() - 1).split(":");// since there is only one
        for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
            String[] sp = w.split(":");
            v = v + "And " + sp[0].trim() + " like " + sp[1].trim() + " ";
        }
        return "select coalesce(sum(coalesce(to_number(" + scole + "),0)),0) cpx from sqltype where  " + s[0].trim()
                + " = '" + s[1].trim() + "' and fichi = '" + fichier + "' " + v + "and dar =to_date('" + date
                + "','yyyy-mm-dd') and etab = '" + etab + "'";
    }

    private String getCols(String scol, String fichier, String date, String etab) {

        return "select coalesce(sum(coalesce(to_number(" + scol + "),0)),0) as cpx from sqltype where fichi = '"
                + fichier + "' and dar =to_date('" + date + "','yyyy-mm-dd') and etab = '" + etab + "'";
    }

    private String getColsv1(String col, String fichier, String date, String ty, int c) {

        return "select coalesce(sum(coalesce(to_number(col" + col.trim() + "),0)),0) as cpx from ("
                + subquerytab(ty, date, fichier, c, "")
                + ") where fichi = '"
                + fichier + "' and dar =to_date('" + date.trim() + "','yyyy-mm-dd')";
    }

    private String getSumBetweenPostRp(String fichier, String post1, String post2, String col, String date,
            String etab) {

        return "SELECT coalesce(SUM(coalesce(valm,0)),0) cpx FROM rprep WHERE col= " + col + " and" + " dar = to_date('"
                + date + "','yyyy-mm-dd') and etab = '" + etab + "' and fichier = '" + fichier + "'"
                + " and rang between (select distinct rang from rppfich where poste ='" + post1 + "' and fich = '"
                + fichier + "')" + " and (select distinct rang from rppfich where poste ='" + post2 + "' and fich = '"
                + fichier + "')" + "";
    }

    private String getSumBetweenPostRpv1(String fichier, String post1, String post2, String col, String date,
            String ty, int colm) {
        Object e1 = new Object();
        Object e2 = new Object();
        try {
            e1 = jdbcTemplate.queryForMap(subquerytab(ty, date, fichier, colm, " and poste = '" + post1.trim() + "'")).get("rang");
            e2 = jdbcTemplate.queryForMap(subquerytab(ty, date, fichier, colm, " and poste = '" + post2.trim() + "'")).get("rang");
        } catch (EmptyResultDataAccessException e) {
            e1 = 0;
            e2 = 0;
        }
        System.out.println(" the range are " + e1 + " - " + e2);
        String se = " select coalesce(SUM(to_number(col" + col.trim() + ")),0) cpx FROM ("
                + subquerytab(ty, date, fichier, colm, " ")
                + ") where (rang between " + e1 + " and " + e2 + ")";
//        System.out.println(se);
        return se;
    }
//sum along a column and ligne

    private String getSumBetweenPostAllcol(String fichier, String post1, String post2, String col, String date,
            String ty, int colm, String sumcolons) {
        Object e1 = new Object();
        Object e2 = new Object();
        try {
            e1 = jdbcTemplate.queryForMap(subquerytab(ty, date, fichier, colm, " and poste = '" + post1.trim() + "'")).get("rang");
            e2 = jdbcTemplate.queryForMap(subquerytab(ty, date, fichier, colm, " and poste = '" + post2.trim() + "'")).get("rang");
        } catch (EmptyResultDataAccessException e) {
            e1 = 0;
            e2 = 0;
        }
        System.out.println(" the range are " + e1 + " - " + e2);
        String eg = "";
        if (sumcolons != null) {
            for (String w : sumcolons.trim().substring(1, sumcolons.trim().length() - 1).split(",")) {
                eg += (eg == "" ? "" : "+") + "coalesce(SUM(to_number(col" + w.trim() + ")),0)";
            }
        } else {
            for (int i = 1; i <= colm; i++) {
                eg += (eg == "" ? "" : "+") + "coalesce(SUM(to_number(col" + col.trim() + ")),0)";
            }
        }
        String se = " select sum(" + eg + ") cpx FROM ("
                + subquerytab(ty, date, fichier, colm, " ")
                + ") where (rang between " + e1 + " and " + e2 + ")";
        System.out.println(se);
        return se;
    }

    private Map<String, String> getArguments(String arg) {
        Map<String, String> ret = new HashMap<String, String>();
        for (String w : arg.split(";")) {
            String[] s = w.split("=");
            ret.put(s[0].trim(), s[1].trim());
        }
        return ret;
    }

    private Map<String, String> getArgumentsv1(String arg) {
        Map<String, String> ret = new HashMap<String, String>();
        for (String r : arg.split("[-+*/]")) {
            for (String w : r.split(";")) {
                String[] s = w.split("=");
                ret.put(s[0].trim(), s[1].trim());
            }
        }
        return ret;
    }

    public Double getResultComplex(String sql, String date, String etab) {
        String s = sql.replaceAll("//date//", "to_date('" + date + "','yyyy-mm-dd')").replaceAll("//etab//", etab);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        return (Double) tmp.get(0).get("cpx");
    }

    public String getResultComplex2(String sql, String date, String etab) {
        return sql.replaceAll("//date//", "to_date('" + date + "','yyyy-mm-dd')").replaceAll("//etab//", etab);
    }

    public BigDecimal getResultQuality(String sql, String date, String etab) {
        String s = sql.replaceAll("//date//", "to_date('" + date + "','yyyy-mm-dd')").replaceAll("//etab//", etab);
        System.out.println(s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        System.out.println(tmp);
        return tryCastnumner(tmp.get(0).get("cpx"));
    }

    public BigDecimal getResultQualityV1(String argument, String date, String etab, Map<String, Map<String, String>> yy) {
        String[] y = argument.split("\\:");
        String s = y[1].replaceAll("//date//", "to_date('" + date + "','yyyy-mm-dd')").replaceAll("//etab//", etab)
                .replaceAll("//fichier//", "(" + subquerytab(yy.get(y[0].trim()).get("typ"), date, y[0].trim(), Integer.parseInt(yy.get(y[0].trim()).get("ncol")), "") + ")");
        System.out.println("Query :" + s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        System.out.println(tmp);
        return tryCastnumner(tmp.get(0).get("cpx"));
    }

    public BigDecimal getResultparameter(String acscd, String query, String date, String etab) {
        String s = "";
        switch (acscd) {
            case "0001":
                for (Integer ts : WeekendMonthnull(date)) {
                    s += (s == "") ? (" (col = " + (ts + 1) + " and (valc is not null ))") : (" or (col = " + (ts + 1) + "  and (valc is not null ))");
                }
                s = "select count(*) as cpx from rprep where  fichier = 'F2001' and  (" + s + ") and dar = to_date('" + date + "','yyyy-mm-dd')";
                break;
            case "0002":
                s = "select count(*) as cpx from rprep where ((col = 31 and valc = null) or (col = 32 and valc =null)) and dar = to_date('" + date + "','yyyy-mm-dd')";
                break;
            default:
                s = query.replaceAll("//date//", "to_date('" + date + "','yyyy-mm-dd')").replaceAll("//etab//", etab);//"select sum(valc) as cpx from rprep where fichier = 'F2001' and rang = (select rang from rprep where fichier = 'F2001' and valc = 'A3001'and dar = to_date('"+date+"','yyyy-mm-dd')) and col!=1 and dar = to_date('"+date+"','yyyy-mm-dd')";
        }
        System.out.println(s);
        List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
        System.out.println(tmp);
        if (tmp.get(0).get("cpx") == null) {
            return new BigDecimal("0");
        }
        return tryCastnumner(tmp.get(0).get("cpx")).abs();
    }

    @Override
    public void Controle(String date, String etab, String cuser) {
        String sql = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);
        List<ReportControleInter> listInter = (List<ReportControleInter>) ReportControleInterRepository.findAll();
        BigDecimal cd = DEFAULT_FOOBAR_VALUE;
        BigDecimal cg = DEFAULT_FOOBAR_VALUE;
        Boolean val = false;
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
        for (ReportControleInter s : listInter) {
            Map<String, String> ps = new HashMap<String, String>();
            try {
                ps = reportCalculateService.getType11(s.getFich());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (ps.get("result").equals("calculate") || ps.get("result").equals("duplicate")) {
                try {
                    cd = getResultInter(s.getCd(), s.getFich(), date, etab);
                    System.out.println("le resultat gouche: " + cd);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    cg = getResultInter(s.getCg(), s.getFich(), date, etab);
                    System.out.println("le resultat droit: " + cg);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (ps.get("result").equals("sql")) {
                String cdcol = s.getCd().trim().replaceAll("C", "col");
                String cgcol = s.getCg().trim().replaceAll("C", "col");
                String sql2 = "";
                if (s.getCd().substring(0, 2).equalsIgnoreCase("IF")) {
//					String zz = s.getCd().trim();
                    String zz = cdcol;
                    String cond = zz.substring(3, zz.indexOf(')'));
                    String[] se = zz.split(":");
                    String trueCond = se[1].trim().substring(0, se[1].trim().indexOf(";"));
                    String falseCond = se[2].trim().substring(0, se[2].trim().indexOf(";"));
                    String min = "";
                    if (trueCond.contains("MIN")) {
                        String firstArg = trueCond.trim().substring(trueCond.trim().indexOf("(") + 1,
                                trueCond.trim().indexOf(","));
                        String secondArg = trueCond.trim().substring(trueCond.trim().indexOf(",") + 1,
                                trueCond.trim().indexOf(")"));
                        min = "case when(" + firstArg + "<" + secondArg + ")THEN " + secondArg + " else " + secondArg
                                + " end";
                    }
                    cdcol = "(case when(" + cond + ")then (" + min + ") else " + falseCond + " end)";
                } else if (s.getCg().substring(0, 2).equalsIgnoreCase("IF")) {
//					String zz = s.getCg().trim();
                    String zz = cgcol;
                    String cond = zz.substring(3, zz.indexOf(')'));
                    String[] se = zz.split(":");
                    String trueCond = se[1].trim().substring(0, se[1].trim().indexOf(";"));
                    String falseCond = se[2].trim().substring(0, se[2].trim().indexOf(";"));
                    String min = "";
                    if (trueCond.contains("MIN")) {
                        String firstArg = trueCond.trim().substring(trueCond.trim().indexOf("(") + 1,
                                trueCond.trim().indexOf(","));
                        String secondArg = trueCond.trim().substring(trueCond.trim().indexOf(",") + 1,
                                trueCond.trim().indexOf(")"));
                        min = "case when(" + firstArg + "<" + secondArg + ")THEN " + secondArg + " else " + secondArg
                                + " end";
                    }
                    cgcol = "(case when(" + cond + ")then (" + min + ") else " + falseCond + " end);";
                }
                sql2 = "select * from sqltype where fichi = '" + s.getFich() + "' and dar = to_date('" + date
                        + "','yyyy-mm-dd')" + " and " + cdcol + " = " + cgcol;
                List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
                if (result.size() > 0) {
                    cg = BigDecimal.valueOf(result.size());
                    cd = new BigDecimal(0);
                }
            }

            if (s.getSign().equalsIgnoreCase("<")) {
                int res;
                res = cg.compareTo(cd);
                val = (res < 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "inter", s.getFich(), s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else if (s.getSign().equalsIgnoreCase(">")) {
                int res;
                res = cg.compareTo(cd);
                val = (res > 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "inter", s.getFich(), s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else if (s.getSign().equalsIgnoreCase("!=")) {
                int res;
                res = cg.compareTo(cd);
                val = (res > 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "inter", s.getFich(), s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else {
                int res;
                res = cg.compareTo(cd);
                val = (res == 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "inter", s.getFich(), s.getSign());

                    ReportAnomalyTmpRepository.save(repo);
                }
            }
        }
        List<ReportControleIntra> listIntra = (List<ReportControleIntra>) ReportControleIntraRepository.findAll();
        for (ReportControleIntra s : listIntra) {

            try {
                cd = getResultIntra(s.getCd(), date, etab);
            } catch (ParseException ex) {
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                cg = getResultIntra(s.getCg(), date, etab);
            } catch (ParseException ex) {
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
//         if((s.getSign().equalsIgnoreCase("=") && (!cd.equals(cg))) || ((s.getSign().equalsIgnoreCase("!=")) && (cd.equals(cg)))){
//             ReportAnomaly repo = new ReportAnomaly(etab,date,cg.toString(),cd.toString(),cuser,null,s.getCg().toString(),s.getCd().toString(),"intra",null);
//             ReportAnomalyRepository.save(repo);
//         }
            if (s.getSign().equalsIgnoreCase("<")) {
                int res;
                res = cg.compareTo(cd);
                val = (res < 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "intra", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else if (s.getSign().equalsIgnoreCase(">")) {
                int res;
                res = cg.compareTo(cd);
                val = (res > 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "intra", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else if (s.getSign().equalsIgnoreCase("!=")) {
                int res;
                res = cg.compareTo(cd);
                val = (res > 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "intra", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            } else {
                int res;
                res = cg.compareTo(cd);
                val = (res == 0);
                if (!val) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                            Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
                            "intra", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            }
        }
        List<ReportControleQuality> listQuality = reportControleQualityRepository.getQualityControls("0");
        for (ReportControleQuality s : listQuality) {
            BigDecimal cx1 = getResultQuality(s.getQrycot(), date, etab);
            System.out.println(cx1 + " sdfsdfsdf");
            if (cx1.doubleValue() != 0) {
                System.out.println("test");
                ReportAnomalyTmp repo = null;
                repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
                        s.getQrycot().toString(), "0", "Quality", null, "=");
                ReportAnomalyTmpRepository.save(repo);
            }

        }

        List<ReportControleComplex> listComplex = reportControleComplexRepository.getComplexControls("0");
        BigDecimal cx1 = new BigDecimal("0.0");
        BigDecimal cx2 = new BigDecimal("0.0");
        for (ReportControleComplex s : listComplex) {
            if (s.getSign().equalsIgnoreCase("N")) {// compering n and n-1 period
                List<Map<String, Object>> tmpe = jdbcTemplate
                        .queryForList("select distinct dar from rprep order by dar desc");
                if (tmpe.isEmpty()) {// no preceding
                    System.out.println("No precident value");
                    continue;
                } else {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    cx1 = getResultComplex1(s.getCd(), formatter.format(tmpe.get(0).get("dar")), etab, s.getTypd());
                    System.out.println(" output of left query :" + cx1);
                    cx2 = getResultComplex1(s.getCg(), date, etab, s.getTypg());
                    System.out.println(" output of right query :" + cx2);
                }

            } else {
                cx1 = getResultComplex1(s.getCd(), date, etab, s.getTypd());
                System.out.println(" output of left query :" + cx1);
                cx2 = getResultComplex1(s.getCg(), date, etab, s.getTypg());
                System.out.println(" output of right query :" + cx2);
            }

            if (s.getSign().equalsIgnoreCase("<") && (cx1.doubleValue() >= cx2.doubleValue())) {
                System.out.println("test");
                ReportAnomalyTmp repo = null;
                repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
                        s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                ReportAnomalyTmpRepository.save(repo);
            } else if (s.getSign().equalsIgnoreCase(">") && (cx1.doubleValue() <= cx2.doubleValue())) {
                System.out.println("test");
                ReportAnomalyTmp repo = null;
                repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
                        s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                ReportAnomalyTmpRepository.save(repo);
            } else if (s.getSign().equalsIgnoreCase("!=") && (cx1 == cx2)) {
                System.out.println("test");
                ReportAnomalyTmp repo = null;
                repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
                        s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                ReportAnomalyTmpRepository.save(repo);
            } else {
                if (cx1 != cx2) {
                    System.out.println("test");
                    System.out.println(cx1);
                    System.out.println(cx2);
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
                            null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                }
            }
        }

        sql = "insert into  rpanom ( crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg) select  crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);
        String sql1 = "delete from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql1);

    }

    @Override
    public void Controle1(String date, String etab, String cuser, List<String> controle_typ) {
        String sql = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);

        BigDecimal cd = DEFAULT_FOOBAR_VALUE;
        BigDecimal cg = DEFAULT_FOOBAR_VALUE;
        Boolean val = false;
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

        if (controle_typ.contains("intra")) {
            List<ReportControleInter> listInter = (List<ReportControleInter>) ReportControleInterRepository.findAll();
            for (ReportControleInter s : listInter) {
                Map<String, String> ps = new HashMap<String, String>();
                try {
                    ps = reportCalculateService.getType11(s.getFich());
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (ps.get("result").equals("calculate") || ps.get("result").equals("duplicate")) {
                    try {
                        cd = getResultInter(s.getCd(), s.getFich(), date, etab);
                        System.out.println("le resultat gouche: " + cd);
                    } catch (ParseException ex) {
                        Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        cg = getResultInter(s.getCg(), s.getFich(), date, etab);
                        System.out.println("le resultat droit: " + cg);
                    } catch (ParseException ex) {
                        Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (ps.get("result").equals("sql")) {
                    String cdcol = s.getCd().trim().replaceAll("C", "col");
                    String cgcol = s.getCg().trim().replaceAll("C", "col");
                    String sql2 = "";
                    if (s.getCd().substring(0, 2).equalsIgnoreCase("IF")) {
//						String zz = s.getCd().trim();
                        String zz = cdcol;
                        String cond = zz.substring(3, zz.indexOf(')'));
                        String[] se = zz.split(":");
                        String trueCond = se[1].trim().substring(0, se[1].trim().indexOf(";"));
                        String falseCond = se[2].trim().substring(0, se[2].trim().indexOf(";"));
                        String min = "";
                        if (trueCond.contains("MIN")) {
                            String firstArg = trueCond.trim().substring(trueCond.trim().indexOf("(") + 1,
                                    trueCond.trim().indexOf(","));
                            String secondArg = trueCond.trim().substring(trueCond.trim().indexOf(",") + 1,
                                    trueCond.trim().indexOf(")"));
                            min = "case when(" + firstArg + "<" + secondArg + ")THEN " + secondArg + " else "
                                    + secondArg + " end";
                        }
                        cdcol = "(case when(" + cond + ")then (" + min + ") else " + falseCond + " end)";
                    } else if (s.getCg().substring(0, 2).equalsIgnoreCase("IF")) {
//						String zz = s.getCg().trim();
                        String zz = cgcol;
                        String cond = zz.substring(3, zz.indexOf(')'));
                        String[] se = zz.split(":");
                        String trueCond = se[1].trim().substring(0, se[1].trim().indexOf(";"));
                        String falseCond = se[2].trim().substring(0, se[2].trim().indexOf(";"));
                        String min = "";
                        if (trueCond.contains("MIN")) {
                            String firstArg = trueCond.trim().substring(trueCond.trim().indexOf("(") + 1,
                                    trueCond.trim().indexOf(","));
                            String secondArg = trueCond.trim().substring(trueCond.trim().indexOf(",") + 1,
                                    trueCond.trim().indexOf(")"));
                            min = "case when(" + firstArg + "<" + secondArg + ")THEN " + firstArg + " else " + secondArg
                                    + " end";
                        }
                        cgcol = "(case when(" + cond + ")then (" + min + ") else " + falseCond + " end);";
                    }
                    sql2 = "select * from sqltype where fichi = '" + s.getFich() + "' and dar = to_date('" + date
                            + "','yyyy-mm-dd')" + " and " + cdcol + " = " + cgcol;
                    System.out.println(sql2);
                    List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
                    cg = BigDecimal.valueOf(result.size());
                    List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from sqltype where fichi = '"
                            + s.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd')");
                    cd = new BigDecimal(r.size());

                }

                if (s.getSign().equalsIgnoreCase("<")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res < 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", s.getFich(), s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase(">")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", s.getFich(), s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase("!=")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", s.getFich(), s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res == 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", s.getFich(), s.getSign());

                        ReportAnomalyTmpRepository.save(repo);
                    }
                }
            }
        }

        if (controle_typ.contains("inter")) {
            List<ReportControleIntra> listIntra = (List<ReportControleIntra>) ReportControleIntraRepository.findAll();
            for (ReportControleIntra s : listIntra) {

                try {
                    cd = getResultIntra(s.getCd(), date, etab);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    cg = getResultIntra(s.getCg(), date, etab);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
//	         if((s.getSign().equalsIgnoreCase("=") && (!cd.equals(cg))) || ((s.getSign().equalsIgnoreCase("!=")) && (cd.equals(cg)))){
//	             ReportAnomaly repo = new ReportAnomaly(etab,date,cg.toString(),cd.toString(),cuser,null,s.getCg().toString(),s.getCd().toString(),"intra",null);
//	             ReportAnomalyRepository.save(repo);
//	         }
                if (s.getSign().equalsIgnoreCase("<")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res < 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "intra", null, s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase(">")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "intra", null, s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase("!=")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "intra", null, s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res == 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "intra", null, s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                }
            }
        }
        if (controle_typ.contains("QLITE")) {
            List<ReportControleQuality> listQuality = reportControleQualityRepository.getQualityControls("0");
            for (ReportControleQuality s : listQuality) {
                BigDecimal cx1 = getResultQuality(s.getQrycot(), date, etab);
                System.out.println(cx1 + " sdfsdfsdf");
                if (cx1.doubleValue() != 0) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
                            s.getQrycot().toString(), "0", "Quality", null, "=");
                    ReportAnomalyTmpRepository.save(repo);
                }

            }
        }
        if (controle_typ.contains("CMPX")) {
            List<ReportControleComplex> listComplex = reportControleComplexRepository.getComplexControls("0");
            BigDecimal cx1 = new BigDecimal("0.0");
            BigDecimal cx2 = new BigDecimal("0.0");
            for (ReportControleComplex s : listComplex) {
                if (s.getSign().equalsIgnoreCase("N")) {// compering n and n-1 period
                    List<Map<String, Object>> tmpe = jdbcTemplate
                            .queryForList("select distinct dar from rprep order by dar desc");
                    if (tmpe.isEmpty()) {// no preceding
                        System.out.println("No precident value");
                        continue;
                    } else {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        cx1 = getResultComplex1(s.getCd(), formatter.format(tmpe.get(0).get("dar")), etab, s.getTypd());
                        System.out.println(" output of left query :" + cx1);
                        cx2 = getResultComplex1(s.getCg(), date, etab, s.getTypg());
                        System.out.println(" output of right query :" + cx2);
                    }

                } else {
                    cx1 = getResultComplex1(s.getCd(), date, etab, s.getTypd());
                    System.out.println(" output of left query :" + cx1);
                    cx2 = getResultComplex1(s.getCg(), date, etab, s.getTypg());
                    System.out.println(" output of right query :" + cx2);
                }

                if (s.getSign().equalsIgnoreCase("<") && (cx1.doubleValue() >= cx2.doubleValue())) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
                            null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                } else if (s.getSign().equalsIgnoreCase(">") && (cx1.doubleValue() <= cx2.doubleValue())) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
                            null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                } else if (s.getSign().equalsIgnoreCase("!=") && (cx1 == cx2)) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
                            null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                    ReportAnomalyTmpRepository.save(repo);
                } else {
                    if (cx1 != cx2) {
                        System.out.println("test");
                        System.out.println(cx1);
                        System.out.println(cx2);
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
                                cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
                        ReportAnomalyTmpRepository.save(repo);
                    }
                }
            }
        }
        sql = "insert into  rpanom ( crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg) select  crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql);
        String sql1 = "delete from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
        jdbcTemplate.execute(sql1);

    }

    @Override
    public String subquerytab(String ty, String date, String f, int colm, String cond) {
        date = "to_date('" + date + "','yyyy-mm-dd')";
        String r = "";
        switch (ty) {
            case "calculate":
                r = "SELECT fich FICHI,poste COL1," + date + " DAR,RANG,";
                for (int i = 2; i <= colm; i++) {
                    r += "(SELECT TO_CHAR(case when A.VALM=999999999999999 then 0 else a.valm end) FROM RPREP A WHERE TO_CHAR(A.COL)='" + i + "' AND A.FICHIER = fich AND A.POST = poste AND A.DAR = " + date + ") col" + i + (i != colm ? "," : "");
                }
                r += " FROM RPPFICH WHERE FICH = '" + f.trim() + "' " + cond.trim() + " GROUP BY FICH,POSTE,RANG ORDER BY FICH,RANG";
                break;
            case "duplicate":
                r = "SELECT b.FICHIER FICHI,b.RANG,b.DAR,";
                for (int i = 1; i <= colm; i++) {
                    r += "(SELECT VALC FROM RPREP a WHERE a.COL=" + i + " AND a.FICHIER = b.FICHIER AND a.RANG = b.RANG AND a.DAR = b.DAR) col" + i + (i != colm ? "," : " ");
                }
                r += "FROM RPREP b WHERE b.FICHIER = '" + f + "' AND b.DAR = " + date.trim() + " " + cond + " GROUP BY b.FICHIER,b.RANG,b.dar ORDER BY b.FICHIER,b.RANG";
                break;
            case "duplicateNoPost":
                r = "SELECT fich FICHI,poste COL1," + date + " dar,RANG,";
                for (int i = 2; i <= colm; i++) {
                    r += "(SELECT VALC FROM RPREP A WHERE TO_CHAR(A.COL)='2' AND A.FICHIER = fich AND A.POST = poste AND A.DAR = " + date.trim() + ") col" + i + (i != colm ? "," : " ");
                }
//               eg cond : poste = 'Z002'
                r += "FROM RPPFICH WHERE FICH = '" + f + "' " + cond + " GROUP BY FICH,POSTE,RANG ORDER BY FICH,RANG";
                break;
            case "sql":
                r = "SELECT  FICHI," + date.trim() + " dar,id as rang,";
                for (int i = 1; i <= colm; i++) {
                    r += " col" + i + (i != colm ? "," : " ");
                }
                r += "FROM sqltype WHERE FICHi = '" + f + "' and dar = " + date.trim() + " " + cond.trim() + " order by id ";
                break;
        }
//        System.out.println(r);
        return r;
    }

    public class IntraThreadTeatment implements Runnable {

        String controle_type;
        String date;
        Long idOpe;
        String etab;
        String ssd;
        String cuser;
        Map<String, Map<String, String>> yy;
        List<ReportControleInter> s2;
        ScriptEngine u;
        String app_s;//1 sesam 2 spectra

        public IntraThreadTeatment(List<ReportControleInter> s2, ScriptEngine u, String date, Long idOpe, String etab, String ssd, Map<String, Map<String, String>> yy, String s) {
            this.controle_type = controle_type;
            this.date = date;
            this.idOpe = idOpe;
            this.etab = etab;
            this.ssd = ssd;
            this.yy = yy;
            this.cuser = "000";
            this.s2 = s2;
            this.u = u;
            this.app_s = s;
        }

        private List<ReportControleInter> ConvertListIntra(Map<String, String> ps, String date, ReportControleInter rii) {
            List<Map<String, Object>> tmp = null;
            ReportControleInter r = rii;
            List<ReportControleInter> pp = new ArrayList<>();
            for (String se : r.getCd().split("\\+")) {
                if (se.trim().substring(0, 2).equalsIgnoreCase("CH")) {
                    pp.add(r);
                    return pp;
                }
            }
            String ww = "";
            if (r.getFich().trim().length() > 5 && app_s.equalsIgnoreCase("2")) { //F1001(F010-T630)
                ww = r.getFich();
                String post1 = r.getFich().trim().substring(6, 10);
                String post2 = r.getFich().trim().substring(11, 15);
                r.setFich(r.getFich().substring(0, 5));
                if (ps.get("result").equals("calculate") || ps.get("result").equals("duplicateNoPost")) {
                    tmp = jdbcTemplate.queryForList("select poste post from rppfich where fich ='" + r.getFich().trim() + "' and col = 2  and "
                            + "rang between (select rang from rppfich where fich ='" + r.getFich().trim() + "' and poste ='" + post1 + "' and col = 2) and "
                            + "(select rang from rppfich where fich ='" + r.getFich().trim() + "' and poste ='" + post2 + "' and col = 2) order by rang");
                } else if (ps.get("result").equals("sql")) {
                    tmp = jdbcTemplate.queryForList("select col1 post from sqltype where fichi ='" + r.getFich().trim() + "'  and "
                            + "id between (select id from sqltype where fichi ='" + r.getFich().trim() + "' and col1 ='" + post1 + "' ) and "
                            + "(select id from sqltype where fichi ='" + r.getFich().trim() + "' and col1 ='" + post2 + "' ) ");
                } else if (ps.get("result").equals("duplicate")) {//input do not have post, only rang
                    tmp = jdbcTemplate.queryForList("select col1 post from sqltype where fichi ='" + r.getFich().trim() + "'  and "
                            + "id between (select id from sqltype where fichi ='" + r.getFich().trim() + "' and col1 ='" + post1 + "' ) and "
                            + "(select id from sqltype where fichi ='" + r.getFich().trim() + "' and col1 ='" + post2 + "' ) order by rang");
                }
                String sg = "";
                String sd = "";
                for (Map<String, Object> re : tmp) {
                    sg += (sg == "" ? "" : "+") + r.getCg().replaceAll("C", "CH" + re.get("POST") + "C");
                    sd += (sd == "" ? "" : "+") + r.getCd().replaceAll("C", "CH" + re.get("POST") + "C");
                }

                pp.add(new ReportControleInter(
                        r.getFich(), sg,
                        r.getTyp(), r.getSign(),
                        sd));

            } else {
                ww = r.getFich();
                if (ps.get("result").equals("calculate") || ps.get("result").equals("duplicateNoPost")) {
                    tmp = jdbcTemplate.queryForList("select poste post from rppfich where fich ='" + r.getFich().trim() + "' and col = 2 order by rang");
                } else if (ps.get("result").equals("duplicate")) {
                    tmp = jdbcTemplate.queryForList("select post from rprep where fichier ='" + r.getFich() + "' and col = 1  and dar= to_date('" + date + "','yyyy-mm-dd')order by rang");
                } else if (ps.get("result").equals("sql")) {
                    if (ps.get("postlike_query") != null && ps.get("postlike_query").equals("1")) {
                        tmp = jdbcTemplate.queryForList("select col1 post from sqltype where fichi = '" + r.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd') order by id");
                    } else {//since it is not post like elements, we use id to reference them
                        tmp = jdbcTemplate.queryForList("select id post from sqltype where fichi = '" + r.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd') order by id");
                    }
                }
                for (Map<String, Object> re : tmp) {
                    pp.add(new ReportControleInter(
                            r.getFich(), r.getCg().replaceAll("C", "CH" + re.get("POST") + "C"),
                            r.getTyp(), r.getSign(),
                            r.getCd().replaceAll("C", "CH" + re.get("POST") + "C")));
                }

            }
            System.out.println(pp);
            return pp;
        }

        @Override
        public void run() {
            threadIntraControlTreatment();
            System.out.println("treatment done");
            System.gc();
        }

        private void threadIntraControlTreatment() throws NumberFormatException {
            String cuser = "000";
            BigDecimal cd = new BigDecimal(0);
            BigDecimal cg = new BigDecimal(0);
            Map<String, String> ps = new HashMap<>();
            String cuser1 = "000";
            String t;
            int h = 0;
            List<ReportAnomalyTmp> rr = new ArrayList<>();
            String fich = null;
            for (ReportControleInter s1 : s2) {
                h++;
                Boolean brk = null;
                try {
                    if (app_s.equalsIgnoreCase("2")) {
                        ps = yy.get(s1.getFich().trim().substring(0, 5));
                    } else {
                        ps = yy.get(s1.getFich().trim().substring(0, 6));
                    }
                    //object would help to detect once for queries which are non post type
                    String tt = ps.get("postlike_query");
                    fich = s1.getFich();
                    brk = ps.get("result").equalsIgnoreCase("sql") && (tt == null || (tt != null && tt.trim() == ""));
                } catch (NullPointerException r) {
                    System.out.println("S1 VALUE :" + s1.getFich() + " AND YY : " + yy);
                }
                int g = 0;
                for (ReportControleInter s : ConvertListIntra(ps, date, s1)) {
                    if (s.getSign().contains("IF") && s.getCg().contains("min(")) {
                        String ii = s.getCd();
                        String iii = s.getCg();
                        for (String key : extractKey(s.getCd(), '{', '}')) {
                            s.setCd(s.getCd().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                        }
                        String[] saa = s.getCg().split("min");
                        String rev = saa[1];
                        int uu = min(rev, s, ps);
                        int yr = 0;
                        for (String key : extractKey(saa[0], '{', '}')) {
                            s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                            yr = Integer.parseInt(convertElemt(key, ps, etab, s.getFich(), date));
                        }
                        try {
                            if (Boolean.TRUE.equals(u.eval(s.getCd())) && yr > uu) {
                                s.setCd("IF(" + s.getCd() + "):" + s.getCg());
                                s.setCg("IF(" + ii + ")" + iii);
                                s.setSign("=");
                                cd = new BigDecimal("0.0");
                                cg = new BigDecimal("1.0");
                            } else {
                                s.setSign("!=");
                                cd = new BigDecimal("0.0");
                                cg = new BigDecimal("1.0");
                            }
                        } catch (ScriptException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (s.getSign().contains("IF")) {
                        t = "IF(" + s.getCd() + "):" + s.getCg();
                        for (String key : extractKey(s.getCg() + s.getCd(), '{', '}')) {
                            s.setCd(s.getCd().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                            s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                        }
                        try {
                            String[] r = s.getCg().split("\\|");
                            if (((r.length == 2)
                                    && ((Boolean.TRUE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[0])))
                                    || (Boolean.FALSE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[1])))))
                                    || (r.length == 1 && Boolean.TRUE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[0])))) {
//                                System.out.println(s.getCd() + " : " + r[0] + "...");
                                s.setSign("=");
                                cd = new BigDecimal("0.0");
                                cg = new BigDecimal("1.0");
                            } else {
                                s.setSign("=");
                                cd = new BigDecimal("0.0");
                                cg = new BigDecimal("0.0");
                            }
                            s.setCd("IF(" + s.getCd() + "):" + s.getCg());
                            s.setCg(t);
                        } catch (ScriptException ex) {
                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (s.getCd().contains("min(")) {
                        try {
                            cd = new BigDecimal(min(s.getCd(), s, ps));
                            cg = getResultInter_v1(s.getCg(), s.getFich(), date, etab, ps);
                        } catch (ParseException ex) {
                            System.out.println("error :" + ex.getMessage());
                        }
                    } else {
                        try {
                            cd = getResultInter_v1(s.getCd(), s.getFich(), date, etab, ps);
                            cg = getResultInter_v1(s.getCg(), s.getFich(), date, etab, ps);
                        } catch (ParseException ex) {
                            System.out.println("error :" + ex.getMessage());
                        }
                    }
                    Boolean val = false;
                    if (s.getSign().equalsIgnoreCase("<")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res < 0);
                        if (!val) {
                            System.out.println("test 3");
                            ReportAnomalyTmpRepository.save(new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", fich, s.getSign(), s1.getCg() + " " + s1.getSign() + " " + s1.getCd(), s1.getId()));
                            if (brk) {
                                System.out.println("found for sql type therefor breaking it :" + s1.getCg() + " " + s1.getCd());
                                break;
                            }
                        }
                    } else if (s.getSign().equalsIgnoreCase(">")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res > 0);
                        if (!val) {
                            System.out.println("test 2");
                            ReportAnomalyTmpRepository.save(new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", fich, s.getSign(), s1.getCg() + " " + s1.getSign() + " " + " " + s1.getCd(), s1.getId()));
                            if (brk) {
                                System.out.println("found for sql type therefor breaking it :" + s1.getCg() + " " + s1.getCd());
                                break;
                            }
                        }
                    } else if (s.getSign().equalsIgnoreCase("!=")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res > 0);
                        if (!val) {
                            System.out.println("test 1");
                            ReportAnomalyTmpRepository.save(new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", fich, s.getSign(), s1.getCg() + " " + s1.getSign() + " " + " " + s1.getCd(), s1.getId()));
                            if (brk) {
                                System.out.println("found for sql type therefor breaking it :" + s1.getCg() + " " + s1.getCd());
                                break;
                            }
                        }
                    } else {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res == 0);
                        if (!val) {
                            try {
                                ReportAnomalyTmpRepository.save(new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", fich, s.getSign(), s1.getCg() + " " + s1.getSign() + " " + " " + s1.getCd(), s1.getId()));
                                if (brk) {
                                    System.out.println("found for sql type therefor breaking it :" + s1.getCg() + " " + s1.getCd());
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("error :" + e.getMessage() + " : " + e.getLocalizedMessage());
                            }
                        }
                    }

//                                        if ((s.getSign().equalsIgnoreCase("<") && cg.compareTo(cd) >= 0)
//                            || (s.getSign().equalsIgnoreCase(">") && cg.compareTo(cd) <= 0)
//                            || (s.getSign().equalsIgnoreCase("<=") && cg.compareTo(cd) > 0)
//                            || (s.getSign().equalsIgnoreCase(">=") && cg.compareTo(cd) < 0)
//                            || (s.getSign().equalsIgnoreCase("!=") && cg.compareTo(cd) == 0)
//                            || (s.getSign().equalsIgnoreCase("=") && cg.compareTo(cd) != 0)) {
//                        try {
//                            ReportAnomalyTmpRepository.save(new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", s.getFich(), s.getSign(), s1.getCg() + " " + s1.getCd()));
//                            if (brk) {
//                                break;
//                            }
//                        } catch (Exception e) {
//                            System.out.println("error :" + e.getMessage() + " : " + e.getLocalizedMessage());
//                        }
//                    }
                }
                liveReportingService.detailsReportingToTheVue3(ssd, "intra", 1L);
            }
        }

        private int min(String rev, ReportControleInter s, Map<String, String> ps) throws NumberFormatException {
            int uu = 999999999;
            for (String key : extractKey(rev, '{', '}')) {
                s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                int ie = Integer.parseInt(convertElemt(key, ps, etab, s.getFich(), date));
                uu = (uu < ie) ? uu : ie;
            }
            return uu;
        }

    }

    public class Treatement implements Runnable {

        String controle_type;
        String date;
        Long idOpe;
        String etab;
        String ssd;
        String cuser;
        Map<String, Map<String, String>> yy;

        public Treatement(String controle_type, String date, Long idOpe, String etab, String ssd, Map<String, Map<String, String>> yy) {
            this.controle_type = controle_type;
            this.date = date;
            this.idOpe = idOpe;
            this.etab = etab;
            this.ssd = ssd;
            this.yy = yy;
            this.cuser = "000";
        }

        @Override
        public void run() {
            controlTreatment(controle_type);
        }

        public Treatement(String controle_type, String date, Long idOpe, String etab, String ssd, String cuser, Map<String, Map<String, String>> yy) {
            this.controle_type = controle_type;
            this.date = date;
            this.idOpe = idOpe;
            this.etab = etab;
            this.ssd = ssd;
            this.cuser = cuser;
            this.yy = yy;
        }

        private void controlTreatment(String controle_type) throws DataAccessException, NumberFormatException {
            String e = "";
            String s = "";
            String g = "";
            try {
                Connection r = connectDB();
                g = "Select lib2 from sanm where tabcd = '0012' and acscd ='0036' and dele = 0";
                ResultSet tt = r.createStatement().executeQuery(g);
                tt.next();
                s = tt.getString("lib2");
            } catch (SQLException ex) {
                System.out.println("query has error :" + g);
            } catch (ClassNotFoundException ex) {
                System.out.println("class not found:");
            } catch (JSONException ex) {
                System.out.println("return json generated error");
            }
            if (controle_type.equals("intra")) {
                simpleIntraControl(s);
            }
            if (controle_type.equals("inter")) {
                simpleInterControl(s);
            }
            if (controle_type.equals("CMXPARM")) {
                configureIntraControl();
            }
            if (controle_type.equals("QLITE")) {
                qualityControlTreatment();
            }
            if (controle_type.equals("CMPX")) {
                ComplexTreatment();
                controle_type = "Complex";
            }

            String sql = "insert into  rpanom ( crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg,DESCRP,ctrid) select  crdt,cuser,dar,etab,fichier,mntd,mntg,muser,sign,type,cd,cg,DESCRP,ctrid from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd') and type = '" + controle_type + "'";
            jdbcTemplate.execute(sql);
            String sql1 = "delete from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd') and type = '" + controle_type + "'";
            jdbcTemplate.execute(sql1);
        }

        private void simpleIntraControl(String s) {
            Boolean val = false;
            String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'intra'";
            jdbcTemplate.execute(sql12);
            List<ReportControleInter> listInter = (List<ReportControleInter>) ReportControleInterRepository
                    .getInterControls("0");
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, "intra",
                    Long.valueOf(listInter.size()));
            Map<String, String> ps = new HashMap<String, String>();
            String t = "";
            ScriptEngineManager m = new ScriptEngineManager();
            ScriptEngine u = m.getEngineByName("js");
            int Friqunce = (int) Math.ceil(listInter.size() / 100.0);
            int h = 0;
            List<ReportControleInter> loop = new ArrayList<>();
            List<ReportControleInter> loop1 = new ArrayList<>();
            List<List<ReportControleInter>> r = new ArrayList<>();
            for (ReportControleInter s1 : listInter) {
                if (s1.getCd().contains("CH")) {//unit direct on one side
                    loop1.add(s1);
                    continue;
                }
                h++;
                loop.add(s1);
                if (h % 1 == 0) {
                    r.add(new ArrayList<>(loop));
                    loop.clear();
                }
            }
            if (loop.size() > 0) {
                r.add(new ArrayList<>(loop));
            }
            if (loop1.size() > 0) {
                r.add(loop1);
            }
            TreatThreadControl(r, u, date, idOpe, etab, ssd, yy, listInter.size(), s);

        }

        private void TreatThreadControl(List<List<ReportControleInter>> r, ScriptEngine u, String date, Long idOpe, String etab, String ssd, Map<String, Map<String, String>> yy, int total, String s) {
            ExecutorService service = Executors.newFixedThreadPool(10);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    do {
                        try {
                            Map<String, String> type = new HashMap<>();
                            for (List<ReportControleInter> rt : r) {
                                service.execute(new ReportControleServiceImpl.IntraThreadTeatment(rt, u, date, idOpe, etab, ssd, yy, s));
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

                        liveReportingService.endDetailsReportingToTheVue2(ssd, "intra", 1L, Long.valueOf(total),
                                Long.valueOf(total));
                    } while (!service.isShutdown());
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                System.out.println("Waiting for the intra control of all the threads");
            }
        }
//
//        private void threadIntraControlTreatment(List<ReportControleInter> s2, ScriptEngine u, String cuser1, int h, int Friqunce) throws NumberFormatException {
//            String cuser = "000";
//            BigDecimal cd = new BigDecimal(0);
//            BigDecimal cg = new BigDecimal(0);
//            Map<String, String> ps;
//            String t;
//            Boolean val;
//            for (ReportControleInter s1 : s2) {
//                ps = yy.get(s1.getFich().trim().substring(0, 5));
//                for (ReportControleInter s : ConvertListIntra(ps, date, s1)) {
//                    if (s.getSign().contains("IF") && s.getCg().contains("min(")) {
//                        String ii = s.getCd();
//                        String iii = s.getCg();
//                        for (String key : extractKey(s.getCd(), '{', '}')) {
//                            s.setCd(s.getCd().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
//                        }
//                        String[] saa = s.getCg().split("min");
//                        String rev = saa[1];
//                        int uu = min(rev, s, ps);
//                        int yr = 0;
//                        for (String key : extractKey(saa[0], '{', '}')) {
//                            s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
//                            yr = Integer.parseInt(convertElemt(key, ps, etab, s.getFich(), date));
//                        }
//                        try {
//                            if (Boolean.TRUE.equals(u.eval(s.getCd())) && yr > uu) {
//                                s.setCd("IF(" + s.getCd() + "):" + s.getCg());
//                                s.setCg("IF(" + ii + ")" + iii);
//                                s.setSign("=");
//                                cd = new BigDecimal("0.0");
//                                cg = new BigDecimal("1.0");
//                            } else {
//                                s.setSign("!=");
//                                cd = new BigDecimal("0.0");
//                                cg = new BigDecimal("1.0");
//                            }
//                        } catch (ScriptException ex) {
//                            System.out.println(ex.getMessage());
//                        }
//                    } else if (s.getSign().contains("IF")) {
//                        t = "IF(" + s.getCd() + "):" + s.getCg();
//                        for (String key : extractKey(s.getCg() + s.getCd(), '{', '}')) {
//                            s.setCd(s.getCd().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
//                            s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
//                        }
//                        try {
//                            String[] r = s.getCg().split("\\|");
//                            if (((r.length == 2)
//                                    && ((Boolean.TRUE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[0])))
//                                    || (Boolean.FALSE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[1])))))
//                                    || (r.length == 1 && Boolean.TRUE.equals(u.eval(s.getCd())) && Boolean.FALSE.equals(u.eval(r[0])))) {
////                                System.out.println(s.getCd() + " : " + r[0] + "...");
//                                s.setSign("=");
//                                cd = new BigDecimal("0.0");
//                                cg = new BigDecimal("1.0");
//                            } else {
//                                s.setSign("=");
//                                cd = new BigDecimal("0.0");
//                                cg = new BigDecimal("0.0");
//                            }
//                            s.setCd("IF(" + s.getCd() + "):" + s.getCg());
//                            s.setCg(t);
//                        } catch (ScriptException ex) {
//                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    } else if (s.getCd().contains("min(")) {
//                        try {
//                            for (String key : extractKey(s.getCd(), '{', '}')) {
//                                s.setCd(s.getCd().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
//                            }
//                            System.out.println("cote droit :" + s.getCd());
//                            cd = new BigDecimal(min(s.getCd(), s, ps));
//                            cg = getResultInter_v1(s.getCg(), s.getFich(), date, etab, ps);
//                        } catch (ParseException ex) {
//                            System.out.println("error :" + ex.getMessage());
//                        }
//                    } else {
//                        try {
//                            cd = getResultInter_v1(s.getCd(), s.getFich(), date, etab, ps);
//                            cg = getResultInter_v1(s.getCg(), s.getFich(), date, etab, ps);
//                        } catch (ParseException ex) {
//                            System.out.println("error :" + ex.getMessage());
//                        }
//                    }
//                    if (s.getSign().equalsIgnoreCase("<")) {
//                        int res;
//                        res = cg.compareTo(cd);
//                        val = (res < 0);
//                        if (!val) {
//                            System.out.println("test 3");
//                            ReportAnomalyTmp repo = null;
//                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", s.getFich(), s.getSign());
//                            ReportAnomalyTmpRepository.save(repo);
//                        }
//                    } else if (s.getSign().equalsIgnoreCase(">")) {
//                        int res;
//                        res = cg.compareTo(cd);
//                        val = (res > 0);
//                        if (!val) {
//                            System.out.println("test 2");
//                            ReportAnomalyTmp repo = null;
//                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", s.getFich(), s.getSign());
//                            ReportAnomalyTmpRepository.save(repo);
//                        }
//                    } else if (s.getSign().equalsIgnoreCase("!=")) {
//                        int res;
//                        res = cg.compareTo(cd);
//                        val = (res > 0);
//                        if (!val) {
//                            System.out.println("test 1");
//                            ReportAnomalyTmp repo = null;
//                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", s.getFich(), s.getSign());
//                            ReportAnomalyTmpRepository.save(repo);
//                        }
//                    } else {
//                        int res;
//                        res = cg.compareTo(cd);
//                        val = (res == 0);
//                        if (!val) {
//                            ReportAnomalyTmp repo = null;
//                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()), Double.parseDouble(cd.toString()), cuser1, null, s.getCg().toString(), s.getCd().toString(), "intra", s.getFich(), s.getSign());
//                            ReportAnomalyTmpRepository.save(repo);
//                        }
//                    }
//                }
//                if ((h % Friqunce) == 0) {
//                    liveReportingService.detailsReportingToTheVue3(ssd, "intra", new Long(Friqunce));
//                }
//
//            }
//        }

        private int min(String rev, ReportControleInter s, Map<String, String> ps) throws NumberFormatException {
            int uu = 999999999;
            for (String key : extractKey(rev, '{', '}')) {
                s.setCg(s.getCg().replace("{" + key + "}", convertElemt(key, ps, etab, s.getFich(), date)));
                int ie = Integer.parseInt(convertElemt(key, ps, etab, s.getFich(), date));
                uu = (uu < ie) ? uu : ie;
            }
            return uu;
        }

        private void simpleInterControl(String sT) throws NumberFormatException, DataAccessException {
            Boolean val;
            String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'inter'";
            jdbcTemplate.execute(sql12);
            List<ReportControleIntra> listIntra = (List<ReportControleIntra>) ReportControleIntraRepository
                    .findAll();
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, "inter", Long.valueOf(listIntra.size()));
            int h = 0;
            int Friqunce = (int) Math.ceil(listIntra.size() / 100.0);
            BigDecimal cd = new BigDecimal("0");
            BigDecimal cg = new BigDecimal("0");
            for (ReportControleIntra s : listIntra) {
                h++;
                try {
                    cd = getResultIntrav1(s.getCd(), date, etab);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    cg = getResultIntrav1(s.getCg(), date, etab);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (s.getSign().equalsIgnoreCase("<")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res < 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", null, s.getSign(), null, s.getId(), "");
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase(">")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", null, s.getSign(), null, s.getId(), "");
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else if (s.getSign().equalsIgnoreCase("!=")) {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res > 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", null, s.getSign(), null, s.getId(), "");
                        ReportAnomalyTmpRepository.save(repo);
                    }
                } else {
                    int res;
                    res = cg.compareTo(cd);
                    val = (res == 0);
                    if (!val) {
                        System.out.println("test");
                        ReportAnomalyTmp repo = null;
                        repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                s.getCd().toString(), "inter", null, s.getSign(), null, s.getId(), "");
                        ReportAnomalyTmpRepository.save(repo);
                    }
                }
                if ((h % Friqunce) == 0) {
                    liveReportingService.detailsReportingToTheVue3(ssd, "inter", new Long(Friqunce));
                }
            }
            liveReportingService.endDetailsReportingToTheVue2(ssd, "inter", 1L, Long.valueOf(listIntra.size()), Long.valueOf(listIntra.size()));
        }

        private void configureIntraControl() throws NumberFormatException, DataAccessException {
            BigDecimal cd;
            BigDecimal cg;
            Boolean val;
            try {
                Connection ss = connectDB();
                Statement sy = ss.createStatement();
                String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'CMXPARM'";
                jdbcTemplate.execute(sql12);
                List<ReportControleIntra> listIntra = reportCalculateService.QUERYSET(sy);
                liveReportingService.beginDetailsReportingToTheVue2(idOpe, "CMXPARM", Long.valueOf(listIntra.size()));
                for (ReportControleIntra s : listIntra) {
                    cd = getResultparameter(s.getDele(), s.getCd(), date, etab);
                    cg = getResultparameter(s.getDele(), s.getCg(), date, etab);
                    if (s.getSign() == null) {
                        int res;
                        res = cg.compareTo(new BigDecimal("0"));
                        val = (res == 0);
                        if (!val) {
                            System.out.println("test");
                            ReportAnomalyTmp repo = null;
                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                    0.0, cuser, null, s.getCg(),
                                    s.getCd(), "CMXPARM", null, s.getSign(), null, s.getId());
                            repo.setDESCRP(s.getPer());
                            ReportAnomalyTmpRepository.save(repo);
                        }
                    } else if (s.getSign().equalsIgnoreCase("<")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res < 0);
                        if (!val) {
                            System.out.println("test");
                            ReportAnomalyTmp repo = null;
                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                    Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                    s.getCd().toString(), "CMXPARM", null, s.getSign(), null, s.getId());
                            repo.setDESCRP(s.getPer());
                            ReportAnomalyTmpRepository.save(repo);
                        }
                    } else if (s.getSign().equalsIgnoreCase(">")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res > 0);
                        if (!val) {
                            System.out.println("test");
                            ReportAnomalyTmp repo = null;
                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                    Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                    s.getCd().toString(), "CMXPARM", null, s.getSign(), null, s.getId());
                            repo.setDESCRP(s.getPer());
                            ReportAnomalyTmpRepository.save(repo);
                        }
                    } else if (s.getSign().equalsIgnoreCase("!=")) {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res > 0);
                        if (!val) {
                            System.out.println("test");
                            ReportAnomalyTmp repo = null;
                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                    Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                    s.getCd().toString(), "CMXPARM", null, s.getSign(), null, s.getId());
                            repo.setDESCRP(s.getPer());
                            ReportAnomalyTmpRepository.save(repo);
                        }
                    } else {
                        int res;
                        res = cg.compareTo(cd);
                        val = (res == 0);
                        if (!val) {
                            System.out.println("test");
                            ReportAnomalyTmp repo = null;
                            repo = new ReportAnomalyTmp(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
                                    Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
                                    s.getCd().toString(), "CMXPARM", null, s.getSign(), null, s.getId());
                            repo.setDESCRP(s.getPer());
                            ReportAnomalyTmpRepository.save(repo);
                        }
                    }
                    liveReportingService.detailsReportingToTheVue3(ssd, "CMXPARM", 1L);

                }
                liveReportingService.endDetailsReportingToTheVue2(ssd, "CMXPARM", 1L, Long.valueOf(listIntra.size()), Long.valueOf(listIntra.size()));
            } catch (SQLException ex) {
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void qualityControlTreatment() throws DataAccessException, NumberFormatException {
            String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'QLITE'";
            jdbcTemplate.execute(sql12);
            List<ReportControleQuality> listQuality = reportControleQualityRepository.getQualityControls("0");
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, "QLITE", Long.valueOf(listQuality.size()));
            for (ReportControleQuality s : listQuality) {
                System.out.println("Quality control :" + s.getDescp());
                BigDecimal cx1 = getResultQualityV1(s.getQrycot(), date, etab, yy);
                if (cx1.doubleValue() != 0) {
                    System.out.println("test");
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
                            s.getQrycot().toString(), "0", "QLITE", null, "=", null, s.getId());
                    repo.setDESCRP(s.getLebelle());
                    ReportAnomalyTmpRepository.save(repo);
                }
//                liveReportingService.detailsReportingToTheVue1(ssd, "QLITE", Long.parseLong(minimun));
                liveReportingService.detailsReportingToTheVue3(ssd, "QLITE", 1L);

            }
//            liveReportingService.endDetailsReportingToTheVue1(sse, "QLITE", 1L, Long.valueOf(listQuality.size()),
//                    Long.valueOf(listQuality.size()));
            liveReportingService.endDetailsReportingToTheVue2(ssd, "QLITE", 1L, Long.valueOf(listQuality.size()), Long.valueOf(listQuality.size()));

        }

        private void ComplexTreatment() throws DataAccessException, NumberFormatException {
            String cuser = "000";
            String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'Complex'";
            jdbcTemplate.execute(sql12);
            List<ReportControleComplex> listComplex = reportControleComplexRepository.getComplexControls("0");
            liveReportingService.beginDetailsReportingToTheVue2(idOpe, "CMPX", Long.valueOf(listComplex.size()));
            BigDecimal cx1 = new BigDecimal("0.0");
            BigDecimal cx2 = new BigDecimal("0.0");
            for (ReportControleComplex s : listComplex) {
                if (s.getSign().equalsIgnoreCase("N")) {// compering n and n-1 period
                    List<Map<String, Object>> tmpe = jdbcTemplate
                            .queryForList("select distinct dar from rprep order by dar desc");
                    if (tmpe.isEmpty()) {// no preceding
                        System.out.println("No precident value");
                        continue;
                    } else {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        cx1 = getResultComplex4(s.getTypd(), s.getCd(), formatter.format(tmpe.get(0).get("dar")), yy, etab);
                        System.out.println(" output of left query :" + cx1);
                        cx2 = getResultComplex4(s.getTypg(), s.getCg(), date, yy, etab);
                        System.out.println(" output of right query :" + cx2);
                    }

                } else if (s.getSign().equalsIgnoreCase("IF")) {
                    String srg = s.getCg();
                    String srd = s.getCd();
                    for (String o : extractKey(s.getCg() + s.getCd(), '{', '}')) {
                        try {
                            System.out.println("the code to search value is :" + o);
                            Connection sr = connectDB();
                            Statement ers = sr.createStatement();
                            ResultSet sp = ers.executeQuery("select lib1,lib3 from sanm where tabcd = '3020' and acscd = '" + o.trim() + "' and dele = 0");
                            if (sp.next()) {
                                BigDecimal r = getResultComplex4(sp.getString("lib1").trim(), sp.getString("lib3").trim(), date, yy, etab);
                                sp.close();
                                System.out.println("replaceing: " + o + " to :" + r);
                                srg = srg.replace("{" + o + "}", r.toString());
                                srd = srd.replace("{" + o + "}", r.toString());
                            } else {
                                System.out.println("code: " + o + " not found");
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    ScriptEngineManager m = new ScriptEngineManager();
                    ScriptEngine u = m.getEngineByName("js");

                    try {
                        s.setLebelle(s.getLebelle() + " : if(" + srg + "): " + srd);
                        String[] ro = srd.split("\\|");
                        if (Boolean.TRUE.equals(u.eval(srg))) {
                            System.out.println("main equate to true");
                            if (Boolean.FALSE.equals(u.eval(ro[0]))) {
                                System.out.println("first set is abnormal");
                                //error
                                cx1 = new BigDecimal("0.0");
                                cx2 = new BigDecimal("1.0");
                            }
                        } else {
                            System.out.println("main equate to false");
                            if (Boolean.FALSE.equals(u.eval(ro[1]))) {
                                System.out.println("second set is abnormal");
                                //error
                                cx1 = new BigDecimal("0.0");
                                cx2 = new BigDecimal("1.0");
                            }
                        }
                    } catch (ScriptException ex) {
                        Logger.getLogger(ReportControleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    cx1 = getResultComplex4(s.getTypd(), s.getCd(), date, yy, etab);
                    System.out.println(" output of left query :" + cx1);
                    cx2 = getResultComplex4(s.getTypg(), s.getCg(), date, yy, etab);
                    System.out.println(" output of right query :" + cx2);
                }
                int tees = cx1.abs().compareTo(cx2.abs());
                System.out.println("the comparison of the two results :" + tees);
                Boolean equalto = (tees != 0 && s.getSign().trim().equalsIgnoreCase("="));
                Boolean greaterthan = (tees != 1 && s.getSign().trim().equalsIgnoreCase(">"));
                Boolean lessthan = (tees != -1 && s.getSign().trim().equalsIgnoreCase("<"));
                Boolean greatherequalto = ((tees != 1 || tees != 0) && (s.getSign().trim().equalsIgnoreCase(">=")));
                Boolean lessthanequalto = ((tees != -1 || tees != 0) && (s.getSign().trim().equalsIgnoreCase("<=")));
                Boolean otherop = (s.getSign().trim().equalsIgnoreCase("IF") || s.getSign().equalsIgnoreCase("N"));
                Boolean otherCond = (tees == 0 && otherop);

                if (equalto || lessthan || greaterthan || greatherequalto || lessthanequalto || otherCond) {
                    ReportAnomalyTmp repo = null;
                    repo = new ReportAnomalyTmp(etab, Date.valueOf(date), cx2.doubleValue(), cx1.doubleValue(),
                            cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign(), null, s.getId());

                    repo.setDESCRP(s.getDescp() + " || " + s.getLebelle());
                    ReportAnomalyTmpRepository.save(repo);
                }
                liveReportingService.detailsReportingToTheVue3(ssd, "CMPX", 1L);
            }
            liveReportingService.endDetailsReportingToTheVue2(ssd, "CMPX", 1L, Long.valueOf(listComplex.size()), Long.valueOf(listComplex.size()));
        }

    }

    @Override
    public void Controle2(String date, String etab, String cuser, List<String> controle_typ, String ssd) {

        Statement r1 = null;
        try {
            Connection r = connectDB();
            r1 = r.createStatement();
            Map<String, Map<String, String>> yy = getType_v1(r1);

            Long idOpe = liveReportingService.beginGobalReportingToTheVue(ssd, etab, cuser, "control",
                    Long.valueOf(controle_typ.size()));
            ExecutorService service = Executors.newFixedThreadPool(4);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    do {
                        try {
                            Map<String, String> type = new HashMap<>();

                            for (String rt : controle_typ) {
                                service.execute(new ReportControleServiceImpl.Treatement(rt, date, idOpe, etab, ssd, yy));
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

    }

    private String convertElemt(String s, Map<String, String> yy, String etab, String f, String date) {
        String val = "";
        List<String> string = separeteData1(s);
        if (yy.get("result").equals("calculate") || yy.get("result").equals("duplicate") || yy.get("result").equals("duplicateNoPost")) {
            List<Map<String, Object>> tmp = jdbcTemplate.queryForList("select * from rprep"
                    + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                    + "' and fichier='" + f + "' and post='" + string.get(0) + "' and col='"
                    + string.get(1).trim() + "'");
            if (!tmp.isEmpty() && tmp.size() == 1) {
                val = tmp.get(0).get("valm").toString();
                val = (val.equals("999999999999999")) ? "0" : val;
                return val;
            } else {
                System.out.println("Did not see field " + s + " of file :" + f);
            }
        } else if (yy.get("result").equals("sql")) {
            List<Map<String, Object>> tmp = null;
            if (yy.get("postlike_query") != null && yy.get("postlike_query").equals("1")) {
                tmp = jdbcTemplate.queryForList("select * from sqltype"
                        + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichi='" + f + "' and col1='" + string.get(0) + "' ");

            } else {
                tmp = jdbcTemplate.queryForList("select * from sqltype"
                        + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichi='" + f + "' and id='" + string.get(0) + "' ");
            }
            if (!tmp.isEmpty() && tmp.size() == 1) {
                try {
                    if (tmp.get(0).get("col" + string.get(1).trim()) != null) {
                        val = tmp.get(0).get("col" + string.get(1).trim()).toString();
                    } else {
                        val = "0";
                    }
                } catch (Exception ex) {
                    val = "0";
                    System.out.println("the field:" + s + " of field :" + f + " from which line identify is :" + string.get(0) + " and col:" + string.get(1) + " and the object taken is :" + tmp.get(0));
                }
                val = (val.equals("999999999999999")) ? "0" : val;
                return val;
            } else {
                System.out.println("Did not see field " + s + " of file :" + f);
            }
        }
        return "0";
    }

    public BigDecimal getResultInter(String formule, String fichier, String date, String etab) throws ParseException {
//        System.out.println(formule);
        formule = " " + formule.replaceAll("\\+", " + ").replaceAll("\\-", " - ") + " ";
        String[] r = formule.split(" ");
        List<String> str = new ArrayList<String>();
        for (int i = 0; i < r.length; i++) {
            if (r[i].length() > 3 && r[i].substring(0, 2).equals("CH")) {
                str.add(r[i]);
            }
        }
        BigDecimal rt = new BigDecimal(0);
        String val = "0";
        int i = 0;
        if (!formule.equalsIgnoreCase("")) {
            for (String s : str) {
                s = s.trim();
                List<String> string = separeteData1(s);
                String sql = "select * from rprep where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
                        + string.get(1).trim() + "'";
                List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
                if (!tmp.isEmpty() && tmp.size() == 1) {
                    val = tmp.get(0).get("valm").toString();
                    val = (val.equals("999999999999999")) ? "0" : val;
                    formule = formule.replaceAll(" " + s.trim() + " ", val + "");
                    i = 1;

                }
            }
            System.out.println("Value Replaced :" + formule);
            if (i == 1) {
                rt = new BigDecimal(reportCalculateServiceImpl.eval(formule));
            }

        }
        return rt.abs();

    }

    public BigDecimal getResultInter_v1(String formule, String fichier, String date, String etab, Map<String, String> yy) throws ParseException {
//        System.out.println(formule);
        formule = " " + formule.replaceAll("\\+", " + ").replaceAll("\\-", " - ").replaceAll("\\*", " * ") + " ";
        String[] r = formule.split(" ");
        List<String> str = new ArrayList<String>();
        for (int i = 0; i < r.length; i++) {
            if (r[i].length() > 3 && r[i].substring(0, 2).equals("CH")) {
                str.add(r[i]);
            }
        }
        BigDecimal rt = new BigDecimal(0);
        String val = "0";
        int i = 0;
        if (!formule.equalsIgnoreCase("")) {
            for (String s : str) {
                s = s.trim();
                List<String> string = separeteData1(s);
                if (yy.get("result").equals("calculate") || yy.get("result").equals("duplicate") || yy.get("result").equals("duplicateNoPost")) {
                    List<Map<String, Object>> tmp = jdbcTemplate.queryForList("select * from rprep"
                            + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                            + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
                            + string.get(1).trim() + "'");
                    if (!tmp.isEmpty() && tmp.size() == 1) {
                        val = (tmp.get(0).get("valm") == null) ? "0" : tmp.get(0).get("valm").toString();
                        val = (val.equals("999999999999999")) ? "0" : val;
                        formule = formule.replaceAll(" " + s.trim() + " ", val + "");
                        i = 1;
                    } else {
                        System.out.println("Did not see field :" + s.trim() + " in file:" + fichier);
                        formule = formule.replaceAll(" " + s.trim() + " ", "0");
                    }
                } else if (yy.get("result").equals("sql")) {
                    List<Map<String, Object>> tmp = null;
                    if (yy.get("postlike_query") != null && yy.get("postlike_query").equals("1")) {

                        tmp = jdbcTemplate.queryForList("select * from sqltype"
                                + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                                + "' and fichi='" + fichier + "' and col1='" + string.get(0) + "'");
                    } else {
                        tmp = jdbcTemplate.queryForList("select * from sqltype"
                                + " where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                                + "' and fichi='" + fichier + "' and id='" + string.get(0) + "'");

                    }
                    if (!tmp.isEmpty() && tmp.size() == 1) {
                        if (tmp.get(0).get("col" + string.get(1).trim()) != null) {
                            val = tmp.get(0).get("col" + string.get(1).trim()).toString();
                        } else {
                            val = "0";
                        }
                        val = (val.equals("999999999999999")) ? "0" : val;
                        formule = formule.replaceAll(" " + s.trim() + " ", val + "");
                        i = 1;
                    } else {
                        System.out.println("Did not see field :" + s.trim() + " in file:" + fichier);
                        formule = formule.replaceAll(" " + s.trim() + " ", "0");
                    }
                }
            }
            if (i == 1) {
                try {
                    rt = new BigDecimal(reportCalculateServiceImpl.eval(formule));
                } catch (RuntimeException ro) {
                    System.out.println("the expression evaluating with error :" + formule);
                    rt = new BigDecimal("0");
                }
            }
        }
        return rt.abs();

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
//     System.out.println(stack.pop());
        BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(stack.pop());
//     return Double.valueOf(stack.pop());
        return DEFAULT_FOOBAR_VALUE;
    }

    public BigDecimal getResultIntra(String formule, String date, String etab) throws ParseException {

        BigDecimal ret = DEFAULT_FOOBAR_VALUE;
        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        String frm = formule = formule.replaceAll(" ", "");
//		String frm = formule.replaceAll("-", "+");
//		frm = formule.replaceAll("/", "+");
//		frm = formule.replaceAll("\\*", "+");
        int sf = formule.lastIndexOf(":");
        System.out.println(sf);
        if (!formule.contains(":")) {
            return new BigDecimal(formule);
        }
        frm = frm.substring(sf + 1, frm.length());
        String fichier = formule.substring(0, sf);
        String[] str = frm.split("\\+");
        String val = "0";
        int i = 0;
        int j = 1;
        if (!frm.equalsIgnoreCase("")) {
            for (String s : str) {
                List<String> string = separeteData(s);
                String sql = "select * from rprep where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
                        + "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
                        + string.get(1).substring(1, 2) + "'";
                List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//             List<ReportResult> calc = ReportResultRepository.findByEtabAndDarAndFichierAndPostAndCol(etab, new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(date), fichier, string.get(0), string.get(1));
                if (!tmp.isEmpty()) {
                    System.out.println("passé");
                    for (int t = 0; t < tmp.size(); t++) {
                        val = tmp.get(t).get("valm").toString();
                        System.out.println(val);
                    }
                    val = (val.equals("999999999999999")) ? "0" : val;
                    formule = formule.replaceAll(s, " " + val + " ");
                    i = 1;
                }
            }
            if (i == 1) {
                System.out.println("result formule : " + formule);
                String[] input = formule.split(" ");
                String[] output = infixToRPN(input);

                // Build output RPN string minus the commas
                for (String token : output) {
                    System.out.print(token + " ");
                }

                // Feed the RPN string to RPNtoDouble to give result
                rt = RPNtoDouble(output);
                System.out.println("result after :  " + rt);
                ;
            }
        }
        return rt;

    }

    public BigDecimal getResultIntrav1(String formule, String date, String etab) throws ParseException {

        BigDecimal rt = DEFAULT_FOOBAR_VALUE;
        String frm = " " + formule.replaceAll("\\+", " + ").replaceAll("\\-", " - ") + " ";
        String[] eachFormular = formule.split("[-+*/]");
        String val = "0";
        int i = 0;
        for (String r : eachFormular) {
            if (!r.contains(":")) {
                frm = frm.replaceAll(" " + r + " ", "0");
            } else {
                int sf = r.lastIndexOf(":");
                List<String> u = separeteData1(r.substring(sf + 1, r.length()).trim());
                String sql = "select * from rprep where dar =to_date('" + date + "','yyyy-mm-dd') and fichier='" + r.substring(0, sf).trim() + "' and post='" + u.get(0).trim() + "' and col='"
                        + u.get(1).trim() + "'";
//                System.out.println("query for " + r + " : " + sql);
                List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
                if (!tmp.isEmpty()) {
//                    System.out.println("passé");
                    for (int t = 0; t < tmp.size(); t++) {
                        val = tmp.get(t).get("valm").toString();
                        System.out.println("The value of " + r + " is :" + val);
                    }
                    val = (val.equals("999999999999999")) ? "0" : val;
                    frm = frm.replaceAll(r, " " + val + " ");
                    i = 1;
                } else {
                    frm = frm.replaceAll(" " + r + " ", "0");
                }
            }
        }
//        System.out.println("The formule " + formule + "  = " + val);
        if (i == 1) {
            rt = new BigDecimal(eval(frm.replaceAll("999999999999999", "0")));
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

    public List<String> separeteData(String formule) {
        System.out.println("formule disolve :" + formule);
        List<String> ret = new ArrayList<String>();
        formule = formule.substring(2, formule.length());
        if (formule.length() != 6) {
            formule = formule.substring(0, 4) + "C" + formule.substring(4, formule.length());
        }
        int lasttIndex = formule.lastIndexOf('C');
        String post = formule.substring(0, lasttIndex);
        String colone = formule.substring(lasttIndex, formule.length());
        ret.add(post);
        ret.add(colone);
        System.out.println(post + " : " + colone);
        return ret;

    }

    public List<String> separeteData1(String formule) {
        List<String> ret = new ArrayList<String>();
        formule = formule.substring(2, formule.length());
        int lasttIndex = formule.lastIndexOf('C');
        String post = formule.substring(0, lasttIndex);
        String colone = formule.substring(lasttIndex + 1, formule.length());
        ret.add(post);
        ret.add(colone);
        return ret;

    }

    @SuppressWarnings("unchecked")
    public List<ReportControleInter> constructfilterqueryinter1(ReportControleInter filter) {

        return reportControleInterRepository.findAll(new Specification<ReportControleInter>() {

            @Override
            public Predicate toPredicate(Root<ReportControleInter> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getFich() != null) {
                    predicates.add(cb.equal(root.get("fichi"), filter.getFich()));
                }

                // If attribute is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getCd() != null) {
                    predicates.add(cb.like(cb.lower(root.get("cd")), "%" + filter.getCd().toLowerCase() + "%"));
                }

                // If lastName is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getCg() != null) {
                    predicates.add(cb.like(cb.lower(root.get("cg")), "%" + filter.getCg().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }

        });
    }

    @SuppressWarnings("unchecked")
    public List<ReportControleIntra> constructfilterqueryintra1(ReportControleIntra filter) {
        return reportControleIntraRepository.findAll(new Specification<ReportControleIntra>() {
            @Override
            public Predicate toPredicate(Root<ReportControleIntra> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                // If attribute is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getCd() != null) {
                    predicates.add(cb.like(cb.lower(root.get("cd")), "%" + filter.getCd().toLowerCase() + "%"));
                }

                // If lastName is specified in filter, add contains (lile)
                // filter to where clause with ignore case
                if (filter.getCg() != null) {
                    predicates.add(cb.like(cb.lower(root.get("cg")), "%" + filter.getCg().toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        });
    }

}

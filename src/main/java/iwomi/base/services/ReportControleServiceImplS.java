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
import iwomi.base.ServiceInterface.ReportCalculateServiceS;
import iwomi.base.ServiceInterface.ReportControleServiceS;
import iwomi.base.objects.ReportAnomalyTmpS;
import iwomi.base.objects.ReportControleComplexS;
import iwomi.base.objects.ReportControleIntraSS;
import iwomi.base.objects.ReportControleInterSS;
import iwomi.base.objects.ReportControleQualityS;
import iwomi.base.repositories.ReportAnomalyRepositoryS;
import iwomi.base.repositories.ReportAnomalyTmpRepositoryS;
import iwomi.base.repositories.ReportControleComplexRepositoryS;
import iwomi.base.repositories.ReportControleInterRepositoryS;
import iwomi.base.repositories.ReportControleIntraRepositoryS;
import iwomi.base.repositories.ReportControleQualityRepositoryS;
import iwomi.base.repositories.ReportResultRepositoryS;
import java.sql.Date;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Service
@Component
//@Transactional(readOnly = false)
public class ReportControleServiceImplS implements ReportControleServiceS {
	@Autowired
	private ReportControleInterRepositoryS reportControleInterRepository;
	@Autowired
	private ReportControleIntraRepositoryS reportControleIntraRepository;
        @Autowired
        ReportCalculateServiceImpl reportCalculateServiceImpl;
	@Autowired
	private LiveReportingServiceS liveReportingService;
	@Autowired
	private ReportCalculateServiceS reportCalculateService;
	@Autowired
	private ReportControleIntraRepositoryS ReportControleIntraRepository;
	@Autowired
	private ReportControleInterRepositoryS ReportControleInterRepository;
	@Autowired
	private ReportAnomalyRepositoryS ReportAnomalyRepository;
	@Autowired
	private ReportAnomalyTmpRepositoryS ReportAnomalyTmpSRepository;
	@Autowired
	private ReportResultRepositoryS ReportResultRepository;
	@Autowired
	private ReportControleComplexRepositoryS reportControleComplexRepository;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private ReportControleQualityRepositoryS reportControleQualityRepository;

	private static final BigDecimal DEFAULT_FOOBAR_VALUE = new BigDecimal(0);
	private static final int LEFT_ASSOC = 0;
	private static final int RIGHT_ASSOC = 1;

	// Operators
	private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
	static {
		// Map<"token", []{precendence, associativity}>
		OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
		OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
	}

	// Test if token is an operator
	private static boolean isOperator(String token) {
		return OPERATORS.containsKey(token);
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
		System.out.println(s);
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(s);
		BigDecimal ee =  (BigDecimal) tmp.get(0).get("cpx");
		if(ee!=null) {
			return ee;
		}else {
			return (BigDecimal) tmp.get(0).get("a");
		}

	}

	private String getColPostLk(String fichier, String col, String whereLk, String date, String etab) {
		String v = "";
		System.out.println(whereLk);
		for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
			v = v + "And post like " + w + " ";
		}
		System.out.println("final output");
		System.out.println(v);
		return "select  coalesce(SUM(coalesce(valm,0)),0) cpx from rprep where  fichier = '" + fichier + "' " + v
				+ " and col= " + col + " and dar = to_date('" + date + "','yyyy-mm-dd') and etab = '" + etab + "' ";
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
		return "select coalesce(sum(coalesce(to_number(" + scol + "),0)),0) cpx from ssqltype where  " + s[0].trim()
				+ " = '" + s[1].trim() + "' and fichi = '" + fichier + "' and dar =to_date('" + date
				+ "','yyyy-mm-dd') and etab = '" + etab + "'";
	}

	private String getColWhereColsWhereLk(String scole, String col, String where, String whereLk, String fichier,
			String date, String etab) {
		String v = "";
		String[] s = where.substring(1, where.length() - 1).split(":");// since there is only one
		for (String w : whereLk.substring(1, whereLk.length() - 1).split(",")) {
			String[] sp = w.split(":");
			v = v + "And " + sp[0].trim() + " like " + sp[1].trim() + " ";
		}
		return "select coalesce(sum(coalesce(to_number(" + scole + "),0)),0) cpx from ssqltype where  " + s[0].trim()
				+ " = '" + s[1].trim() + "' and fichi = '" + fichier + "' " + v + "and dar =to_date('" + date
				+ "','yyyy-mm-dd') and etab = '" + etab + "'";
	}

	private String getCols(String scol, String fichier, String date, String etab) {

		return "select coalesce(sum(coalesce(to_number(" + scol + "),0)),0) as cpx from ssqltype where fichi = '"
				+ fichier + "' and dar =to_date('" + date + "','yyyy-mm-dd') and etab = '" + etab + "'";
	}

	private String getSumBetweenPostRp(String fichier, String post1, String post2, String col, String date,
			String etab) {

		return "SELECT coalesce(SUM(coalesce(valm,0)),0) cpx FROM rprep WHERE col= " + col + " and" + " dar = to_date('"
				+ date + "','yyyy-mm-dd') and etab = '" + etab + "' and fichier = '" + fichier + "'"
				+ " and rang between (select distinct rang from rppfich where poste ='" + post1 + "' and fich = '"
				+ fichier + "')" + " and (select distinct rang from rppfich where poste ='" + post2 + "' and fich = '"
				+ fichier + "')" + "";
	}

	private Map<String, String> getArguments(String arg) {
		Map<String, String> ret = new HashMap<String, String>();
		for (String w : arg.split(";")) {
			String[] s = w.split("=");
			ret.put(s[0].trim(), s[1].trim());
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
		return (BigDecimal) tmp.get(0).get("cpx");
	}

	@Override
	public void Controle(String date, String etab, String cuser) {
		String sql = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd')";
		jdbcTemplate.execute(sql);
		List<ReportControleIntraSS> listInter = (List<ReportControleIntraSS>) ReportControleIntraRepository.findAll();
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
		for (ReportControleIntraSS s : listInter) {
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
					Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
				}
				try {
					cg = getResultInter(s.getCg(), s.getFich(), date, etab);
					System.out.println("le resultat droit: " + cg);
				} catch (ParseException ex) {
					Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
				sql2 = "select * from ssqltype where fichi = '" + s.getFich() + "' and dar = to_date('" + date
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
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"inter", s.getFich(), s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else if (s.getSign().equalsIgnoreCase(">")) {
				int res;
				res = cg.compareTo(cd);
				val = (res > 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"inter", s.getFich(), s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else if (s.getSign().equalsIgnoreCase("!=")) {
				int res;
				res = cg.compareTo(cd);
				val = (res > 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"inter", s.getFich(), s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else {
				int res;
				res = cg.compareTo(cd);
				val = (res == 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"inter", s.getFich(), s.getSign());

					ReportAnomalyTmpSRepository.save(repo);
				}
			}
		}
		List<ReportControleInterSS> listIntra = (List<ReportControleInterSS>) ReportControleInterRepository.findAll();
		for (ReportControleInterSS s : listIntra) {

			try {
				cd = getResultIntra(s.getCd(), date, etab);
			} catch (ParseException ex) {
				Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				cg = getResultIntra(s.getCg(), date, etab);
			} catch (ParseException ex) {
				Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"intra", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else if (s.getSign().equalsIgnoreCase(">")) {
				int res;
				res = cg.compareTo(cd);
				val = (res > 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"intra", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else if (s.getSign().equalsIgnoreCase("!=")) {
				int res;
				res = cg.compareTo(cd);
				val = (res > 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"intra", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			} else {
				int res;
				res = cg.compareTo(cd);
				val = (res == 0);
				if (!val) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
							Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(), s.getCd().toString(),
							"intra", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				}
			}
		}
		List<ReportControleQualityS> listQuality = reportControleQualityRepository.getQualityControls("0");
		for (ReportControleQualityS s : listQuality) {
			BigDecimal cx1 = getResultQuality(s.getQrycot(), date, etab);
			System.out.println(cx1 + " sdfsdfsdf");
			if (cx1.doubleValue() != 0) {
				System.out.println("test");
				ReportAnomalyTmpS repo = null;
				repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
						s.getQrycot().toString(), "0", "Quality", null, "=");
				ReportAnomalyTmpSRepository.save(repo);
			}

		}

		List<ReportControleComplexS> listComplex = reportControleComplexRepository.getComplexControls("0");
		BigDecimal cx1 = new BigDecimal("0.0");
		BigDecimal cx2 = new BigDecimal("0.0");
		for (ReportControleComplexS s : listComplex) {
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
				ReportAnomalyTmpS repo = null;
				repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
						s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
				ReportAnomalyTmpSRepository.save(repo);
			} else if (s.getSign().equalsIgnoreCase(">") && (cx1.doubleValue() <= cx2.doubleValue())) {
				System.out.println("test");
				ReportAnomalyTmpS repo = null;
				repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
						s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
				ReportAnomalyTmpSRepository.save(repo);
			} else if (s.getSign().equalsIgnoreCase("!=") && (cx1 == cx2)) {
				System.out.println("test");
				ReportAnomalyTmpS repo = null;
				repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser, null,
						s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
				ReportAnomalyTmpSRepository.save(repo);
			} else {
				if (cx1 != cx2) {
					System.out.println("test");
					System.out.println(cx1);
					System.out.println(cx2);
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
							null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
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
			List<ReportControleIntraSS> listInter = (List<ReportControleIntraSS>) ReportControleIntraRepository.findAll();
			for (ReportControleIntraSS s : listInter) {
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
						Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
					}
					try {
						cg = getResultInter(s.getCg(), s.getFich(), date, etab);
						System.out.println("le resultat droit: " + cg);
					} catch (ParseException ex) {
						Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
					sql2 = "select * from ssqltype where fichi = '" + s.getFich() + "' and dar = to_date('" + date
							+ "','yyyy-mm-dd')" + " and " + cdcol + " = " + cgcol;
					System.out.println(sql2);
					List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
					cg = BigDecimal.valueOf(result.size());
					List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from ssqltype where fichi = '"
							+ s.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd')");
					cd = new BigDecimal(r.size());

				}

				if (s.getSign().equalsIgnoreCase("<")) {
					int res;
					res = cg.compareTo(cd);
					val = (res < 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "inter", s.getFich(), s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else if (s.getSign().equalsIgnoreCase(">")) {
					int res;
					res = cg.compareTo(cd);
					val = (res > 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "inter", s.getFich(), s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else if (s.getSign().equalsIgnoreCase("!=")) {
					int res;
					res = cg.compareTo(cd);
					val = (res > 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "inter", s.getFich(), s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else {
					int res;
					res = cg.compareTo(cd);
					val = (res == 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "inter", s.getFich(), s.getSign());

						ReportAnomalyTmpSRepository.save(repo);
					}
				}
			}
		}

		if (controle_typ.contains("inter")) {
			List<ReportControleInterSS> listIntra = (List<ReportControleInterSS>) ReportControleInterRepository.findAll();
			for (ReportControleInterSS s : listIntra) {

				try {
					cd = getResultIntra(s.getCd(), date, etab);
				} catch (ParseException ex) {
					Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
				}
				try {
					cg = getResultIntra(s.getCg(), date, etab);
				} catch (ParseException ex) {
					Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
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
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "intra", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else if (s.getSign().equalsIgnoreCase(">")) {
					int res;
					res = cg.compareTo(cd);
					val = (res > 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "intra", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else if (s.getSign().equalsIgnoreCase("!=")) {
					int res;
					res = cg.compareTo(cd);
					val = (res > 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "intra", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				} else {
					int res;
					res = cg.compareTo(cd);
					val = (res == 0);
					if (!val) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
								Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
								s.getCd().toString(), "intra", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				}
			}
		}
		if (controle_typ.contains("QLITE")) {
			List<ReportControleQualityS> listQuality = reportControleQualityRepository.getQualityControls("0");
			for (ReportControleQualityS s : listQuality) {
				BigDecimal cx1 = getResultQuality(s.getQrycot(), date, etab);
				System.out.println(cx1 + " sdfsdfsdf");
				if (cx1.doubleValue() != 0) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
							s.getQrycot().toString(), "0", "Quality", null, "=");
					ReportAnomalyTmpSRepository.save(repo);
				}

			}
		}
		if (controle_typ.contains("CMPX")) {
			List<ReportControleComplexS> listComplex = reportControleComplexRepository.getComplexControls("0");
			BigDecimal cx1 = new BigDecimal("0.0");
			BigDecimal cx2 = new BigDecimal("0.0");
			for (ReportControleComplexS s : listComplex) {
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
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
							null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				} else if (s.getSign().equalsIgnoreCase(">") && (cx1.doubleValue() <= cx2.doubleValue())) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
							null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				} else if (s.getSign().equalsIgnoreCase("!=") && (cx1 == cx2)) {
					System.out.println("test");
					ReportAnomalyTmpS repo = null;
					repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(), cuser,
							null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
					ReportAnomalyTmpSRepository.save(repo);
				} else {
					if (cx1 != cx2) {
						System.out.println("test");
						System.out.println(cx1);
						System.out.println(cx2);
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
								cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					}
				}
			}
		}
		sql = "insert into srpanom select * from rpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
		jdbcTemplate.execute(sql);
		String sql1 = "delete from srpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
		jdbcTemplate.execute(sql1);

	}

	@Override
	public void Controle2(String date, String etab, String cuser, List<String> controle_typ, String ssd) {
		
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
		Double idOpe = (double) liveReportingService.beginGobalReportingToTheVue(ssd, etab, cuser, "control",
				Long.valueOf(controle_typ.size()));
		String sse = "";
		String minimun = "0";
		try {
			minimun = reportCalculateService.getmin();
		} catch (ClassNotFoundException | SQLException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int y = 0; y < controle_typ.size(); y++) {
			
			System.out.println("this is the control starting :"+controle_typ.get(y));
			if (controle_typ.get(y).equals("intra")) {
				String sql12 = "delete from srpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'intra'";
				jdbcTemplate.execute(sql12);
				System.out.println("this is the control starting :"+controle_typ.get(y));
				List<ReportControleIntraSS> listInter = (List<ReportControleIntraSS>) ReportControleIntraRepository
						.findAll();
				sse = liveReportingService.beginDetailsReportingToTheVue1(idOpe.longValue(), "intra",
						Long.valueOf(listInter.size()));
				for (ReportControleIntraSS s : listInter) {
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
							Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
						}
						try {
							cg = getResultInter(s.getCg(), s.getFich(), date, etab);
							System.out.println("le resultat droit: " + cg);
						} catch (ParseException ex) {
							Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
						}

					} else if (ps.get("result").equals("sql")) {
						String cdcol = s.getCd().trim().replaceAll("C", "col");
						String cgcol = s.getCg().trim().replaceAll("C", "col");
						String sql2 = "";
						if (s.getCd().substring(0, 2).equalsIgnoreCase("IF")) {
//							String zz = s.getCd().trim();
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
//							String zz = s.getCg().trim();
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
								min = "case when(" + firstArg + "<" + secondArg + ")THEN " + firstArg + " else "
										+ secondArg + " end";
							}
							cgcol = "(case when(" + cond + ")then (" + min + ") else " + falseCond + " end);";
						}
						sql2 = "select * from ssqltype where fichi = '" + s.getFich() + "' and dar = to_date('" + date
								+ "','yyyy-mm-dd')" + " and " + cdcol + " = " + cgcol;
						System.out.println(sql2);
						List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
						cg = BigDecimal.valueOf(result.size());
						List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from ssqltype where fichi = '"
								+ s.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd')");
						cd = new BigDecimal(r.size());

					} else if (ps.get("result").equals("duplicateNoPost")) {
						String cdcol = s.getCd().trim().replaceAll("C", "col");
						String cgcol = s.getCg().trim().replaceAll("C", "col");
						String sql2 = "select * from ssqltype where fichi = '" + s.getFich() + "' and dar = to_date('"
								+ date + "','yyyy-mm-dd')" + " and (" + cdcol + ") != (" + cgcol + ")";
						List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2);
						List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from ssqltype where fichi = '"
								+ s.getFich() + "' and dar = to_date('" + date + "','yyyy-mm-dd')");
						cd = new BigDecimal(r.size());
						cg = new BigDecimal(result.size());
					}

					if (s.getSign().equalsIgnoreCase("<")) {
						int res;
						res = cg.compareTo(cd);
						val = (res < 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "intra", s.getFich(), s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else if (s.getSign().equalsIgnoreCase(">")) {
						int res;
						res = cg.compareTo(cd);
						val = (res > 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "intra", s.getFich(), s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else if (s.getSign().equalsIgnoreCase("!=")) {
						int res;
						res = cg.compareTo(cd);
						val = (res > 0);

						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "intra", s.getFich(), s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else {
						int res;
						res = cg.compareTo(cd);
						val = (res == 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "intra", s.getFich(), s.getSign());

							ReportAnomalyTmpSRepository.save(repo);
						}
					}
					// individual progress
					liveReportingService.detailsReportingToTheVue1(ssd, "intra", Long.parseLong(minimun));

				}
				// closing a reaction
				liveReportingService.endDetailsReportingToTheVue1(sse, "intra", 1L, Long.valueOf(listInter.size()),
						Long.valueOf(listInter.size()));

			}

			if (controle_typ.get(y).equals("inter")) {
				String sql12 = "delete from srpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'inter'";
				jdbcTemplate.execute(sql12);
				List<ReportControleInterSS> listIntra = (List<ReportControleInterSS>) ReportControleInterRepository
						.findAll();
				sse = liveReportingService.beginDetailsReportingToTheVue1(idOpe.longValue(), "inter",
						Long.valueOf(listIntra.size()));
				for (ReportControleInterSS s : listIntra) {

					try {
						cd = getResultIntra(s.getCd(), date, etab);
					} catch (ParseException ex) {
						Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
					}
					try {
						cg = getResultIntra(s.getCg(), date, etab);
					} catch (ParseException ex) {
						Logger.getLogger(ReportControleServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
					}
					if (s.getSign().equalsIgnoreCase("<")) {
						int res;
						res = cg.compareTo(cd);
						val = (res < 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "inter", null, s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else if (s.getSign().equalsIgnoreCase(">")) {
						int res;
						res = cg.compareTo(cd);
						val = (res > 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "inter", null, s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else if (s.getSign().equalsIgnoreCase("!=")) {
						int res;
						res = cg.compareTo(cd);
						val = (res > 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "inter", null, s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					} else {
						int res;
						res = cg.compareTo(cd);
						val = (res == 0);
						if (!val) {
							System.out.println("test");
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), Double.parseDouble(cg.toString()),
									Double.parseDouble(cd.toString()), cuser, null, s.getCg().toString(),
									s.getCd().toString(), "inter", null, s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					}
					liveReportingService.detailsReportingToTheVue1(ssd, "inter", Long.parseLong(minimun));

				}
				liveReportingService.endDetailsReportingToTheVue1(sse, "inter", 1L, Long.valueOf(listIntra.size()),
						Long.valueOf(listIntra.size()));

			}
			if (controle_typ.get(y).equals("QLITE")) {
				String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'QLITE'";
				jdbcTemplate.execute(sql12);
				List<ReportControleQualityS> listQuality = reportControleQualityRepository.getQualityControls("0");
				sse = liveReportingService.beginDetailsReportingToTheVue1(idOpe.longValue(), "QLITE",
						Long.valueOf(listQuality.size()));
				for (ReportControleQualityS s : listQuality) {
					BigDecimal cx1 = getResultQuality(s.getQrycot(), date, etab);
					System.out.println(cx1 + " sdfsdfsdf");
					if (cx1.doubleValue() != 0) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), 0.0, cuser, null,
								s.getQrycot().toString(), "0", "Quality", null, "=");
						ReportAnomalyTmpSRepository.save(repo);
					}
					liveReportingService.detailsReportingToTheVue1(ssd, "QLITE", Long.parseLong(minimun));

				}
				liveReportingService.endDetailsReportingToTheVue1(sse, "QLITE", 1L, Long.valueOf(listQuality.size()),
						Long.valueOf(listQuality.size()));

			}
			if (controle_typ.get(y).equals("CMPX")) {
				String sql12 = "delete from rpanom where dar =to_date('" + date + "','yyyy-mm-dd') and type = 'CMPX'";
				jdbcTemplate.execute(sql12);
				List<ReportControleComplexS> listComplex = reportControleComplexRepository.getComplexControls("0");
				sse = liveReportingService.beginDetailsReportingToTheVue1(idOpe.longValue(), "CMPX",
						Long.valueOf(listComplex.size()));
				BigDecimal cx1 = new BigDecimal("0.0");
				BigDecimal cx2 = new BigDecimal("0.0");
				for (ReportControleComplexS s : listComplex) {
					if (s.getSign().equalsIgnoreCase("N")) {// compering n and n-1 period
						List<Map<String, Object>> tmpe = jdbcTemplate
								.queryForList("select distinct dar from rprep order by dar desc");
						if (tmpe.isEmpty()) {// no preceding
							System.out.println("No precident value");
							continue;
						} else {
							DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							cx1 = getResultComplex1(s.getCd(), formatter.format(tmpe.get(0).get("dar")), etab,
									s.getTypd());
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
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
								cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					} else if (s.getSign().equalsIgnoreCase(">") && (cx1.doubleValue() <= cx2.doubleValue())) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
								cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					} else if (s.getSign().equalsIgnoreCase("!=") && (cx1 == cx2)) {
						System.out.println("test");
						ReportAnomalyTmpS repo = null;
						repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
								cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null, s.getSign());
						ReportAnomalyTmpSRepository.save(repo);
					} else {
						if (cx1 != cx2) {
							System.out.println("test");
							System.out.println(cx1);
							System.out.println(cx2);
							ReportAnomalyTmpS repo = null;
							repo = new ReportAnomalyTmpS(etab, Date.valueOf(date), cx1.doubleValue(), cx2.doubleValue(),
									cuser, null, s.getCg().toString(), s.getCd().toString(), "Complex", null,
									s.getSign());
							ReportAnomalyTmpSRepository.save(repo);
						}
					}
					liveReportingService.detailsReportingToTheVue1(ssd, "CMPX", Long.parseLong(minimun));

				}
				liveReportingService.endDetailsReportingToTheVue1(sse, "CMPX", 1L, Long.valueOf(listComplex.size()),
						Long.valueOf(listComplex.size()));

			}
		}
		
		Long statutope = Long.valueOf(1);
		liveReportingService.endGobalReportingToTheVue1(idOpe.longValue(), statutope);
		String sql = "insert into srpanom select * from srpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
		jdbcTemplate.execute(sql);
		String sql1 = "delete from srpanomtmp where dar =to_date('" + date + "','yyyy-mm-dd')";
		jdbcTemplate.execute(sql1);

	}

	public BigDecimal getResultInter(String formule, String fichier, String date, String etab) throws ParseException {
		System.out.println(formule);
//		formule = formule.replaceAll(" ", "");
//		frm = formule.replaceAll("/", "+");
//		frm = formule.replaceAll("\\*", "+");
//		BOTMAS
		
		formule = formule.replaceAll("\\+", " + ");
		formule = " " + formule + " ";
		String [] r = formule.split(" ");
		List<String> str = new ArrayList<String>();
        for(int i = 0;i<r.length;i++){
        	System.out.println(r[i]);
           if(r[i].length()>3 && r[i].substring(0,2).equals("CH")){
               str.add(r[i]);
           } 
        }
		BigDecimal ret = DEFAULT_FOOBAR_VALUE;
		BigDecimal rt = DEFAULT_FOOBAR_VALUE;
		String val = "0";
		int i = 0;
		int j = 1;
		System.out.println(formule);
		if (!formule.equalsIgnoreCase("")) {
			for (String s : str) {
				s = s.trim();
				List<String> string = separeteData1(s);
				String sql = "select * from srprep where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
						+ "' and fichier='" + fichier + "' and post='" + string.get(0) + "' and col='"
						+ string.get(1).trim() + "'";
				System.out.println(sql);
				List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//             List<ReportResult> calc = ReportResultRepository.findByEtabAndDarAndFichierAndPostAndCol(etab, new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(date), fichier, string.get(0), string.get(1));
				if (!tmp.isEmpty() && tmp.size() == 1) {
					System.out.println("passé");
					val = tmp.get(0).get("valm").toString();
					System.out.println(val);
					val = (val.equals("999999999999999")) ? "0" : val;
					formule = formule.replaceAll(s, val);
					System.out.println(formule);
					ret = new BigDecimal(val);
					i = 1;

				}
//             i =s.length();
			}
			if (i == 1) {
				System.out.println("result formule : " + formule);
				rt =new BigDecimal(reportCalculateServiceImpl.eval(formule));
//				String[] input = formule.split(" ");
//				String[] output = infixToRPN(input);
				// Build output RPN string minus the commas
//				for (String token : output) {
//					System.out.print(token + " ");
//				}

//				// Feed the RPN string to RPNtoDouble to give result
//				rt = RPNtoDouble(output);
				System.out.println("result after :  " + rt);
			}

		}
		return rt;

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
			}
			// If token is a left bracket '('
			else if (token.equals("(")) {
				stack.push(token); //
			}
			// If token is a right bracket ')'
			else if (token.equals(")")) {
				while (!stack.empty() && !stack.peek().equals("(")) {
					out.add(stack.pop());
				}
				stack.pop();
			}
			// If token is a number
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
		System.out.println(formule);
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
				String sql = "select * from srprep where dar =to_date('" + date + "','yyyy-mm-dd') and etab='" + etab
						+ "' and fichier='" + fichier + "' and post='" + string.get(0).trim() + "' and col='"
						+ string.get(1).substring(1).trim() + "'";
//                                System.out.println("this the query : "+sql);
				List<Map<String, Object>> tmp = jdbcTemplate.queryForList(sql);
//             List<ReportResult> calc = ReportResultRepository.findByEtabAndDarAndFichierAndPostAndCol(etab, new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(date), fichier, string.get(0), string.get(1));
				if (!tmp.isEmpty()) {
					System.out.println("passé");
					for (int t = 0; t < tmp.size(); t++) {
//                                            System.out.println(" its the value :"+tmp.get(t).get("valm"));
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

	public List<String> separeteData(String formule) {
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
//		System.out.println("post : " + post);
//		System.out.println("col : " + colone);
		return ret;

	}

	@SuppressWarnings("unchecked")
	public List<ReportControleIntraSS> constructfilterqueryinter1(ReportControleIntraSS filter) {

		return reportControleIntraRepository.findAll(new Specification<ReportControleIntraSS>() {

			@Override
			public Predicate toPredicate(Root<ReportControleIntraSS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
	public List<ReportControleInterSS> constructfilterqueryintra1(ReportControleInterSS filter) {
		return reportControleIntraRepository.findAll(new Specification<ReportControleInterSS>() {
			@Override
			public Predicate toPredicate(Root<ReportControleInterSS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

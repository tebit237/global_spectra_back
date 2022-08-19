package iwomi.base.services;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import iwomi.base.ServiceInterface.ReportRepSErvice;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportRep;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportControleInterRepository;
import iwomi.base.repositories.ReportRepRepository;
import java.util.Calendar;


@Component
public class ReportRepServiceImpl implements ReportRepSErvice {
	@Autowired
	private ReportRepRepository reportRepRepository;
	@Autowired
	private NomenclatureRepository nomenclatureRepository;
	
	private Date date_arr;

	@SuppressWarnings("unchecked")
	public List<ReportControleInter> constructfilterqueryinter1(ReportRep filter) {
		System.out.println("filter: " + filter);
		return reportRepRepository.findAll(new Specification<ReportRep>() {

			@Override
	     	public Predicate toPredicate(Root<ReportRep> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (filter.getPost() != null) {
					predicates.add(cb.equal(root.get("post"), filter.getPost()));
				}

				// If attribute is specified in filter, add contains (lile)
				// filter to where clause with ignore case
				System.out.println("----------------------------------------------------" + "to_date("
						+  filter.getDar() + ")" + "---------"+  filter.getFichier() +"-------------------------------------------");
//				try {
//					System.out.println("----------------------------------------------------"
//							+ new SimpleDateFormat("dd/MM/YYYY").parse(filter.getFichier())
//							+ "----------------------------------------------------");
//				} catch (ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				if (filter.getFichier() != " ") {
					System.out.println("entered-----------------");
//					try {
						System.out.println("entered2-----------------");
						SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
						System.out.println("entered3-----------------"+df);
						df.setLenient(false);
						System.out.println("entered4-----------------"+df);
//						date_arr = df.parse(filter.getFichier());
						System.out.println("date_arr 1: "+date_arr);
//					} catch (ParseException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
						System.out.println("date_arr 1 error: "+date_arr);
//					}
					System.out.println("date_arr 2: "+date_arr);
					predicates.add(cb.equal(cb.lower(root.get("dar")),date_arr));
//					predicates.add(cb.equal(cb.lower(root.get("dar")), filter.getDar()));
					System.out.println("predicates 1: "+predicates);
					//predicates.add(cb.equal(cb.lower(root.get("dar")),"to_date('2019-11-30','yyyy-mm-dd')"));
//					predicates.add(cb.equal(cb.lower(root.get("dar")),filter.getFichier()));
//						predicates.add(cb.equal("dar","to_date("+filter.getFichier()+")"));
				}

				predicates.add(cb.like(cb.lower(root.get("fichier")), "%" + filter.getFichier().toLowerCase() + "%"));
				return cb.and(predicates.toArray(new Predicate[0]));
			}

		});
	}

	 public int monthLastDay(int s) {//month to return the last day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.getInstance().get(Calendar.YEAR), s - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public Boolean isPeriod(String date, String code) throws ParseException {
        Nomenclature detail = nomenclatureRepository.findTabcdAndAcsd("9011", code.trim(), "0");
//        System.out.println("yo yann");
//        System.out.println(code.trim());
//        System.out.println(date);
//        System.out.println("yo yann");

        java.util.Date datet = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(datet);
        int s = cal.get(Calendar.DAY_OF_MONTH);//
        int s1 = cal.get(Calendar.MONTH) + 1;//

        //day of month end 
        int monthend = cal.getActualMaximum(Calendar.DATE);
        //System.out.println("inside periodicity "+"9011 and "+ code.trim() + "  "+detail.getLib1());

        switch (detail.getLib1().trim()) {
            case "01"://daily
                return true;
            case "02"://Hebdomadaire
                System.out.println("the day is :" + new Long(datet.getDay()));
                return detail.getMnt1().equals(new Long(datet.getDay())) ? true : false;
            case "03"://Décadaire
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
                    int v = detail.getMnt2().intValue();
                    if (v == s || v + 10 == s || v + 20 == s || v + 30 == s || monthend == s) {
                        return true;
                    }
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//standard 1 10 20 30 or last
                    if (1 == s || 10 == s || 20 == s || 30 == s || monthend == s) {//TO_DO end of month
                        return true;
                    }

                }
                return false;
            case "04"://Quinzaine
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
                    int v = detail.getMnt3().intValue();
                    if (v + 1 == s || v + 15 == s || v + 30 == s || monthend == s) {
                        return true;
                    }
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//standard 1 15 30 or last
                    if (1 == s || 15 == s || 30 == s || monthend == s) {
                        return true;
                    }

                }
                return false;
            case "05"://Mensuels
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
                    int v = detail.getMnt4().intValue();
                    if (v == s) {
                        return true;
                    }
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//monthly
                    if (monthend == s) {//TO_DO end of month
                        return true;
                    }

                }
                return false;
            case "06":// trimestriel 3
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
//                    String[] v = detail.getLib7().split("/");
//                    int s1p = Integer.parseInt(v[0]);
//                    int sp = Integer.parseInt(v[1]);
//                    if ((sp == s1 && s1p == s)
//                            || (sp + 3 == s1 && s1p == s)
//                            || (sp + 6 == s1 && s1p == s)
//                            || (sp + 9 == s1 && s1p == s)
//                            || (sp + 12 == s1 && s1p == s)) {
//                        return true;
//                    }
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//standard je ap au de
                    if ((3 == s1 && monthLastDay(3) == s)
                            || (6 == s1 && monthLastDay(6) == s)
                            || (9 == s1 && monthLastDay(9) == s)
                            || (12 == s1 && monthLastDay(12) == s)) {//TO_DO end of month
                        return true;
                    }
                }
                return false;
            case "07":// semestriel 6
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
//                    String[] v = detail.getLib7().split("/");
//                    int s1p = Integer.parseInt(v[0]);
//                    int sp = Integer.parseInt(v[1]);
//                    if ((sp == s1 && s1p == s)
//                            || (sp + 6 == s1 && s1p == s)
//                            || (sp + 12 == s1 && s1p == s)) {
//                        return true;
//                    }//lastDay
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//standard je ap au de
                    if ((6 == s1 && monthLastDay(6) == s)
                            || (12 == s1 && monthLastDay(12) == s)) {//TO_DO end of month
                        return true;
                    }
                }
                return false;
            case "08":// annuel 6
                if (detail.getLib2().trim().equalsIgnoreCase("02")) {//variable
//                    String[] v = detail.getLib7().split("/");
//                    int s1p = Integer.parseInt(v[0]);
//                    int sp = Integer.parseInt(v[1]);
//                    if (sp == s1 && s1p == s) {
//                        return true;
//                    }
                } else if (detail.getLib2().trim().equalsIgnoreCase("01")) {//standard je ap au de
                    if (12 == s1 && monthLastDay(12) == s) {//TO_DO end of month
                        return true;
                    }
                }
                return false;
        }

        return false;
    }
}

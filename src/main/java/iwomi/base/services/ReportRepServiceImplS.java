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

import iwomi.base.ServiceInterface.ReportRepSErviceS;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportControleIntraSS;
import iwomi.base.objects.ReportRepSS;
import iwomi.base.repositories.ReportControleInterRepository;
import iwomi.base.repositories.ReportRepRepository;
import iwomi.base.repositories.ReportRepRepositoryS;


@Component
public class ReportRepServiceImplS implements ReportRepSErviceS {
	@Autowired
	private ReportRepRepositoryS reportRepRepository;
	
	private Date date_arr;

	@SuppressWarnings("unchecked")
	public List<ReportControleIntraSS> constructfilterqueryinter1(ReportRepSS filter) {
		System.out.println("filter: " + filter);
		return reportRepRepository.findAll(new Specification<ReportRepSS>() {

			@Override
	     	public Predicate toPredicate(Root<ReportRepSS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

	
}

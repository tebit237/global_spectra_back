package iwomi.base.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import iwomi.base.ServiceInterface.ReportAnomalyService;
import iwomi.base.ServiceInterface.ReportAnomalyServiceS;
import iwomi.base.objects.ReportAnomalyS;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.repositories.ReportAnomalyRepository;
import iwomi.base.repositories.ReportAnomalyRepositoryS;
@Service
public class ReportAnomalyServiceImplS implements ReportAnomalyServiceS{
@Autowired
private ReportAnomalyRepositoryS reportAnomalyRepository;

	 @SuppressWarnings("unchecked")
	public List<ReportAnomalyS> constructfilterquery1(ReportAnomalyS filter) {

			return reportAnomalyRepository.findAll(new Specification<ReportAnomalyS>() {

				@Override
				public Predicate toPredicate(Root<ReportAnomalyS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();
					
					if (filter.getType() != null) {
						predicates.add(cb.like(cb.lower(root.get("type")), 
	                                                    "%" + filter.getType().toLowerCase() + "%"));
					}

					if (filter.getFichier() != null) {
						predicates.add(cb.like(cb.lower(root.get("fichier")), 
	                                                    "%" + filter.getFichier().toLowerCase() + "%"));
					}

					return cb.and(predicates.toArray(new Predicate[0]));
				}
				
			});
		}


}

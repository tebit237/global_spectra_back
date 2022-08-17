package iwomi.base.services;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import iwomi.base.ServiceInterface.ReportAttributeServiceS;
import iwomi.base.objects.ReportAttributeS;
import iwomi.base.repositories.ReportAttributeRepositoryS;
@Component
public class ReportAttributeServiceImplS  implements ReportAttributeServiceS{
	
	@Autowired
	private ReportAttributeRepositoryS reportAttributeRepository;
	 @Override
	    public List<ReportAttributeS> listAll() {
		 List<ReportAttributeS> reportAttributes = new ArrayList<>();
	        return reportAttributes;
	       }
		
	 public List<ReportAttributeS> constructfilterquery(ReportAttributeS r) {
			String m = " WHERE 1=1";
			if(r.getAtt()!=null) {
				m+=" AND e.att = '"+r.getAtt()+"'";
			}
			if(r.getType()!=null) {
				m+=" AND e.type = '"+r.getType()+"'";
			}
			 System.out.println(m);
			return reportAttributeRepository.filtersearch(m);
		}

	 @SuppressWarnings("unchecked")
	public List<ReportAttributeS> constructfilterquery1(ReportAttributeS filter) {

			return reportAttributeRepository.findAll(new Specification<ReportAttributeS>() {

				@Override
				public Predicate toPredicate(Root<ReportAttributeS> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();


					// If attribute is specified in filter, add contains (lile)
					// filter to where clause with ignore case
					if (filter.getAtt() != null) {
						predicates.add(cb.like(cb.lower(root.get("att")), 
	                                                    "%" + filter.getAtt().toLowerCase() + "%"));
					}

					// If lastName is specified in filter, add contains (lile)
					// filter to where clause with ignore case
					if (filter.getType() != null) {
						predicates.add(cb.like(cb.lower(root.get("type")), 
	                                                    "%" + filter.getType().toLowerCase() + "%"));
					}

					return cb.and(predicates.toArray(new Predicate[0]));
				}
				
			});
		}

}
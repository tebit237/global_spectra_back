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

import iwomi.base.ServiceInterface.ReportAttributeService;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.repositories.ReportAttributeRepository;
@Component
public class ReportAttributeServiceImpl  implements ReportAttributeService{
	
	@Autowired
	private ReportAttributeRepository reportAttributeRepository;
	 @Override
	    public List<ReportAttribute> listAll() {
		 List<ReportAttribute> reportAttributes = new ArrayList<>();
	        return reportAttributes;
	       }
		
	 public List<ReportAttribute> constructfilterquery(ReportAttribute r) {
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
	public List<ReportAttribute> constructfilterquery1(ReportAttribute filter) {

			return reportAttributeRepository.findAll(new Specification<ReportAttribute>() {

				@Override
				public Predicate toPredicate(Root<ReportAttribute> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
package iwomi.base.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import iwomi.base.ServiceInterface.NomenclatureService;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.repositories.NomenclatureRepository;

@Component
public class NomenclatureServiceImpl implements NomenclatureService {
	@Autowired
	private NomenclatureRepository nomenclatureRepository;
	private int dele = 0;
	private Date now;

	@Override
	public List<Nomenclature> listAll() {
		List<Nomenclature> nomenclatures = new ArrayList<>();
		return nomenclatureRepository.findAll(); // fun with Java 8
	}

	public Nomenclature save(Nomenclature n) {
		return nomenclatureRepository.save(setdefaults(n));
	}

	public Nomenclature setdefaults(Nomenclature n) {
		now = new java.sql.Timestamp(System.currentTimeMillis());
		n.setDele(dele);
		n.setMdfi(now);
		if (n.getId() == null) {
//			n.setCrtd(now.toString());
			n.setCrtd("1511");
		}
		return n;
	}

	public Nomenclature delete(long id) {
		Nomenclature n = setdefaults(nomenclatureRepository.findById(id));
		n.setDele(1);
		return nomenclatureRepository.save(n);
	}

	 @SuppressWarnings("unchecked")
	public List<Nomenclature> constructfilterquery2(Nomenclature filter) {

			return nomenclatureRepository.findAll(new Specification<Nomenclature>() {

				@Override
				public Predicate toPredicate(Root<Nomenclature> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();

					predicates.add(cb.equal(root.get("tabcd"), filter.getTabcd()));

					// If attribute is specified in filter, add contains (lile)
					// filter to where clause with ignore case
					if (filter.getAcscd() != null) {
						predicates.add(cb.like(cb.lower(root.get("acscd")), 
	                                                    "%" + filter.getAcscd().toLowerCase() + "%"));
					}

					// If lastName is specified in filter, add contains (lile)
					// filter to where clause with ignore case
					if (filter.getLib1() != null) {
						predicates.add(cb.like(cb.lower(root.get("lib1")), 
	                                                    "%" + filter.getLib1().toLowerCase() + "%"));
					}

					return cb.and(predicates.toArray(new Predicate[0]));
				}
				
			});
		}

}
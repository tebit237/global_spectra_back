package iwomi.base.repositories;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.form.countReturn;
import iwomi.base.objects.Conn;
import iwomi.base.objects.ReportAttributeS;

public interface ReportAttributeRepositoryS extends CrudRepository<ReportAttributeS, Long>, JpaSpecificationExecutor {
	ReportAttributeS findById(Long id);

	ReportAttributeS findByAtt(String attr);

	List<ReportAttributeS> findByAttAndDele(String attr, Boolean d);

	ReportAttributeS findByAttAndIdNot(String attr, long id);

	List<ReportAttributeS> findByIdAndDele(Long id, Boolean g);

	List<ReportAttributeS> findFirst20ByDeleOrderByIdDesc(Boolean dele);

	List<ReportAttributeS> findByDeleOrderByIdDesc(Boolean dele);

	@Query(value = "SELECT e.* FROM srpatt e ?1  ", nativeQuery = true)
	List<ReportAttributeS> filtersearch(String s);

	@Query(value = "SELECT MAX(e.id) as s FROM srpatt e ", nativeQuery = true)
	List<BigDecimal> getMax();

//	@Query(value =  "SELECT e.* FROM rpatt e WHERE ?1  ",nativeQuery=true)
//	List<ReportAttributeS> findquByDele(Integer dele);
	@Query(value = "SELECT e.* FROM srpatt e WHERE ?1  ", nativeQuery = true)
	List<ReportAttributeS> findreporsir(String s);

	List<ReportAttributeS> findByDeleAndAttContaining(Boolean dele, String s);

	void deleteById(Long id);

	List<ReportAttributeS> findByDele(Boolean dele);
//	List<ReportAttributeS> findByDele(Boolean dele,Pageable topTen);
}
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
import iwomi.base.objects.ReportAttribute;

public interface ReportAttributeRepository extends CrudRepository<ReportAttribute, Long>, JpaSpecificationExecutor {
	ReportAttribute findById(Long id);

	ReportAttribute findByAtt(String attr);

	List<ReportAttribute> findByAttAndDele(String attr, Boolean d);

	ReportAttribute findByAttAndIdNot(String attr, long id);

	List<ReportAttribute> findByIdAndDele(Long id, Boolean g);

	List<ReportAttribute> findFirst20ByDeleOrderByIdDesc(Boolean dele);

	List<ReportAttribute> findByDeleOrderByIdDesc(Boolean dele);

	List<ReportAttribute> findByDeleOrderByMdfiDesc(Boolean dele);

	@Query(value = "SELECT e.* FROM rpatt e ?1  ", nativeQuery = true)
	List<ReportAttribute> filtersearch(String s);

	@Query(value = "SELECT COALESCE(MAX(e.id),0) as s FROM rpatt e ", nativeQuery = true)
	List<BigDecimal> getMax();

//	@Query(value =  "SELECT e.* FROM rpatt e WHERE ?1  ",nativeQuery=true)
//	List<ReportAttribute> findquByDele(Integer dele);
	@Query(value = "SELECT e.* FROM rpatt e WHERE ?1  ", nativeQuery = true)
	List<ReportAttribute> findreporsir(String s);

	List<ReportAttribute> findByDeleAndAttContaining(Boolean dele, String s);

	void deleteById(Long id);

	List<ReportAttribute> findByDele(Boolean dele);
//	List<ReportAttribute> findByDele(Boolean dele,Pageable topTen);
}
package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportPostS;

public interface ReportPostRepositoryS extends JpaRepository<ReportPostS, Long>{
	List<ReportPostS>findReportPostSByDele(Long dele);
	List<ReportPostS>findReportPostSByCodep(String codep);
	void deleteById(Long s);
	List<ReportPostS>findReportPostSByCodepAndDele(String codep,long l);
	ReportPostS findById(Long id);
	@Query(value = "SELECT MAX(e.id) as s FROM srppost e ", nativeQuery = true)
	List<BigDecimal> getMax();
}

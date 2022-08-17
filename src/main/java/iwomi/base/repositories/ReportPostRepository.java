package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportPost;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long>{
	List<ReportPost>findReportPostByDele(Long dele);
	List<ReportPost>findReportPostByCodep(String codep);
	void deleteById(Long s);
	List<ReportPost>findReportPostByCodepAndDele(String codep,long l);
	ReportPost findById(Long id);
	@Query(value = "SELECT COALESCE(MAX(e.id),0) as s FROM rppost e ", nativeQuery = true)
	List<BigDecimal> getMax();
	
}

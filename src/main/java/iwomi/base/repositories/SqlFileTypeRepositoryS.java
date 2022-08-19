package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportPost;
import iwomi.base.objects.SqlFileTypeS;

public interface SqlFileTypeRepositoryS extends JpaRepository<SqlFileTypeS, Long>{
	List<SqlFileTypeS>findSqlFileTypeSByDarAndFichiAndEtab(Date date,String fichi,String Etab);
	@Query(value = "SELECT * FROM ssqltype e WHERE e.dar = to_date(?1,'YYYY-MM-DD') and fichi = ?2 ", nativeQuery = true)
	List<SqlFileTypeS>findSqlFileTypeSByDarAndFichi(String date,String fichi);

	SqlFileTypeS findById(Long id);
	void deleteById(Long id);
	
}

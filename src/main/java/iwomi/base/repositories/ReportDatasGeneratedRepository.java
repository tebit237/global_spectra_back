/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.ReportDatasGenerated;
import java.util.Date;
import java.util.List;
import javax.persistence.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */
public interface ReportDatasGeneratedRepository extends JpaRepository<ReportDatasGenerated, Long> {

//https://stackoverflow.com/questions/25380984/how-to-sort-by-multiple-properties-in-spring-data-jpa-derived-queries  

	// List<ReportDatasGenerated>
	// findReportDatasGeneratedByDateOrderByFichierAscPostAsc(Date date);
	@Query(value = "SELECT * FROM rprep e WHERE e.dar = ?1   ORDER BY e.fichier Asc, e.rang Asc,e.col Asc", nativeQuery = true)
	List<ReportDatasGenerated> findReportDatasGeneratedByDarOrderByFichierAscRangAscColAsc(String dar);

	@Query(value = "SELECT * FROM rprep e WHERE e.dar = to_date(?1,'YYYY-MM-DD')  and e.fichier = ?2 and e.status = ?3 and rang > 0  ORDER BY e.fichier Asc, to_number(e.rang) Asc,to_number(e.col) Asc", nativeQuery = true)
	List<ReportDatasGenerated> findReportDatasGeneratedByDarOrderByFichierAscRangAscColAsc(String dar, String fichier,
			int stat);
}

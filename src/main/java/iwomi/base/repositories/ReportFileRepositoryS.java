package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import iwomi.base.objects.ReportFileS;

@Repository
public interface ReportFileRepositoryS extends JpaRepository<ReportFileS, Long> {

	List<ReportFileS> findReportFileSByFich(String fich);

	List<ReportFileS> findReportFileSByFichAndPoste(String fich, String poste);

	List<ReportFileS> findReportFileSByFichOrderByRangAscColAsc(String fich);
	
	@Query(value = "SELECT * FROM srppfich e WHERE e.fich = ?1 AND rownum<=?3 and rownum>=?	2  ORDER BY e.rang Asc, e.col Asc  ",nativeQuery=true)
	List<ReportFileS> findReportFileSByFichOR(String fich,int lowLimt,int upLimt);
	
	@Query(value = "SELECT * FROM srppfich e WHERE e.fich = ?1 AND e.poste LIKE %?4%  ORDER BY e.rang Asc, e.col Asc OFFSET ?2 ROWS FETCH NEXT ?3  ROWS ONLY ",nativeQuery=true)
	List<ReportFileS> findReportFileSByFichOrSearch(String fich,int lowLimt,int upLimt,String val);
	
	@Query(value = "SELECT * FROM srppfich e WHERE e.fich = ?1 AND e.poste LIKE %?2%  ORDER BY e.rang Asc, e.col Asc",nativeQuery=true)
	List<ReportFileS> findReportFileSSeach(String fich,String val);

	List<ReportFileS> findReportFileSByFichContaining(String fich);

	ReportFileS findById(Long id);
	
	Long countByFichAndPosteContaining(String fich,String post);
	
	Long countByFich(String fich);
	
	Long countByFichAndSource(String fich ,String s);
	Long countByFichAndSourceAndPosteContaining(String fich ,String s,String p);

	List<ReportFileS> findReportFileSByFichAndPosteContainingOrderByRangAscColAsc(String fich,String poste);
	
	List<ReportFileS> findReportFileSByFichAndPosteContainingOrGenContainingOrderByRangAscColAsc(String fich,String poste,String gen);
	
	List<ReportFileS> findReportFileSByFichAndPosteAndCol(String fich,String poste,Long col);
	
	
}

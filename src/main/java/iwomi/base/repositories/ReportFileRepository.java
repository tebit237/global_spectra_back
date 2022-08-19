package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import iwomi.base.objects.ReportFile;
import java.math.BigDecimal;
import java.math.BigInteger;

@Repository
public interface ReportFileRepository extends JpaRepository<ReportFile, Long> {

    List<ReportFile> findReportFileByFich(String fich);
//	ReportFile findReportFileByFichAndPosteAndCol(String fich,String poste,Long col);

    List<ReportFile> findReportFileByFichAndPoste(String fich, String poste);

    List<ReportFile> findReportFileByFichOrderByRangAscColAsc(String fich);

    @Query(value = "SELECT * FROM rppfich e WHERE e.fich = ?1 AND rownum<=?3 and rownum>=?	2  ORDER BY e.rang Asc, e.col Asc  ", nativeQuery = true)
    List<ReportFile> findReportFileByFichOR(String fich, int lowLimt, int upLimt);

    @Query(value = "SELECT * FROM rppfich e WHERE e.fich = ?1 AND e.poste LIKE %?4%  ORDER BY e.rang Asc, e.col Asc OFFSET ?2 ROWS FETCH NEXT ?3  ROWS ONLY ", nativeQuery = true)
    List<ReportFile> findReportFileByFichOrSearch(String fich, int lowLimt, int upLimt, String val);

    @Query(value = "SELECT * FROM rppfich e WHERE e.fich = ?1 AND e.poste LIKE %?2%  ORDER BY e.rang Asc, e.col Asc", nativeQuery = true)
    List<ReportFile> findReportFileSeach(String fich, String val);

    @Query(value = "SELECT e.* FROM rppfich e WHERE e.fich = ?1 AND e.poste =?2 ", nativeQuery = true)
    List<ReportFile> getabove(String fich, String val);

    @Query(value = "SELECT MAX(e.id) as s FROM rppfich e ", nativeQuery = true)
    List<BigDecimal> getMax();

    List<ReportFile> findReportFileByFichContaining(String fich);

    ReportFile findById(Long id);

    Long countByFichAndPosteContaining(String fich, String post);

    Long countByFich(String fich);

    Long countByFichAndSource(String fich, String s);

    Long countByFichAndSourceAndPosteContaining(String fich, String s, String p);

    List<ReportFile> findReportFileByFichAndPosteContainingOrderByRangAscColAsc(String fich, String poste);

    List<ReportFile> findReportFileByFichAndPosteContainingOrGenContainingOrderByRangAscColAsc(String fich, String poste, String gen);

    List<ReportFile> findReportFileByFichAndPosteAndCol(String fich, String poste, Long col);

}

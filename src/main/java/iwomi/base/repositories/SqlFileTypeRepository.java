package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportPost;
import iwomi.base.objects.SqlFileType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface SqlFileTypeRepository extends JpaRepository<SqlFileType, Long> {

    @Query(value = "SELECT count(*) FROM sqltype e WHERE e.dar = to_date(?2,'YYYY-MM-DD') and fichi = ?1 ", nativeQuery = true)
    long countByFichierAndDar(String fichi, String dar);

    List<SqlFileType> findSqlFileTypeByDarAndFichiAndEtab(Date date, String fichi, String Etab);

    @Query(value = "SELECT * FROM sqltype e WHERE e.dar = to_date(?1,'YYYY-MM-DD') and fichi = ?2 AND etab = ?3 order by id desc OFFSET ?4 ROWS FETCH NEXT ?5  ROWS ONLY  ", nativeQuery = true)
    List<SqlFileType> findSqlFileTypeByDarAndFichiAndEtab1(String date, String fichi, String Etab, int l, int t);

    @Query(value = "SELECT * FROM sqltype e WHERE e.dar = to_date(?1,'YYYY-MM-DD') and fichi = ?2 ", nativeQuery = true)
    List<SqlFileType> findSqlFileTypeByDarAndFichi(String date, String fichi);

    SqlFileType findById(Long id);

    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "update sqltype switch case  set nbtraite = nbtotal, statut = 1 where code_unique = ?1 and codefichier = ?2", nativeQuery = true)
    void opdatefinaltraitement(String codeUnique, String codefichier);
}

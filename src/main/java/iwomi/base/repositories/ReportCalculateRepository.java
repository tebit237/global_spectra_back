package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportRep;
import java.math.BigInteger;

public interface ReportCalculateRepository extends CrudRepository<ReportCalculate, Long>, JpaSpecificationExecutor {
//	@Query(value = "SELECT * FROM rpcalc e WHERE e.fichi = ?2 and e.dele=?1  ORDER BY  e.mdfi DESC FETCH FIRST 10  ROWS ONLY  ",nativeQuery=true)

    @Query(value = "SELECT * FROM rpcalc e WHERE e.fichi = ?2 and e.dele=?1 AND rownum<21  ORDER BY  e.id DESC", nativeQuery = true)
    List<ReportCalculate> findByDeleAndFichi(Long l, String s);

    List<ReportCalculate> findByFichi(String s);

    List<ReportCalculate> findByDeleAndFichiAndPostAndCol(Long f, String s, String e, String t);

    ReportCalculate findById(Long s);

    void deleteById(Long s);

    List<ReportCalculate> findByDeleAndField(Long d, String s);
//	List<ReportCalculate> findByDele(Long d);
//	List<ReportCalculate> findbyDeleAndFichiAndcolAndPost(Long d,String f,String c,String e);

    @Query(value = "SELECT * FROM rpcalc e WHERE e.fichi = ?1 and e.dele=0 AND col = ?2 and post = ?3  ORDER BY  e.mdfi DESC", nativeQuery = true)
    List<ReportCalculate> findbyelm(String f, String c, String e);

    @Query(value = "SELECT e.* FROM rpcalc e WHERE typeval = 'P' ", nativeQuery = true)
    List<ReportCalculate> findallPost();

    List<ReportCalculate> findByFichiAndPostAndCol(String f, String c, String e);

    List<ReportCalculate> findByFichiAndField(String fichi, String field);

    List<ReportCalculate> findByDeleAndFichiAndField(Long s, String fichi, String field);

    @Query(value = "SELECT MAX(e.id) as s FROM rpcalc e ", nativeQuery = true)
    List<BigInteger> getMax();

    @Query(value = "select * from rpcalc where fichi = ?1 and calc like ?2 ", nativeQuery = true)
    List<ReportCalculate> findOthers(String f, String rs);
}

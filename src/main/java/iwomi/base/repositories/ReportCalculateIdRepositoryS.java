package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportCalculateS2;
import iwomi.base.objects.ReportCalculeIdS;

public interface ReportCalculateIdRepositoryS extends CrudRepository<ReportCalculateS2, ReportCalculeIdS>, JpaSpecificationExecutor {
    @Query(value = "select * from (select rownum r,rpcalc.* from rpcalc WHERE fichi = ?2 and dele=?1 order by mdfi desc) where rownum < 21 ", nativeQuery = true)
    List<ReportCalculateS2> findByDeleAndFichi(Long l, String s);
    @Query(value = "SELECT * from rpcalc where (?1 is null or fichi=?1)and (?2 is null or field like ?2)and (?3 is null or post=?3)and (?4 is null or tysorce=?4)", nativeQuery = true)
    List<ReportCalculateS2> findByDeleAndFichit(String fh, String fl, String p, String s);    
    @Query(value = "SELECT max(id)+1 from rpcalc", nativeQuery = true)
    List<BigDecimal> getMax();
    List<ReportCalculateS2> findByFichiAndField(String fichi, String field);

    @Query(value = "SELECT * from rpcalc where fichi=?1 and post =?2", nativeQuery = true)
    List<ReportCalculateS2> findByFilePost(String fh, String fl);

}

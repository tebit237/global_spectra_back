package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportCalculateS;
public interface ReportCalculateRepositoryS extends CrudRepository<ReportCalculateS, Long>,JpaSpecificationExecutor {
//	@Query(value =  "SELECT * FROM (select* from srpcalc order by mdfi desc) e WHERE e.fichi = ?2 and e.dele= ?1 AND rownum<21",nativeQuery=true)
	@Query(value =  "SELECT e.* FROM (select rownum r,srpcalc.* from srpcalc WHERE fichi = ?2 and dele= ?1 order by mdfi desc) e where e.r<21",nativeQuery=true)
	List<ReportCalculateS> findByDeleAndFichi(Long l,String s);
	List<ReportCalculateS> findByFichi(String s);
	List<ReportCalculateS> findByDeleAndFichiAndPostAndCol(Long f,String s,String e,String t);
	ReportCalculateS findById(Long s);
	void deleteById(Long s);
	List<ReportCalculateS> findByDeleAndField(Long d,String s);
	@Query(value =  "SELECT * FROM srpcalc e WHERE e.fichi = ?1 and e.dele=0 AND col = ?2 and post = ?3  ORDER BY  e.mdfi DESC",nativeQuery=true)
	List<ReportCalculateS>  findbyelm(String f,String c,String e);
	@Query(value =  "SELECT e.* FROM srpcalc e WHERE typeval = 'P' ",nativeQuery=true)
	List<ReportCalculateS>  findallPost();
	List<ReportCalculateS>  findByFichiAndPostAndCol(String f,String c,String e);
	List<ReportCalculateS>  findByFichiAndField(String fichi,String field);
	List<ReportCalculateS>  findByDeleAndFichiAndField(Long s,String fichi,String field);
	@Query(value = "SELECT MAX(e.id) as s FROM srpcalc e ", nativeQuery = true)
	List<BigDecimal> getMax();
}

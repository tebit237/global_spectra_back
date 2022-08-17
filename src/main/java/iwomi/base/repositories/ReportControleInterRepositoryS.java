/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportControleIntraSS;
import iwomi.base.objects.ReportPostS;
import iwomi.base.objects.ReportControleInterSS;




/**
 *
 * @author User
 */
public interface ReportControleInterRepositoryS extends CrudRepository<ReportControleInterSS, Long>,JpaSpecificationExecutor {
	
	@Query(value = "SELECT * FROM srpctrlt e WHERE e.fich = ?1   ORDER BY e.cd Asc, e.cg Asc",nativeQuery=true)
	List<ReportControleInterSS> findReportControle1(String fich);
	
	@Query(value = "SELECT * FROM srpctrlt e   ORDER BY e.fich Asc, e.post Asc",nativeQuery=true)
	List<ReportControleInterSS> findReportControleAll();
//	@Query(value = "SELECT * FROM srpctrl e WHERE e.dele = ? ORDER BY e.mdfi DESC limit 10",nativeQuery=true)
	@Query(value = "SELECT * FROM srpctrlt e WHERE e.dele = ? AND rownum<9999 ORDER BY e.mdfi DESC",nativeQuery=true)
	List<ReportControleInterSS> getInterControls(String s);
	ReportControleInterSS getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT MAX(e.id) as s FROM srpctrlt e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleInterSS findById(Long id);

	ReportControleInterSS save(ReportPostS t);
	//after correcting intra to inter
	@Query(value = "SELECT count(id) as s FROM srpctrlt e ", nativeQuery = true)
	List<BigDecimal> countIntra();
}

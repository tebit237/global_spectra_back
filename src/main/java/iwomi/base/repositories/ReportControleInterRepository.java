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

import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportPost;
import java.math.BigInteger;




/**
 *
 * @author User
 */
public interface ReportControleInterRepository extends CrudRepository<ReportControleInter, Long>,JpaSpecificationExecutor {
	
	@Query(value = "SELECT * FROM rpctrl e WHERE e.fich = ?1   ORDER BY e.cd Asc, e.cg Asc",nativeQuery=true)
	List<ReportControleInter> findReportControle1(String fich);
	
	@Query(value = "SELECT * FROM rpctrl e   ORDER BY e.fich Asc, e.post Asc",nativeQuery=true)
	List<ReportControleInter> findReportControleAll();
//	@Query(value = "SELECT * FROM rpctrl e WHERE e.dele = ? ORDER BY e.mdfi DESC limit 10",nativeQuery=true)
	@Query(value = "SELECT * FROM rpctrl e WHERE e.dele = ? ORDER BY e.mdfi DESC",nativeQuery=true)
	List<ReportControleInter> getInterControls(String s);
	ReportControleInter getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT MAX(e.id) as s FROM rpctrl e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleInter findById(Long id);

	ReportControleInter save(ReportPost t);
	//after correcting intra to inter
	@Query(value = "SELECT count(id) as s FROM rpctrlt e ", nativeQuery = true)
	List<BigDecimal> countIntra();
	@Query(value = "SELECT * FROM rpctrl where ctrid = ?1 ", nativeQuery = true)
	ReportControleInter getCtrid(Long d);
}

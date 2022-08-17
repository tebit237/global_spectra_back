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
import iwomi.base.objects.ReportControleComplexS;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportPost;
import java.math.BigInteger;




/**
 *
 * @author User
 */
public interface ReportControleComplexRepositoryS extends CrudRepository<ReportControleComplexS, Long>,JpaSpecificationExecutor {
	
	
	@Query(value = "SELECT * FROM srpctrcx e   ORDER BY e.descp Asc",nativeQuery=true)
	List<ReportControleComplexS> findReportControleAll();

	@Query(value = "SELECT * FROM srpctrcx e WHERE e.dele = ? ORDER BY e.mdfi DESC",nativeQuery=true)
	List<ReportControleComplexS> getComplexControls(String s);
	ReportControleComplexS getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT NVL(MAX(e.id),0) as s FROM srpctrcx e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleComplexS findById(Long id);
	ReportControleComplexS save(ReportPost t);
	@Query(value = "SELECT count(id) as s FROM srpctrcx e ", nativeQuery = true)
	List<BigDecimal> countComplexe();
}

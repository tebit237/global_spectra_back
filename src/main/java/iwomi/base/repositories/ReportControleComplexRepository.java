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
import iwomi.base.objects.ReportControleComplex;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportPost;
import java.math.BigInteger;
import javax.transaction.Transactional;




/**
 *
 * @author User
 */
public interface ReportControleComplexRepository extends CrudRepository<ReportControleComplex, Long>,JpaSpecificationExecutor {
	
	
	@Query(value = "SELECT * FROM rpctrcx e   ORDER BY e.descp Asc",nativeQuery=true)
	List<ReportControleComplex> findReportControleAll();

	@Query(value = "SELECT * FROM rpctrcx e WHERE e.dele = ? ORDER BY e.mdfi DESC",nativeQuery=true)
	List<ReportControleComplex> getComplexControls(String s);
	ReportControleComplex getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT NVL(MAX(e.id),0) as s FROM rpctrcx e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleComplex findById(Long id);
	ReportControleComplex save(ReportPost t);
	@Query(value = "SELECT count(id) as s FROM rpctrcx e ", nativeQuery = true)
	List<Integer> countComplexe();
        @Transactional
        ReportControleComplex save(ReportControleComplex d);
}

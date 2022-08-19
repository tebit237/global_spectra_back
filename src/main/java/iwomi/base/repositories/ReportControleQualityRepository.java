/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportControleComplex;
import iwomi.base.objects.ReportControleQuality;
import iwomi.base.objects.ReportPost;
import java.math.BigInteger;




/**
 *
 * @author User
 */
public interface ReportControleQualityRepository extends CrudRepository<ReportControleQuality, Long>,JpaSpecificationExecutor {
	
	
	@Query(value = "SELECT * FROM rpctrq e   ORDER BY e.descp Asc",nativeQuery=true)
	List<ReportControleQuality> findReportControleAll();

	@Query(value = "SELECT * FROM rpctrq e WHERE e.dele = ? ORDER BY e.id DESC",nativeQuery=true)
	List<ReportControleQuality> getQualityControls(String s);
	ReportControleQuality getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT NVL(MAX(e.id),0) as s FROM rpctrq e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleQuality findById(Long id);

	ReportControleComplex save(ReportPost t);
	@Query(value = "SELECT count(id) as s FROM rpctrq e ", nativeQuery = true)
	List<BigDecimal> countQuality();
}

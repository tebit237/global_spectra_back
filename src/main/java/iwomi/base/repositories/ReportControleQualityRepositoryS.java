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
import iwomi.base.objects.ReportControleComplexS;
import iwomi.base.objects.ReportControleQualityS;
import iwomi.base.objects.ReportPostS;




/**
 *
 * @author User
 */
public interface ReportControleQualityRepositoryS extends CrudRepository<ReportControleQualityS, Long>,JpaSpecificationExecutor {
	
	
	@Query(value = "SELECT * FROM srpctrq e   ORDER BY e.descp Asc",nativeQuery=true)
	List<ReportControleQualityS> findReportControleAll();

	@Query(value = "SELECT * FROM srpctrq e WHERE e.dele = ? ORDER BY e.id DESC",nativeQuery=true)
	List<ReportControleQualityS> getQualityControls(String s);
	ReportControleQualityS getOne(Long s);
	void deleteById(Long s);
	@Query(value = "SELECT NVL(MAX(e.id),0) as s FROM srpctrq e ", nativeQuery = true)
	List<BigDecimal> getMax();
	ReportControleQualityS findById(Long id);

	ReportControleComplexS save(ReportPost t);
	@Query(value = "SELECT count(id) as s FROM srpctrq e ", nativeQuery = true)
	List<BigDecimal> countQuality();
}

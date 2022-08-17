/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportControleIntra;
import java.math.BigInteger;

/**
 *
 * @author User
 */
public interface ReportControleIntraRepository extends CrudRepository<ReportControleIntra, Long>,JpaSpecificationExecutor {
	List<ReportControleIntra> findByDele(String d);
	@Query(value = "SELECT * FROM rpctrlt where dele = 0 ",nativeQuery=true)
	List<ReportControleIntra> findIntra();
	ReportControleIntra findById(Long d);
	@Query(value = "SELECT MAX(e.id) as s FROM rpctrlt e ", nativeQuery = true)
	List<BigDecimal> getMax();
	void deleteById(Long id);
	//after correcting intra to inter
	@Query(value = "SELECT count(id) as s FROM rpctrl e ", nativeQuery = true)
	List<BigDecimal> countInter();
}

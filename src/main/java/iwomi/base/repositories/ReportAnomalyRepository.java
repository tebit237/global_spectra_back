/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportResult;

/**
 *
 * @author UserR
 */
public interface ReportAnomalyRepository extends  CrudRepository<ReportAnomaly, Long>,JpaSpecificationExecutor{

    @Query(value="delete from rprep where dar = ?1")
    void deleteByDar(String s);
    List<ReportAnomaly> findByDar(Date date);
    @Query(value = "SELECT * FROM rpanom e  where dar = to_date(?1,'yyyy-mm-dd') ORDER BY e.dar Asc, e.type Asc",nativeQuery=true)
     List<ReportAnomaly> findByDar1(String date);
    @Query(value = "SELECT * FROM rpanom e  where dar = to_date(?1,'yyyy-mm-dd') and type = ?2 ORDER BY e.dar Asc, e.type Asc",nativeQuery=true)
     List<ReportAnomaly> findByinterCont(String date,String s);
//    public List<ReportResult> findByEtabAndDarAndFichierAndPostAndCol(String etab,Date dar, String Fichier,String post,String col);
    @Query(value = "SELECT * FROM rpanom e   ORDER BY e.dar Asc, e.type Asc",nativeQuery=true)
    List<ReportAnomaly> findReportAnomalyAll();
}

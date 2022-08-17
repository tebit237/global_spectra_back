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

import iwomi.base.objects.ReportAnomalyS;
import iwomi.base.objects.ReportAttributeS;
import iwomi.base.objects.ReportResultS;

/**
 *
 * @author UserR
 */
public interface ReportAnomalyRepositoryS extends  CrudRepository<ReportAnomalyS, Long>,JpaSpecificationExecutor{

    @Query(value="delete from srprep where dar = ?1")
    void deleteByDar(String s);
    List<ReportAnomalyS> findByDar(Date date);
    @Query(value = "SELECT * FROM srpanom e  where dar = to_date(?1,'yyyy-mm-dd') ORDER BY e.dar Asc, e.type Asc",nativeQuery=true)
     List<ReportAnomalyS> findByDar1(String date);
//    public List<ReportResult> findByEtabAndDarAndFichierAndPostAndCol(String etab,Date dar, String Fichier,String post,String col);
    @Query(value = "SELECT * FROM srpanom e   ORDER BY e.dar Asc, e.type Asc",nativeQuery=true)
    List<ReportAnomalyS> findReportAnomalyAll();
}

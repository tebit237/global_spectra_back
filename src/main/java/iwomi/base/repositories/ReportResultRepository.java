/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.ReportResult;
//import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface ReportResultRepository extends JpaRepository<ReportResult, Long>{
    @Query(value="delete from rprep where dar = ?1")
    void deleteByDar(String s);
    public List<ReportResult> findByEtabAndDarAndFichierAndPostAndCol(String etab,String dar, String Fichier,String post,String col);
}

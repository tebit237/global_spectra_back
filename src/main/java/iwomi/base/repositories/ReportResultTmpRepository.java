/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.ReportResultTmp;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface ReportResultTmpRepository extends JpaRepository<ReportResultTmp, Long>{
    List<ReportResultTmp> deleteByDar(String s);
    List<ReportResultTmp> findByDar(String dar);
    public List<ReportResultTmp> findByEtabAndDarAndFichierAndPostAndCol(String etab,String dar, String Fichier,String post,String col);
}

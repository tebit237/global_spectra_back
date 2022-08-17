/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.ReportResultTmpS;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface ReportResultTmpRepositoryS extends JpaRepository<ReportResultTmpS, Long>{
    List<ReportResultTmpS> deleteByDar(String s);
    List<ReportResultTmpS> findByDar(String dar);
    public List<ReportResultTmpS> findByEtabAndDarAndFichierAndPostAndCol(String etab,String dar, String Fichier,String post,String col);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import iwomi.base.objects.ReportAnomalyTmp;
import iwomi.base.objects.ReportAnomalyTmpS;

/**
 *
 * @author User
 */
public interface ReportAnomalyTmpRepositoryS extends JpaRepository<ReportAnomalyTmpS, Long>{

    public List<ReportAnomalyTmpS> findByDar(String date);
    
}

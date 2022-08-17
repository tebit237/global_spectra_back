/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import iwomi.base.objects.ReportAnomalyTmp;

/**
 *
 * @author User
 */
public interface ReportAnomalyTmpRepository extends JpaRepository<ReportAnomalyTmp, Long>{

    public List<ReportAnomalyTmp> findByDar(String date);
    
}

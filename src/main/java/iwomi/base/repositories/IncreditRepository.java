/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.Incredit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */
public interface IncreditRepository extends JpaRepository<Incredit, Long>{
    
    	@Query(value = "SELECT * FROM incre e WHERE e.dar = to_date(?1,'DD/MM/YYYY')",nativeQuery=true)
	List<Incredit> findIncreditByDar(String dar);
    
}

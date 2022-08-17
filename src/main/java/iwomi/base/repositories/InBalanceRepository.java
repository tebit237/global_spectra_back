/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.InBalance;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */

public interface InBalanceRepository extends JpaRepository<InBalance, Long>{
    
//      public  List<InBalance> save(List<InBalance> itrbl);
	@Query(value = "DELETE FROM inbal e WHERE e.dar = ?1",nativeQuery=true)    
        List<InBalance> deleteInBalanceByDar(String dar);
        
        @Query(value = "SELECT * FROM inbal e WHERE e.dar = to_date(?1,'DD/MM/YYYY)",nativeQuery=true)
	List<InBalance> findInbalanceByDar(String dar);
        
}

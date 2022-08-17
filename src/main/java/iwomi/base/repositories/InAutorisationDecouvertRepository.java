/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.InAutorisationDecouvert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */
public interface InAutorisationDecouvertRepository extends JpaRepository<InAutorisationDecouvert, Long>{
    
      // public InAutorisationDecouvert save(List<InAutorisationDecouvert> itrbl);
    	@Query(value = "SELECT e.* FROM inaut e WHERE e.dar = to_date(?1,'yyyy-mm-dd')",nativeQuery=true)
	List<InAutorisationDecouvert> findInAutorisationDecouvertByDar(String dar);
}

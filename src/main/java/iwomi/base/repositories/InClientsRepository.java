/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.InClients;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author fabri
 */
public interface InClientsRepository extends JpaRepository<InClients, Long>{
    
//      public  InClients save(List<InClients> itrbl);
    	@Query(value = "SELECT * FROM incli e WHERE e.dar = to_date(?1,'DD/MM/YYYY')",nativeQuery=true)
	List<InClients> findInClientsByDar(String dar);

}

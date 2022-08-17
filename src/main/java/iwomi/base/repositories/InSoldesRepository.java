/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.InSoldes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */
public interface InSoldesRepository extends JpaRepository<InSoldes, Long>{
    
 //     public  InSoldes save(List<InSoldes> itrbl);
    	@Query(value = "SELECT * FROM insld e WHERE e.dar = to_date(?1,'DD/MM/YYYY')",nativeQuery=true)
	List<InSoldes> findInSoldesByDar(String dar);
    	@Query(value = "SELECT * FROM insld e WHERE e.dar = to_date(?1,'yyyy-MM-dd')",nativeQuery=true)
	List<InSoldes> findInSoldesByDar1(String dar);
        
    
}

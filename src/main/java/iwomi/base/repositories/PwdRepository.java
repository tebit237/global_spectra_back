/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.Pwd;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author TAGNE
 */
public interface PwdRepository extends CrudRepository<Pwd, Long>,JpaSpecificationExecutor {
    
     @Query(value = "SELECT * FROM pwd e WHERE e.acscd = ?1 and e.dele = ?2",nativeQuery=true)
    public Pwd findGfcliByAscd(String acscd, String dele);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.GenFile;
import iwomi.base.objects.GenFileId;
import iwomi.base.objects.Pwd;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author TAGNE
 */
public interface GenFileRepository extends CrudRepository<GenFile, GenFileId>, JpaSpecificationExecutor {

    @Query(value = "SELECT * FROM genfile e WHERE (e.dar = ?1 or ?1 is null) and (e.fich = ?2 or ?2 is null)", nativeQuery = true)
//    @Query(value = "SELECT * FROM genfile ", nativeQuery = true)
    List<GenFile> getGeneratedFilter(Date dar, String fichi);
}

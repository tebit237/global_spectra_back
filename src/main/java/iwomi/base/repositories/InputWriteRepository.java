/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.Indat;
import iwomi.base.objects.PostWriteFiles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fabri
 */
public interface InputWriteRepository extends JpaRepository<PostWriteFiles, Long>{
    @Query(value = "SELECT e.* FROM inputcmt e WHERE e.fichi = ?1",nativeQuery=true)
    List<PostWriteFiles> findcomment(String dar);
    boolean existsByFichi(String foo);
    
}

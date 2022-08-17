/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.LiveTraitement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 *     
 * @author fabri
 */

@Service
public interface LiveTraitementRepository extends JpaRepository <LiveTraitement, Long>{
      
       // LiveTraitement  findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codefichier);  
//        LiveTraitement  findLiveTraitementByCodeUnique(String codeUnique);
        List<LiveTraitement>  findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codeFichier);
        List<LiveTraitement>  findLiveTraitementByCodeUnique(String codeUnique);
        List<LiveTraitement> save (List<LiveTraitement> liveTraitement);
        LiveTraitement save (LiveTraitement liveTraitement);



} 
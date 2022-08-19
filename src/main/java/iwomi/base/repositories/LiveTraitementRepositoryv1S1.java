/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.LiveTraitementS;
import iwomi.base.objects.LiveTraitementv1S;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 *     
 * @author fabri
 */

@Service
public interface LiveTraitementRepositoryv1S1 extends JpaRepository <LiveTraitementv1S, Long>{
      
       // LiveTraitement  findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codefichier);  
//        LiveTraitement  findLiveTraitementByCodeUnique(String codeUnique);
        List<LiveTraitementv1S>  findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codeFichier);
        List<LiveTraitementv1S>  findLiveTraitementByCodeUnique(String codeUnique);
       



} 
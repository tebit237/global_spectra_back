/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.repositories;

import iwomi.base.objects.LiveTraitementS;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fabri
 */
@Service
public interface LiveTraitementRepositoryS extends JpaRepository<LiveTraitementS, Long> {

    // LiveTraitement  findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codefichier);  
//        LiveTraitement  findLiveTraitementByCodeUnique(String codeUnique);
    List<LiveTraitementS> findLiveTraitementByCodeUniqueAndCodefichier(String codeUnique, String codeFichier);

    List<LiveTraitementS> findLiveTraitementByCodeUnique(String codeUnique);

    List<LiveTraitementS> save(List<LiveTraitementS> liveTraitement);

    LiveTraitementS save(LiveTraitementS liveTraitement);

    @Modifying
    @Transactional
    @Query(value = "update slive_trait1 set nbtraite = case when  nbtraite + ?3 < nbtotal - 10 then nbtraite + ?3 else nbtraite end where code_unique = ?1 and codefichier = ?2", nativeQuery = true)
    void updatelivetreatm( String codeUnique, String codefichier, Long adder);
    
    @Modifying
    @Transactional
    @Query(value = "update slive_trait1 set nbtraite = nbtraite + ?3 where code_unique = ?1 and codefichier = ?2", nativeQuery = true)
    void updateQueryprogress( String codeUnique, String codefichier, Long adder);

    
//    @Query(value = "update LiveTraitementS t set t.nbtraite = t.nbtraite + :adder where t.codeUnique = :codeUnique and t.codefichier = :codefichier", nativeQuery = true)
//    void updatelivetreatm(@Param("codeUnique") String codeUnique, @Param("codefichier") String codefichier,@Param("adder") Long adder);

    @Modifying
    @Transactional
    @Query(value = "update slive_trait1 set nbtraite = nbtotal, statut = 1 where code_unique = ?1 and codefichier = ?2", nativeQuery = true)
    void opdatefinaltraitement(String codeUnique, String codefichier);
    
    @Modifying
    @Transactional
    @Query(value = "update slive_op set nbtraite = nbtraite+1 where code_unique = ?1", nativeQuery = true)
    void updateoperation(String codeUnique);

}

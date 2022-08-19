package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportAttribute;
import java.math.BigInteger;

public interface NomenclatureRepository extends CrudRepository<Nomenclature, Long>,JpaSpecificationExecutor {
	@Query(value =  "Select g.* from sanm g where g.tabcd = ?1 and g.dele=?2 ORDER BY  g.mdfi DESC",nativeQuery=true)
	List<Nomenclature> findBytabcdAndDelequery(String tabcd,int d);
	@Query(value =  "Select g.* from sanm g where g.tabcd = ?1 and g.dele=?2  and g.acscd = ?3 ORDER BY  g.mdfi DESC",nativeQuery=true)
	List<Nomenclature> findBytabcdAndDeleAndAcscd1(String tabcd,int d,String s);
	Nomenclature findBytabcdAndDeleAndAcscd(String tabcd,int d,String s);
	List<Nomenclature> findByAcscd(String Acscd);
	List<Nomenclature> findBytabcd(String tabcd);
	List<Nomenclature> findByAcscdAndIdNot(String acscd,long id);
	void deleteById(long id);
	Nomenclature findById(Long id);
	List<Nomenclature> findAll();
	Nomenclature getOne(long id);
	@Query(value = "SELECT MAX(e.id) as s FROM sanm e ", nativeQuery = true)
	List<BigInteger> getMax();
        
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3",nativeQuery=true)
    public Nomenclature findTabcdAndAcsd(String string, String access_code_patner, String string0);
    
}

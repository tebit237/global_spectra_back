package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import iwomi.base.objects.Nomenclature;

public interface PostNomenclature extends JpaRepository<Nomenclature,Long> {
//	final String tabcd = "";
//	static List<Nomenclature> findAlls(){
//		return findAllByDeleAndTabcd(1,tabcd);
//	}
	List<Nomenclature> findAllByDeleAndTabcd(int s,String d);

}

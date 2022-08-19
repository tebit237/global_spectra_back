package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportPost;
import iwomi.base.objects.ReportRepTmp;
import iwomi.base.objects.ReportResultTmp;

public interface ReportRepTempRepository extends JpaRepository<ReportRepTmp, Long> {
    List<ReportRepTmp> findByFichierAndPostAndRang(String s,String d,long r);
	@Query(value = "UPDATE rpreptmp SET valm = ?1,status = 1 where fichier = ?2 and post = ?3 and rang = ?4 ", nativeQuery = true)
    void saveChangeFichPostRang(String f,String s,String d,long r);
}

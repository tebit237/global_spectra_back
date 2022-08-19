package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iwomi.base.objects.ReportPost;
import iwomi.base.objects.ReportRepTmpS;
import iwomi.base.objects.ReportResultTmp;

public interface ReportRepTempRepositoryS extends JpaRepository<ReportRepTmpS, Long> {
    List<ReportRepTmpS> findByFichierAndPostAndRang(String s,String d,long r);
	@Query(value = "UPDATE srpreptmp SET valm = ?1,status = 1 where fichier = ?2 and post = ?3 and rang = ?4 ", nativeQuery = true)
    void saveChangeFichPostRang(String f,String s,String d,long r);
}

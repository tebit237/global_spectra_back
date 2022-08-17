package iwomi.base.repositories;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import iwomi.base.form.FileRangLimit;
import iwomi.base.form.countElm;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportRepSS;

public interface ReportRepRepositoryS extends CrudRepository<ReportRepSS, Long>, JpaSpecificationExecutor {
	@Query(value = "SELECT e.* FROM srprep e WHERE e.fichier = ?1 ORDER BY e.rang Asc, e.col Asc ", nativeQuery = true)
	List<ReportRepSS> findRepor(String fich);

	List<ReportRepSS> findByFichier(String fich);

	List<ReportRepSS> findByRangAndFichier(Long id,String f);
	@Query(value = "select * from srprep where dar = to_date(?3,'yyyy-mm-dd') and fichier=?2 and post =?1", nativeQuery = true)
	List<ReportRepSS> findByFichierPostDar(String a, String c, String d);
	@Query(value = "select MAX(rang) rang,MAX(id) mx from srprep ", nativeQuery = true)
	List<BigDecimal>  getMaxFileLine();

	@Query(value = "select * from srprep where dar = to_date(?2,'yyyy-mm-dd') and fichier=?1 ", nativeQuery = true)
	List<ReportRepSS> findByFichierDar(String c, String d);

	List<ReportRepSS> findByFichierAndPostAndColAndDar(String f, String post, String col, Date d);
	List<ReportRepSS> findByFichierAndRangAndColAndDar(String f, Long rang, String col, Date d);
	Long countById(Long id);

	Long countByFichier(String fich);

	Long countByFichierAndDarContaining(String fich, String dar);

	List<ReportRepSS> findByPostAndFichierAndCol(String s, String ss, String sss);

	Long countByFichierAndDarEndsWith(String fich, String dar);

	@Query(value = "SELECT count(e.*) cou FROM srppfich e WHERE e.fich = ?1", nativeQuery = true)
	Object[][] fcountByFichierAndDarEndsWith(String fich);

	@Query(value = "SELECT * FROM srprep e WHERE e.fichier = ?1 AND e.post LIKE %?4%  ORDER BY e.rang Asc, e.col Asc OFFSET ?2 ROWS FETCH NEXT ?3  ROWS ONLY ", nativeQuery = true)
	List<ReportRepSS> findReportRepSByFichierOrSearch(String fich, int lowLimt, int upLimt, String val);

	@Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM srprep e WHERE e.fichier = ?1  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
	List<ReportRepSS> preparedResult(String fich, int lowLimt, int upLimt);

	@Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM srprep e WHERE e.fichier = ?1 AND e.dar = ?4  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
	List<ReportRepSS> preparedResult2(String fich, int lowLimt, int upLimt, String d);

	@Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM srprep e WHERE e.fichier = ?1 AND e.dar LIKE ?4  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
	List<ReportRepSS> preparedResult1(String fich, int lowLimt, int upLimt, String d);
	
	@Query(value = "select p.* from (SELECT rownum kg, mm.* from (SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \r\n"
			+ "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\r\n"
			+ "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\r\n"
			+ "					 ,rownum r FROM (select * from srppfich j where j.fich = ?1) f\r\n"
			+ "			      left join (select g.* from (select * from srprep where fichier =?1) g inner join\r\n"
			+ "					(select post,fichier,col from srprep where fichier = ?1 and dar like to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1) \r\n"
			+ "					q on q.fichier = g.fichier and q.post = g.post and q.col = g.col\r\n"
			+ "					where g.fichier = ?1 AND g.dar LIKE to_date(?2,'yyyy-mm-dd')\r\n"
			+ "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY to_number(f.rang) Asc, to_number(f.col) Asc \r\n"
			+ "					) mm)p where p.kg>?3  AND p.kg<=?4", nativeQuery = true)
	List<ReportRepSS> preparedResult11(String fich, String d, int lowLimt, int upLimt);
	@Query(value = "select p.* from (SELECT rownum kg, mm.* from (SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \r\n"
			+ "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\r\n"
			+ "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\r\n"
			+ "					 ,rownum r FROM (select * from srppfich j where j.fich = ?1 and source = ?5) f\r\n"
			+ "			      left join (select g.* from (select * from srprep where fichier =?1) g inner join\r\n"
			+ "					(select post,fichier,col from srprep where fichier = ?1 and dar like to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1) \r\n"
			+ "					q on q.fichier = g.fichier and q.post = g.post and q.col = g.col\r\n"
			+ "					where g.fichier = ?1 AND g.dar LIKE to_date(?2,'yyyy-mm-dd')\r\n"
			+ "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY f.rang Asc, f.col Asc \r\n"
			+ "					) mm)p where p.kg>?3  AND p.kg<=?4", nativeQuery = true)
	List<ReportRepSS> preparedResult11_F1139( String fich, String d, int lowLimt, int upLimt, String s);

	@Query(value = "select * from\r\n"
			+ "(SELECT e.* ,rownum f FROM srprep e WHERE e.fichier = ?1 AND e.dar =to_date(?2,'yyyy-mm-dd') ORDER BY e.rang Asc, e.col Asc ) where f >?3 and f<= ?4", nativeQuery = true)
	List<ReportRepSS> preparedResult111(String fich, String d, int lowLimt, int upLimt);
	@Query(value = "SELECT * FROM srprep e WHERE e.fichier = ?1 AND e.post LIKE %?2%  ORDER BY e.rang Asc, e.col Asc", nativeQuery = true)
	List<ReportRepSS> findReportFileSeach(String fich, String val);

	@Query(value = "SELECT count(e.*) s FROM srprep e WHERE e.fichier = ?1 ", nativeQuery = true)
	countElm countAll(String fich);

	@Query(value = "SELECT e.* FROM srprep e WHERE e.fichier = ?1 and e.field = ?2 and e.dar = to_date(?3,'yyyy-mm-dd') and e.valm != 999999999999999 ORDER BY e.rang Asc, e.col Asc ", nativeQuery = true)
	List<ReportRepSS> fld(String fichi, String y, String date);

	ReportRepSS findByFichierAndColAndRangAndDar(String fichier, String col, Long rang, Date dar);

	List<ReportRepSS> findByFichierAndRangAndDar(String fichier, Long rang, Date dar);
	
	@Query(value = "SELECT e.* FROM srprep e WHERE e.fichier = ?1  and e.dar = ?2 ORDER BY to_number(e.rang) Asc, to_number(e.col) Asc ", nativeQuery = true)
	List<ReportRepSS> findByFichierAndDarOrderByRangAscColAsc(String fichier, Date dar);

	void deleteByRang(Long id);

	List<ReportRepSS> findByRang(Long id);

}

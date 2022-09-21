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
import iwomi.base.objects.ReportRep;
import iwomi.base.objects.ReportRepSS;
import java.math.BigInteger;
import javax.transaction.Transactional;

public interface ReportRepRepository extends CrudRepository<ReportRep, Long>, JpaSpecificationExecutor {

    @Query(value = "SELECT e.* FROM rprep e WHERE e.fichier = ?1 ORDER BY e.rang Asc, e.col Asc ", nativeQuery = true)
    List<ReportRep> findRepor(String fich);

    List<ReportRep> findByRangAndFichier(Long id, String f);

    List<ReportRep> findByFichier(String fich);

    @Query(value = "select * from rprep where dar = to_date(?3,'yyyy-mm-dd') and fichier=?2 and post =?1", nativeQuery = true)
    List<ReportRep> findByFichierPostDar(String a, String c, String d);

    @Transactional
    Long deleteByFichierAndDar(String fich, Date dar);

    @Query(value = "select MAX(rang) rang,MAX(id) mx from rprep ", nativeQuery = true)
    List<BigDecimal> getMaxFileLine();

    @Query(value = "select COALESCE(MAX(rang),0) rang from rprep where fichier =?1 and dar = to_date(?2,'yyyy-mm-dd')", nativeQuery = true)
    List<BigDecimal> getMaxFileLinev1(String e, String dar);

    @Query(value = "select COALESCE(MAX(id),0) id from rprep", nativeQuery = true)
    List<BigDecimal> getMaxFileLinev2();

    @Query(value = "select * from rprep where dar = to_date(?2,'yyyy-mm-dd') and fichier=?1 ", nativeQuery = true)
    List<ReportRep> findByFichierDar(String c, String d);

    List<ReportRep> findByFichierAndRangAndColAndDar(String f, Long rang, String col, Date d);

    List<ReportRep> findByFichierAndPostAndColAndDar(String f, String post, String col, Date d);

    @Query(value = "select * from rprep where dar = to_date(?4,'yyyy-mm-dd') and fichier = ?1 and post = ?2 and col = ?3 ", nativeQuery = true)
    ReportRep findByFichierAndPostAndColAndDar1(String f, String post, String col, String d);

    Long countById(Long id);

    Long countByFichier(String fich);

    Long countByFichierAndDarContaining(String fich, String dar);

    List<ReportRep> findByPostAndFichierAndCol(String s, String ss, String sss);

    Long countByFichierAndDarEndsWith(String fich, String dar);

    @Query(value = "SELECT count(e.*) cou FROM rppfich e WHERE e.fich = ?1", nativeQuery = true)
    Object[][] fcountByFichierAndDarEndsWith(String fich);

    @Query(value = "SELECT * FROM rprep e WHERE e.fichier = ?1 AND e.post LIKE %?4%  ORDER BY e.rang Asc, e.col Asc OFFSET ?2 ROWS FETCH NEXT ?3  ROWS ONLY ", nativeQuery = true)
    List<ReportRep> findReportRepByFichierOrSearch(String fich, int lowLimt, int upLimt, String val);

    @Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM rprep e WHERE e.fichier = ?1  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
    List<ReportRep> preparedResult(String fich, int lowLimt, int upLimt);

    @Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM rprep e WHERE e.fichier = ?1 AND e.dar = ?4  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
    List<ReportRep> preparedResult2(String fich, int lowLimt, int upLimt, String d);

    @Query(value = "SELECT g.* FROM (SELECT e.*,rownum r FROM rprep e WHERE e.fichier = ?1 AND e.dar LIKE ?4  ORDER BY e.rang Asc, e.col Asc) g WHERE  r > ?2 AND r <= ?3 ", nativeQuery = true)
    List<ReportRep> preparedResult1(String fich, int lowLimt, int upLimt, String d);

    @Query(value = "SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \n"
            + "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\n"
            + "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\n"
            + "                     FROM (select * from rppfich j where j.fich = ?1) f\n"
            + "			      left join (select g.* from (select * from rprep where fichier =?1 AND dar = to_date(?2,'yyyy-mm-dd'))g\n"
            + "				   inner join(select post,fichier,col from rprep where fichier = ?1 and dar = to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1)q\n"
            + "				    on q.fichier = g.fichier and q.post = g.post and q.col = g.col\n"
            + "					where g.fichier = ?1 \n"
            + "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY  f.rang Asc, f.col Asc offset ?3 rows fetch next (?4-?3) rows only", nativeQuery = true)
    List<ReportRep> preparedResult11(String fich, String d, int lowLimt, int upLimt);

    @Query(value = "SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \n"
            + "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\n"
            + "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\n"
            + "                     FROM (select * from rppfich j where j.fich = ?1) f\n"
            + "			      left join (select g.* from (select * from rprep where fichier =?1 AND dar = to_date(?2,'yyyy-mm-dd'))g\n"
            + "				   inner join(select post,fichier,col from rprep where fichier = ?1 and dar = to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1)q\n"
            + "				    on q.fichier = g.fichier and q.post = g.post and q.col = g.col\n"
            + "					where g.fichier = ?1 \n"
            + "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY  f.rang Asc, f.col ", nativeQuery = true)
    List<ReportRep> preparedResult12(String fich, String d);
@Query(value = "SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \n"
            + "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\n"
            + "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\n"
            + "                     FROM (select * from rppfich j where j.fich = ?1) f\n"
            + "			      left join (select g.* from (select * from rprep where fichier =?1 AND dar = to_date(?2,'yyyy-mm-dd'))g\n"
            + "				   inner join(select post,fichier,col from rprep where fichier = ?1 and dar = to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1)q\n"
            + "				    on q.fichier = g.fichier and q.post = g.post and q.col = g.col\n"
            + "					where g.fichier = ?1 \n"
            + "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY  f.rang Asc, f.col ", nativeQuery = true)
    List<ReportRep> extractReport(String fich, String d);


    @Query(value = " SELECT f.col,f.crdt,f.cuser,e.dele,e.doc,f.etab,e.feuille,f.fich fichier,e.field,e.mdfi,e.muser,f.poste post, \n"
            + "					 (case when (f.gen = '***') then f.gen else e.valc end) valc,e.valm,e.valt,e.vald\n"
            + "			      ,e.crtd, e.dar,e.feild,e.filter,e.sens,f.rang,f.id,(case when(status is null)then 0 else status end )status\n"
            + "                     FROM (select * from rppfich j where j.fich = ?1 and source = ?5) f\n"
            + "			      left join (select g.* from (select * from rprep where fichier =?1  AND dar = to_date(?2,'yyyy-mm-dd'))g\n"
            + "				   inner join(select post,fichier,col from rprep where fichier = ?1 and dar = to_date(?2,'yyyy-mm-dd') group by fichier,post,col having count(*)=1)q\n"
            + "				    on q.fichier = g.fichier and q.post = g.post and q.col = g.col\n"
            + "					where g.fichier = ?1\n"
            + "			     ) e on f.poste = e.post and f.fich = e.fichier and f.col=e.col  ORDER BY  f.rang Asc, f.col  Asc offset ?3 rows fetch next (?4-?3) rows only", nativeQuery = true)
    List<ReportRep> preparedResult11_F1139(String fich, String d, int lowLimt, int upLimt, String s);

    @Query(value = "select * from\r\n"
            + "(SELECT e.* ,rownum f FROM rprep e WHERE e.fichier = ?1 AND e.dar =to_date(?2,'yyyy-mm-dd') ORDER BY e.rang Asc, e.col Asc ) where f >?3 and f<= ?4", nativeQuery = true)
    List<ReportRep> preparedResult111(String fich, String d, int lowLimt, int upLimt);

    @Query(value = "SELECT * FROM rprep e WHERE e.fichier = ?1 AND e.post LIKE %?2%  ORDER BY e.rang Asc, e.col Asc", nativeQuery = true)
    List<ReportRep> findReportFileSeach(String fich, String val);

    @Query(value = "SELECT count(e.*) s FROM rprep e WHERE e.fichier = ?1 ", nativeQuery = true)
    countElm countAll(String fich);

    @Query(value = "SELECT e.* FROM rprep e WHERE e.fichier = ?1 and e.field = ?2 and e.dar = to_date(?3,'yyyy-mm-dd') and e.valm != 999999999999999 ORDER BY e.rang Asc, e.col Asc ", nativeQuery = true)
    List<ReportRep> fld(String fichi, String y, String date);

    ReportRep findByFichierAndColAndRangAndDar(String fichier, String col, Long rang, Date dar);

    List<ReportRep> findByFichierAndRangAndDar(String fichier, Long rang, Date dar);

    @Query(value = "SELECT e.* FROM rprep e WHERE e.fichier = ?1  and e.dar = ?2 ORDER BY to_number(e.rang) Asc, to_number(e.col) Asc ", nativeQuery = true)
    List<ReportRep> findByFichierAndDarOrderByRangAscColAsc(String fichier, Date dar);

    @Query(value = "SELECT e.* FROM rprep e WHERE e.fichier = ?1  and e.dar = to_date(?2,'yyyy-mm-dd') ORDER BY to_number(e.rang) Asc, to_number(e.col) Asc ", nativeQuery = true)
    List<ReportRep> findFichierDar(String fichier, String dar);

    void deleteByRang(Long id);

    List<ReportRep> findByRang(Long id);

}

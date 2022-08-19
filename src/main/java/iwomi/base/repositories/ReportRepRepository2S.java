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
import iwomi.base.objects.ReportAttributeS;
import iwomi.base.objects.ReportRepSS;
import iwomi.base.objects.ReportRep2S;
import iwomi.base.objects.ReportRepIdS;

public interface ReportRepRepository2S extends CrudRepository<ReportRep2S, ReportRepIdS> {

	ReportRep2S save(ReportRep2S reportRep2);
	
	List<ReportRep2S> findByRang(Long id);
        List<ReportRep2S> findByRangAndFichier(Long id,String f);
        ReportRep2S findByColAndDarAndFichierAndRang(String id,Date dar,String e,Long r);
}

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
import iwomi.base.objects.ReportRep2;
import iwomi.base.objects.ReportRepId;

public interface ReportRepRepository2 extends CrudRepository<ReportRep2, ReportRepId> {

	ReportRep2 save(ReportRep2 reportRep2);
	
	List<ReportRep2> findByRang(Long id);

}

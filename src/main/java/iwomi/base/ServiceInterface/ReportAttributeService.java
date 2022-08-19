package iwomi.base.ServiceInterface;

import java.util.List;

import iwomi.base.objects.ReportAttribute;

public interface ReportAttributeService {
	List<ReportAttribute> listAll();
	 List<ReportAttribute> constructfilterquery(ReportAttribute s);
	List<ReportAttribute> constructfilterquery1(ReportAttribute s);
	 
}

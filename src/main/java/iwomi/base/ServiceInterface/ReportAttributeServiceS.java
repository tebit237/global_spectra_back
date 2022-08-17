package iwomi.base.ServiceInterface;

import java.util.List;

import iwomi.base.objects.ReportAttributeS;

public interface ReportAttributeServiceS {
	List<ReportAttributeS> listAll();
	 List<ReportAttributeS> constructfilterquery(ReportAttributeS s);
	List<ReportAttributeS> constructfilterquery1(ReportAttributeS s);
	 
}

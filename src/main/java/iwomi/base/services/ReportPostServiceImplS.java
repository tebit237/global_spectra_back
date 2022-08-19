package iwomi.base.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iwomi.base.ServiceInterface.ReportPostServiceS;
import iwomi.base.objects.ReportPostS;
import iwomi.base.repositories.ReportPostRepositoryS;
@Component
public class ReportPostServiceImplS  implements ReportPostServiceS{
	
	@Autowired
	private ReportPostRepositoryS ReportPostRepository;
	 @Override
	    public List<ReportPostS> listAll() {
		 List<ReportPostS> reportPosts = new ArrayList<>();
	        return reportPosts;
	       }

}
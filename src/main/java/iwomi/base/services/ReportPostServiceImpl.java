package iwomi.base.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iwomi.base.ServiceInterface.ReportPostService;
import iwomi.base.objects.ReportPost;
import iwomi.base.repositories.ReportPostRepository;
@Component
public class ReportPostServiceImpl  implements ReportPostService{
	
	@Autowired
	private ReportPostRepository ReportPostRepository;
	 @Override
	    public List<ReportPost> listAll() {
		 List<ReportPost> reportPosts = new ArrayList<>();
	        return reportPosts;
	       }

}
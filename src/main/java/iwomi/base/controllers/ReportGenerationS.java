package iwomi.base.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import iwomi.base.form.countElm;
import iwomi.base.objects.Conn;
import iwomi.base.objects.ReportAnomalyS;
import iwomi.base.objects.ReportAttributeS;
import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportFileS;
import iwomi.base.objects.ReportPostS;
import iwomi.base.objects.ReportRepSS;
import iwomi.base.objects.ReportRepSSview;
import iwomi.base.objects.User;
import iwomi.base.repositories.ConRepository;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportAnomalyRepositoryS;
import iwomi.base.repositories.ReportAttributeRepositoryS;
import iwomi.base.repositories.ReportCalculateRepositoryS;
import iwomi.base.repositories.ReportFileRepositoryS;
import iwomi.base.repositories.ReportPostRepositoryS;
import iwomi.base.repositories.ReportRepRepositoryS;
//import iwomi.base.repositories.UserRepository;

@RestController
@Component
public class ReportGenerationS {

	@Autowired
	private NomenclatureRepository nomenclatureRepository;

	@Autowired
	private ReportPostRepositoryS reportPostRepository;

	@Autowired
	private ReportFileRepositoryS reportFileRepository;

	@Autowired
	private ReportCalculateRepositoryS reportCalculateRepository;

	@Autowired

	private ReportAttributeRepositoryS reportAttributeRepository;

	@Autowired
	private ReportAnomalyRepositoryS reportAnomalyRepository;
	// @Autowired
	@Autowired
	private ReportRepRepositoryS reportRepRepository;
	// @Autowired
	// private ReportControleInterRepository reportControleInterRepository;

	 @GetMapping("/jpa/test2S")
	 public String reportPosts1S(){
	 return "sdfsdf";
	 }
//	 @GetMapping("/jpa/all_reportPost2")
//	 public List<ReportPost> reportPosts1(){
//	 return reportPostRepository.getUseCaseReportPost();
//	 }
	// get all by poste
	@GetMapping("/jpa/all_reportPost1S/{codep}")
	public List<ReportPostS> getReportPostfromCodepS(@PathVariable String codep) {
		return reportPostRepository.findReportPostSByCodep(codep);
	}

	// get from id
	@GetMapping(path = "/jpa/reportPostS/{id}")
	public ReportPostS getreportPostS(@PathVariable Long id) {
		return reportPostRepository.getOne(id);
	}

	// save from id
	@PostMapping(path = "/jpa/save_reportPostS")
	public ReportPostS saveReportPostS(@RequestBody ReportPostS reportPosts) {
		return reportPostRepository.save(reportPosts);
	}

	// delete id
//	@PostMapping(path = "/jpa/delete_reportPostS")
//	public ReportPost deleteReportPost(@RequestBody ReportPost reportPosts) {
//		reportPosts.setDele(1L);
//		return reportPostRepository.save(reportPosts);
//	}

//    //get from id
//    @GetMapping(path ="/jpa/all_reportFile_api/{id}")
//	public ReportPost getReportPost(@PathVariable Long id) {
//		return reportPostRepository.getOne(id);	
//	}
	// ****file
	// get all
	@GetMapping("/jpa/all_reportFileS")
	public List<ReportFileS> reportFilesS() {
		return reportFileRepository.findAll();
	}

	// get from id
	@GetMapping(path = "/jpa/reportFileS/{id}")
	public ReportFileS getReportFileS(@PathVariable Long id) {
		return reportFileRepository.getOne(id);
	}

	// get from fich
	@GetMapping(path = "/jpa/reportFilePostS/{fich}")
	public List<ReportFileS> getReportFilefromPostS(@PathVariable String fich) {
		return reportFileRepository.findReportFileSByFich(fich);
	}

	// get from sens
	// @GetMapping(path ="/jpa/reportFileSens/{sens}")
	// public List<ReportPost> getReportPost(@PathVariable String sens) {
	// return reportPostRepository.findReportPostBySens(sens);
	// }

	// get from fich
	@GetMapping(path = "/jpa/reportFileLineS/{fich}/{poste}")
	public List<ReportFileS> getReportFilefromLineS(@PathVariable String fich, @PathVariable String poste) {
		return reportFileRepository.findReportFileSByFichAndPoste(fich, poste);
	}

	// get from fich orderby rang
	@GetMapping(path = "/jpa/reportFilePostOrS/{fich}")
	public List<ReportFileS> getReportFilefromPostOrderS(@PathVariable String fich) {
		return reportFileRepository.findReportFileSByFichOrderByRangAscColAsc(fich);
	}

	// get from fich orderby rang
	@GetMapping(path = "/reportFilePostOrLimitS/{fich}/{lowLimt}/{upLimt}")
	public List<ReportFileS> reportFilePostOrLimitS(@PathVariable String fich, @PathVariable int lowLimt,
			@PathVariable int upLimt) {
		return reportFileRepository.findReportFileSByFichOR(fich, lowLimt, upLimt);
	}

	// get from fich orderby rang search
	@GetMapping(path = "/reportFilePostOrLimitsearchS/{fich}/{lowLimt}/{upLimt}/{val}")
	public List<ReportFileS> reportFilePostOrLimitSearchS(@PathVariable String fich, @PathVariable int lowLimt,
			@PathVariable int upLimt, @PathVariable String val) {
		return reportFileRepository.findReportFileSByFichOrSearch(fich, lowLimt, upLimt, val);
	}

	// get from fich orderby rang search
	@GetMapping(path = "/reportFilePostOrsearchS/{fich}/{val}")
	public List<ReportFileS> reportFilePostsearchS(@PathVariable String fich, @PathVariable String val) {
		return reportFileRepository.findReportFileSSeach(fich, val);
	}

	// save from id
	@PostMapping(path = "/jpa/save_reportFileS")
	public ReportFileS saveReportFileS(@RequestBody ReportFileS reportFile) {
		return reportFileRepository.save(reportFile);
	}

	// ****Calculate
	// get all
	@GetMapping("/jpa/all_reportCalculateS")
	public List<ReportCalculateS> reportCalculatesS() {
		return (List<ReportCalculateS>) reportCalculateRepository.findAll();
	}

	// get from id
	@GetMapping(path = "/jpa/reportCalculateS/{id}")
	public ReportCalculateS getReportCalculateS(@PathVariable Long id) {
		return reportCalculateRepository.findById(id);
	}

	// save from id
	@PostMapping(path = "/jpa/save_reportCalculateS")
	public ReportCalculateS savereportCalculateS(@RequestBody ReportCalculateS reportCalculate) {
		return reportCalculateRepository.save(reportCalculate);
	}

	// get from id
	@GetMapping(path = "/jpa/reportAttributeS/{id}")
	public ReportAttributeS getreportAttributeS(@PathVariable Long id) {
		return new ReportAttributeS();
	}

	// get anomaly
	@PostMapping(path = "/reportAnomalyAllS")
	public List<ReportAnomalyS> reportAnamaly2S(@RequestBody ReportAnomalyS reportAnomaly) {
		System.out.println("its a trial");
		reportAnomaly.getDar().setHours(0);
		System.out.println(reportAnomaly.getDar());
		return reportAnomalyRepository.findByDar(reportAnomaly.getDar());
	}

	// save from id
	@GetMapping(path = "/jpa/save_reportFile2S/{gen}/{id}")
	public ReportFileS saveReportFile1S(@PathVariable String gen, @PathVariable Long id) {
		ReportFileS reportFileToUpdate = reportFileRepository.findById(id);
		reportFileToUpdate.setGen(gen);
		return reportFileRepository.save(reportFileToUpdate);
	}

	// get from fich orderby rang service filtre
	@GetMapping(path = "/jpa/reportFilePostFiltreS/{fich}/{poste}")
	public List<ReportFileS> getReportFilefromPostFiltreS(@PathVariable String fich, @PathVariable String poste) {
		return reportFileRepository.findReportFileSByFichAndPosteContainingOrderByRangAscColAsc(fich, poste);
	}

	// get from fich orderby rang service filtre
	@GetMapping(path = "/jpa/reportFilePostFiltre1S/{fich}/{poste}/{gen}")
	public List<ReportFileS> getReportFilefromPostFiltre1S(@PathVariable String fich, @PathVariable String poste,
			@PathVariable String gen) {
		return reportFileRepository.findReportFileSByFichAndPosteContainingOrGenContainingOrderByRangAscColAsc(fich,
				poste, gen);
	}

	// get from fich orderby rang
	@GetMapping(path = "/jpa/reportRepPostOrS/{fich}")
	public List<ReportRepSS> getReportPrepfromPostOrderS(@PathVariable String fich) {
		return reportRepRepository.findRepor(fich);
	}

	@GetMapping(path = "/jpa/reportRepCountS/{fich}")
	public Long reportRepCountS(@PathVariable String fich) {
		return reportRepRepository.countByFichier(fich);
	}

	@GetMapping(path = "/jpa/reportRepCount1S/{fich}/{dar}")
	public Long reportRepCount1S(@PathVariable String fich, @PathVariable String dar) {
		return reportRepRepository.countByFichierAndDarEndsWith(fich, dar);
//		return reportRepRepository.countByFichier(fich);
	}
	
	@GetMapping(path = "/jpa/reportRepCount11S/{fich}")
	public Long reportRepCount11S(@PathVariable String fich) {
		if (fich.equalsIgnoreCase("F1139")) {
			return reportFileRepository.countByFichAndSource(fich, "CMR");
		} else {
			return reportFileRepository.countByFich(fich);
		}
//		return reportRepRepository.countByFichier(fich);
	}

	@GetMapping(path = "/jpa/reportRepCount111S/{fich}/{lkpost}")
	public Long reportRepCount111S(@PathVariable String fich,@PathVariable String post) {
		if (fich.equalsIgnoreCase("F1139")) {
			return reportFileRepository.countByFichAndSourceAndPosteContaining(fich, "CMR",post);
		} else {
			return reportFileRepository.countByFichAndPosteContaining(fich,post);
		}
//		return reportRepRepository.countByFichier(fich);
	}

	// get from fich orderby rang search
	@GetMapping(path = "/preparedResultS/{fich}/{lowLimt}/{upLimt}")
	public List<ReportRepSS> reportRepPostOrLimitSearchS(@PathVariable String fich, @PathVariable int lowLimt,
			@PathVariable int upLimt) {
		return reportRepRepository.preparedResult(fich, lowLimt, upLimt);
	}

	@GetMapping(path = "/preparedResult1S/{fich}/{dar}/{lowLimt}/{upLimt}")
	public List<ReportRepSS> reportRepPostOrLimitSearch1S(@PathVariable String dar, @PathVariable String fich,
			@PathVariable int lowLimt, @PathVariable int upLimt) {

		System.out.println("Value of upper limit :" + upLimt);
		return reportRepRepository.preparedResult1(fich, lowLimt, upLimt, "%" + dar);
	}

	@GetMapping(path = "/preparedResult111S/{fich}/{dar}/{lowLimt}/{upLimt}")
	public List<ReportRepSS> reportRepPostOrLimitSearch111S(@PathVariable String dar, @PathVariable String fich,
			@PathVariable int lowLimt, @PathVariable int upLimt) {
		return reportRepRepository.preparedResult111(fich, dar, lowLimt, upLimt);
	}

	@GetMapping(path = "/preparedResult11S/{fich}/{dar}/{lowLimt}/{upLimt}")
	public List<ReportRepSS> reportRepPostOrLimitSearch11S(@PathVariable String dar, @PathVariable String fich,
			@PathVariable int lowLimt, @PathVariable int upLimt) {
		upLimt = upLimt < 0 ? 999999999 : upLimt;
		System.out.println("Value of upper limit :" + upLimt+" :"+ dar+" :"+ fich+" :"+ lowLimt+" :");
		if (fich.equalsIgnoreCase("F1139")) {
			return reportRepRepository.preparedResult11_F1139(fich, dar, lowLimt, upLimt, "CMR");
		} else {
                    System.out.println("this is the area :");
			return reportRepRepository.preparedResult11(fich, dar, lowLimt, upLimt);
		}
	}

	// get from fich orderby rang search
	@GetMapping(path = "/countAllS/{fich}")
	public countElm countAllS(@PathVariable String fich) {
		return reportRepRepository.countAll(fich);
	}

	// get from fich orderby rang search
	@GetMapping(path = "/reportRepPostOrLimitsearchS/{fich}/{lowLimt}/{upLimt}/{val}")
	public List<ReportRepSS> reportRepPostOrLimitSearchS(@PathVariable String fich, @PathVariable int lowLimt,
			@PathVariable int upLimt, @PathVariable String val) {
		return reportRepRepository.findReportRepSByFichierOrSearch(fich, lowLimt, upLimt, val);
	}

	// get from fich orderby rang search
	@GetMapping(path = "/reportRepPostOrsearchS/{fich}/{val}")
	public List<ReportRepSS> reportRepPostsearchS(@PathVariable String fich, @PathVariable String val) {
		return reportRepRepository.findReportFileSeach(fich, val);
	}

	@Autowired
	private ConRepository conRepository;

//	@GetMapping(path = "/connect")
//	public List<Conn> reportRepPostsearch() {
//		return conRepository.findAll();
//	}

//	@Autowired
//	private UserRepository userRepository;

//liste user
	// @SuppressWarnings("unchecked")
//	@GetMapping("/allUser/{val}")
//	public List<User> reportUser(@PathVariable String val) {
//		// String val="0";
//		// return userRepository.findAll();
//		return (List<User>) userRepository.findBySttus(val);
//	}
//
//	// user par id
//	// @SuppressWarnings("unchecked")
//	@GetMapping("/idUser/{val}")
//	public User idUser(@PathVariable Long val) {
//
//		return userRepository.findById(val);
//	}
//
//	// save user
//	@PostMapping(path = "/saveUser")
//	public User saveUser(@RequestBody User user) {
//		user.setCrtd((new Timestamp(System.currentTimeMillis()).toString()));
//		user.setMdfi((new Timestamp(System.currentTimeMillis()).toString()));
//		user.setSttus("0");
//		return userRepository.save(user);
//
//	}
//
//// update user
//	@PostMapping(path = "/updateUser")
//	public User updateUser(@RequestBody User user) {
//		User t = userRepository.findById(user.getId());
//		if (t.setByUser1(user)) {
//			t = userRepository.save(t);
//			return t;
//		} else {
//			return null;
//		}
//	}
//
//// delete user
//	@GetMapping(path = "/deleteUser/{val}")
//	public User deleteUser(@PathVariable Long val) {
//		User t = userRepository.findById(val);
//		t.setSttus("1");
//		t = userRepository.save(t);
//		return t;
//	}
//
//	@GetMapping(path = "/jpa/ReportGetCalculation2")
//	public String ReportGetCalculation2(Model s) {
//		System.out.println("tring the syssteme before leaving and doing the check status");
//		s.addAttribute("test", "good");
//		return "good";
//	}
//
//	@GetMapping(path = "/jpa/reportFilePost1")
//	public Model getReportFilefromPost1(Model m) {
//		List<String> s = new ArrayList<String>();
//		s.add("roger");
//		s.add("Carine");
//		m.addAttribute("test", s);
//		System.out.println("tring the syssteme before leaving and doing the check status");
//		return m;
//	}

}

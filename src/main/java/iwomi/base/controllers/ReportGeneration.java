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
import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportFile;
import iwomi.base.objects.ReportPost;
import iwomi.base.objects.ReportRep;
import iwomi.base.objects.User;
import iwomi.base.repositories.ConRepository;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportAnomalyRepository;
import iwomi.base.repositories.ReportAttributeRepository;
import iwomi.base.repositories.ReportCalculateRepository;
import iwomi.base.repositories.ReportFileRepository;
import iwomi.base.repositories.ReportPostRepository;
import iwomi.base.repositories.ReportRepRepository;
import java.util.HashMap;
import java.util.Map;
//import iwomi.base.repositories.UserRepository;

@RestController
@Component
public class ReportGeneration {

    @Autowired
    private NomenclatureRepository nomenclatureRepository;

    @Autowired
    private ReportPostRepository reportPostRepository;

    @Autowired
    private ReportFileRepository reportFileRepository;

    @Autowired
    private ReportCalculateRepository reportCalculateRepository;

    @Autowired

    private ReportAttributeRepository reportAttributeRepository;

    @Autowired
    private ReportAnomalyRepository reportAnomalyRepository;
    // @Autowired
    @Autowired
    private ReportRepRepository reportRepRepository;
    // @Autowired
    // private ReportControleInterRepository reportControleInterRepository;

    @GetMapping("/jpa/test2")
    public String reportPosts1() {
        return "sdfsdf";
    }
//	 @GetMapping("/jpa/all_reportPost2")
//	 public List<ReportPost> reportPosts1(){
//	 return reportPostRepository.getUseCaseReportPost();
//	 }
    // get all by poste

    @GetMapping("/jpa/all_reportPost1/{codep}")
    public List<ReportPost> getReportPostfromCodep(@PathVariable String codep) {
        return reportPostRepository.findReportPostByCodep(codep);
    }

    // get from id
    @GetMapping(path = "/jpa/reportPost/{id}")
    public ReportPost getreportPost(@PathVariable Long id) {
        return reportPostRepository.getOne(id);
    }

    // save from id
    @PostMapping(path = "/jpa/save_reportPost")
    public ReportPost saveReportPost(@RequestBody ReportPost reportPosts) {
        return reportPostRepository.save(reportPosts);
    }

    // delete id
    @PostMapping(path = "/jpa/delete_reportPost")
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
    @GetMapping("/jpa/all_reportFile")
    public List<ReportFile> reportFiles() {
        return reportFileRepository.findAll();
    }

    // get from id
    @GetMapping(path = "/jpa/reportFile/{id}")
    public ReportFile getReportFile(@PathVariable Long id) {
        return reportFileRepository.getOne(id);
    }

    // get from fich
    @GetMapping(path = "/jpa/reportFilePost/{fich}")
    public List<ReportFile> getReportFilefromPost(@PathVariable String fich) {
        return reportFileRepository.findReportFileByFich(fich);
    }

    // get from sens
    // @GetMapping(path ="/jpa/reportFileSens/{sens}")
    // public List<ReportPost> getReportPost(@PathVariable String sens) {
    // return reportPostRepository.findReportPostBySens(sens);
    // }
    // get from fich
    @GetMapping(path = "/jpa/reportFileLine/{fich}/{poste}")
    public List<ReportFile> getReportFilefromLine(@PathVariable String fich, @PathVariable String poste) {
        return reportFileRepository.findReportFileByFichAndPoste(fich, poste);
    }

    // get from fich orderby rang
    @GetMapping(path = "/jpa/reportFilePostOr/{fich}")
    public List<ReportFile> getReportFilefromPostOrder(@PathVariable String fich) {
        return reportFileRepository.findReportFileByFichOrderByRangAscColAsc(fich);
    }

    // get from fich orderby rang
    @GetMapping(path = "/reportFilePostOrLimit/{fich}/{lowLimt}/{upLimt}")
    public List<ReportFile> reportFilePostOrLimit(@PathVariable String fich, @PathVariable int lowLimt,
            @PathVariable int upLimt) {
        return reportFileRepository.findReportFileByFichOR(fich, lowLimt, upLimt);
    }

    // get from fich orderby rang search
    @GetMapping(path = "/reportFilePostOrLimitsearch/{fich}/{lowLimt}/{upLimt}/{val}")
    public List<ReportFile> reportFilePostOrLimitSearch(@PathVariable String fich, @PathVariable int lowLimt,
            @PathVariable int upLimt, @PathVariable String val) {
        return reportFileRepository.findReportFileByFichOrSearch(fich, lowLimt, upLimt, val);
    }

    // get from fich orderby rang search
    @GetMapping(path = "/reportFilePostOrsearch/{fich}/{val}")
    public List<ReportFile> reportFilePostsearch(@PathVariable String fich, @PathVariable String val) {
        return reportFileRepository.findReportFileSeach(fich, val);
    }

    // save from id
    @PostMapping(path = "/jpa/save_reportFile")
    public ReportFile saveReportFile(@RequestBody ReportFile reportFile) {
        return reportFileRepository.save(reportFile);
    }

    // ****Calculate
    // get all
    @GetMapping("/jpa/all_reportCalculate")
    public List<ReportCalculate> reportCalculates() {
        return (List<ReportCalculate>) reportCalculateRepository.findAll();
    }

    // get from id
    @GetMapping(path = "/jpa/reportCalculate/{id}")
    public ReportCalculate getReportCalculate(@PathVariable Long id) {
        return reportCalculateRepository.findById(id);
    }

    // save from id
    @PostMapping(path = "/jpa/save_reportCalculate")
    public ReportCalculate savereportCalculate(@RequestBody ReportCalculate reportCalculate) {
        return reportCalculateRepository.save(reportCalculate);
    }

    // get from id
    @GetMapping(path = "/jpa/reportAttribute/{id}")
    public ReportAttribute getreportAttribute(@PathVariable Long id) {
        return new ReportAttribute();
    }

    // get anomaly
    @PostMapping(path = "/reportAnomalyAll")
    public List<ReportAnomaly> reportAnamaly2(@RequestBody ReportAnomaly reportAnomaly) {
        Map<String, Object> ret = new HashMap<String, Object>();
        reportAnomaly.getDar().setHours(0);
        System.out.println(reportAnomaly.getDar());
        return reportAnomalyRepository.findByDar(reportAnomaly.getDar());
    }

    // save from id
    @GetMapping(path = "/jpa/save_reportFile2/{gen}/{id}")
    public ReportFile saveReportFile1(@PathVariable String gen, @PathVariable Long id) {
       // System.out.println("Config bonjour yvo gen="+gen);
        //System.out.println("Config bonjour yvo id="+id);
        ReportFile reportFileToUpdate = reportFileRepository.findById(id);
         if(gen.equalsIgnoreCase("ok")){
           reportFileToUpdate.setGen("");
            }else{
          reportFileToUpdate.setGen(gen);   
         }
        return reportFileRepository.save(reportFileToUpdate);
    }

    // get from fich orderby rang service filtre
    @GetMapping(path = "/jpa/reportFilePostFiltre/{fich}/{poste}")
    public List<ReportFile> getReportFilefromPostFiltre(@PathVariable String fich, @PathVariable String poste) {
        return reportFileRepository.findReportFileByFichAndPosteContainingOrderByRangAscColAsc(fich, poste);
    }

    // get from fich orderby rang service filtre
    @GetMapping(path = "/jpa/reportFilePostFiltre1/{fich}/{poste}/{gen}")
    public List<ReportFile> getReportFilefromPostFiltre1(@PathVariable String fich, @PathVariable String poste,
            @PathVariable String gen) {
        return reportFileRepository.findReportFileByFichAndPosteContainingOrGenContainingOrderByRangAscColAsc(fich,
                poste, gen);
    }

    // get from fich orderby rang
    @GetMapping(path = "/jpa/reportRepPostOr/{fich}")
    public List<ReportRep> getReportPrepfromPostOrder(@PathVariable String fich) {
        return reportRepRepository.findRepor(fich);
    }

    @GetMapping(path = "/jpa/reportRepCount/{fich}")
    public Long reportRepCount(@PathVariable String fich) {
        return reportRepRepository.countByFichier(fich);
    }

    @GetMapping(path = "/jpa/reportRepCount1/{fich}/{dar}")
    public Long reportRepCount1(@PathVariable String fich, @PathVariable String dar) {
        return reportRepRepository.countByFichierAndDarEndsWith(fich, dar);
//		return reportRepRepository.countByFichier(fich);
    }

    @GetMapping(path = "/jpa/reportRepCount11/{fich}")
    public Long reportRepCount11(@PathVariable String fich) {
        if (fich.equalsIgnoreCase("F1139")) {
            return reportFileRepository.countByFichAndSource(fich, "CMR");
        } else {
            return reportFileRepository.countByFich(fich);
        }
//		return reportRepRepository.countByFichier(fich);
    }

    @GetMapping(path = "/jpa/reportRepCount111/{fich}/{lkpost}")
    public Long reportRepCount111(@PathVariable String fich, @PathVariable String post) {
        if (fich.equalsIgnoreCase("F1139")) {
            return reportFileRepository.countByFichAndSourceAndPosteContaining(fich, "CMR", post);
        } else {
            return reportFileRepository.countByFichAndPosteContaining(fich, post);
        }
//		return reportRepRepository.countByFichier(fich);
    }

    // get from fich orderby rang search
    @GetMapping(path = "/preparedResult/{fich}/{lowLimt}/{upLimt}")
    public List<ReportRep> reportRepPostOrLimitSearch(@PathVariable String fich, @PathVariable int lowLimt,
            @PathVariable int upLimt) {
        return reportRepRepository.preparedResult(fich, lowLimt, upLimt);
    }

    @GetMapping(path = "/preparedResult1/{fich}/{dar}/{lowLimt}/{upLimt}")
    public List<ReportRep> reportRepPostOrLimitSearch1(@PathVariable String dar, @PathVariable String fich,
            @PathVariable int lowLimt, @PathVariable int upLimt) {

        System.out.println("Value of upper limit :" + upLimt);
        return reportRepRepository.preparedResult1(fich, lowLimt, upLimt, "%" + dar);
    }

    @GetMapping(path = "/preparedResult111/{fich}/{dar}/{lowLimt}/{upLimt}")
    public List<ReportRep> reportRepPostOrLimitSearch111(@PathVariable String dar, @PathVariable String fich,
            @PathVariable int lowLimt, @PathVariable int upLimt) {
        return reportRepRepository.preparedResult111(fich, dar, lowLimt, upLimt);
    }

    @GetMapping(path = "/preparedResult11/{fich}/{dar}/{lowLimt}/{upLimt}")
    public List<ReportRep> reportRepPostOrLimitSearch11(@PathVariable String dar, @PathVariable String fich,
            @PathVariable int lowLimt, @PathVariable int upLimt) {
        upLimt = upLimt < 0 ? 999999999 : upLimt;
        System.out.println("Value of upper limit :" + upLimt);
        if (fich.equalsIgnoreCase("F1139")) {
            return reportRepRepository.preparedResult11_F1139(fich, dar, lowLimt, upLimt, "CMR");
        } else {
            return reportRepRepository.preparedResult11(fich, dar, lowLimt, upLimt);
        }
    }

    // get from fich orderby rang search
    @GetMapping(path = "/countAll/{fich}")
    public countElm countAll(@PathVariable String fich) {
        return reportRepRepository.countAll(fich);
    }

    // get from fich orderby rang search
    @GetMapping(path = "/reportRepPostOrLimitsearch/{fich}/{lowLimt}/{upLimt}/{val}")
    public List<ReportRep> reportRepPostOrLimitSearch(@PathVariable String fich, @PathVariable int lowLimt,
            @PathVariable int upLimt, @PathVariable String val) {
        return reportRepRepository.findReportRepByFichierOrSearch(fich, lowLimt, upLimt, val);
    }

    // get from fich orderby rang search
    @GetMapping(path = "/reportRepPostOrsearch/{fich}/{val}")
    public List<ReportRep> reportRepPostsearch(@PathVariable String fich, @PathVariable String val) {
        return reportRepRepository.findReportFileSeach(fich, val);
    }

    @Autowired
    private ConRepository conRepository;

    @GetMapping(path = "/connect")
    public List<Conn> reportRepPostsearch() {
        return conRepository.findAll();
    }

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

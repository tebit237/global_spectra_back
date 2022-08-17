package iwomi.base.controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iwomi.base.ServiceInterface.NomenclatureService;
import iwomi.base.ServiceInterface.ReportAnomalyServiceS;
import iwomi.base.ServiceInterface.ReportAttributeServiceS;
import iwomi.base.ServiceInterface.ReportCalculateServiceS;
import iwomi.base.ServiceInterface.ReportControleServiceS;
import iwomi.base.ServiceInterface.ReportRepSErviceS;
//import iwomi.base.ServiceInterface.UserService;
import iwomi.base.objects.Conn;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportAnomalyS;
import iwomi.base.objects.ReportAttributeS;
import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportCalculateS2;
import iwomi.base.objects.ReportControleComplexS;
import iwomi.base.objects.ReportControleIntraSS;
import iwomi.base.objects.ReportControleInterSS;
import iwomi.base.objects.ReportControleQualityS;
import iwomi.base.objects.ReportPostS;
import iwomi.base.objects.ReportRepSS;
import iwomi.base.objects.ReportRep2S;
import iwomi.base.objects.ReportRepIdS;
import iwomi.base.objects.SqlFileType;
import iwomi.base.objects.SqlFileTypeS;
import iwomi.base.objects.User;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportAttributeRepositoryS;
import iwomi.base.repositories.ReportCalculateIdRepositoryS;
import iwomi.base.repositories.ReportCalculateRepositoryS;
import iwomi.base.repositories.ReportControleComplexRepositoryS;
import iwomi.base.repositories.ReportControleInterRepositoryS;
import iwomi.base.repositories.ReportControleIntraRepositoryS;
import iwomi.base.repositories.ReportControleQualityRepositoryS;
import iwomi.base.repositories.ReportFileRepositoryS;
import iwomi.base.repositories.ReportPostRepositoryS;
import iwomi.base.repositories.ReportRepRepositoryS;
import iwomi.base.repositories.ReportRepRepository2S;
import iwomi.base.repositories.SqlFileTypeRepository;
import iwomi.base.repositories.SqlFileTypeRepositoryS;
import iwomi.base.services.NomenclatureServiceImpl;

@RestController
@Component
public class ReportParameterscerber {
	@Autowired
	private ReportFileRepositoryS reportFileRepository;
	@Autowired
	private NomenclatureRepository nomenclatureRepository;
	@Autowired
	private ReportAttributeRepositoryS reportAttributeRepository;
	@Autowired
	private ReportAttributeServiceS reportAttributeService;
	@Autowired
	private NomenclatureServiceImpl nomenclatureService;
	@Autowired
	private NomenclatureService nomenclatureService1;
	@Autowired
	private ReportRepRepositoryS reportRepRepository;
	@Autowired
	private ReportRepRepository2S reportRepRepository2;
	@Autowired
	private ReportCalculateRepositoryS reportCalculateRepository;
	@Autowired
	ReportCalculateServiceS reportCalculateService;
//	@Autowired
//	UserService userService;
	@Autowired
	ReportRepSErviceS reportRepSErvice;
	@Autowired
	private ReportAnomalyServiceS reportAnormalyService;
	@Autowired
	private ReportControleIntraRepositoryS reportControleIntraRepository;
	@Autowired
	private ReportControleComplexRepositoryS reportControleComplexRepository;
	@Autowired
	private ReportControleQualityRepositoryS reportControleQualityRepository;
	@Autowired
	private ReportControleServiceS reportControleService;
	@Autowired
	private SqlFileTypeRepositoryS sqlFileTypeRepository;
	@Autowired
	private SqlFileTypeRepository sqlFileTypeRepositoryr;
	@Autowired
	private ReportCalculateIdRepositoryS reportCalculateIdRepositoryS;

	@GetMapping(path = "/calculationS/{fich}")
	public ReportAttributeS getreportAttributeS(@PathVariable String fich) {
		return new ReportAttributeS();
	}

	@GetMapping(path = "/updatecellS/{fich}/{post}/{col}/{val}")
	public String getcellS(@PathVariable String fich, @PathVariable String post, @PathVariable Long col,
			@PathVariable String val) {
		ArrayList<iwomi.base.objects.ReportFileS> t = (ArrayList<iwomi.base.objects.ReportFileS>) reportFileRepository
				.findReportFileSByFichAndPosteAndCol(fich, post, col);
		iwomi.base.objects.ReportFileS tp = t.get(0);
		try {
			tp.setGen(java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name()));
			reportFileRepository.save(tp);
			return "Data Updated";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
	}

	@RequestMapping("/booksS")
	public String booksS(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("from") LocalDate from,
			Model model) {
		model.addAttribute("from", from);
		return "books.jsp";
	}

	@PostMapping(path = "/updatecellpre1S")
	public ReportRepSS updatecellpre1S(@RequestBody ReportRepSS reportRep) {
//	public Date updatecellpre1(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") Date from) {
		try {
			reportRep.setDar(new SimpleDateFormat("yyyy-MM-dd").parse(reportRep.getFeuille()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reportRep.setFeuille(null);
		ReportRepSS s;
		// valc is the parameter always comming back with the value
		List<ReportRepSS> t = reportRepRepository.findByPostAndFichierAndCol(reportRep.getPost(), reportRep.getFichier(),
				reportRep.getCol());
		if (t.size() == 1 && t.get(0).setByColumn11(reportRep)) {
			return reportRepRepository.save(t.get(0));
		} else {
			return null;
		}
	}

	@PostMapping(path = "/updatecellpre11S")
	public Map<String, Object> updatecellpre11S(@RequestBody ReportRepSS reportRep) {
		ReportRepSS s;
		Map<String, Object> ret = new HashMap<String, Object>();
		// valc is the parameter always coming back with the value
		reportRep.getDar();
		System.out.println(reportRep.getDar());
		System.out.println(reportRep.getFichier());
		System.out.println(reportRep.getPost());
		System.out.println(reportRep.getCol());
		System.out.println(reportRep.initialDate());
		List<ReportRepSS> t = reportRepRepository.findByFichierAndPostAndColAndDar(reportRep.getFichier(),
				reportRep.getPost(), reportRep.getCol(), reportRep.initialDate());
		System.out.println("this is the date :" + reportRep.initialDate().toString());
		System.out.println("tebit t size is :" + t.toString());
		if (t.size() == 1 && t.get(0).setByColumn11(reportRep)) {
			System.out.println("here");
			ret.put("obj", reportRepRepository.save(t.get(0)));
			ret.put("success", 1);
			ret.put("status", "01");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			ret.put("msg", "Field : CH" + reportRep.getPost() + "C" + reportRep.getCol() + ", of Closing date : "
					+ dateFormat.format(reportRep.initialDate()) + " successfully modified");
			ret.put("obj", t);
			return ret;
		} else {
			System.out.println("else");
			ret.put("success", 0);
			ret.put("status", "0010");
			ret.put("msg", "field is duplicated, call administrator for further assistance");
			return ret;
		}
	}
//exclusively to modify
	@PostMapping(path = "/updatecellpre11S1")
	public Map<String, Object> updatecellpre111S(@RequestBody ReportRepSS reportRep) {
		ReportRepSS s;
		Map<String, Object> ret = new HashMap<String, Object>();
		// valc is the parameter always coming back with the value
		reportRep.getDar();
		System.out.println(reportRep.getDar());
		System.out.println(reportRep.getFichier());
		System.out.println(reportRep.getPost());
		System.out.println(reportRep.getCol());
		System.out.println(reportRep.initialDate());
		List<ReportRepSS> t = reportRepRepository.findByFichierAndRangAndColAndDar(reportRep.getFichier(),
				reportRep.getRang(), reportRep.getCol(), reportRep.initialDate());
		System.out.println("this is the date :" + reportRep.initialDate().toString());
		System.out.println("tebit t size is :" + t.size());
		if (t.size() == 1 ) {
			System.out.println("here");
                        t.get(0).setValc(reportRep.getValc());
			ret.put("obj", reportRepRepository.save(t.get(0)));
			ret.put("success", 1);
			ret.put("status", "01");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			ret.put("msg", "Field : CH" + reportRep.getPost() + "C" + reportRep.getCol() + ", of Closing date : "
					+ dateFormat.format(reportRep.initialDate()) + " successfully modified");
			ret.put("obj", t);
			return ret;
		} else {
			System.out.println("else");
			ret.put("success", 0);
			ret.put("status", "0010");
			ret.put("msg", "field is duplicated, call administrator for further assistance");
			return ret;
		}
	}

	// **Attribute
	@PostMapping("/allReportAttributeFilterS")
	public List<ReportAttributeS> reportAttributesS(@RequestBody ReportAttributeS s) {
		return reportAttributeService.constructfilterquery1(s);
	}

	@PostMapping("/getFormulaElementS")
	public Model getFormulaElementS(@RequestBody ReportRepSS s) {

		return reportCalculateService.get_cells(s);
	}

	// **Attribute
	@PostMapping("/allReportCalcuteFilterS")
	public List<ReportCalculateS> allReportCalcuteFilterS(@RequestBody ReportCalculateS s) {
		return reportCalculateService.constructfilterquery1(s);
	}

	@PostMapping("/allReportPostFilterS")
	public List<Nomenclature> allReportPostFilterS(@RequestBody Nomenclature s) {
		s.setTabcd("8021");
		return nomenclatureService1.constructfilterquery2(s);
	}
        
        
//	@PostMapping("/allReportPostFilterS")
//	public List<Nomenclature> allReportPostFilterS(@RequestBody Nomenclature s) {
//		s.setTabcd("8021");
//		return nomenclatureService1.constructfilterquery2(s);
//	}

	@PostMapping("/allControlIntraFilterS")
	public List<ReportControleInterSS> allControlIntraFilterS(@RequestBody ReportControleInterSS s) {
		return reportControleService.constructfilterqueryintra1(s);
	}

	@PostMapping("/allControlInterFilterS")
	public List<ReportControleIntraSS> allControlInterFilterS(@RequestBody ReportControleIntraSS s) {
		return reportControleService.constructfilterqueryinter1(s);
	}

	@PostMapping("/allreportRecodeFilterS")
	public List<ReportRepSS> allreportRecodeFilterS(@RequestBody ReportRepSS s) {
		System.out.println("the post value is" + s.getPost());
		System.out.println("the post value is" + s.getFichier());
		System.out.println("the post value is" + s.getFeild());
		if (s.getPost() != null) {
			return reportRepRepository.findByFichierPostDar(s.getPost(), s.getFichier(), s.getFeild());
		} else {
			return reportRepRepository.findByFichierDar(s.getFichier(), s.getFeild());
		}
	}

	@PostMapping("/allreportAnomalyFilterS")
	public List<ReportAnomalyS> allreportAnomalyFilterS(@RequestBody ReportAnomalyS s) {
		return reportAnormalyService.constructfilterquery1(s);
	}

	@GetMapping("/allReportAttributeS")
	public List<ReportAttributeS> reportAttributesS() {
		return reportAttributeRepository.findByDeleOrderByIdDesc(false);
//		PageRequest topTen =  new PageRequest(0, 10, Direction.ASC, "id"); 
//		return reportAttributeRepository.findByDele(false, topTen);
	}

	@PostMapping(path = "/updateAttribue1S")
	public Map updateAttribue1S(@RequestBody ReportAttributeS reportAttribute) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ReportAttributeS t = reportAttributeRepository.findById(reportAttribute.getId());
		if (reportAttribute.getAtt() != null
				&& reportAttributeRepository.findByAttAndDele(reportAttribute.getAtt(), false).size() > 0) {
			ret.put("success", 0);
			ret.put("status", "0003");
			ret.put("msg", "Modified to an existing Attribute");
			ret.put("obj", t);
			return ret;
		}
		if (t.setByColumn1(reportAttribute)) {
			t = reportAttributeRepository.save(t);
			ret.put("success", 1);
			ret.put("status", "01");
			ret.put("msg", "Attribute successfully Updated");
			ret.put("obj", t);
			return ret;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/updateFilefieldS")
	public Map updateFilefieldS(@RequestBody ReportRepSS reportRep) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Date e = reportRep.getDar();
		e.setHours(0);
		ReportRepSS t = reportRepRepository.findByFichierAndColAndRangAndDar(reportRep.getFichier(), reportRep.getCol(),
				reportRep.getRang(), e);
		System.out.println("the sizeo is :");
		System.out.println(t);
		if (t != null) {
			t.setValc(reportRep.getValc());
			t = reportRepRepository.save(t);
			ret.put("success", 1);
			ret.put("status", "01");
			ret.put("msg", "file :" + (reportRep.getFichier()) + " successfully Updated");
			ret.put("obj", t);
			return ret;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/updateFilefieldv1S")
	public ReportRep2S updateFilefieldv1S(@RequestBody ReportRep2S reportRep) {
            System.out.println("entered firt");
		Map<String, Object> ret = new HashMap<String, Object>();
		Date e = reportRep.getDar();
                e.setHours(0);
                ReportRepIdS r = new ReportRepIdS();
                r.setCol(reportRep.getCol());
                r.setDar(e);
                r.setFichier(reportRep.getFichier());
                r.setRang(reportRep.getRang());
            System.out.println("entered sdfs"+reportRep.getCol()+e+reportRep.getFichier()+reportRep.getRang());
//		return reportRep;
		ReportRep2S reportRep1 = reportRepRepository2.findByColAndDarAndFichierAndRang(reportRep.getCol(),e,reportRep.getFichier(),reportRep.getRang());
                
                System.out.println("this is the id line in the system :"+reportRep1.getId());
                System.out.println("this is value to insert :"+reportRep.getValc());
                
                reportRep1.setValc(reportRep.getValc());
		return reportRepRepository2.save(reportRep1);
	}

	// **Attribute
	@GetMapping("/allReportAttributeFilterS/{type}/{lib}/{att}")
	public List<ReportAttributeS> allReportAttributeFilterS(@PathVariable String type, @PathVariable String lib,
			@PathVariable String att) {
		String p = "";

		if (type != "null") {
			p = "e.type = " + type;
		}
		if (type != "null" && lib != "null") {
			p += "AND";
		}
		if (lib != "null") {
			p += "e.lib = " + lib;
		}
		if ((att != "null" && lib != "null") || (att != "null" && type != "null")) {
			p += "AND";
		}
		if (att != "null") {
			p += "e.att = " + att;
		}
		return reportAttributeRepository.findreporsir(p);
//		PageRequest topTen =  new PageRequest(0, 10, Direction.ASC, "id"); 
//		return reportAttributeRepository.findByDele(false, topTen);
	}

	@GetMapping(path = "/updateAttribueS/{id}/{column}/{val}")
	public ReportAttributeS updateAttribueS(@PathVariable Long id, @PathVariable String column,
			@PathVariable String val) {
		try {
			val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
			ReportAttributeS t = reportAttributeRepository.findById(id);
			ReportAttributeS tr = reportAttributeRepository.findByAttAndIdNot(val, id);
			if (tr != null && column == "attr") {
				return null;
			}
			if (t.setByColumn(column, val)) {
				t = reportAttributeRepository.save(t);
				return t;
			} else {
				return null;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping(path = "/getAttributeKeyS/{id}")
	public Map getAttributeKeyS(@PathVariable Long id) {
		List<ReportAttributeS> t = reportAttributeRepository.findByIdAndDele(id, false);

		Map<String, String> m = new HashMap<>();
		m.put("att", t.get(0).getAtt());
		return m;

	}

//	@GetMapping(path = "/deleteFileField/{id}")
	@PostMapping(path = "/deleteFileField1S")
	@Transactional
	public Boolean deleteFileField1S(@RequestBody ReportRepSS reportRep) {
		Date s = reportRep.getDar();
		s.setHours(0);
		reportRep.setDar(s);
		System.out.println(reportRep.getDar());
		List<ReportRepSS> p = reportRepRepository.findByFichierAndRangAndDar(reportRep.getFichier(), reportRep.getRang(),
				reportRep.getDar());
		p.forEach(e -> {
			System.out.println("delating this object " + reportRep.getFichier() + " ");
			reportRepRepository.delete(e);
		});
		return false;
	}

	@GetMapping(path = "/deleteFileFieldS/{id}/{file}")
	@Transactional
	public Boolean deleteFileFieldS(@PathVariable Long id,@PathVariable String fichier) {
			System.out.println("it is the range"+id);
		if (id != null) {
			System.out.println("it is the range"+id);
			List<ReportRep2S> s = reportRepRepository2.findByRangAndFichier(id,fichier);
			System.out.println("the syste is " + s.size());
			s.forEach(e -> {
				System.out.println("its delating "+e.getValc());
				reportRepRepository2.delete(new ReportRepIdS(e.getRang(), e.getDar(), e.getCol(), e.getFichier()));
			});
			return true;
		}
		return false;
	}
        
        
        
	@PostMapping(path = "/deleteFileFieldSv1")
	@Transactional
	public Boolean deleteFileFieldS(@RequestBody HashMap<String,String> ld) {
			System.out.println("it is the range"+ld.get("rang"));
			List<ReportRepSS> s = reportRepRepository.findByRangAndFichier(Long.parseLong(ld.get("rang")),ld.get("fichier"));
			System.out.println("the syste is " + s.size());
			s.forEach(e -> {
				System.out.println("its delating "+e.getValc());
                                reportRepRepository.delete(e.getId());
//				reportRepRepository2.delete(new ReportRepIdS(e.getRang(), e.getDar(), e.getCol(), e.getFichier()));
			});
			return true;
	}

	@GetMapping(path = "/deleteFileFieldTypeS/{id}")
	@Transactional
	public Boolean deleteFileFieldTypeS(@PathVariable Long id) {
		if (id != null) {
			sqlFileTypeRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@GetMapping(path = "/deleteAttributeS/{id}")
	@Transactional
	public Boolean deleteAttributeS(@PathVariable Long id) {
		if (id != null) {
			reportAttributeRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@PostMapping(path = "/saveAttributeS")
	public Map<String, Object> saveAttributeS(@RequestBody ReportAttributeS reportAttribute) {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<ReportAttributeS> d = reportAttributeRepository.findByAttAndDele(reportAttribute.getAtt(), false);
		if (d.size() > 0) {
			ret.put("success", 0);
			ret.put("status", "0001");
			ret.put("msg", "Attribute exist already");
			return ret;
		}
		reportAttribute.setCrdt(new Timestamp(System.currentTimeMillis()));
		reportAttribute.setMdfi(new Timestamp(System.currentTimeMillis()));
		reportAttribute.setDele(false);
		reportAttribute.setId(null);

		reportAttribute.setId(reportAttributeRepository.getMax().get(0).longValue() + 1L);
		reportAttribute = reportAttributeRepository.save(reportAttribute);
		if (reportAttribute != null) {
			ret.put("success", 1);
			ret.put("status", "01");
			ret.put("msg", "Attribute successfully saved");
			ret.put("obj", reportAttribute);
			return ret;
		} else {
			ret.put("success", 0);
			ret.put("status", "0002");
			ret.put("mssg", "try Again,Operation failed");
			return ret;
		}
	}

	// **Attribute
	@GetMapping("/AttributDropdownS")
	public Map<String, Object> AttributDropdownS() {
		Map<Long, String> m = new HashMap<>();
		Map<Long, String> p = new HashMap<>();
		Map<String, Object> s = new HashMap<>();
		reportAttributeRepository.findByDele(false).forEach(f -> {
//		reportAttributeRepository.findFirst10ByDele(false).forEach(f -> {
			m.put(f.getId(), f.getLib());
			p.put(f.getId(), f.getAtt());
		});
		s.put("keyDropdown", p);
		s.put("lebelDropdown", m);
		return s;
	}

	// **Attribute
	@GetMapping("/AttributkeyDropdownS")
	public Map<String, Object> AttributkeyDropdownS() {
		Map<Long, String> m = new HashMap<>();
//		Map<Long, String> p = new HashMap<>();
		Map<String, Object> s = new HashMap<>();
		reportAttributeRepository.findFirst20ByDeleOrderByIdDesc(false).forEach(f -> {
			m.put(f.getId(), f.getLib());
//			p.put(f.getId(), f.getAtt());
		});
//		s.put("keyDropdown", p);
		s.put("lebelDropdown", m);
		return s;
	}


	private String tabcd = "8021";

	@GetMapping(path = "/getPostS")
	public List<Nomenclature> getPost() {
		return nomenclatureRepository.findBytabcdAndDelequery(tabcd, 0);
	}

	@GetMapping(path = "/getPost1S")
	public String getPost1S() {
		Float s = 999999999999999f;
		if (s == 999999999999999.0) {
			return "4";
		} else {
			return "5";
		}
	}

	// filter by post
	@GetMapping(path = "/getPostFilterS/{acscd}")
	public List<Nomenclature> getPostFilterS(@PathVariable String acscd) {
		return nomenclatureRepository.findBytabcdAndDeleAndAcscd1(tabcd, 0, acscd);
	}

	/*
	 * chapter collection
	 * 
	 */
	@Autowired
	private ReportPostRepositoryS reportPostRepository;

	@PostMapping(path = "/saveNewPostS")
	public Map savePostS(@RequestBody Nomenclature post) {
		post.setTabcd(tabcd);
		post.setCrtd("sdfsdf");
		post.setMdfi(new Date());
		post.setMuser("5454");
		post.setCuser("5454");
		post.setDele(0);
		Map<String, Object> ret = new HashMap<String, Object>();

		if (nomenclatureRepository.findByAcscd(post.getAcscd()).size() > 0) {
			ret.put("success", 0);
			ret.put("status", "0005");
			ret.put("msg", "Post Aready Exists");
			ret.put("obj", nomenclatureRepository.findByAcscd(post.getAcscd()));
			return ret;
		}
		post.setId(nomenclatureRepository.getMax().get(0).longValue() + 1L);
		ret.put("obj", nomenclatureRepository.save(post));
		ret.put("success", 1);
		ret.put("status", "01");
		ret.put("msg", "Post successfully saved");
		return ret;
	}

	@GetMapping(path = "/updatePostS/{id}/{column}/{val}")
	public Map updatePostS(@PathVariable Long id, @PathVariable String column, @PathVariable String val) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
			val = (val.equals("--")) ? "" : val;
			Nomenclature t = nomenclatureRepository.findById(id);
			if (column.equals("acscd") && nomenclatureRepository.findByAcscdAndIdNot(val, id).size() > 0) {
				ret.put("success", 0);
				ret.put("status", "0005");
				ret.put("msg", "Post Aready Exists");
				ret.put("obj", nomenclatureRepository.findByAcscdAndIdNot(val, id));
				return ret;
			}
			if (t.setByColumn(column, val)) {
				t = nomenclatureRepository.save(t);
				ret.put("success", 1);
				ret.put("status", "01");
				ret.put("msg", "Post Save Successfully");
				ret.put("obj", nomenclatureRepository.findByAcscdAndIdNot(val, id));
				return ret;
			} else {
				ret.put("success", 0);
				ret.put("status", "0004");
				ret.put("msg", "Column to Update is not set");
				return ret;
			}
		} catch (UnsupportedEncodingException e) {
			ret.put("success", 0);
			ret.put("status", "0007");
			ret.put("msg", "Error Saving Entered String");
			return ret;
		}
	}

	@GetMapping(path = "/deletePostS/{id}")
	@Transactional
	public Boolean deletePostS(@PathVariable long id) {
		nomenclatureRepository.deleteById(id);
		return true;
	}

	@GetMapping(path = "/getChapterS/{post}")
	public List<ReportPostS> getChapterS(@PathVariable String post) {
		return reportPostRepository.findReportPostSByCodepAndDele(post, 0L);
	}

	@GetMapping(path = "/updateChapitreS/{id}/{column}/{val}")
	public ReportPostS updateChapitreS(@PathVariable Long id, @PathVariable String column, @PathVariable String val) {
		try {
			val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
			ReportPostS t = reportPostRepository.findById(id);
			if (t.setByColumn(column, val)) {
				t = reportPostRepository.save(t);
				return t;
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping(path = "/updateChapitre1S")
	public ReportPostS updateChapitre1S(@RequestBody ReportPostS reportPost) {
		ReportPostS t = reportPostRepository.findById(reportPost.getId());
		if (t.setByColumn1(reportPost)) {
			t = reportPostRepository.save(t);
			return t;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/saveNewChapterS")
	public Map saveChapterS(@RequestBody ReportPostS reportPost) {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Nomenclature> s = nomenclatureRepository.findBytabcdAndDeleAndAcscd1(tabcd, 0, reportPost.getCodep());
		reportPost.setCrdt((new Timestamp(System.currentTimeMillis())).toString());
		reportPost.setMdfi(new Timestamp(System.currentTimeMillis()));
		reportPost.setDele(0L);
		reportPost.setLebel(s.get(0).getLib1());
		reportPost.setId(reportPostRepository.getMax().get(0).longValue() + 1L);
		reportPost = reportPostRepository.save(reportPost);
		if ((reportPost) != null) {
			ret.put("success", 1);
			ret.put("status", "01");
			ret.put("msg", "Chapter Saved Successfully");
			ret.put("obj", reportPost);
			return ret;
		} else {
			ret.put("success", 0);
			ret.put("status", "0004");
			ret.put("msg", "Chapter was not saved, try again");
			return ret;
		}
	}
        @Transactional
	@GetMapping(path = "/deleteChapitreS/{id}")
	public ReportPostS deleteChapitreS(@PathVariable long id) {
		ReportPostS f = reportPostRepository.getOne(id);
		f.setDele(1L);
		f.setMdfi(new Timestamp(System.currentTimeMillis()));
//		return reportPostRepository.save(f);
		reportPostRepository.deleteById(id);
		return f;
	}

	@GetMapping(path = "/calculationListS/{fichi}")
	public List<ReportCalculateS2> calculationListS(@PathVariable String fichi) {
		return reportCalculateIdRepositoryS.findByDeleAndFichi(0L, fichi);
	}

    // **Attribute
    @PostMapping("/allReportCalcuteFilterr")
    public List<ReportCalculateS2> allReportCalcuteFilterS(@RequestBody ReportCalculateS2 s) {
        System.out.println(s.getFichi()+s.getField()+s.getPost()+s.getTysorce());
        return reportCalculateIdRepositoryS.findByDeleAndFichit(s.getFichi(),s.getField(),s.getPost(),s.getTysorce());
    }

	@PostMapping(path = "/saveCalculateS")
	public Map saveCalculateS(@RequestBody ReportCalculateS reportCalculate) {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<ReportCalculateS> r;
                //EXIST
		if (reportCalculate.getTysorce() == "FINAL") {
			r = reportCalculateRepository.findByDeleAndFichiAndPostAndCol(0L, reportCalculate.getFichi(),
					reportCalculate.getPost(), reportCalculate.getCol());
		} else {
			r = reportCalculateRepository.findByDeleAndFichiAndField(0L, reportCalculate.getFichi(),
					reportCalculate.getField());
		}

		if (r.size() >= 1 && reportCalculate.getId() == null) {// NEW
			ret.put("msg", "Formula Field alread exists " + r.get(0).getField());
			ret.put("status", "0008");
			ret.put("success", 0);
			return ret;
		} else if (r.size() >= 1 && !reportCalculate.getId().equals(r.get(0).getId())) {// select one and update another
			// NO!!!
			ret.put("msg", "Failed: You are tring to update :" + (reportCalculate.getField()) + ", delete it first");
			ret.put("status", "0009");
			ret.put("success", 0);
			return ret;
		} else if (r.size() >= 1 && reportCalculate.getId().equals(r.get(0).getId())) {// update a formular
			reportCalculate.setMdfi(new Timestamp(System.currentTimeMillis()));
			reportCalculate.setCrdt(r.get(0).getCrdt());
			reportCalculate.setEtab(r.get(0).getEtab());
			reportCalculate.setDele(0L);
			reportCalculate = reportCalculateRepository.save(reportCalculate);
			if (reportCalculate != null) {
				ret.put("msg", "successfully Updated");
				ret.put("status", "01");
				ret.put("success", 1);
				ret.put("obj", reportCalculate);
				return ret;
			} else {
				ret.put("msg", "try Again,Operation failed");
				ret.put("status", "002");
				ret.put("success", 0);
				return ret;
			}
		} else {
			System.out.println("we +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(r.size());

			reportCalculate.setCrdt(new Timestamp(System.currentTimeMillis()));
			reportCalculate.setMdfi(new Timestamp(System.currentTimeMillis()));
			reportCalculate.setDele(0L);
			reportCalculate.setId(reportCalculateRepository.getMax().get(0).longValue() + 1L);
			reportCalculate = reportCalculateRepository.save(reportCalculate);
			if (reportCalculate != null) {
				ret.put("msg", "successfully saved");
				ret.put("status", "01");
				ret.put("success", 1);
				ret.put("obj", reportCalculate);
				return ret;
			} else {
				ret.put("msg", "try Again,Operation failed");
				ret.put("status", "002");
				ret.put("success", 0);
				return ret;
			}
		}

	}

	@GetMapping(path = "/deleteCalculeS/{id}")
	@Transactional
	public Boolean deleteCalculeS(@PathVariable Long id) {
		reportCalculateRepository.deleteById(id);
//		t.setDele(1L);
//		t = reportCalculateRepository.save(t);
		ReportCalculateS t = reportCalculateRepository.findById(id);
		if (t == null) {
			return true;
		} else {
			return false;
		}
	}

	@GetMapping(path = "/getCalculAttS/{calstring}")
	public List<Long> getCalculAttS(@PathVariable String calstring) {
		return reportCalculateService.dismantlePostCalculate(calstring);
	}

	@GetMapping(path = "/getCalculAtt1S/{calstring}")
	public List<Long> getCalculAtt1S(@PathVariable String calstring) {
		return reportCalculateService.dismantlePostCalculate2(calstring);
	}

	@PostMapping(path = "/getCalculAtt1S")
	public List<Long> getCalculAtt2S(@RequestBody Conn calstring) {
		return reportCalculateService.dismantlePostCalculate2(calstring.getConn());
	}

	/*
	 * intra models
	 */
	// **intra Control

	@GetMapping(path = "/intraListS")
	public List<ReportControleIntraSS> intraListS() {
		return reportControleIntraRepository.findIntra("0");
	}

	@PostMapping(path = "/saveInterControlS")
	public ReportControleInterSS saveIntraControlS(@RequestBody ReportControleInterSS reportControleIntra) {
		Date s = new Timestamp(System.currentTimeMillis());
		reportControleIntra.setCrdt(s.toString());
		reportControleIntra.setMdfi((new Timestamp(System.currentTimeMillis()).toString()));
		reportControleIntra.setDele("0");
		reportControleIntra.setTyp("T");
		reportControleIntra.setId(reportControleInterRepository.getMax().get(0).longValue() + 1L);
		return reportControleInterRepository.save(reportControleIntra);

	}

	@GetMapping(path = "/deleteIntraControlS/{id}")
	public ReportControleIntraSS deleteIntraControlS(@PathVariable Long id) {
		ReportControleIntraSS t = reportControleIntraRepository.findById(id);
		t.setDele("1");
		t = reportControleIntraRepository.save(t);
		reportControleIntraRepository.deleteById(id);
		return t;

	}

	@GetMapping(path = "/updateInterControleS/{id}/{column}/{val}")
	public ReportControleInterSS updateIntraControleS(@PathVariable Long id, @PathVariable String column,
			@PathVariable String val) {
		try {
			String vsal = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
			ReportControleInterSS t = reportControleInterRepository.findOne(id);
			if (t.setByColumn(column, vsal)) {
				return reportControleInterRepository.save(t);
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * inter models
	 */
	// **Inter Control reportRepCount11
	@Autowired
	private ReportControleInterRepositoryS reportControleInterRepository;

	@GetMapping(path = "/InterListS")
	public List<ReportControleInterSS> InterListS() {
		return reportControleInterRepository.getInterControls("0");
	}

	@PostMapping(path = "/saveIntraControlS")
	public ReportControleIntraSS saveInterControlS(@RequestBody ReportControleIntraSS reportControleInter) {
		reportControleInter.setCrdt((new Timestamp(System.currentTimeMillis()).toString()));
		reportControleInter.setMdfi((new Timestamp(System.currentTimeMillis()).toString()));
		reportControleInter.setDele("0");
		reportControleInter.setTyp("R");
		reportControleInter.setId(reportControleIntraRepository.getMax().get(0).longValue() + 1L);
		return reportControleIntraRepository.save(reportControleInter);
	}

	@GetMapping(path = "/deleteInterControlS/{id}")
	@Transactional
	public Boolean deleteInterControlS(@PathVariable Long id) {
		reportControleInterRepository.deleteById(id);
//		ReportControleInter t = reportControleInterRepository.findById(id);
//		if (t.equals(null)) {
		return true;
//		} else {
//			return false;
//		}
	}

	@GetMapping(path = "/updateIntraControleS/{id}/{column}/{val}")
	public ReportControleIntraSS updateInterControleS(@PathVariable Long id, @PathVariable String column,
			@PathVariable String val) {
		try {
			val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
			ReportControleIntraSS t = reportControleIntraRepository.findById(id);
			if (t.setByColumn(column, val)) {
				t = reportControleIntraRepository.save(t);
				return t;
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping(path = "/updateInterControle1S")
	public ReportControleInterSS updateIntraControle1S(@RequestBody ReportControleInterSS reportControleIntra) {
		ReportControleInterSS t = reportControleInterRepository.findById(reportControleIntra.getId());
		if (t.setByColumn1(reportControleIntra)) {
			t = reportControleInterRepository.save(t);
			return t;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/updateIntraControle1S")
	public ReportControleIntraSS updateInterControle1S(@RequestBody ReportControleIntraSS reportControleInter) {
		ReportControleIntraSS t = reportControleIntraRepository.findById(reportControleInter.getId());
		if (t.setByColumn1(reportControleInter)) {
			t = reportControleIntraRepository.save(t);
			return t;
		} else {
			return null;
		}
	}

	@GetMapping(path = "/CalculationDetailS/{fichi}/{date}/{post}/{col}")
	public Map CalculationDetailS(@PathVariable String fichi, @PathVariable String date, @PathVariable String post,
			@PathVariable String col) {
		return reportCalculateService.getFormularAndSubFields(fichi, post, col, date);
	}

	@PostMapping(path = "/GetFilesS")
	public List<ReportRepSS> GetFilesS(@RequestBody ReportRepSS reportRep) {

		Date s = reportRep.getDar();
		s.setHours(0);
		reportRep.setDar(s);
		List<ReportRepSS> e = reportRepRepository.findByFichierAndDarOrderByRangAscColAsc(reportRep.getFichier(),
				reportRep.getDar());
		System.out.println(" this is the output of reportrep");
		System.out.println(e.size());
		return e;
	}

	@GetMapping(path = "/GetMaxFileLineS")
	public List<BigDecimal> GetMaxFileLineS() {
		return reportRepRepository.getMaxFileLine();
	}

	@PostMapping(path = "/saveFileS")
	public Map saveFileS(@RequestBody List<ReportRepSS> reportRep) {
		Map<String, Object> ret = new HashMap<String, Object>();
		reportRep.forEach(e -> {
			Date s = e.getDar();
			s.setHours(0);
			e.setDar(s);
		});
		Iterable<ReportRepSS> iterable = reportRep;
		Iterable<ReportRepSS> d = reportRepRepository.save(iterable);
		ret.put("obj", d);
		ret.put("success", 1);
		ret.put("status", "01");
		ret.put("msg", "Post successfully saved");
		return ret;
	}

	@PostMapping(path = "/getSqlFileTypeS")
	public List<SqlFileType> getSqlFileTypeS(@RequestBody ReportRepSS reportRep) {
		System.out.println(reportRep.getDar());
//		reportRep.setDar
		return sqlFileTypeRepositoryr.findSqlFileTypeByDarAndFichiAndEtab(removeTime(reportRep.getDar()),
				reportRep.getFichier(), reportRep.getEtab());
	}

	private static Date removeTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@PostMapping(path = "/updateSqlFileS")
	public SqlFileType updateSqlFileS(@RequestBody SqlFileType sqlFileType) {
		SqlFileType t = sqlFileTypeRepositoryr.findById(sqlFileType.getId());
		if (t.setByColumn1(sqlFileType)) {
			t = sqlFileTypeRepositoryr.save(t);
			return t;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/newSqlFileS")
	public Map newSqlFileS(@RequestBody SqlFileType sqlFileType) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Date d = sqlFileType.getDar();
		d.setHours(0);
		sqlFileType.setDar(d);
		sqlFileType.setId(null);
		SqlFileType t = sqlFileTypeRepositoryr.save(sqlFileType);
		ret.put("obj", t);
		ret.put("success", 1);
		ret.put("status", "01");
		ret.put("msg", "Sql successfully saved");
		return ret;
	}

	@PostMapping(path = "/saveFileTypeS")
	public Map saveFileTypeS(@RequestBody List<ReportRepSS> reportRep) {
		Map<String, Object> ret = new HashMap<String, Object>();
		reportRep.forEach(e -> {
			Date s = e.getDar();
			s.setHours(0);
			e.setDar(s);
		});
		Iterable<ReportRepSS> iterable = reportRep;
		Iterable<ReportRepSS> d = reportRepRepository.save(iterable);
		ret.put("obj", d);
		ret.put("success", 1);
		ret.put("status", "01");
		ret.put("msg", "Post successfully saved");
		return ret;
	}

	@PostMapping(path = "/updateComplexControle1S")
	public ReportControleComplexS updateComplexControle1S(@RequestBody ReportControleComplexS reportControleComplex) {
		ReportControleComplexS t = reportControleComplexRepository.findById(reportControleComplex.getId());
		if (t.setByColumn1(reportControleComplex)) {
			t = reportControleComplexRepository.save(t);
			return t;
		} else {
			return null;
		}
	}

	@GetMapping(path = "/deleteComplexControlS/{id}")
	@Transactional
	public Boolean deleteComplexControlS(@PathVariable Long id) {
		reportControleComplexRepository.deleteById(id);
		return true;
	}

	@PostMapping(path = "/saveComplexControlS")
	public ReportControleComplexS saveComplexControlS(@RequestBody ReportControleComplexS reportControleComplex) {
		reportControleComplex.setCrdt((new Timestamp(System.currentTimeMillis()).toString()));
		reportControleComplex.setMdfi((new Timestamp(System.currentTimeMillis()).toString()));
		reportControleComplex.setDele("0");
		reportControleComplex.setId(reportControleComplexRepository.getMax().get(0).longValue() + 1L);
		System.out.println("tgstts sdf");
		System.out.println(reportControleComplex.getId());
		return reportControleComplexRepository.save(reportControleComplex);
	}

	@GetMapping(path = "/ComplexListS")
	public List<ReportControleComplexS> ComplexListS() {
		return reportControleComplexRepository.getComplexControls("0");
	}

	@PostMapping(path = "/updateQualityControle1S")
	public ReportControleQualityS updateQualityControle1S(@RequestBody ReportControleQualityS reportControleQuality) {
		ReportControleQualityS t = reportControleQualityRepository.findById(reportControleQuality.getId());
		if (t.setByColumn1(reportControleQuality)) {
			t = reportControleQualityRepository.save(t);
			return t;
		} else {
			return null;
		}
	}

	@GetMapping(path = "/deleteQualityControlS/{id}")
	@Transactional
	public Boolean deleteQualityControlS(@PathVariable Long id) {
		reportControleQualityRepository.deleteById(id);
		return true;
	}

	@PostMapping(path = "/saveQualityControlS")
	public ReportControleQualityS saveQualityControlS(@RequestBody ReportControleQualityS reportControleQuality) {
		reportControleQuality.setDele("0");
		reportControleQuality.setId(reportControleQualityRepository.getMax().get(0).longValue() + 1L);
		System.out.println("tgstts sdf");
		System.out.println(reportControleQuality.getId());
		return reportControleQualityRepository.save(reportControleQuality);
	}

	@GetMapping(path = "/QualityListS")
	public List<ReportControleQualityS> QualityListS() {
		return reportControleQualityRepository.getQualityControls("0");
	}

	// function to return attribute not having parent in formular and their formular
	// as well as column, post and file
	@PostMapping(path = "/getNotExistingAttributeS")
	public List<Object> getNotExistingAttributeS(@RequestBody ReportRepSS sr, Model m) {
		List<Object> s = reportCalculateService.getAttributeNotExist(m);
		System.out.println("toto enfine boler");
		return s;
	}

}

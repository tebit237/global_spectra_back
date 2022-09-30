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
import org.springframework.http.MediaType;

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
import iwomi.base.ServiceInterface.ReportAnomalyService;
import iwomi.base.ServiceInterface.ReportAttributeService;
import iwomi.base.ServiceInterface.ReportCalculateService;
import iwomi.base.ServiceInterface.ReportControleService;
import iwomi.base.ServiceInterface.ReportRepSErvice;
import iwomi.base.form.ReportRepf;
//import iwomi.base.ServiceInterface.UserService;
import iwomi.base.objects.Conn;
import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportControleComplex;
import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.ReportControleQuality;
import iwomi.base.objects.ReportFile;
import iwomi.base.objects.ReportPost;
import iwomi.base.objects.ReportRep;
import iwomi.base.objects.ReportRep2;
import iwomi.base.objects.ReportRepId;
import iwomi.base.objects.ReportRepSS;
import iwomi.base.objects.SqlFileType;
import iwomi.base.objects.User;
import iwomi.base.objects.GenFile;
import iwomi.base.objects.Invgenr;
import iwomi.base.objects.ReportCalculateS2;
import iwomi.base.repositories.GenFileRepository;
import iwomi.base.repositories.InputWriteRepository;
import iwomi.base.repositories.InvgenrRepository;
import iwomi.base.repositories.NomenclatureRepository;
import iwomi.base.repositories.ReportAttributeRepository;
import iwomi.base.repositories.ReportCalculateIdRepositoryS;
import iwomi.base.repositories.ReportCalculateRepository;
import iwomi.base.repositories.ReportControleComplexRepository;
import iwomi.base.repositories.ReportControleInterRepository;
import iwomi.base.repositories.ReportControleIntraRepository;
import iwomi.base.repositories.ReportControleQualityRepository;
import iwomi.base.repositories.ReportFileRepository;
import iwomi.base.repositories.ReportPostRepository;
import iwomi.base.repositories.ReportRepRepository;
import iwomi.base.repositories.ReportRepRepository2;
import iwomi.base.repositories.SqlFileTypeRepository;
import iwomi.base.services.ManageExcelFiles;
import iwomi.base.services.MediaTypeUtils;
import iwomi.base.services.NomenclatureServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Types;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResponseMessage;

@RestController
@Component
public class ReportParameters {

    @Autowired
    private ReportFileRepository reportFileRepository;
    @Autowired
    private NomenclatureRepository nomenclatureRepository;
    @Autowired
    private ReportAttributeRepository reportAttributeRepository;
    @Autowired
    private ReportAttributeService reportAttributeService;
    @Autowired
    private NomenclatureServiceImpl nomenclatureService;
    @Autowired
    private NomenclatureService nomenclatureService1;
    @Autowired
    private ReportRepRepository reportRepRepository;
    @Autowired
    private ReportRepRepository2 reportRepRepository2;
    @Autowired
    private ReportCalculateRepository reportCalculateRepository;
    @Autowired
    InputWriteRepository inputWriteRepository;
    @Autowired
    ReportCalculateService reportCalculateService;
//	@Autowired
//	UserService userService;
    @Autowired
    ReportRepSErvice reportRepSErvice;
    @Autowired
    private ReportAnomalyService reportAnormalyService;
    @Autowired
    private ReportControleIntraRepository reportControleIntraRepository;
    @Autowired
    private ReportControleComplexRepository reportControleComplexRepository;
    @Autowired
    private ReportControleQualityRepository reportControleQualityRepository;
    @Autowired
    private ReportControleService reportControleService;
    @Autowired
    private SqlFileTypeRepository sqlFileTypeRepository;

    @GetMapping(path = "/calculation/{fich}")
    public ReportAttribute getreportAttribute(@PathVariable String fich) {
        return new ReportAttribute();
    }

//    @GetMapping(path = "/automatic")
//    public String perodic(@PathVariable String fich) {
//        Thread t = new Thread(new Runnable() {
//
//            public void run() {
//                chargerDonneesService.extrairesFromClientDatabase(fic);
//            }
//        ;
//        });
//         t.start();
//        return "1";
//    }
    @GetMapping(path = "/updatecell/{fich}/{post}/{col}/{val}")
    public String getcell(@PathVariable String fich, @PathVariable String post, @PathVariable Long col,
            @PathVariable String val) {
        ArrayList<iwomi.base.objects.ReportFile> t = (ArrayList<iwomi.base.objects.ReportFile>) reportFileRepository
                .findReportFileByFichAndPosteAndCol(fich, post, col);
        iwomi.base.objects.ReportFile tp = t.get(0);
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

    @RequestMapping("/books")
    public String books(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("from") LocalDate from,
            Model model) {
        model.addAttribute("from", from);
        return "books.jsp";
    }

    @PostMapping(path = "/updatecellpre1")
    public ReportRep updatecellpre1(@RequestBody ReportRep reportRep) {
//	public Date updatecellpre1(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") Date from) {
        try {
            reportRep.setDar(new SimpleDateFormat("yyyy-MM-dd").parse(reportRep.getFeuille()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        reportRep.setFeuille(null);
        ReportRep s;
        // valc is the parameter always comming back with the value
        List<ReportRep> t = reportRepRepository.findByPostAndFichierAndCol(reportRep.getPost(), reportRep.getFichier(),
                reportRep.getCol());
        if (t.size() == 1 && t.get(0).setByColumn11(reportRep)) {
            return reportRepRepository.save(t.get(0));
        } else {
            return null;
        }
    }

    @PostMapping(path = "/updatecellpre11")
    public Map<String, Object> updatecellpre11(@RequestBody ReportRep reportRep) {
        ReportRep s;
        Map<String, Object> ret = new HashMap<String, Object>();
        // valc is the parameter always coming back with the value
        reportRep.getDar();
        System.out.println(reportRep.getDar());
        System.out.println(reportRep.getFichier());
        System.out.println(reportRep.getPost());
        System.out.println(reportRep.getCol());
        System.out.println(reportRep.initialDate());
        List<ReportRep> t = reportRepRepository.findByFichierAndPostAndColAndDar(reportRep.getFichier(),
                reportRep.getPost(), reportRep.getCol(), reportRep.initialDate());
        System.out.println("this is the date :" + reportRep.initialDate().toString());
        System.out.println("tebit t size is :" + t.toString());
        if (t.size() == 1 && t.get(0).setByColumn11(reportRep)) {
            System.out.println("here");
            ret.put("obj", reportRepRepository.save(t.get(0)));
            ret.put("success", 1);
            ret.put("status", "01");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            ret.put("msg", "Champ : CH" + reportRep.getPost() + "C" + reportRep.getCol() + ", a date de cloture : "
                    + dateFormat.format(reportRep.initialDate()) + " Modifier avec success");
            ret.put("obj", t);
            return ret;
        } else {
            System.out.println("else");
            ret.put("success", 0);
            ret.put("status", "0010");
            ret.put("msg", "le champ est dupliqué, appelez l'administrateur pour plus d'assistance");
            return ret;
        }
    }

    // **Attribute
    @PostMapping("/allReportAttributeFilter")
    public List<ReportAttribute> reportAttributes(@RequestBody ReportAttribute s) {

        return reportAttributeService.constructfilterquery1(s);
//		PageRequest topTen =  new PageRequest(0, 10, Direction.ASC, "id"); 
//		return reportAttributeRepository.findByDele(false, topTen);
    }

    @PostMapping("/getFormulaElement")
    public Model getFormulaElement(@RequestBody ReportRep s) {

        return reportCalculateService.get_cells(s);
    }

    // **Attribute
    @PostMapping("/allReportCalcuteFilter")
    public List<ReportCalculate> allReportCalcuteFilter(@RequestBody ReportCalculate s) {
        return reportCalculateService.constructfilterquery1(s);
    }

    @PostMapping("/allReportPostFilter")
    public List<Nomenclature> allReportPostFilter(@RequestBody Nomenclature s) {
        s.setTabcd("4001");
        return nomenclatureService1.constructfilterquery2(s);
    }

//	@PostMapping("/allUserFilter")
//	public List<User> allUserFilter(@RequestBody User s) {
//		return userService.constructfilterquery2(s);
//	}
    @PostMapping("/allControlIntraFilter")
    public List<ReportControleIntra> allControlIntraFilter(@RequestBody ReportControleIntra s) {
        return reportControleService.constructfilterqueryintra1(s);
    }

    @PostMapping("/allControlInterFilter")
    public List<ReportControleInter> allControlInterFilter(@RequestBody ReportControleInter s) {
        return reportControleService.constructfilterqueryinter1(s);
    }

    @PostMapping("/allreportRecodeFilter")
    public List<ReportRep> allreportRecodeFilter(@RequestBody ReportRep s) {
        System.out.println("the post value is" + s.getPost());
        System.out.println("the post value is" + s.getFichier());
        System.out.println("the post value is" + s.getFeild());
        if (s.getPost() != null) {
            return reportRepRepository.findByFichierPostDar(s.getPost(), s.getFichier(), s.getFeild());
        } else {
            return reportRepRepository.findByFichierDar(s.getFichier(), s.getFeild());
        }
    }

    @Autowired
    ReportCalculateIdRepositoryS reportCalculateIdRepositoryS;

    @RequestMapping("/postinsertstructure/{fich}/{abovePost}/{addingPost}")
    public int postinsertstructure(@PathVariable String fich, @PathVariable String abovePost, @PathVariable String addingPost, Model model) {
        List<ReportFile> r = reportFileRepository.getabove(fich, abovePost);
        List<ReportFile> ryy = reportFileRepository.getabove(fich, addingPost);
        if (ryy.size() == 0) {
            //add in r+1
            Long rg = r.get(0).getRang();
            jdbcTemplate.update("update rppfich set rang = rang+ 1 where rang >" + rg + " and fich = '" + fich + "'");
            Long o = reportFileRepository.getMax().get(0).longValue();
            System.out.println("max id is " + o);
            for (ReportFile t : r) {
                System.out.println("inserting id is " + o);
                ReportFile y = new ReportFile();
                y.setCol(t.getCol());
                y.setCrdt(t.getCrdt());
                y.setEtab(t.getEtab());
                y.setFeui(t.getFeui());
                y.setFich(t.getFich());
                y.setGen(t.getGen());
                y.setPoste(addingPost);
                y.setTfic(t.getTfic());
                y.setRang(rg + 1);
                y.setId(++o);
                reportFileRepository.save(y);
            }
        }
        List<ReportCalculateS2> rty = reportCalculateIdRepositoryS.findByFilePost(fich, abovePost);
        List<ReportCalculateS2> rt = reportCalculateIdRepositoryS.findByFilePost(fich, addingPost);
        if (rt.size() == 0) {
            Long ot = reportFileRepository.getMax().get(0).longValue();
            for (ReportCalculateS2 t : rty) {
                ReportCalculateS2 y = new ReportCalculateS2();
                y.setCol(t.getCol());
                y.setCrdt(t.getCrdt());
                y.setEtab(t.getEtab());
                y.setDele(t.getDele());
                y.setFichi(t.getFichi());
                y.setDivd(t.getDivd());
                y.setPost(addingPost);
                y.setField(t.getField().replaceAll(t.getPost(), addingPost));
                y.setSource(t.getSource());
                y.setTypeval(t.getTypeval());
                y.setTysorce(t.getTysorce());
                y.setCalc(t.getCalc().replaceAll(t.getPost(), addingPost));
                y.setId(++ot);
                reportCalculateIdRepositoryS.save(y);
            }
        }
        return 1;
    }

    @RequestMapping("/postinsertstructureabov/{fich}/{abovePost}/{addingPost}")
    public int postinsertstructureabov(@PathVariable String fich, @PathVariable String abovePost, @PathVariable String addingPost, Model model) {
        List<ReportFile> r = reportFileRepository.getabove(fich, abovePost);
        List<ReportFile> ryy = reportFileRepository.getabove(fich, addingPost);
        if (ryy.size() == 0) {
            //add in r+1
            Long rg = r.get(0).getRang();
            jdbcTemplate.update("update rppfich set rang = rang+ 1 where rang >=" + rg + " and fich = '" + fich + "'");
            Long o = reportFileRepository.getMax().get(0).longValue();
            System.out.println("max id is " + o);
            for (ReportFile t : r) {
                System.out.println("inserting id is " + o);
                ReportFile y = new ReportFile();
                y.setCol(t.getCol());
                y.setCrdt(t.getCrdt());
                y.setEtab(t.getEtab());
                y.setFeui(t.getFeui());
                y.setFich(t.getFich());
                y.setGen(t.getGen());
                y.setPoste(addingPost);
                y.setTfic(t.getTfic());
                y.setRang(rg);
                y.setId(++o);
                reportFileRepository.save(y);
            }
        }
        List<ReportCalculateS2> rty = reportCalculateIdRepositoryS.findByFilePost(fich, abovePost);
        List<ReportCalculateS2> rt = reportCalculateIdRepositoryS.findByFilePost(fich, addingPost);
        if (rt.size() == 0) {
            Long ot = reportFileRepository.getMax().get(0).longValue();
            for (ReportCalculateS2 t : rty) {
                ReportCalculateS2 y = new ReportCalculateS2();
                y.setCol(t.getCol());
                y.setCrdt(t.getCrdt());
                y.setEtab(t.getEtab());
                y.setDele(t.getDele());
                y.setFichi(t.getFichi());
                y.setDivd(t.getDivd());
                y.setPost(addingPost);
                y.setField(t.getField().replaceAll(t.getPost(), addingPost));
                y.setSource(t.getSource());
                y.setTypeval(t.getTypeval());
                y.setTysorce(t.getTysorce());
                y.setCalc(t.getCalc().replaceAll(t.getPost(), addingPost));
                y.setId(++ot);
                reportCalculateIdRepositoryS.save(y);
            }
        }
        return 1;
    }

    @RequestMapping("/deletePostelm/{fich}/{post}")
    public int deletePost(@PathVariable String fich, @PathVariable String post, Model model) {
        List<ReportFile> r = reportFileRepository.getabove(fich, post);
        for (ReportFile t : r) {
            reportFileRepository.delete(t);
        }
        List<ReportCalculateS2> rty = reportCalculateIdRepositoryS.findByFilePost(fich, post);
        for (ReportCalculateS2 t : rty) {
            reportCalculateIdRepositoryS.delete(t);
        }
        return 1;
    }

    @RequestMapping("/postinsertcalc/{fich}/{abovePost}/{addingPost}")
    public void postinsertcalc(@PathVariable String fich, @PathVariable String abovePost, @PathVariable String addingPost, Model model) {
        List<ReportCalculateS2> rty = reportCalculateIdRepositoryS.findByFilePost(fich, abovePost);
        Long ot = reportFileRepository.getMax().get(0).longValue();
        for (ReportCalculateS2 t : rty) {
            ReportCalculateS2 y = new ReportCalculateS2();
            y.setCol(t.getCol());
            y.setCrdt(t.getCrdt());
            y.setEtab(t.getEtab());
            y.setDele(t.getDele());
            y.setFichi(t.getFichi());
            y.setPost(addingPost);
            y.setField(t.getField().replaceAll(t.getPost(), addingPost));
            y.setSource(t.getSource());
            y.setTypeval(t.getTypeval());
            y.setTysorce(t.getTysorce());
            y.setCalc(t.getCalc().replaceAll(t.getPost(), addingPost));
            y.setId(++ot);
            reportCalculateIdRepositoryS.save(y);
        }
    }

    @PostMapping("/allreportAnomalyFilter")
    public List<ReportAnomaly> allreportAnomalyFilter(@RequestBody ReportAnomaly s) {
        return reportAnormalyService.constructfilterquery1(s);
    }
//
//    @PostMapping("/IVGFile")
//    public List<Invgenr> IVGFile(@RequestBody Invgenr s) {
//        return reportAnormalyService.constructfilterquery1(s);
//    }

    @GetMapping("/allReportAttribute")
    public List<ReportAttribute> reportAttributes() {
        return reportAttributeRepository.findByDeleOrderByMdfiDesc(false);
//		PageRequest topTen =  new PageRequest(0, 10, Direction.ASC, "id"); 
//		return reportAttributeRepository.findByDele(false, topTen);
    }

    @PostMapping(path = "/updateAttribue1")
    public Map updateAttribue1(@RequestBody ReportAttribute reportAttribute) {
        Map<String, Object> ret = new HashMap<String, Object>();
        ReportAttribute t = reportAttributeRepository.findById(reportAttribute.getId());
        if (reportAttribute.getAtt() != null
                && reportAttributeRepository.findByAttAndDele(reportAttribute.getAtt(), false).size() > 0) {
            ret.put("success", 0);
            ret.put("status", "0003");
            ret.put("msg", "Modification d'un attribut existant");
            ret.put("obj", t);
            return ret;
        }
        if (t.setByColumn1(reportAttribute)) {
            t.setMdfi(new Date());
            t = reportAttributeRepository.save(t);
            ret.put("success", 1);
            ret.put("status", "01");
            ret.put("msg", "Attribut mis à jour avec succès");
            ret.put("obj", t);
            return ret;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/updateFilefield")
    public Map updateFilefield(@RequestBody ReportRep reportRep) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Date e = reportRep.getDar();
        e.setHours(0);
        ReportRep t = reportRepRepository.findByFichierAndColAndRangAndDar(reportRep.getFichier(), reportRep.getCol(),
                reportRep.getRang(), e);
        System.out.println("the sizeo is :");
        System.out.println(t);
        if (t != null) {
            t.setValc(reportRep.getValc());
            t = reportRepRepository.save(t);
            ret.put("success", 1);
            ret.put("status", "01");
            ret.put("msg", "Champ :" + (reportRep.getFichier()) + " mise à jour réussie");
            ret.put("obj", t);
            return ret;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/updateFilefield_v1")
    public ReportRep2 updateFilefield_v1(@RequestBody ReportRep2 reportRep) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Date e = reportRep.getDar();
        e.setHours(0);
//		return reportRep;
        return reportRepRepository2.save(reportRep);
    }

    @GetMapping(path = "/getComments/{fichi}")
    public Map<String, Object> getComments(@PathVariable String fichi) {
        Map<String, Object> ret = new HashMap<String, Object>();
        inputWriteRepository.findcomment(fichi).forEach(e -> {
            ret.put(e.getPost().trim(), e.getQuest());
        });
        return ret;
    }

    // **Attribute
    @GetMapping("/allReportAttributeFilter/{type}/{lib}/{att}")
    public List<ReportAttribute> allReportAttributeFilter(@PathVariable String type, @PathVariable String lib,
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

    @GetMapping(path = "/updateAttribue/{id}/{column}/{val}")
    public ReportAttribute updateAttribue(@PathVariable Long id, @PathVariable String column,
            @PathVariable String val) {
        try {
            val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
            ReportAttribute t = reportAttributeRepository.findById(id);
            ReportAttribute tr = reportAttributeRepository.findByAttAndIdNot(val, id);
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

    @GetMapping(path = "/getAttributeKey/{id}")
    public Map getAttributeKey(@PathVariable Long id) {
        List<ReportAttribute> t = reportAttributeRepository.findByIdAndDele(id, false);

        Map<String, String> m = new HashMap<>();
        m.put("att", t.get(0).getAtt());
        return m;

    }

//	@GetMapping(path = "/deleteFileField/{id}")
    @PostMapping(path = "/deleteFileField1")
    @Transactional
    public Boolean deleteFileField1(@RequestBody ReportRep reportRep) {
        Date s = reportRep.getDar();
        s.setHours(0);
        reportRep.setDar(s);
        System.out.println(reportRep.getDar());
        List<ReportRep> p = reportRepRepository.findByFichierAndRangAndDar(reportRep.getFichier(), reportRep.getRang(),
                reportRep.getDar());
        p.forEach(e -> {
            System.out.println("delating this object " + reportRep.getFichier() + " ");
            reportRepRepository.delete(e);
        });
        return false;
    }

    @GetMapping(path = "/deleteFileField/{id}")
    @Transactional
    public Boolean deleteFileField(@PathVariable Long id) {

        if (id != null) {
            System.out.println("it is the range" + id);
            List<ReportRep2> s = reportRepRepository2.findByRang(id);
            System.out.println("the syste is " + s.size());
            s.forEach(e -> {
                System.out.println("its delating 1" + e.getValc());
                reportRepRepository2.delete(new ReportRepId(e.getRang(), e.getDar(), e.getCol(), e.getFichier()));
            });
            return true;
        }
        return false;
    }

    @GetMapping(path = "/deleteFileFieldType/{id}")
    @Transactional
    public Boolean deleteFileFieldType(@PathVariable Long id) {
        if (id != null) {
            sqlFileTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @GetMapping(path = "/deleteAttribute/{id}")
    @Transactional
    public Boolean deleteAttribute(@PathVariable Long id) {
        if (id != null) {
            reportAttributeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Autowired
    ManageExcelFiles manageExcelFiles;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/upload")
    public Map<String, Object> uploadFile2(
            @RequestParam("fil") MultipartFile fileext,
            @RequestParam("numcol") int numcol,
            @RequestParam("filename") String fich,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dar
    ) {
        String message = "";

        Map<String, Object> ret = new HashMap<String, Object>();
        if (manageExcelFiles.hasExcelFormat(fileext)) {
            try {
                System.out.println("verify the date :" + dar);

                Long w = reportRepRepository.deleteByFichierAndDar(fich, dar);
                System.out.println("total deleted :" + w);

                List<ReportRep> r = manageExcelFiles.excelToReport(fileext.getInputStream(), fich, dar, numcol);
                reportRepRepository.save(r);
                message = "Uploaded the file successfully: " + fileext.getOriginalFilename();
                ret.put("msg", "Fichier Charger avec success");
                ret.put("status", "01");
                return ret;
            } catch (Exception e) {
                message = "Could not upload the file: " + fileext.getOriginalFilename() + "!";
                ret.put("msg", "Fichier invalide.Verifier et recharger fichier");
                ret.put("status", "100");
                return ret;
            }
        }
        message = "Please upload an excel file!";
        ret.put("msg", message);
        ret.put("status", "100");
        return ret;
    }

    @PostMapping(path = "/saveAttribute")
    public Map<String, Object> saveAttribute(@RequestBody ReportAttribute reportAttribute) {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ReportAttribute> d = reportAttributeRepository.findByAttAndDele(reportAttribute.getAtt(), false);
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

//        reportAttribute.setId(reportAttributeRepository.getMax().get(0).longValue() + 1L);
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
    //exclusively to modify

    @PostMapping(path = "/updatecellprev1")
    public Map<String, Object> updatecellprev1(@RequestBody ReportRep reportRep) {
        ReportRepSS s;
        Map<String, Object> ret = new HashMap<String, Object>();
        // valc is the parameter always coming back with the value
        reportRep.getDar();
        System.out.println(reportRep.getDar());
        System.out.println(reportRep.getFichier());
        System.out.println(reportRep.getPost());
        System.out.println(reportRep.getCol());
        System.out.println(reportRep.initialDate());

        List<ReportRep> t = reportRepRepository.findByFichierAndRangAndColAndDar(reportRep.getFichier(),
                reportRep.getRang(), reportRep.getCol(), reportRep.initialDate());
        System.out.println("this is the date :" + reportRep.initialDate().toString());
        System.out.println("tebit t size is :" + t.size());
        if (t.size() == 1) {
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

    @PostMapping(path = "/updatecellfileWithPost")
    public Map<String, Object> updatecellprev2(@RequestBody ReportRep reportRep) {
        ReportRepSS s;
        Map<String, Object> ret = new HashMap<String, Object>();
        // valc is the parameter always coming back with the value
        reportRep.getDar();
        System.out.println(reportRep.getDar());
        System.out.println(reportRep.getFichier());
        System.out.println(reportRep.getPost());
        System.out.println(reportRep.getCol());
        System.out.println(reportRep.initialDate());

        List<ReportRep> t = reportRepRepository.findByFichierAndPostAndColAndDar(reportRep.getFichier(),
                reportRep.getPost(), reportRep.getCol(), reportRep.initialDate());
        System.out.println("this is the date :" + reportRep.initialDate().toString());
        System.out.println("tebit t size is :" + t.size());
        if (t.size() == 1) {
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
//           BigDecimal rg =  reportRepRepository.getMaxFileLinev1(reportRep.getFichier()).get(0);
            ReportFile d = reportFileRepository.findReportFileByFichAndPosteAndCol(reportRep.getFichier(), reportRep.getPost(), new Long(reportRep.getCol())).get(0);
            System.out.println("else :" + d.getRang());

//            BigDecimal id = reportRepRepository.getMaxFileLinev2().get(0);
//            System.out.println(id);
//           reportRep.setId(id.longValue()+1);
            reportRep.setRang(d.getRang());
            reportRep = reportRepRepository.save(reportRep);
            ret.put("success", 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            ret.put("msg", "Field : CH" + reportRep.getPost() + "C" + reportRep.getCol() + ", of Closing date : "
                    + dateFormat.format(reportRep.initialDate()) + " successfully modified");
            ret.put("status", "01");
            ret.put("obj", reportRep);
//            ret.put("msg", "field is duplicated, call administrator for further assistance");
            return ret;
        }
    }

    @PostMapping(path = "/deleteFileFieldv1")
    @Transactional
    public Boolean deleteFileFieldv1(@RequestBody HashMap<String, String> ld) {
        System.out.println("it is the range" + ld.get("rang"));
        List<ReportRep> s = reportRepRepository.findByRangAndFichier(Long.parseLong(ld.get("rang")), ld.get("fichier"));
        System.out.println("the syste is " + s.size());
        s.forEach(e -> {
            System.out.println("its delating 4" + e.getValc());
            reportRepRepository.delete(e.getId());
//				reportRepRepository2.delete(new ReportRepIdS(e.getRang(), e.getDar(), e.getCol(), e.getFichier()));
        });
        return true;
    }
    // **Attribute

    @GetMapping("/AttributDropdown")
    public Map<String, Object> AttributDropdown() {
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
    @GetMapping("/AttributkeyDropdown")
    public Map<String, Object> AttributkeyDropdown() {
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

    private String tabcd = "4001";

    @GetMapping(path = "/getPost")
    public List<Nomenclature> getPost() {
        List<Nomenclature> e = nomenclatureRepository.findBytabcdAndDelequery(tabcd, 0);
//        System.out.println("its delating " + e.size());
        return e;
    }

    @GetMapping(path = "/getPost1")
    public String getPost1() {
        Float s = 999999999999999f;
        if (s == 999999999999999.0) {
            return "4";
        } else {
            return "5";
        }
    }

    // filter by post
    @GetMapping(path = "/getPostFilter/{acscd}")
    public List<Nomenclature> getPostFilter(@PathVariable String acscd) {
        return nomenclatureRepository.findBytabcdAndDeleAndAcscd1(tabcd, 0, acscd);
    }

    /*
	 * chapter collection
	 * 
     */
    @Autowired
    private ReportPostRepository reportPostRepository;

    @PostMapping(path = "/saveNewPost")
    public Map savePost(@RequestBody Nomenclature post) {
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
//        post.setId(nomenclatureRepository.getMax().get(0).longValue() + 1L);
        ret.put("obj", nomenclatureRepository.save(post));
        ret.put("success", 1);
        ret.put("status", "01");
        ret.put("msg", "Post successfully saved");
        return ret;
    }

    @GetMapping(path = "/updatePost/{id}/{column}/{val}")
    public Map updatePost(@PathVariable Long id, @PathVariable String column, @PathVariable String val) {
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

    @PostMapping(path = "/updatePost1")
    public Map updatePost1(@RequestBody Nomenclature nomenclature) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Nomenclature t = nomenclatureRepository.findById(nomenclature.getId());
//		if (reportAttribute.getAtt() != null
//				&& reportAttributeRepository.findByAttAndDele(reportAttribute.getAtt(), false).size() > 0) {
//			ret.put("success", 0);
//			ret.put("status", "0003");
//			ret.put("msg", "Modified to an existing Attribute");
//			ret.put("obj", t);
//			return ret;
//		}
        if (t.setByColumn1(nomenclature)) {
            t.setMdfi(new Date());
            t = nomenclatureRepository.save(t);
            ret.put("success", 1);
            ret.put("status", "01");
            ret.put("msg", "Post successfully Updated");
            ret.put("obj", t);
            return ret;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/deletePost/{id}")
    @Transactional
    public Boolean deletePost(@PathVariable long id) {
        nomenclatureRepository.deleteById(id);
        return true;
    }

    @GetMapping(path = "/getChapter/{post}")
    public List<ReportPost> getChapter(@PathVariable String post) {
        return reportPostRepository.findReportPostByCodepAndDele(post, 0L);
    }

    @GetMapping(path = "/updateChapitre/{id}/{column}/{val}")
    public ReportPost updateChapitre(@PathVariable Long id, @PathVariable String column, @PathVariable String val) {
        try {
            val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
            ReportPost t = reportPostRepository.findById(id);
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

    @PostMapping(path = "/updateChapitre1")
    public ReportPost updateChapitre1(@RequestBody ReportPost reportPost) {
        ReportPost t = reportPostRepository.findById(reportPost.getId());
        if (t.setByColumn1(reportPost)) {
            t = reportPostRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/saveNewChapter")
    public Map saveChapter(@RequestBody ReportPost reportPost) {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<Nomenclature> s = nomenclatureRepository.findBytabcdAndDeleAndAcscd1(tabcd, 0, reportPost.getCodep());
        reportPost.setCrdt((new Timestamp(System.currentTimeMillis())).toString());
        reportPost.setMdfi(new Timestamp(System.currentTimeMillis()));
        reportPost.setDele(0L);
        reportPost.setLebel(s.get(0).getLib1());
//        reportPost.setId(reportPostRepository.getMax().get(0).longValue() + 1L);
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
    @GetMapping(path = "/deleteChapitre/{id}")
    public Long deleteChapitre(@PathVariable long id) {
//		return reportPostRepository.save(f);
        reportPostRepository.deleteById(id);
        return id;
    }

    @GetMapping(path = "/calculationList/{fichi}")
    public List<ReportCalculate> calculationList(@PathVariable String fichi) {
        return reportCalculateRepository.findByDeleAndFichi(0L, fichi);
    }

    @PostMapping(path = "/saveCalculate")
    public Map saveCalculate(@RequestBody ReportCalculate reportCalculate) {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ReportCalculate> r;
        if (reportCalculate.getTysorce() == "final") {
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

    @GetMapping(path = "/deleteCalcule/{id}")
    @Transactional
    public Boolean deleteCalcule(@PathVariable Long id) {
        reportCalculateRepository.deleteById(id);
//		t.setDele(1L);
//		t = reportCalculateRepository.save(t);
        ReportCalculate t = reportCalculateRepository.findById(id);
        if (t == null) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(path = "/getCalculAtt/{calstring}")
    public List<Long> getCalculAtt(@PathVariable String calstring) {
        return reportCalculateService.dismantlePostCalculate(calstring);
    }

    @GetMapping(path = "/getCalculAtt1/{calstring}")
    public List<Long> getCalculAtt1(@PathVariable String calstring) {
        return reportCalculateService.dismantlePostCalculate2(calstring);
    }

    @PostMapping(path = "/getCalculAtt1")
    public List<Long> getCalculAtt2(@RequestBody Conn calstring) {
        return reportCalculateService.dismantlePostCalculate2(calstring.getConn());
    }

    /*
	 * intra models
     */
    // **intra Control
    @GetMapping(path = "/intraList")
    public List<ReportControleIntra> intraList() {
        return reportControleIntraRepository.findIntra();
//        return reportControleIntraRepository.findIntra("0");
    }

    @PostMapping(path = "/saveIntraControl")
    public ReportControleIntra saveIntraControl(@RequestBody ReportControleIntra reportControleIntra) {
        Date s = new Timestamp(System.currentTimeMillis());
        reportControleIntra.setCrdt(s.toString());
        reportControleIntra.setMdfi((new Timestamp(System.currentTimeMillis())));
        reportControleIntra.setDele("0");
        reportControleIntra.setTyp("T");
//        reportControleIntra.setId(reportControleIntraRepository.getMax().get(0).longValue() + 1L);
        return reportControleIntraRepository.save(reportControleIntra);

    }

    @GetMapping(path = "/deleteIntraControl/{id}")
    public ReportControleIntra deleteIntraControl(@PathVariable Long id) {
        ReportControleIntra t = reportControleIntraRepository.findById(id);
        t.setDele("1");
        t = reportControleIntraRepository.save(t);
        try {
            reportControleIntraRepository.deleteById(id);
        } catch (Exception r) {
            System.out.println("Cant delete the file :" + r);
        }
        return t;

    }

    @GetMapping(path = "/updateIntraControle/{id}/{column}/{val}")
    public ReportControleIntra updateIntraControle(@PathVariable Long id, @PathVariable String column,
            @PathVariable String val) {
        try {
            String vsal = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
            ReportControleIntra t = reportControleIntraRepository.findOne(id);
            if (t.setByColumn(column, vsal)) {
                return reportControleIntraRepository.save(t);
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
    private ReportControleInterRepository reportControleInterRepository;

    @GetMapping(path = "/InterList")
    public List<ReportControleInter> InterList() {
        return reportControleInterRepository.getInterControls("0");
    }

    @PostMapping(path = "/saveInterControl")
    public ReportControleInter saveInterControl(@RequestBody ReportControleInter reportControleInter) {
        reportControleInter.setCrdt((new Timestamp(System.currentTimeMillis()).toString()));
        reportControleInter.setMdfi((new Timestamp(System.currentTimeMillis())));
        reportControleInter.setDele("0");
        reportControleInter.setTyp("R");
//        reportControleInter.setId(reportControleIntraRepository.getMax().get(0).longValue() + 1L);
        return reportControleInterRepository.save(reportControleInter);
    }

    @GetMapping(path = "/deleteInterControl/{id}")
    @Transactional
    public Boolean deleteInterControl(@PathVariable Long id) {
        reportControleInterRepository.deleteById(id);
//		ReportControleInter t = reportControleInterRepository.findById(id);
//		if (t.equals(null)) {
        return true;
//		} else {
//			return false;
//		}
    }

    @GetMapping(path = "/updateInterControle/{id}/{column}/{val}")
    public ReportControleInter updateInterControle(@PathVariable Long id, @PathVariable String column,
            @PathVariable String val) {
        try {
            val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
            ReportControleInter t = reportControleInterRepository.findById(id);
            if (t.setByColumn(column, val)) {
                t = reportControleInterRepository.save(t);
                return t;
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(path = "/updateIntraControle1")
    public ReportControleIntra updateIntraControle1(@RequestBody ReportControleIntra reportControleIntra) {
        ReportControleIntra t = reportControleIntraRepository.findById(reportControleIntra.getId());
        if (t.setByColumn1(reportControleIntra)) {
            t = reportControleIntraRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/updateInterControle1")
    public ReportControleInter updateInterControle1(@RequestBody ReportControleInter reportControleInter) {
        ReportControleInter t = reportControleInterRepository.findById(reportControleInter.getId());
        if (t.setByColumn1(reportControleInter)) {
            t = reportControleInterRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/CalculationDetail/{fichi}/{date}/{post}/{col}")
    public Map CalculationDetail(@PathVariable String fichi, @PathVariable String date, @PathVariable String post,
            @PathVariable String col) {
        return reportCalculateService.getFormularAndSubFields(fichi, post, col, date);
    }

    @GetMapping(path = "/controlDetail/{id}/{key}/{date}")
    public Map controlDetail(@PathVariable Long id, @PathVariable String key, @PathVariable String date) {
        return reportCalculateService.getFormularAndSubFieldsControl(id, key, date);
    }

    @PostMapping(path = "/GetFiles")
    public List<ReportRep> GetFiles(@RequestBody ReportRep reportRep) {
        System.out.println("the date is :" + reportRep.getDar());
        Date s = reportRep.getDar();
        s.setHours(0);
        reportRep.setDar(s);
        List<ReportRep> e = reportRepRepository.findByFichierAndDarOrderByRangAscColAsc(reportRep.getFichier(),
                reportRep.getDar());
        System.out.println(" this is the output of reportrep");
        System.out.println(e.size());
        return e;
    }

    @PostMapping(path = "/GetMaxFileLine")
    public List<BigDecimal> GetMaxFileLine(@RequestBody HashMap<String, String> h) {
        return reportRepRepository.getMaxFileLinev1(h.get("fichier"), h.get("dar"));
    }

    @PostMapping(path = "/saveFile")
    public Map saveFile(@RequestBody List<ReportRep> reportRep) {
        Map<String, Object> ret = new HashMap<String, Object>();
        reportRep.forEach(e -> {
            Date s = e.getDar();
            s.setHours(0);
            e.setDar(s);
        });
        Iterable<ReportRep> iterable = reportRep;
        Iterable<ReportRep> d = reportRepRepository.save(iterable);
        ret.put("obj", d);
        ret.put("success", 1);
        ret.put("status", "01");
        ret.put("msg", "Post successfully saved");
        return ret;
    }
    @Autowired
    InvgenrRepository invgenrRepository;

    @PostMapping(path = "/saveFileinvg")
    public Map saveFileinvg(@RequestBody Invgenr ing) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Invgenr d = invgenrRepository.save(ing);
        ret.put("obj", d);
        ret.put("success", 1);
        ret.put("status", "01");
        ret.put("msg", "Post successfully saved");
        return ret;
    }

    @PostMapping(path = "/getSqlFileType")
    public List<SqlFileType> getSqlFileType(@RequestBody ReportRep reportRep) {
        System.out.println(reportRep.getDar());
//		reportRep.setDar
        return sqlFileTypeRepository.findSqlFileTypeByDarAndFichiAndEtab(removeTime(reportRep.getDar()),
                reportRep.getFichier(), reportRep.getEtab());
    }

    @PostMapping(path = "/getSqlFileType1")
    public Map<String, Object> getSqlFileType1(@RequestBody ReportRep reportRep) {
        Map<String, Object> ret = new HashMap<String, Object>();
        System.out.println(reportRep.getDar());
        ret.put("count", sqlFileTypeRepository.countByFichierAndDar(reportRep.getFichier(), reportRep.getFeuille()));
        ret.put("sql", sqlFileTypeRepository.findSqlFileTypeByDarAndFichiAndEtab1(reportRep.getFeuille(),
                reportRep.getFichier(), reportRep.getEtab(), new Integer(reportRep.getCuser()), new Integer(reportRep.getMuser())));
        return ret;
    }

    @PostMapping(path = "/getExportSqlFileType")
    public void exportToExcel(HttpServletResponse response, @RequestBody ReportRepf f) {

        response.setContentType("application/vnd.ms-excel");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<SqlFileType> r = sqlFileTypeRepository.findSqlFileTypeByDarAndFichiAndEtab(removeTime(f.getDar()),
                f.getFichier(), f.getEtab());
        System.out.println("this is the content :" + f.getDesp());
        ManageExcelFiles excelExporter = new ManageExcelFiles(r, new Integer(f.getCuser()), f.getDesp());
        excelExporter.export(response);
    }

//    @PostMapping(path = "/downloadGenFile")
//    public void downloadGenFile(HttpServletResponse response, @RequestBody GenFile f) {
//        FileInputStream stream = null;
//        try {
//            // Get the directory and iterate them to get file by file...
//            File file = new File(f.getCode());
//            if (!file.exists()) {
//                System.out.println("file not found : " + f.getCode());
//            } else {
//                response.setContentType("text/plain");
//                response.setHeader("Content-Disposition", "attachment;"
//                        + "filename= filename");
//                stream = new FileInputStream(file);
//                response.setContentLength(stream.available());
//                OutputStream os = response.getOutputStream();
//                os.close();
//                response.flushBuffer();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public String saveZipfile(List<File> f, String zip_file_name) {
//        List<String> srcFiles = Arrays.asList("test1.txt", "test2.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(zip_file_name);

            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (File fileToZip : f) {
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                    System.out.println("writing");
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
        } catch (IOException ex) {
            System.out.println("excetion : " + ex.getMessage());
        }
        return "0";
    }
    @Autowired
    private ServletContext servletContext;

    // http://localhost:8080/download1
    // Using ResponseEntity<InputStreamResource>
    @PostMapping(path = "/downloadGenFile")
    public ResponseEntity<InputStreamResource> downloadFile1(@RequestBody GenFile f) throws IOException {
        String fileName = f.getCode();
        String DIRECTORY = f.getFich();
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);

        File file = new File(DIRECTORY + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }

    @PostMapping(path = "/downloadGenFile2")
    //zipe multiple files
    public ResponseEntity<InputStreamResource> downloadFile2(@RequestBody List<GenFile> tf) throws IOException {
        String yy = "";
        String file_name = "file.zip";
        List<File> o = new ArrayList<>();
        for (GenFile f : tf) {

            o.add(new File(f.getFich() + "/" + f.getCode()));
            yy = f.getFich() + "/" + file_name;
        }
        new File(yy).delete();
        saveZipfile(o, yy);
        File file = new File(yy);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, file_name);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        Long y = file.length();
        String t = file.getName();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + t)
                .contentType(mediaType)
                .contentLength(y) //
                .body(resource);
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

    @Transactional
    @PostMapping(path = "/updateSqlFile")
    public SqlFileType updateSqlFile(@RequestBody SqlFileType sqlFileType) {
        SqlFileType t = sqlFileTypeRepository.findById(sqlFileType.getId());
        if (t.setByColumn1(sqlFileType)) {
            t = sqlFileTypeRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/updateSqlFile1")
    public Integer updateSqlFile1(@RequestBody Map<String, String> r) {
        SqlFileType t = sqlFileTypeRepository.findById(new Long(r.get("id")));
        if (t != null) {
            Object[] params = {r.get("value")};
            int[] types = {Types.VARCHAR};
            return jdbcTemplate.update("update sqltype set " + r.get("key") + " = ? where id=" + r.get("id"), params, types);
        } else {
            return null;
        }
    }

    @PostMapping(path = "/newSqlFile")
    public Map newSqlFile(@RequestBody SqlFileType sqlFileType) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Date d = sqlFileType.getDar();
        d.setHours(0);
        sqlFileType.setDar(d);
        sqlFileType.setId(null);
        SqlFileType t = sqlFileTypeRepository.save(sqlFileType);
        ret.put("obj", t);
        ret.put("success", 1);
        ret.put("status", "01");
        ret.put("msg", "Sql successfully saved");
        return ret;
    }

    @PostMapping(path = "/saveFileType")
    public Map saveFileType(@RequestBody List<ReportRep> reportRep) {
        Map<String, Object> ret = new HashMap<String, Object>();
        reportRep.forEach(e -> {
            Date s = e.getDar();
            s.setHours(0);
            e.setDar(s);
        });
        Iterable<ReportRep> iterable = reportRep;
        Iterable<ReportRep> d = reportRepRepository.save(iterable);
        ret.put("obj", d);
        ret.put("success", 1);
        ret.put("status", "01");
        ret.put("msg", "Post successfully saved");
        return ret;
    }

    @PostMapping(path = "/updateComplexControle1")
    public ReportControleComplex updateComplexControle1(@RequestBody ReportControleComplex reportControleComplex) {
        ReportControleComplex t = reportControleComplexRepository.findById(reportControleComplex.getId());
        if (t.setByColumn1(reportControleComplex)) {
            System.out.println("element : " + t.getTypg());
            t = reportControleComplexRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/deleteComplexControl/{id}")
    @Transactional
    public Boolean deleteComplexControl(@PathVariable Long id) {
        reportControleComplexRepository.deleteById(id);
        return true;
    }

    @PostMapping(path = "/saveComplexControl")
    public ReportControleComplex saveComplexControl(@RequestBody ReportControleComplex reportControleComplex) {
        reportControleComplex.setCrdt((new Timestamp(System.currentTimeMillis()).toString()));
        reportControleComplex.setMdfi((new Timestamp(System.currentTimeMillis()).toString()));
        reportControleComplex.setDele("0");
        reportControleComplex.setId(reportControleComplexRepository.getMax().get(0).longValue() + 1L);
        System.out.println("tgstts sdf");
        System.out.println(reportControleComplex.getId());
        return reportControleComplexRepository.save(reportControleComplex);
    }

    @GetMapping(path = "/ComplexList")
    public List<ReportControleComplex> ComplexList() {
        return reportControleComplexRepository.getComplexControls("0");
    }

    @PostMapping(path = "/updateQualityControle1")
    public ReportControleQuality updateQualityControle1(@RequestBody ReportControleQuality reportControleQuality) {
        ReportControleQuality t = reportControleQualityRepository.findById(reportControleQuality.getId());
        if (t.setByColumn1(reportControleQuality)) {
            t = reportControleQualityRepository.save(t);
            return t;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/deleteQualityControl/{id}")
    @Transactional
    public Boolean deleteQualityControl(@PathVariable Long id) {
        reportControleQualityRepository.deleteById(id);
        return true;
    }

    @PostMapping(path = "/saveQualityControl")
    public ReportControleQuality saveQualityControl(@RequestBody ReportControleQuality reportControleQuality) {
        reportControleQuality.setDele("0");
        reportControleQuality.setId(reportControleQualityRepository.getMax().get(0).longValue() + 1L);
        System.out.println("tgstts sdf");
        System.out.println(reportControleQuality.getId());
        return reportControleQualityRepository.save(reportControleQuality);
    }

    @GetMapping(path = "/QualityList")
    public List<ReportControleQuality> QualityList() {
        return reportControleQualityRepository.getQualityControls("0");
    }

    // function to return attribute not having parent in formular and their formular
    // as well as column, post and file
    @PostMapping(path = "/getNotExistingAttribute")
    public List<Object> getNotExistingAttribute(@RequestBody ReportRep sr, Model m) {
        List<Object> s = reportCalculateService.getAttributeNotExist(m);
        System.out.println("toto enfine boler");
        return s;
    }
    @Autowired
    GenFileRepository genFileRepository;

    @PostMapping(path = "/getGenerated")
    public Iterable<GenFile> getGenerated(@RequestBody GenFile sr) {
        System.out.println(sr.getDar());
        return genFileRepository.getGeneratedFilter(sr.getDar(), sr.getFich());
    }
}

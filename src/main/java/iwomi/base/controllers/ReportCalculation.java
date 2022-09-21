/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.controllers;

//import static com.sun.javafx.css.SizeUnits.S;
import iwomi.base.ServiceInterface.ReportCalculateService;
import iwomi.base.ServiceInterface.ReportControleService;
import iwomi.base.objects.LiveOperationS;
import iwomi.base.objects.LiveTraitementv1S;
import iwomi.base.objects.ReportAnomaly;
import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportFile;
import iwomi.base.objects.ReportPost;
import iwomi.base.repositories.LiveOperationRepositoryS;
import iwomi.base.repositories.LiveTraitementRepositoryS;
import iwomi.base.repositories.LiveTraitementRepositoryv1S1;
import iwomi.base.repositories.ReportAnomalyRepository;
import iwomi.base.repositories.ReportCalculateRepository;
import iwomi.base.repositories.ReportFileRepository;
import iwomi.base.services.ReportCalculateServiceImpl;
import iwomi.base.services.ReportCalculateServiceImplS;
import java.math.BigDecimal;
import java.text.ParseException;

import static java.util.Arrays.sort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.annotations.Sort;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@Component
public class ReportCalculation {

    @Autowired
    private ReportCalculateRepository reportCalculateRepository;
    @Autowired
    private ReportFileRepository reportFileRepository;
    @Autowired
    private ReportAnomalyRepository ReportAnomalyRepository;
    @Autowired
    private ReportCalculateService reportCalculateService;
    @Autowired
    private ReportControleService reportControleService;
    @Autowired
    private ReportCalculateServiceImpl reportCalculateServiceImpl;

    @PostMapping(path = "/jpa/ReportGetCalculation1/")
    public void ReportGetCalculation1(@RequestBody String json) {
        reportCalculateService.CalculData(json);
    }

    @Autowired
    ReportCalculateServiceImplS reportCalculateServiceImplS;

    @PostMapping(path = "/jpa/testConditions/")
    public BigDecimal testConditions(@RequestBody HashMap<String, String> json) throws ParseException {
        return reportCalculateServiceImplS.separeteDataCond(json.get("formule"), json.get("etab"), json.get("fichier"), json.get("date"));

    }

    @PostMapping(path = "/jpa/ReportGetCalculation/")
    public Map<String, String> ReportGetCalculation(@RequestBody String json, Model m) {
        return reportCalculateService.CalculData1(json, m);
    }
    @PostMapping(path = "/testcolcuConvertion")
    public String testcolcuConvertion(@RequestBody HashMap<String, String> s) {
        return reportControleService.subquerytab( s.get("ty"),  s.get("date"),  s.get("f"), Integer.parseInt(s.get("colm")), s.get("cond"));
    }
    @PostMapping(path = "/testcolcuConvertion1")
    public BigDecimal testcolcuConvertion1(@RequestBody HashMap<String, String> s) {
        return reportControleService.getResultComplex3( s.get("arguments"),  s.get("date"),  s.get("cod"));
    }

    @PostMapping(path = "/jpa/ReportGetControle1/")
    public String ReportGetControle1(@RequestBody String json) {
        Map<String, Object> fich = ReportGetCalculate(json);
        List<String> controle_typ = (List<String>) fich.get("controle");
        Thread t = new Thread(new Runnable() {
            public void run() {
                reportControleService.Controle2(fich.get("date").toString(), fich.get("etab").toString(),
                        fich.get("cuser").toString(), controle_typ, fich.get("codeUnique").toString());
            }
        ;
        });
		t.start();
        return "{Status:01; Message: Success}";
    }

    
    @PostMapping(path = "/autoCorrection")
    public String autoCorrection(@RequestBody String json) {
        Map<String, Object> fich = ReportGetCalculate(json);
        Thread t = new Thread(new Runnable() {
            public void run() {
                reportControleService.autoCorrectInterControl(fich.get("date").toString(), fich.get("etab").toString());
            }
        ;
        });
		t.start();
        return "{Status:01; Message: Success}";
    }
    
    @PostMapping(path = "/jpa/ReportGetControle/")
    public Map<String, String> ReportGetControle(@RequestBody String json, Model m) {
        Map<String, Object> fich = ReportGetCalculate(json);
        List<String> controle_typ = (List<String>) fich.get("controle");
        Thread t = new Thread(new Runnable() {
            public void run() {
                reportControleService.Controle2(fich.get("date").toString(), fich.get("etab").toString(),
                        fich.get("cuser").toString(), controle_typ, fich.get("codeUnique").toString());
            }
        });
		t.start();
        return reportCalculateServiceImpl.getAveragtimeControle(json, m);
    }

    @PostMapping(path = "/jpa/ReportGetAnomaly/")
    public List<ReportAnomaly> ReportGetAnomaly(@RequestBody String json) {
        Map<String, Object> fich = ReportGetCalculate(json);
        return ReportAnomalyRepository.findByDar1(fich.get("date").toString());
    }
    @Autowired
    LiveOperationRepositoryS liveOperationRepositoryS;
    @Autowired
    LiveTraitementRepositoryv1S1 liveTraitementRepositoryS;

    @PostMapping(path = "/progresstraitement/")
    public Map<String, Object> progresstraitement(@RequestBody HashMap<String, String> p) {
//        System.out.println(p.get("code_unique"));
        LiveOperationS e = liveOperationRepositoryS.findLiveOperationByCodeUnique(p.get("code_unique"));
        List<LiveTraitementv1S> y = liveTraitementRepositoryS.findLiveTraitementByCodeUnique(p.get("code_unique"));
        Map<String, Object> d = new HashMap<String, Object>();
        d.put("global", e);
        d.put("elm", y);
        return d;
    }

    public Map<String, Object> ReportGetCalculate(String json) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> val = parser.parseMap(json);
        return val;
    }

}

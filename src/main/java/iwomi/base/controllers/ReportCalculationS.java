/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.controllers;

//import static com.sun.javafx.css.SizeUnits.S;
import iwomi.base.ServiceInterface.ReportCalculateServiceS;
import iwomi.base.ServiceInterface.ReportControleServiceS;
import iwomi.base.form.LiveReportForm;
import iwomi.base.form.ReportResultFom;
import iwomi.base.objects.Pwd;
import iwomi.base.objects.ReportAnomalyS;
import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportCalculateS2;
import iwomi.base.objects.ReportCalculeIdS;
import iwomi.base.objects.ReportFileS;
import iwomi.base.objects.ReportPostS;
import iwomi.base.repositories.PwdRepository;
import iwomi.base.repositories.ReportAnomalyRepositoryS;
import iwomi.base.repositories.ReportCalculateIdRepositoryS;
import iwomi.base.repositories.ReportCalculateRepositoryS;
import iwomi.base.repositories.ReportFileRepositoryS;
import iwomi.base.services.ReportCalculateServiceImplS;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static java.util.Arrays.sort;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.annotations.Sort;
import org.hibernate.criterion.Example;
import org.json.JSONException;
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
public class ReportCalculationS {

    List<Integer> e = new ArrayList<Integer>();
    @Autowired
    private ReportCalculateRepositoryS reportCalculateRepository;
    @Autowired
    private ReportFileRepositoryS reportFileRepository;
    @Autowired
    private ReportCalculateServiceImplS reportCalculateServiceImplS;
    @Autowired
    private ReportAnomalyRepositoryS ReportAnomalyRepository;
    
     @Autowired
    private PwdRepository pwdRepository;
     
    @Autowired
    private ReportCalculateServiceS reportCalculateService;
    @Autowired
    private ReportControleServiceS reportControleService;
    @Autowired
    private ReportCalculateServiceImplS reportCalculateServiceImpl;

    @PostMapping(path = "/jpa/ReportGetCalculation1S/")
    public void ReportGetCalculation1S(@RequestBody String json) {
        reportCalculateService.CalculData(json);
    }
//    ExecutorService service = Executors.newCachedThreadPool();
    ExecutorService service = Executors.newFixedThreadPool(10);

    public class testr implements Runnable {

        int i;

        public testr(int i) {
            this.i = i;
        }

        @Override
        public void run() {
//            if (!e.contains(i)) {
//                e.add(i);
//                System.out.println("the number of thread on going :" + e);
//            }
            for (int j = 0; j < 10; j++) {
                if (j == 0) {
                    System.out.println("start of thread :" + i);
                }
                if (j == 9) {
                    System.out.println("end of thread :" + i);
                }
                synchronized (this) {
//                    try {
//                        wait(1000L);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ReportCalculationS.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }

            }
        }
    }

    @PostMapping(path = "/testpr/")
    public void testpr(@RequestBody String json) {

        Thread t = new Thread(new Runnable() {
            public void run() {
                // we do the treatment
                do {
                    //method to call or code fragment
                    /**
                     * ********
                     */
                    try {

                        Map<String, String> type = new HashMap<>();
                        for (int i = 0; i < 50000; i++) {
                            System.out.println("new thread operation: " + i);
                            service.execute(new testr(i));
                        }
                        // shutdown
                        // this will get blocked until all task finish
                        service.shutdown();
                        try {
                            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            System.out.println("Yann service did not terminate");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("An error occured during the treatment " + e.getLocalizedMessage());
                    }
                    System.out.println("Setting the status");
                } while (!service.isShutdown());

            }
        ;
        });
        t.start();
    }

    @PostMapping(path = "/jpa/ReportGetCalculationS/")
    public Map<String, String> ReportGetCalculationS(@RequestBody String json, Model m) {
//                ReportCalculateServiceImplS1 r = new ReportCalculateServiceImplS1();
//		return r.CalculData1(json, m);
        return reportCalculateServiceImplS.CalculData1(json, m);
    }

    @PostMapping(path = "/jpa/ReportGetControle1S/")
    public String ReportGetControle1S(@RequestBody String json) {
        Map<String, Object> fich = ReportGetCalculateS(json);
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

    @PostMapping(path = "/jpa/ReportGetControleS/")
    public Map<String, String> ReportGetControleS(@RequestBody String json, Model m) {
        Map<String, Object> fich = ReportGetCalculateS(json);
        List<String> controle_typ = (List<String>) fich.get("controle");
        Thread t = new Thread(new Runnable() {
            public void run() {
                reportControleService.Controle2(fich.get("date").toString(), fich.get("etab").toString(),
                        fich.get("cuser").toString(), controle_typ, fich.get("codeUnique").toString());
            }
        ;
        });
		t.start();
        return reportCalculateServiceImpl.getAveragtimeControle(json, m);
    }

    @PostMapping(path = "/jpa/ReportGetAnomalyS/")
    public List<ReportAnomalyS> ReportGetAnomalyS(@RequestBody String json) {
        Map<String, Object> fich = ReportGetCalculateS(json);
        return ReportAnomalyRepository.findByDar1(fich.get("date").toString());
    }

    public Map<String, Object> ReportGetCalculateS(String json) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> val = parser.parseMap(json);
        return val;
    }
    @Autowired
    ReportCalculateIdRepositoryS reportCalculateIdRepositoryS;

    @PostMapping(path = "/CalculeSave")
    public Map<String, Object> CalculeSave(@RequestBody ReportCalculateS2 r) {
        if(r.getTysorce().equalsIgnoreCase("FINAL")){//disable
            r.setField("CH"+r.getPost()+"C"+r.getCol());
        }else{//disable col on interface
            r.setCol("-1");
        }
        Map<String, String> w = reportCalculateServiceImpl.controlCalculation(r);
        ReportCalculateS2 tr = reportCalculateIdRepositoryS.findOne(r.getIds());
        Map<String, Object> response = new HashMap<>();
        if (tr != null) {
            response.put("status", "0002");
            response.put("msg", "Cette Formule existe déjà");
            return response;
        }
        if (w.get("status") == "01") {
            r.setCrdt(new Date());
            r.setMdfi(new Date());
            r.setId(reportCalculateIdRepositoryS.getMax().get(0).longValue());
            ReportCalculateS2 t = reportCalculateIdRepositoryS.save(r);
            response.put("status", "01");
            response.put("data", t);
        } else {
            response.put("status", "100");
            response.put("msg", w.get("msg"));
        }
        return response;
    }

    @PostMapping(path = "/updateFormularS")
    public Map<String, Object> updateFormularS(@RequestBody ReportCalculateS2 r) {
        r.setMdfi(new Date());
        Map<String, Object> response = new HashMap<>();
        ReportCalculateS2 t = reportCalculateIdRepositoryS.findOne(r.getIds());
        t.takefield(r);

        System.out.println(" old value : " + t.getCalc());
        reportCalculateIdRepositoryS.save(t);
        response.put("status", "01");
        response.put("data", t);
        response.put("msg", "Mise à jour réussie");
        return response;
    }

    @PostMapping(path = "/CalculeAll")
    public Iterable<ReportCalculateS2> CalculeAll(@RequestBody ReportCalculateS2 r) {
        return reportCalculateIdRepositoryS.findAll();

    }

    @PostMapping(path = "/DeleteCalcule")
    public Map<String, String> DeleteCalcule(@RequestBody ReportCalculeIdS r) {
        Map<String, String> response = new HashMap<>();
        reportCalculateIdRepositoryS.delete(r);
        if (reportCalculateIdRepositoryS.findOne(r) == null) {
            response.put("status", "01");
        } else {
            response.put("status", "100");
        }
        return response;
    }
    
    @PostMapping(path = "/testSpectra")
    public Map<String, Object> testSpectra(@RequestBody HashMap<String, String> p) {
       System.out.println(" output of test spectra:" + p);
        Map<String, Object> d = new HashMap<String, Object>();
       Pwd pw= pwdRepository.findGfcliByAscd("0043", "0");
        d.put("nom", p.get("nom"));
        d.put("prenom",p.get("prenom"));
       d.put("data",pw);
        return d;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.controllers;

import iwomi.base.ServiceInterface.ChargerDonneesService;
import iwomi.base.ServiceInterface.ChargerDonneesServiceLocal;
import iwomi.base.ServiceInterface.GenererFichierServices;
import iwomi.base.ServiceInterface.LiveReportingService;
import iwomi.base.form.ClientToSystemForm;
import iwomi.base.form.CodeForm;
import iwomi.base.form.DataIntegrationForm;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import iwomi.base.form.GenererFichierForm;
import iwomi.base.form.LiveReportForm;
import iwomi.base.form.ReportResultFom;
import iwomi.base.objects.InAutorisationDecouvert;
import iwomi.base.objects.InBalance;
import iwomi.base.objects.InCautions;
import iwomi.base.objects.InClients;
import iwomi.base.objects.InComptes;
import iwomi.base.objects.InGaranties;
import iwomi.base.objects.InOib;
import iwomi.base.objects.InRapartriements;
import iwomi.base.objects.InSoldes;
import iwomi.base.objects.Inbdc;
import iwomi.base.objects.Incredit;
import iwomi.base.objects.Indat;
import iwomi.base.objects.Inech;
import iwomi.base.objects.Intitre;
import iwomi.base.objects.Intran;
import iwomi.base.objects.LiveTraitement;
import iwomi.base.repositories.InAutorisationDecouvertRepository;
import iwomi.base.repositories.InBalanceRepository;
import iwomi.base.repositories.InCautionsRepository;
import iwomi.base.repositories.InClientsRepository;
import iwomi.base.repositories.InComptesRepository;
import iwomi.base.repositories.InGarantiesRepository;
import iwomi.base.repositories.InOibRepository;
import iwomi.base.repositories.InSoldesRepository;
import iwomi.base.repositories.InbdcRepository;
import iwomi.base.repositories.IncreditRepository;
import iwomi.base.repositories.IndatRepository;
import iwomi.base.repositories.InechRepository;
import iwomi.base.repositories.InrapatRepository;
import iwomi.base.repositories.IntitreRepository;
import iwomi.base.repositories.IntransRepository;
import iwomi.base.services.ChargerDonneesServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author fabri
 */
@RestController
@Component

public class GenererFichierControllers {

    @Autowired
    private GenererFichierServices genererFichierServices;
    @Autowired
    private ChargerDonneesService chargerDonneesService;
    @Autowired
    private LiveReportingService liveReportingService;

    @Autowired
    private InBalanceRepository inBalanceRepository;
    @Autowired
    private InAutorisationDecouvertRepository inAutorisationDecouvertRepository;
    @Autowired
    private InbdcRepository inbdcRepository;
    @Autowired
    private InCautionsRepository inCautionsRepository;

    @Autowired
    private InClientsRepository inClientsRepository;
    @Autowired
    private InComptesRepository inComptesRepository;
    @Autowired
    private IncreditRepository increditRepository;
    @Autowired
    private IndatRepository indatRepository;
    @Autowired
    private InechRepository inechRepository;
    @Autowired
    private InGarantiesRepository inGarantiesRepository;
    @Autowired
    private InSoldesRepository inSoldesRepository;
    @Autowired
    private InrapatRepository inrapatRepository;
    @Autowired
    private InOibRepository inOibRepository;
    @Autowired
    private IntitreRepository intitreRepository;
    @Autowired
    private IntransRepository intransRepository;

    @PostMapping(path = "/generateFiles")
    public String genererFichier(@RequestBody GenererFichierForm fic) {

        Thread t = new Thread(new Runnable() {

            public void run() {
                genererFichierServices.genererFichiersP(fic);
            }
        ;
        });
         t.start();
        return "1";
    }

    @PostMapping(path = "/prepareFiles")
    public String prepareFilev1(@RequestBody ClientToSystemForm fic) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                chargerDonneesService.extrairesFromClientDatabase(fic);
            }
        });
         t.start();
        return "1";

    }

    @PostMapping(path = "/loadDataIntoSystem")
    public String loadIntoSystem(@RequestBody DataIntegrationForm fic) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                chargerDonneesService.writeInDataBaseSystem(fic);
            }
        ;
        });
         t.start();
        return "1";

    }

    @PostMapping(path = "/liveReport")
    public ReportResultFom giveLiveReport(@RequestBody LiveReportForm liveReport) {
        return liveReportingService.returnLivereport(liveReport);

    }

    @PostMapping(path = "/liveReport1")
    public List<ReportResultFom> giveLiveReport1(@RequestBody LiveReportForm liveReport) {
        return liveReportingService.returnLivereport2(liveReport);

    }

       //liste inventaire: balance findInbdcByDar
    @GetMapping("/getBAL/{dar}")
    public List<InBalance> inBalance(@PathVariable String dar) {
         String[] r = dar.split("-");
        String re =r[2] + "/" + r[1] + "/" + r[0] ;
        System.out.println(re);
        return (List<InBalance>) inBalanceRepository.findInbalanceByDar(re);
    }

    //liste inventaire:  InAutorisationDecouvert
    @GetMapping("/getAUTO/{dar}")
    public List<InAutorisationDecouvert> inAutorisationd(@PathVariable String dar) {
        System.out.println(dar);
        return (List<InAutorisationDecouvert>) inAutorisationDecouvertRepository.findInAutorisationDecouvertByDar(dar);
    }

    //liste inventaire:  findInbdcByDar 
    @GetMapping("/getIBDC/{dar}")
    public List<Inbdc> inbdc(@PathVariable String dar) {
        return (List<Inbdc>) inbdcRepository.findInbdcByDar(dar);
    }

    //liste inventaire: findInbdcByDar InCautions
    @GetMapping("/getCAU/{dar}")
    public List<InCautions> inCautions(@PathVariable String dar) {
        return (List<InCautions>) inCautionsRepository.findInCautionsByDar(dar);
    }

    //liste inventaire: inClients
    @GetMapping("/getCLI/{dar}")
    public List<InClients> inClients(@PathVariable String dar) {
        return (List<InClients>) inClientsRepository.findInClientsByDar(dar);
    }

    //liste inventaire: inComptes
    @GetMapping("/getCOM/{dar}")
    public List<InComptes> inComptes(@PathVariable String dar) {
        return (List<InComptes>) inComptesRepository.findInComptesByDar(dar);
    }

    //liste inventaire: incredit
    @GetMapping("/getCRED/{dar}")
    public List<Incredit> incredit(@PathVariable String dar) {
        return (List<Incredit>) increditRepository.findIncreditByDar(dar);
    }

    //liste inventaire: indat
    @GetMapping("/getDAT/{dar}")
    public List<Indat> indat(@PathVariable String dar) {
        return (List<Indat>) indatRepository.findIndatByDar(dar);
    }

    //liste inventaire: inech
    @GetMapping("/getECH/{dar}")
    public List<Inech> inech(@PathVariable String dar) {
        return (List<Inech>) inechRepository.findInechByDar(dar);
    }

    //liste inventaire: inGaranties
    @GetMapping("/getGAR/{dar}")
    public List<InGaranties> inGaranties(@PathVariable String dar) {
        String[] r = dar.split("-");
        String re =r[2] + "/" + r[1] + "/" + r[0] ;
        System.out.println(re);
        return (List<InGaranties>) inGarantiesRepository.findInGarantiesByDar(re);
    }

    //liste inventaire: inSoldes
    @GetMapping("/getSOLDE/{dar}")
    public List<InSoldes> inSoldes(@PathVariable String dar) {
         String[] r = dar.split("-");
        String re =r[2] + "/" + r[1] + "/" + r[0] ;
        System.out.println(re);
        return (List<InSoldes>) inSoldesRepository.findInSoldesByDar(re);
    }

    //liste inventaire: Rapartriements
    @GetMapping("/getRAPAT/{dar}")
    public List<InRapartriements> inRapartriements(@PathVariable String dar) {
        return (List<InRapartriements>) inrapatRepository.findInRapartriementsByDar(dar);
    }

    //liste inventaire: InOib
    @GetMapping("/getOPIB/{dar}")
    public List<InOib> inOib(@PathVariable String dar) {

        return (List<InOib>) inOibRepository.findInOibByDar(dar);
    }

    //liste inventaire: Intitre
    @GetMapping("/getTITRE/{dar}")
    public List<Intitre> intitre(@PathVariable String dar) {
        return (List<Intitre>) intitreRepository.findIntitreByDar(dar);
    }

    //liste inventaire: Intitre
    @GetMapping("/getTRANS/{dar}")
    public List<Intran> intran(@PathVariable String dar) {
        return (List<Intran>) intransRepository.findIntranByDar(dar);
    }

}

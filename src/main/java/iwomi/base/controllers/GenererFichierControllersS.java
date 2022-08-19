/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.controllers;    

import iwomi.base.ServiceInterface.ChargerDonneesServiceS;
import iwomi.base.ServiceInterface.ChargerDonneesServiceLocalS;
import iwomi.base.ServiceInterface.GenererFichierServicesS;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.form.ClientToSystemForm;
import iwomi.base.form.CodeForm;
import iwomi.base.form.DataIntegrationForm;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import iwomi.base.form.GenererFichierFormS;
import iwomi.base.form.LiveReportFormS;
import iwomi.base.form.ReportResultFom;
import iwomi.base.form.ReportResultFomS;
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
//import iwomi.base.services.ChargerDonneesServiceImplS;
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
//@RequestMapping("/genererFile")
@Component

public class GenererFichierControllersS {
    @Autowired
    private  GenererFichierServicesS genererFichierServices;    
    @Autowired
    private  ChargerDonneesServiceS chargerDonneesService;
    
    @Autowired
    private  ChargerDonneesServiceLocalS chargerDonneesServiceLocal;
    
    @Autowired
    private  LiveReportingServiceS liveReportingService;
    
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
    
  
        
    @PostMapping(path = "/generateFilesS")
    public  String  genererFichierS(@RequestBody GenererFichierFormS fic)
    {
        
      
       Thread t = new Thread(new Runnable() {
            
             public void run() { 
               genererFichierServices.genererFichiersP(fic);
             };
          });
         t.start();
         return "1";
    }
    @PostMapping(path = "/prepareFilesS")
    public  String prepareFileS(@RequestBody ClientToSystemForm fic)
    {
          Thread t = new Thread(new Runnable() {
            
             public void run() { 
//                 try {
//                     Thread.sleep(10000);
//                 } catch (InterruptedException ex) {
//                     Logger.getLogger(GenererFichierControllers.class.getName()).log(Level.SEVERE, null, ex);
//                 }
               //chargerDonneesServiceLocal.extrairesFromClientDatabase(fic);
               chargerDonneesService.extrairesFromClientDatabase(fic);
             };
          });
         t.start();
         return "1";
      
    }
    
    @PostMapping(path = "/loadDataIntoSystemS")
    public  String loadIntoSystemS(@RequestBody  DataIntegrationForm fic)
    {
        
          Thread t = new Thread(new Runnable() {
            
             public void run() { 
               chargerDonneesService.writeInDataBaseSystem(fic);
             };
          });
         t.start();
         return "1";

    }
    
    @PostMapping(path = "/liveReportS")
    public  ReportResultFomS giveLiveReportS(@RequestBody  LiveReportFormS liveReport)            
    {
    	return  liveReportingService.returnLivereport(liveReport);
    	
    }
    
    @PostMapping(path = "/liveReport1S")
        public  List<ReportResultFomS> giveLiveReport1S(@RequestBody  LiveReportFormS liveReport)            
        {
            return  liveReportingService.returnLivereport2(liveReport);

        }
         
       //liste inventaire: balance findInbdcByDar
    
  	@GetMapping("/getBALS/{dar}")
  	public List<InBalance> inBalanceS(@PathVariable String dar) {
  		return (List<InBalance>) inBalanceRepository.findInbalanceByDar(dar);
  	}
  	
  	 //liste inventaire:  InAutorisationDecouvert
  	@GetMapping("/getAUTOS/{dar}")
  	public List<InAutorisationDecouvert> inAutorisationdS(@PathVariable String dar) {
  		return (List<InAutorisationDecouvert>) inAutorisationDecouvertRepository.findInAutorisationDecouvertByDar(dar);
  	}
   
  	 //liste inventaire:  findInbdcByDar 
  	@GetMapping("/getIBDCS/{dar}")
  	public List<Inbdc> inbdcS(@PathVariable String dar) {
  		return (List<Inbdc>) inbdcRepository.findInbdcByDar(dar);
  	}
  	
  	 //liste inventaire: findInbdcByDar InCautions
  	@GetMapping("/getCAUS/{dar}")
  	public List<InCautions> inCautionsS(@PathVariable String dar) {
  		return (List<InCautions>) inCautionsRepository.findInCautionsByDar(dar);
  	}
  	
  	 //liste inventaire: inClients
  	@GetMapping("/getCLIS/{dar}")
  	public List<InClients> inClientsS(@PathVariable String dar) {
  		return (List<InClients>) inClientsRepository.findInClientsByDar(dar);
  	}
  	
  	 //liste inventaire: inComptes
  	@GetMapping("/getCOMS/{dar}")
  	public List<InComptes> inComptesS(@PathVariable String dar) {
  		return (List<InComptes>) inComptesRepository.findInComptesByDar(dar);
  	}
  	
  	 //liste inventaire: incredit
  	@GetMapping("/getCREDS/{dar}")
  	public List<Incredit> increditS(@PathVariable String dar) {
  		return (List<Incredit>) increditRepository.findIncreditByDar(dar);
  	}
  	
  	 //liste inventaire: indat
  	@GetMapping("/getDATS/{dar}")
  	public List<Indat> indatS(@PathVariable String dar) {
  		return (List<Indat>) indatRepository.findIndatByDar(dar);
  	}
  	
  	 //liste inventaire: inech
  	@GetMapping("/getECHS/{dar}")
  	public List<Inech> inechS(@PathVariable String dar) {
  		return (List<Inech>) inechRepository.findInechByDar(dar);
  	}
  	
  	 //liste inventaire: inGaranties
  	@GetMapping("/getGARS/{dar}")
  	public List<InGaranties> inGarantiesS(@PathVariable String dar) {
  		return (List<InGaranties>) inGarantiesRepository.findInGarantiesByDar(dar);
  	}
  	
  	 //liste inventaire: inSoldes
  	@GetMapping("/getSOLDES/{dar}")
  	public List<InSoldes> inSoldesS(@PathVariable String dar) {
  		return (List<InSoldes>) inSoldesRepository.findInSoldesByDar(dar);
  	}
  	
  //liste inventaire: Rapartriements
  	@GetMapping("/getRAPATS/{dar}")
  	public List<InRapartriements> inRapartriementsS(@PathVariable String dar) {
  		return (List<InRapartriements>) inrapatRepository.findInRapartriementsByDar(dar);
  	}
  	
  //liste inventaire: InOib
  	@GetMapping("/getOPIBS/{dar}")
  	public List<InOib> inOibS(@PathVariable String dar) {
 
  		return (List<InOib>) inOibRepository.findInOibByDar(dar);
  	}
  	
  //liste inventaire: Intitre
  	@GetMapping("/getTITRES/{dar}")
  	public List<Intitre> intitreS(@PathVariable String dar) {
  		return (List<Intitre>) intitreRepository.findIntitreByDar(dar);
  	}
 	
   //liste inventaire: Intitre
  	@GetMapping("/getTRANSS/{dar}")
  	public List<Intran> intranS(@PathVariable String dar) {
  		return (List<Intran>) intransRepository.findIntranByDar(dar);
  	}
        
        //liste inventaire: Intitre
//  	@GetMapping("/getAverageDate2")
//  	public List<LiveTraitement> getAverageDate2(@RequestBody CodeForm fic) {
//            
//               System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//               System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//               
//               return liveReportingService.getaverageDuration2(fic.getCode());
//               
//  	}
//        
//            @PostMapping(path = "/getAverageDate")
//        public  List<LiveTraitement> getAverageDate (@RequestBody CodeForm fic){
//               System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//               return  liveReportingService.getaverageDuration(fic.getCode());         
//        }

      
}

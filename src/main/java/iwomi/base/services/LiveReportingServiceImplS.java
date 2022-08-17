/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import iwomi.base.ServiceInterface.ChargerDonneesServiceLocalS;
import iwomi.base.ServiceInterface.LiveReportingServiceS;
import iwomi.base.form.LiveReportFormS;
import iwomi.base.form.ReportResultFomS;
import iwomi.base.objects.LiveOperationS;
import iwomi.base.objects.LiveTraitementS;
import iwomi.base.objects.LiveTraitementv1S;
import iwomi.base.repositories.LiveOperationRepositoryS;
import iwomi.base.repositories.LiveTraitementRepositoryS;
import iwomi.base.repositories.LiveTraitementRepositoryv1S1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fabri
 */
@Service
@Component
public class LiveReportingServiceImplS implements LiveReportingServiceS {

    static int countp = 0;
    Map<String, Integer> progress = new HashMap<String, Integer>();
    @Autowired
    LiveOperationRepositoryS liveOperationRepository;
    @Autowired
    LiveTraitementRepositoryS liveTraitementRepository;
    @Autowired
    LiveTraitementRepositoryv1S1 liveTraitementRepositorv1y;
    @Autowired
    ChargerDonneesServiceLocalS chargerDonneesServiceLocal;
    Map<String, Long> minimp = new HashMap<String, Long>();
    Map<String, String> parameters = new HashMap<>();
    Connection connection = null;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

//	public ReportResultFom returnLivereport(LiveReportForm liveReport) {
//
//		System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
//		List<LiveTraitement> trait = liveTraitementRepository
//				.findLiveTraitementByCodeUniqueAndCodefichier(liveReport.getCodeUnique(), liveReport.getCodeFichier());
//		System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
//		System.out.println(
//				"----------------------------------------------------------------------------------------------------- ");
//		System.out.println(trait);
//		LiveOperation op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
//		String filevalue = "";
//		String opvalue = "";
//		System.out.println("AFTER OPERATION LiveReportingServiceImpl returnLivereport 47 ");
//		if (trait.get(0).getNbtotal().equalsIgnoreCase("0")) {
//			filevalue = "EMPTY";
//		} else {
//			filevalue = String.valueOf(
//					(Long.valueOf(trait.get(0).getNbtraite()) * 100) / Long.valueOf(trait.get(0).getNbtotal()));
//		}
//		if (op.getNbtotal() == 0) {
//			opvalue = "EMPTY";
//		} else {
//			opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());
//		}
//		System.out.println("AFTER filevalue " + filevalue);
//		System.out.println("AFTER getNbtraite " + trait.get(0).getNbtraite());
//		System.out.println("AFTER getNbtotal " + trait.get(0).getNbtotal());
//		System.out.println("AFTER opvalue " + opvalue);
//		System.out.println(" Before returning response LiveReportingServiceImpl returnLivereport 56");
//
//		return new ReportResultFom("1", liveReport.getCodeFichier(), String.valueOf(trait.get(0).getStatut()),
//				filevalue, String.valueOf(op.getStatut()), opvalue);
//
//	}
    public String getDiffDate(Date d1) {
        String valtime = "";
        Date d2 = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            valtime = diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
                    + " seconds.";
            valtime = diffHours + " Hrs, " + diffMinutes + " Min, " + diffSeconds + " Sec";
            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valtime;

    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public ReportResultFomS returnLivereport1(LiveReportFormS liveReport) {
        int s = 0;
        String timeSpend = "";
        System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
        List<LiveTraitementS> trait = liveTraitementRepository
                .findLiveTraitementByCodeUnique(liveReport.getCodeUnique());
        System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
        LiveOperationS op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
        String filevalue = "";
        String opvalue = "";
        System.out.println("AFTER OPERATION LiveReportingServiceImpl returnLivereport 47 ");
        s = trait.size() - 1;
        System.out.println("la position" + s);
        Long sdd = 0L;
        double gtr = op.getNbtraite();
        double gt = op.getNbtotal();
        double tr = new Long(trait.get(s).getNbtraite());
        double t = new Long(trait.get(s).getNbtotal());
        double progress = (100 / gt) * (gtr + (tr / t));
        progress = (progress > 100) ? 100.0 : progress;
        progress = round(progress, 0);
        if (trait.get(trait.size() - 1).getNbtotal().equalsIgnoreCase("0")) {
            filevalue = "EMPTY";
        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            String remain = String.valueOf(
                    (Long.valueOf(trait.get(s).getNbtraite()) * 100) / Long.valueOf(trait.get(s).getNbtotal()));
//			sdd = Long.valueOf(trait.get(s).getNbtraite()+0/Long.valueOf(trait.get(s).getNbtotal()));
            sdd = Long.valueOf(trait.get(s).getNbtraite()) / Long.valueOf(trait.get(s).getNbtotal());

            filevalue = String.valueOf(
                    (Long.valueOf(trait.get(s).getNbtraite()) * 100) / Long.valueOf(trait.get(s).getNbtotal()));
        }

        if (op.getNbtotal() == 0) {

            opvalue = "EMPTY";
        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
//			opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());

            opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());
            opvalue = String.valueOf(((op.getNbtraite()) / op.getNbtotal()) + ((1 / op.getNbtotal()) * sdd) * 100);// rest
            opvalue = progress + "";
            timeSpend = getDiffDate(trait.get(0).getDateDebut());
        }
        System.out.println("AFTER filevalue " + filevalue);// percentage done
        System.out.println("AFTER getNbtraite " + trait.get(0).getNbtraite());
        System.out.println("AFTER getNbtotal " + trait.get(0).getNbtotal());
        System.out.println("AFTER opvalue " + gt);
        System.out.println("AFTER opvalue " + gtr);
        System.out.println("AFTER opvalue " + tr);
        System.out.println("AFTER opvalue " + t);
        System.out.println("AFTER opvalue " + progress);
        System.out.println("AFTER opvalue " + opvalue);
        System.out.println(" Before returning response LiveReportingServiceImpl returnLivereport 56");
        return new ReportResultFomS("1", trait.get(s).getCodefichier(),
                String.valueOf(trait.get(trait.size() - 1).getStatut()), filevalue, String.valueOf(op.getStatut()),
                opvalue, timeSpend);

    }

    public List<ReportResultFomS> returnLivereport2(LiveReportFormS liveReport) {
        int s = 0;
        String timeSpend = "";
        System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
        List<LiveTraitementS> trait = liveTraitementRepository
                .findLiveTraitementByCodeUnique(liveReport.getCodeUnique());
        System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
        LiveOperationS op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
        String filevalue = "";
        String opvalue = "";
        System.out.println("AFTER OPERATION LiveReportingServiceImpl returnLivereport 47 ");
        s = trait.size() - 1;
        System.out.println("la position" + s);
        Long sdd = 0L;
        double gtr = op.getNbtraite();
        double gt = op.getNbtotal();
        double tr = new Long(trait.get(s).getNbtraite());
        double t = new Long(trait.get(s).getNbtotal());
        double progress = (100 / gt) * (gtr + (tr / t));
        progress = (progress > 100) ? 100.0 : progress;
        progress = round(progress, 0);
        if (trait.get(trait.size() - 1).getNbtotal().equalsIgnoreCase("0")) {
            filevalue = "EMPTY";
        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            String remain = String.valueOf(
                    (Long.valueOf(trait.get(s).getNbtraite()) * 100) / Long.valueOf(trait.get(s).getNbtotal()));
//			sdd = Long.valueOf(trait.get(s).getNbtraite()+0/Long.valueOf(trait.get(s).getNbtotal()));
            sdd = Long.valueOf(trait.get(s).getNbtraite()) / Long.valueOf(trait.get(s).getNbtotal());

            filevalue = String.valueOf(
                    (Long.valueOf(trait.get(s).getNbtraite()) * 100) / Long.valueOf(trait.get(s).getNbtotal()));
        }

        if (op.getNbtotal() == 0) {

            opvalue = "EMPTY";
        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
//			opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());

            opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());
            opvalue = String.valueOf(((op.getNbtraite()) / op.getNbtotal()) + ((1 / op.getNbtotal()) * sdd) * 100);// rest
            opvalue = progress + "";
            timeSpend = getDiffDate(trait.get(0).getDateDebut());
        }
        System.out.println("AFTER filevalue " + filevalue);// percentage done
        System.out.println("AFTER getNbtraite " + trait.get(0).getNbtraite());
        System.out.println("AFTER getNbtotal " + trait.get(0).getNbtotal());
        System.out.println("AFTER opvalue " + gt);
        System.out.println("AFTER opvalue " + gtr);
        System.out.println("AFTER opvalue " + tr);
        System.out.println("AFTER opvalue " + t);
        System.out.println("AFTER opvalue " + progress);
        System.out.println("AFTER opvalue " + opvalue);
        System.out.println(" Before returning response LiveReportingServiceImpl returnLivereport 56");
        List<ReportResultFomS> p = new ArrayList<ReportResultFomS>();
        String opvalue1 = opvalue;
        trait.forEach(e -> {
            p.add(new ReportResultFomS("1", e.getCodefichier(), String.valueOf(e.getStatut()), String.valueOf(
                    (Long.valueOf(e.getNbtraite()) * 100) / Long.valueOf(e.getNbtotal())),
                    String.valueOf(op.getStatut()), opvalue1, getDiffDate(e.getDateDebut())));
        });
        return p;

    }

    public ReportResultFomS returnLivereport(LiveReportFormS liveReport) {

        String timeSpend = "";
        System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
        List<LiveTraitementS> trait = liveTraitementRepository
                .findLiveTraitementByCodeUniqueAndCodefichier(liveReport.getCodeUnique(), liveReport.getCodeFichier());
        System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
        LiveOperationS op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
        String filevalue = "";
        String opvalue = "";
        System.out.println("AFTER OPERATION LiveReportingServiceImpl returnLivereport 47 ");

        if (trait.get(0).getNbtotal().equalsIgnoreCase("0")) {

            filevalue = "EMPTY";

        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            filevalue = String.valueOf(
                    (Long.valueOf(trait.get(0).getNbtraite()) * 100) / Long.valueOf(trait.get(0).getNbtotal()));
        }

        if (op.getNbtotal() == 0) {

            opvalue = "EMPTY";

        } else {
            // String filevalue =
            // String.valueOf((trait.getNbtraite()*100)/trait.getNbtotal());
            opvalue = String.valueOf((op.getNbtraite() * 100) / op.getNbtotal());
            timeSpend = getDiffDate(trait.get(0).getDateDebut());
        }
        System.out.println("AFTER filevalue " + filevalue);// percentage done
        System.out.println("AFTER getNbtraite " + trait.get(0).getNbtraite());
        System.out.println("AFTER getNbtotal " + trait.get(0).getNbtotal());
        System.out.println("AFTER opvalue " + opvalue);
        System.out.println(" Before returning response LiveReportingServiceImpl returnLivereport 56");
        return new ReportResultFomS("1", liveReport.getCodeFichier(), String.valueOf(trait.get(0).getStatut()),
                filevalue, String.valueOf(op.getStatut()), opvalue, timeSpend);

    }

    @Override
    public void detailsReportingToTheVue2(String idTrait, String codeFichier, Long minimum) {
        if (!progress.containsKey(codeFichier)) {
            System.out.println("the " + codeFichier + " progress recieve first data");
            progress.put(codeFichier, 1);
        } else {
            progress.put(codeFichier, progress.get(codeFichier) + 1);
        }

        if (progress.get(codeFichier) % minimum == 0) {
            System.out.println("the " + codeFichier + " progress is :" + progress.get(codeFichier));
            liveTraitementRepository.updatelivetreatm(idTrait, codeFichier, minimum);
        }
    }

    @Transactional
    @Override
    public void detailsReportingToTheVue3(String idTrait, String codeFichier, Long minimum) {
        liveTraitementRepository.updatelivetreatm(idTrait, codeFichier, minimum);
    }

    public void detailsReportingToTheVue1(String idTrait, String codeFichier, Long minimum) {

        // la valeur de quotien au premier appel de la fonction doit etre de 1.
        // Minimum represente le nomnbre d element minimum a traité avant d update le
        // statut en, bd.
        // nbreTotal le total de depart qu on doit traiter
        // Nbretraité le nombre deja traite sur le total de depart
        // idTrait id tu traitement en base de données.
        countp++;// increase
        System.out.println(minimum);
        if (countp >= minimum) {
            System.out.println(" Enter minimum in quotien in detailsTraitment 79 ");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            List<LiveTraitementS> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTrait,
                    codeFichier);
            if (Long.valueOf(trait.get(0).getNbtraite()) + 100L < Long.valueOf(trait.get(0).getNbtotal())) {
                Long pp = Long.valueOf(trait.get(0).getNbtraite()) + countp;
                System.out.println("treated element " + pp);
                trait.get(0).setNbtraite(pp.toString());
                trait.get(0).setDateFin(date);
                System.out.println("size of current processing is of size :" + trait.size());
                liveTraitementRepository.save(trait.get(0));

            }
            System.out.println(" number already treated :" + countp);
            countp = 0;
        }

//		return quotien;
    }

    @Override
    public Long detailsReportingToTheVue(String idTrait, String codeFichier, Long nbreTotal, Long Nbretraite,
            Long quotien, Long minimum) {

        // la valeur de quotien au premier appel de la fonction doit etre de 1.
        // Minimum represente le nomnbre d element minimum a traité avant d update le
        // statut en, bd.
        // nbreTotal le total de depart qu on doit traiter
        // Nbretraité le nombre deja traite sur le total de depart
        // idTrait id tu traitement en base de données.
        System.out.println(" LiveReportingServiceImpl detailsTraitment 76 ");
        if (nbreTotal > minimum) {
            System.out.println(" Enter minimum in detailsTraitment 79 ");

            if (Nbretraite > minimum * quotien) {

                System.out.println(" Enter minimum in quotien in detailsTraitment 79 ");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                nbreTotal = nbreTotal + 100;
                List<LiveTraitementS> trait = liveTraitementRepository
                        .findLiveTraitementByCodeUniqueAndCodefichier(idTrait, codeFichier);
                trait.get(0).setNbtraite(String.valueOf(Nbretraite));
                trait.get(0).setDateFin(date);
                trait.get(0).setNbtotal(nbreTotal.toString());
                trait.get(0).setDateFin(date);
                System.out.println("size of current processing is of size :" + trait.size());
                System.out.println(" BEfore save traitement in detailsTraitment 91 ");
                liveTraitementRepository.save(trait.get(0));

                System.out.println(" number quotien :" + quotien);
                System.out.println(" number already treated :" + Nbretraite);
                System.out.println(" After save traitement in detailsTraitment 91 ");
                quotien = quotien + 1;
                return quotien;
            }

        }

        return quotien;

    }

    @Override
    public String beginDetailsReportingToTheVue1(Long idope, String codeFichier, Long nbreTotal) {

        System.out.println("BEGIN_ LiveReportingServiceImpl for file :" + codeFichier);
        DateFormat dateFormat = new SimpleDateFormat("y-m-d");
        Date date = new Date();
        LiveOperationS op = liveOperationRepository.findOne(idope);
        LiveTraitementS liveTraitement = null;
        liveTraitement = new LiveTraitementS(op.getCodeUnique() + randomAlphaNumeric(10), op.getCodeUnique(),
                codeFichier, String.valueOf(nbreTotal), String.valueOf(0), String.valueOf(3), "", date, date);
        LiveTraitementS trait2 = liveTraitementRepository.save(liveTraitement);
        List<LiveTraitementS> trait = liveTraitementRepository
                .findLiveTraitementByCodeUniqueAndCodefichier(op.getCodeUnique(), codeFichier);
        if (trait.size() > 1) {
            for (int i = 0; i < trait.size() - 1; i++) {
                liveTraitementRepository.delete(trait.get(i));
            }
        }

        return liveTraitement.getCodeUnique();

    }

    @Override
    public LiveTraitementv1S beginDetailsReportingToTheVue2(Long idope, String codeFichier, Long nbreTotal) {
        System.out.println("Beging individual progress of :" + codeFichier);
        Date date = new Date();
        LiveOperationS op = liveOperationRepository.findOne(idope);
        LiveTraitementv1S liveTraitement = new LiveTraitementv1S(op.getCodeUnique() + randomAlphaNumeric(10), op.getCodeUnique(), codeFichier, nbreTotal, 0L, "3", "", date);
        LiveTraitementv1S trait2 = liveTraitementRepositorv1y.save(liveTraitement);
        return trait2;

    }

    @Override
    public String beginDetailsReportingToTheVue(Long idope, String codeFichier, Long nbreTotal) {
        System.out.println("BEGIN_ LiveReportingServiceImpl detailsTraitment");
        DateFormat dateFormat = new SimpleDateFormat("y-m-d");
        Date date = new Date();
        LiveOperationS op = liveOperationRepository.findOne(idope);
        LiveTraitementS liveTraitement = null;
        liveTraitement = new LiveTraitementS(op.getCodeUnique() + randomAlphaNumeric(10), op.getCodeUnique(),
                codeFichier, String.valueOf(nbreTotal), String.valueOf(0), String.valueOf(3), "", date, date);
        LiveTraitementS trait2 = liveTraitementRepository.save(liveTraitement);
        List<LiveTraitementS> trait = liveTraitementRepository
                .findLiveTraitementByCodeUniqueAndCodefichier(op.getCodeUnique(), codeFichier);
        if (trait.size() > 1) {

            for (int i = 0; i < trait.size() - 1; i++) {

                liveTraitementRepository.delete(trait.get(i));

            }
        }
        return liveTraitement.getCodeUnique();

    }

    /*
	 * @Override public Long beginDetailsReportingToTheVue2(Long idope, String
	 * codeFichier, Long nbreTotal){
	 * 
	 * System.out.println("BEGIN_ LiveReportingServiceImpl detailsTraitment");
	 * DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date
	 * date = new Date(); LiveOperation op = liveOperationRepository.findOne(idope);
	 * LiveTraitement liveTraitement=null; liveTraitement = new
	 * LiveTraitement(op.getCodeUnique(),op.getId(),codeFichier,nbreTotal,Long.
	 * valueOf(0),Long.valueOf(3),"",date,date); //LiveTraitement
	 * trait=liveTraitementRepository.save(liveTraitement); LiveTraitement trait=
	 * liveTraitementRepository.save(liveTraitement); return trait.getId();
	 * 
	 * }
     */
    @Override
    public void endDetailsReportingToTheVue1(String idTraitement, String codefichier, Long statut, Long nbreTraite, Long nbtotal) {

        System.out.println("END_ LiveReportingServiceImpl detailsTraitment");
        Date date = new Date();
        List<LiveTraitementS> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTraitement, codefichier);
        if (trait.size() > 0) {
            trait.get(0).setStatut(String.valueOf(statut));
            trait.get(0).setNbtraite(String.valueOf(nbtotal));
            trait.get(0).setNbtotal(String.valueOf(nbtotal));
            trait.get(0).setDateFin(date);
            LiveTraitementS s = liveTraitementRepository.save(trait.get(0));
            LiveOperationS r = liveOperationRepository.findLiveOperationByCodeUnique(idTraitement);
            r.setNbtraite(r.getNbtraite() + 1L);
            liveOperationRepository.save(r);
        }
    }

    @Override
    public void endDetailsReportingToTheVue2(String idTraitement, String codefichier, Long statut, Long nbreTraite, Long nbtotal) {
        System.out.println("Finish " + codefichier + " Generating ");
        liveTraitementRepository.opdatefinaltraitement(idTraitement, codefichier);
        liveTraitementRepository.updateoperation(idTraitement);
    }

    @Override
    public void endDetailsReportingToTheVue(String idTraitement, String codefichier, Long statut, Long nbreTraite) {

        System.out.println("END_ LiveReportingServiceImpl detailsTraitment");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        // LiveTraitement trait = liveTraitementRepository.findOne(idTraitement);
        List<LiveTraitementS> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTraitement,
                codefichier);
        System.out.println("sssssssssssssss " + trait.size());
        System.out.println("idsssssssssssssss " + idTraitement);
        System.out.println("codesssssssssssssss " + codefichier);
        if (trait.size() > 0) {
            trait.get(0).setStatut(String.valueOf(statut));
            trait.get(0).setNbtraite(String.valueOf(nbreTraite));
            trait.get(0).setDateFin(date);
            liveTraitementRepository.save(trait.get(0));
        }
    }

    @Override
    public Long beginGobalReportingToTheVue(String codeUnique, String cetab, String usid, String Operation,
            Long NbreTotal) {
        System.out.println("Register the Global progress bar");
        LiveOperationS liveOpe = new LiveOperationS();
        Date date = new Date();
        liveOpe.setCodeUnique(codeUnique);
        liveOpe.setCetab(cetab);
        liveOpe.setUsid(usid);
        liveOpe.setOperations(Operation);
        liveOpe.setNbtotal(NbreTotal);
        liveOpe.setDateDebut(date);
        liveOpe.setDateEnd(date);
        liveOpe.setNbtraite(Long.valueOf(0));
        liveOpe.setStatut(Long.valueOf(3));
        LiveOperationS op = liveOperationRepository.save(liveOpe);
        return op.getId();

    }

    @Override
    public void endGobalReportingToTheVue3(Long id, Long statut) {

        // statut 1 succes statut 2 failed 3 pending statut 4 succes mais il ya des
        // erreur sur les elements traité
        // nbTraite c est le nbre d element traité durant toute l operation.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        LiveOperationS op = liveOperationRepository.findOne(id);
        op.setDateEnd(date);
        op.setStatut(statut);
        System.out.println("its the begining of testing");
        System.out.println(op.getNbtotal());
//        op.setNbtraite(op.getNbtotal());
        liveOperationRepository.save(op);
    }

    @Override
    public void endGobalReportingToTheVue1(Long id, Long statut) {

        // statut 1 succes statut 2 failed 3 pending statut 4 succes mais il ya des
        // erreur sur les elements traité
        // nbTraite c est le nbre d element traité durant toute l operation.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        LiveOperationS op = liveOperationRepository.findOne(id);
        op.setDateEnd(date);
        op.setStatut(statut);
        System.out.println("its the begining of testing");
        System.out.println(op.getNbtotal());
        op.setNbtraite(op.getNbtotal());
        liveOperationRepository.save(op);
    }

    @Override
    public void endGobalReportingToTheVue(Long id, Long statut, Long nbTraite) {
        // statut 1 succes stauut 2 failed 3 pending statut 4 succes mais il ya des
        // erreur sur les elements traité
        // nbTraite c est le nbre d element traité durant toute l operation.
        Date date = new Date();
        LiveOperationS op = liveOperationRepository.findOne(id);
        op.setDateEnd(date);
        op.setStatut(statut);
        op.setNbtraite(op.getNbtotal());
        liveOperationRepository.save(op);

    }

    public void saveP(LiveTraitementS trait) {

        try {

            try {
                parameters = chargerDonneesServiceLocal.getGenerationAndSavingParam();
            } catch (JSONException ex) {
                Logger.getLogger(LiveReportingServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your Oracle JDBC Driver?");
                e.printStackTrace();
            }
            try {

                // connection = DriverManager.getConnection(url, login, password);
                System.out.println("BEFORE ORACLE CONNEXION");
                connection = DriverManager.getConnection(parameters.get("oracleUrl"), parameters.get("loginUrl"),
                        parameters.get("passwordUrl"));
                System.out.println("AFTER ORACLE CONNEXION");
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
            }
            Statement stmt = null;
            // String query = "INSERT INTO live_trait
            // (code_Ligne,code_unique,codefichier,date_debut,date_fin,details,nbtotal,nbtraite,statut)
            // VALUES "
            String query = "INSERT INTO live_trait (code_Ligne,code_unique,codefichier,details,nbtotal,nbtraite,statut) VALUES "
                    + "('" + trait.getCodeLigne() + "','" + trait.getCodeUnique() + "','" + trait.getCodefichier()
                    + "','" + trait.getDetails() + "'," + "'" + trait.getNbtotal() + "','" + trait.getNbtraite() + "','"
                    + trait.getStatut() + "')";
            stmt = connection.createStatement();
            System.out.println("BEFORE ORACLE QUERRY EXECUTION AFTER STATEMENT");
            System.out.println("QUERRY:" + query);
            ResultSet rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(ChargerDonneesServiceLocalImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String randomAlphaNumeric(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }
        return builder.toString();

    }
}

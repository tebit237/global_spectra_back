/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import iwomi.base.ServiceInterface.ChargerDonneesServiceLocal;
import iwomi.base.ServiceInterface.LiveReportingService;
import iwomi.base.form.LiveReportForm;
import iwomi.base.form.ReportResultFom;
import iwomi.base.objects.LiveOperation;
import iwomi.base.objects.LiveTraitement;
import iwomi.base.repositories.LiveOperationRepository;
import iwomi.base.repositories.LiveTraitementRepository;

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
import org.springframework.stereotype.Service;

/**
 *
 * @author fabri
 */

@Service
public class LiveReportingServiceImpl implements LiveReportingService {
	static int countp = 0;
	@Autowired
	LiveOperationRepository liveOperationRepository;
	@Autowired
	LiveTraitementRepository liveTraitementRepository;
	@Autowired
	ChargerDonneesServiceLocal chargerDonneesServiceLocal;

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
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public ReportResultFom returnLivereport1(LiveReportForm liveReport) {
		int s = 0;
		String timeSpend = "";
		System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
		List<LiveTraitement> trait = liveTraitementRepository
				.findLiveTraitementByCodeUnique(liveReport.getCodeUnique());
		System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
		LiveOperation op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
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
		return new ReportResultFom("1", trait.get(s).getCodefichier(),
				String.valueOf(trait.get(trait.size() - 1).getStatut()), filevalue, String.valueOf(op.getStatut()),
				opvalue, timeSpend);

	}

	public List<ReportResultFom> returnLivereport2(LiveReportForm liveReport) {
		int s = 0;
		String timeSpend = "";
		System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
		List<LiveTraitement> trait = liveTraitementRepository
				.findLiveTraitementByCodeUnique(liveReport.getCodeUnique());
		System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
		LiveOperation op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
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
		List<ReportResultFom> p = new ArrayList<ReportResultFom>();
		String opvalue1 = opvalue;
		trait.forEach(e -> {
			p.add(new ReportResultFom("1", e.getCodefichier(), String.valueOf(e.getStatut()),String.valueOf(
					(Long.valueOf(e.getNbtraite()) * 100) / Long.valueOf(e.getNbtotal())),
					String.valueOf(op.getStatut()), opvalue1, getDiffDate(e.getDateDebut())));
		});
		return p;

	}

	public ReportResultFom returnLivereport(LiveReportForm liveReport) {

		String timeSpend = "";
		System.out.println("BEGIN LiveReportingServiceImpl returnLivereport 44 ");
		List<LiveTraitement> trait = liveTraitementRepository
				.findLiveTraitementByCodeUniqueAndCodefichier(liveReport.getCodeUnique(), liveReport.getCodeFichier());
		System.out.println("AFTER TRAITEMENT LiveReportingServiceImpl returnLivereport 47 ");
		LiveOperation op = liveOperationRepository.findLiveOperationByCodeUnique(liveReport.getCodeUnique());
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
		return new ReportResultFom("1", liveReport.getCodeFichier(), String.valueOf(trait.get(0).getStatut()),
				filevalue, String.valueOf(op.getStatut()), opvalue, timeSpend);

	}

	@Override
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
			List<LiveTraitement> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTrait,
					codeFichier);
			if (Long.valueOf(trait.get(0).getNbtraite()) + 100L < Long.valueOf(trait.get(0).getNbtotal())) {
				Long pp = Long.valueOf(trait.get(0).getNbtraite()) + countp;
				System.out.println("treated element " + pp);
				trait.get(0).setNbtraite(pp.toString());
				trait.get(0).setDateFin(date);
				System.out.println("size of current processing is of size :" + trait.size());
				System.out.println(" Before save traitement in detailsTraitment 91 ");
				liveTraitementRepository.save(trait.get(0));
			}
			System.out.println(" number already treated :" + countp);
			System.out.println(" After save traitement in detailsTraitment 91 ");
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
		if (nbreTotal > minimum) {

			if (Nbretraite > minimum * quotien) {
				System.out.println(" Enter minimum in quotien in detailsTraitment : "+minimum);
				Date date = new Date();
				nbreTotal = nbreTotal + 100;
				List<LiveTraitement> trait = liveTraitementRepository
						.findLiveTraitementByCodeUniqueAndCodefichier(idTrait, codeFichier);
				trait.get(0).setNbtraite(String.valueOf(Nbretraite));
				trait.get(0).setDateFin(date);
				trait.get(0).setNbtotal(nbreTotal.toString());
				trait.get(0).setDateFin(date);
				System.out.println("size of current processing is of size :" + trait.size());
				liveTraitementRepository.save(trait.get(0));

				System.out.println(" number quotien :" + quotien);
				System.out.println(" number already treated :" + Nbretraite);
				quotien = quotien + 1;
				return quotien;
			}

		}

		return quotien;

	}

	@Override
	public String beginDetailsReportingToTheVue1(Long idope, String codeFichier, Long nbreTotal) {

		System.out.println("BEGIN_ LiveReportingServiceImpl detailsTraitment");
		DateFormat dateFormat = new SimpleDateFormat("y-m-d");
		Date date = new Date();
		LiveOperation op = liveOperationRepository.findOne(idope);
		LiveTraitement liveTraitement = null;
		liveTraitement = new LiveTraitement(op.getCodeUnique() + randomAlphaNumeric(10), op.getCodeUnique(),
				codeFichier, String.valueOf(nbreTotal), String.valueOf(0), String.valueOf(3), "", date, date);
		LiveTraitement trait2 = liveTraitementRepository.save(liveTraitement);
		List<LiveTraitement> trait = liveTraitementRepository
				.findLiveTraitementByCodeUniqueAndCodefichier(op.getCodeUnique(), codeFichier);
		if (trait.size() > 1) {
			for (int i = 0; i < trait.size() - 1; i++) {
				liveTraitementRepository.delete(trait.get(i));
			}
		}

		// liveTraitementRepository.delete(trait);
		// LiveTraitement trait= liveTraitementRepository.saveAndFlush(liveTraitement);
		// saveP(liveTraitement);
		// LiveTraitement trait=
		// liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(op.getCodeUnique(),
		// codeFichier);
		// return trait.getId();
		return liveTraitement.getCodeUnique();

	}

	@Override
	public String beginDetailsReportingToTheVue(Long idope, String codeFichier, Long nbreTotal) {
		System.out.println("BEGIN_ LiveReportingServiceImpl detailsTraitment");
		DateFormat dateFormat = new SimpleDateFormat("y-m-d");
		Date date = new Date();
		LiveOperation op = liveOperationRepository.findOne(idope);
		LiveTraitement liveTraitement = null;
		liveTraitement = new LiveTraitement(op.getCodeUnique() + randomAlphaNumeric(10), op.getCodeUnique(),
				codeFichier, String.valueOf(nbreTotal), String.valueOf(0), String.valueOf(3), "", date, date);
		LiveTraitement trait2 = liveTraitementRepository.save(liveTraitement);
		List<LiveTraitement> trait = liveTraitementRepository
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
	public void endDetailsReportingToTheVue1(String idTraitement, String codefichier, Long statut, Long nbreTraite,
			Long nbtotal) {

		System.out.println("END_ LiveReportingServiceImpl detailsTraitment");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		// LiveTraitement trait = liveTraitementRepository.findOne(idTraitement);
		List<LiveTraitement> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTraitement,
				codefichier);
		System.out.println("sssssssssssssss " + trait.size());
		System.out.println("idsssssssssssssss " + idTraitement);
		System.out.println("codesssssssssssssss " + codefichier);
		System.out.println("codesssssssssssssss " + trait.size());
		if (trait.size() > 0) {
			trait.get(0).setStatut(String.valueOf(statut));
			trait.get(0).setNbtraite(String.valueOf(nbtotal));
			// this is for treatement not integration
//			trait.get(0).setNbtotal(nbtotal.toString());
			trait.get(0).setNbtotal(String.valueOf(nbtotal));
			trait.get(0).setDateFin(date);
			LiveTraitement s = liveTraitementRepository.save(trait.get(0));
			LiveOperation r = liveOperationRepository.findLiveOperationByCodeUnique(idTraitement);
			r.setNbtraite(r.getNbtraite() + 1L);
			liveOperationRepository.save(r);
			System.out.println("codessssssssssssssst " + s.getNbtotal());
			System.out.println("codessssssssssssssst " + s.getNbtraite());
		}
	}

	@Override
	public void endDetailsReportingToTheVue(String idTraitement, String codefichier, Long statut, Long nbreTraite) {

		System.out.println("END_ LiveReportingServiceImpl detailsTraitment");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		// LiveTraitement trait = liveTraitementRepository.findOne(idTraitement);
		List<LiveTraitement> trait = liveTraitementRepository.findLiveTraitementByCodeUniqueAndCodefichier(idTraitement,
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

		LiveOperation liveOpe = new LiveOperation();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		liveOpe.setDetails("");
		LiveOperation op = liveOperationRepository.save(liveOpe);
		return op.getId();

	}

	@Override
	public void endGobalReportingToTheVue1(Long id, Long statut) {

		// statut 1 succes statut 2 failed 3 pending statut 4 succes mais il ya des
		// erreur sur les elements traité
		// nbTraite c est le nbre d element traité durant toute l operation.
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		LiveOperation op = liveOperationRepository.findOne(id);
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
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		LiveOperation op = liveOperationRepository.findOne(id);
		op.setDateEnd(date);
		op.setStatut(statut);
		op.setNbtraite(nbTraite);
//         Double pcentage = (Double.valueOf(nbTraite)*100)/Double.valueOf(op.getNbtotal());
//         op.setPcentage(pcentage);
		liveOperationRepository.save(op);

	}

	public void saveP(LiveTraitement trait) {

		try {

			try {
				parameters = chargerDonneesServiceLocal.getGenerationAndSavingParam();
			} catch (JSONException ex) {
				Logger.getLogger(LiveReportingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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

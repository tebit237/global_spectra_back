package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INCOM")
@Entity(name = "INCOM")

public class Incom {
	
	@Id
	    @GeneratedValue

	private int ID;
	private String AGE;
	private String CETAB;
	private String CFER;
	private String CHAP;
	private String CHL1;
	private String CHL2;
	private String CHL3;
	private String CHL4;
	private String CHL5;
	private String CHL6;
	private String CLE;
	private String CLI;
	private String COM;
	private String CUMC;
	private String CUMD;
	private String DCRE;
	private String DDC;
	private String DDD;
	private String DDM;
	private int DELE;
	private String DEV;
	private String DFE;
	private String DODB;
	private String DOU;
	private String LIB;
	private String SDE;
	private String SHI;	
        private Date dar;

	
	
	public Incom() {
		
	}

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

  
	
	

	public int getID() {
		return ID;
	}

	public String getAGE() {
		return AGE;
	}

	public String getCETAB() {
		return CETAB;
	}

	public String getCFER() {
		return CFER;
	}

	public String getCHAP() {
		return CHAP;
	}

	public String getCHL1() {
		return CHL1;
	}

	public String getCHL2() {
		return CHL2;
	}

	public String getCHL3() {
		return CHL3;
	}

	public String getCHL4() {
		return CHL4;
	}

	public String getCHL5() {
		return CHL5;
	}

	public String getCHL6() {
		return CHL6;
	}

	public String getCLE() {
		return CLE;
	}

	public String getCLI() {
		return CLI;
	}

	public String getCOM() {
		return COM;
	}

	public String getCUMC() {
		return CUMC;
	}

	public String getCUMD() {
		return CUMD;
	}

	public String getDCRE() {
		return DCRE;
	}

	public String getDDC() {
		return DDC;
	}

	public String getDDD() {
		return DDD;
	}

	public String getDDM() {
		return DDM;
	}

	public int getDELE() {
		return DELE;
	}

	public String getDEV() {
		return DEV;
	}

	public String getDFE() {
		return DFE;
	}

	public String getDODB() {
		return DODB;
	}

	public String getDOU() {
		return DOU;
	}

	public String getLIB() {
		return LIB;
	}

	public String getSDE() {
		return SDE;
	}

	public String getSHI() {
		return SHI;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setAGE(String aGE) {
		AGE = aGE;
	}

	public void setCETAB(String cETAB) {
		CETAB = cETAB;
	}

	public void setCFER(String cFER) {
		CFER = cFER;
	}

	public void setCHAP(String cHAP) {
		CHAP = cHAP;
	}

	public void setCHL1(String cHL1) {
		CHL1 = cHL1;
	}

	public void setCHL2(String cHL2) {
		CHL2 = cHL2;
	}

	public void setCHL3(String cHL3) {
		CHL3 = cHL3;
	}

	public void setCHL4(String cHL4) {
		CHL4 = cHL4;
	}

	public void setCHL5(String cHL5) {
		CHL5 = cHL5;
	}

	public void setCHL6(String cHL6) {
		CHL6 = cHL6;
	}

	public void setCLE(String cLE) {
		CLE = cLE;
	}

	public void setCLI(String cLI) {
		CLI = cLI;
	}

	public void setCOM(String cOM) {
		COM = cOM;
	}

	public void setCUMC(String cUMC) {
		CUMC = cUMC;
	}

	public void setCUMD(String cUMD) {
		CUMD = cUMD;
	}

	public void setDCRE(String dCRE) {
		DCRE = dCRE;
	}

	public void setDDC(String dDC) {
		DDC = dDC;
	}

	public void setDDD(String dDD) {
		DDD = dDD;
	}

	public void setDDM(String dDM) {
		DDM = dDM;
	}

	public void setDELE(int dELE) {
		DELE = dELE;
	}

	public void setDEV(String dEV) {
		DEV = dEV;
	}

	public void setDFE(String dFE) {
		DFE = dFE;
	}

	public void setDODB(String dODB) {
		DODB = dODB;
	}

	public void setDOU(String dOU) {
		DOU = dOU;
	}

	public void setLIB(String lIB) {
		LIB = lIB;
	}

	public void setSDE(String sDE) {
		SDE = sDE;
	}

	public void setSHI(String sHI) {
		SHI = sHI;
	}



	/**
	 * @param aGE
	 * @param cETAB
	 * @param cFER
	 * @param cHAP
	 * @param cHL1
	 * @param cHL2
	 * @param cHL3
	 * @param cHL4
	 * @param cHL5
	 * @param cHL6
	 * @param cLE
	 * @param cLI
	 * @param cOM
	 * @param cUMC
	 * @param cUMD
	 * @param dCRE
	 * @param dDC
	 * @param dDD
	 * @param dDM
	 * @param dELE
	 * @param dEV
	 * @param dFE
	 * @param dODB
	 * @param dOU
	 * @param lIB
	 * @param sDE
	 * @param sHI
	 */
	public Incom(String aGE, String cETAB, String cFER, String cHAP, String cHL1, String cHL2, String cHL3, String cHL4,
			String cHL5, String cHL6, String cLE, String cLI, String cOM, String cUMC, String cUMD, String dCRE,
			String dDC, String dDD, String dDM, int dELE, String dEV, String dFE, String dODB, String dOU, String lIB,
			String sDE, String sHI, Date dar) {
		
		super();
		AGE = aGE;
		CETAB = cETAB;
		CFER = cFER;
		CHAP = cHAP;
		CHL1 = cHL1;
		CHL2 = cHL2;
		CHL3 = cHL3;
		CHL4 = cHL4;
		CHL5 = cHL5;
		CHL6 = cHL6;
		CLE = cLE;
		CLI = cLI;
		COM = cOM;
		CUMC = cUMC;
		CUMD = cUMD;
		DCRE = dCRE;
		DDC = dDC;
		DDD = dDD;
		DDM = dDM;
		DELE = dELE;
		DEV = dEV;
		DFE = dFE;
		DODB = dODB;
		DOU = dOU;
		LIB = lIB;
		SDE = sDE;
		SHI = sHI;
		dar = dar;
	}
	
	
	
	
	

}

package iwomi.base.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "INVENT")
@Entity(name = "INVENT")

public class Invent {
	
	@Id
	
	private int ID;
	private String INV;
	private Date DATD;
	private int STTS;
	private int DELE;
	
	
	public Invent() {
		
	}


	public int getID() {
		return ID;
	}


	public String getINV() {
		return INV;
	}


	public Date getDATD() {
		return DATD;
	}


	public int getSTTS() {
		return STTS;
	}


	public int getDELE() {
		return DELE;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public void setINV(String iNV) {
		INV = iNV;
	}


	public void setDATD(Date dATD) {
		DATD = dATD;
	}


	public void setSTTS(int sTTS) {
		STTS = sTTS;
	}


	public void setDELE(int dELE) {
		DELE = dELE;
	}


	public Invent(String iNV, Date dATD, int sTTS, int dELE) {
		super();
		INV = iNV;
		DATD = dATD;
		STTS = sTTS;
		DELE = dELE;
	}
	
	
	
	
	

}

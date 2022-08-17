
package iwomi.base.objects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "rprep")
@IdClass(ReportRepId.class)
public class ReportRep2 implements Serializable {

	private Long id;
	@Id
	private Long rang;
	private String sens;
	private String filter;
	private String field;
	private String feild;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Id
	private Date dar;
	private Date vald;
	private Double valt;
	private String valc;
	private Double valm;
	private String post;
	private String muser;
	private Date mdfi;
	@Id
	private String fichier;
	private String feuille;
	private String etab;
	private String doc;
	private Long dele;
	private Date crtd;
	private Date crdt;
	private String cuser;
	private long status;
	@Id
	private String col;

	public ReportRep2() {
	}

	public ReportRep2(Long id, Long rang, String sens, String filter, String field, String feild, Date dar, Date vald,
			Double valt, String valc, Double valm, String post, String muser, Date mdfi, String fichier, String feuille,
			String etab, String doc, Long dele, Date crtd, Date crdt, String cuser, String col) {
		this.id = id;
		this.rang = rang;
		this.sens = sens;
		this.filter = filter;
		this.field = field;
		this.feild = feild;
		this.dar = dar;
		this.vald = vald;
		this.valt = valt;
		this.valc = valc;
		this.valm = valm;
		this.post = post;
		this.muser = muser;
		this.mdfi = mdfi;
		this.fichier = fichier;
		this.feuille = feuille;
		this.etab = etab;
		this.doc = doc;
		this.dele = dele;
		this.crtd = crtd;
		this.crdt = crdt;
		this.cuser = cuser;
		this.col = col;
	}
	public void getReportrep(ReportRep r) {
		this.id = r.getId();
		this.rang = r.getRang();
		this.sens = r.getSens();
		this.filter = r.getFilter();
		this.field = r.getField();
		this.feild = r.getFeild();
		this.dar = r.getDar();
		this.vald = r.getVald();
		this.valt = r.getValt();
		this.valc = r.getValc();
		this.valm = r.getValm();
		this.post = r.getPost();
		this.muser = r.getMuser();
		this.mdfi = r.getMdfi();
		this.fichier = r.getFichier();
		this.feuille = r.getFeuille();
		this.etab = r.getEtab();
		this.doc = r.getDoc();
		this.dele = r.getDele();
		this.crtd = r.getCrtd();
		this.crdt = r.getCrdt();
		this.cuser = r.getCuser();
		this.col = r.getCol();
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public Date getVald() {
		return vald;
	}
 
	public void setVald(Date vald) {
		this.vald = vald;
	}

	public String getValc() {
		return valc;
	}

	public void setValc(String valc) {
		this.valc = valc;
	}

	public Double getValt() {
		return valt;
	}

	public void setValt(Double valt) {
		this.valt = valt;
	}

	public Double getValm() {
		return valm;
	}

	public void setValm(Double valm) {
		this.valm = valm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRang() {
		return rang;
	}

	public String getFeild() {
		return feild;
	}

	public void setFeild(String feild) {
		this.feild = feild;
	}

	public Date getCrdt() {
		return crdt;
	}

	public void setCrdt(Date crdt) {
		this.crdt = crdt;
	}

	public void setRang(Long rang) {
		this.rang = rang;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getMuser() {
		return muser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}

	public Date getMdfi() {
		return mdfi;
	}

	public void setMdfi(Date mdfi) {
		this.mdfi = mdfi;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public String getFeuille() {
		return feuille;
	}

	public void setFeuille(String feuille) {
		this.feuille = feuille;
	}

	public String getEtab() {
		return etab;
	}

	public void setEtab(String etab) {
		this.etab = etab;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public Long getDele() {
		return dele;
	}

	public void setDele(Long dele) {
		this.dele = dele;
	}

	public Date getCrtd() {
		return crtd;
	}

	public void setCrtd(Date crtd) {
		this.crtd = crtd;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public Date getDar() {
		return dar;
	}

	@SuppressWarnings("deprecation")
	public Date initialDate() {
		dar.setHours(0);
		dar.setMinutes(0);
		dar.setSeconds(0);
		return dar;
	}

	public void setDar(Date dar) {
		this.dar = dar;
	}

	public ReportRepId getRepId() {
		return new ReportRepId(rang, dar, col, fichier);
	}
	
	@SuppressWarnings("deprecation")
	public void setHZero() {
		dar.setHours(0);
	}
	public Boolean setByColumn1(ReportRep2 d) {
		int r = 0;
		if (this.valm != null) {
			this.valm = d.valm;
			r++;
		}
		if (this.vald != null) {
			this.vald = d.vald;
			r++;
		}
		if (this.valc != null) {
			this.valc = d.valc;
			r++;
		}
		if (this.valt != null) {
			this.valt = d.valt;
			r++;
		}
		return r == 1 ? true : false;
	}

	public Boolean setByColumn11(ReportRep2 d) {
		System.out.println(d.post);
		int r = 0;
		if (this.valm != null) {
			this.valm = Double.parseDouble(d.valc);
			r++;
			System.out.println("its m");
		}
		if (this.vald != null) {
			System.out.println("its m");
			try {
				this.vald = new SimpleDateFormat("yyyy-MM-dd").parse(d.valc);
				r++;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.valc != null) {
			System.out.println("its the value entered :" + d.valc);
			this.valc = d.valc;
			r++;
		}
		if (this.valt != null) {
			System.out.println("its m");
			this.valt = Double.parseDouble(d.valc);
			r++;
		}
		return r == 1 ? true : false;
	}

//	@Override
//	public int compareTo(ReportRep2 arg0) {
//		return this.getCol().compareTo(arg0.getCol());//Integer.parseInt(s)
////		return Integer.parseInt(this.getCol()).compareTo(Integer.parseInt(arg0.getCol()));
//
//	}
}

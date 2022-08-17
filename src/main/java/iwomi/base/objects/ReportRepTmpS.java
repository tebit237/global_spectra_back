
package iwomi.base.objects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "srpreptmp")
public class ReportRepTmpS {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long rang;
	private String sens;
	private String filter;
	private String field1;
	private String feild;
	private String dar;
	private String val;
	private String post;
	private String muser;
	private Date mdfi;
	private String fichier;
	private String feuille;
	private String etab;
	private String doc;
	private Long dele;
//	private Date date1;
	private Date crtd;
	private Date crdt;
	private String cuser;
	private String col;
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
		return field1;
	}

	public void setField(String field) {
		this.field1 = field;
	}

	public String getDar() {
		return dar;
	}

	public void setDar(String dar) {
		this.dar = dar;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
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
//
//	public Date getDate() {
//		return date1;
//	}
//
//	public void setDate(Date date) {
//		this.date1 = date;
//	}

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

	public ReportRepTmpS(Long id, Long rang, String sens, String filter, String field, String feild, String dar, String val,
			String post, String muser, Date mdfi, String fichier, String feuille, String etab, String doc, Long dele,
			Date date, Date crtd, Date crdt, String cuser, String col) {
		super();
		this.id = id;
		this.rang = rang;
		this.sens = sens;
		this.filter = filter;
		this.field1 = field;
		this.feild = feild;
		this.dar = dar;
		this.val = val;
		this.post = post;
		this.muser = muser;
		this.mdfi = mdfi;
		this.fichier = fichier;
		this.feuille = feuille;
		this.etab = etab;
		this.doc = doc;
		this.dele = dele;
//		this.date1 = date;
		this.crtd = crtd;
		this.crdt = crdt;
		this.cuser = cuser;
		this.col = col;
	}

	public ReportRepTmpS() {
	}
}

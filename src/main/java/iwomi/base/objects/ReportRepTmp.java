package iwomi.base.objects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rpreptmp")
public class ReportRepTmp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long rang;
    private String sens;
    private String filter;
    private String field;
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
//	private Date date;
    private Date crtd;
    private Date crdt;
    private String cuser;
    private String col;

    private Date vald;
    private Double valt;
    private String valc;
    private Double valm;

    public Long getId() {
        return id;
    }

    public void setVald(Date vald) {
        this.vald = vald;
    }

    public void setValt(Double valt) {
        this.valt = valt;
    }

    public ReportRepTmp(Long rang, String field, String dar, String post, String etab, Long dele, String col, String typeval, Object solde, Object dev) {
        if (typeval.equalsIgnoreCase("M")) {
            Double dre = Double.parseDouble(solde.toString());
//            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / Double.parseDouble(dev.toString()));
            this.valm = (double) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / Double.parseDouble(dev.toString())));
        } else if (typeval.equalsIgnoreCase("D")) {
            this.vald = null;
        } else if (typeval.equalsIgnoreCase("C")) {
            this.valc = solde.toString();
        } else if (typeval.equalsIgnoreCase("T")) {
            this.valt = null;
        }
        this.rang = rang;
        this.field = field;
        this.dar = dar;
        this.post = post;
        this.etab = etab;
        this.dele = dele;
        this.col = col;
    }

    public void setValc(String valc) {
        this.valc = valc;
    }

    public void setValm(Double valm) {
        this.valm = valm;
    }

    public Date getVald() {
        return vald;
    }

    public Double getValt() {
        return valt;
    }

    public String getValc() {
        return valc;
    }

    public Double getValm() {
        return valm;
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

//	public Date getDate() {
//		return date;
//	}
//
//	public void setDate(Date date) {
//		this.date = date;
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

    public ReportRepTmp(Long id, Long rang, String sens, String filter, String field, String feild, String dar, String val,
            String post, String muser, Date mdfi, String fichier, String feuille, String etab, String doc, Long dele,
            Date date, Date crtd, Date crdt, String cuser, String col) {
        super();
        this.id = id;
        this.rang = rang;
        this.sens = sens;
        this.filter = filter;
        this.field = field;
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
//		this.date = date;
        this.crtd = crtd;
        this.crdt = crdt;
        this.cuser = cuser;
        this.col = col;
    }

    public ReportRepTmp() {
    }
}

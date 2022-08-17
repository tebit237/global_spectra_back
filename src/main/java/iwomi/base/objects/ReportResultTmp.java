/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author User
 */
@Entity
@Table(name = "rpreptmp")
public class ReportResultTmp {

    public ReportResultTmp(Date dar, String etab, String feuille, String feild, String post, String doc,
            String col, String cuser, String muser, Double val, Long rang, String fichier, int status) {
        super();
        this.dar = dar;
        this.etab = etab;
        this.feuille = feuille;
        this.feild = feild;
        this.post = post;
        this.doc = doc;
        this.col = col;
        this.val = val;
        this.fichier = fichier;
        this.cuser = cuser;
        this.muser = muser;
        this.rang = rang;
        this.status = status;
    }

    public ReportResultTmp(Long rang, String field, String post, String etab, Long dele, String col, String typeval, Object solde, Object dev, String date,String fichi) {
        if (typeval.equalsIgnoreCase("M")) {
            Double dre = Double.parseDouble(solde.toString());
//            int sold = (int) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / Double.parseDouble(dev.toString()));
            this.valm = (double) ((dre >= 0 ? 1 : -1) * Math.round((dre >= 0 ? dre : -dre) / Double.parseDouble(dev.toString())));
            this.rawres = solde.toString();
        } else if (typeval.equalsIgnoreCase("D")) {
            this.vald = null;
        } else if (typeval.equalsIgnoreCase("C")) {
            this.valc = solde.toString();
        } else if (typeval.equalsIgnoreCase("T")) {
            this.valt = null;
        }
        this.rang = rang;
        this.field = field;
        this.dar = java.sql.Date.valueOf(date);
        this.post = post;
        this.etab = etab;
        this.dele = dele;
        this.col = col;
        this.fichier = fichi;
    }

    public ReportResultTmp(Date dar, String etab, String feild, String post,
            String col, String cuser, String muser, String valc, Long rang, String fichier, int status) {
        super();
        this.dar = dar;
        this.etab = etab;
        this.feuille = null;
        this.feild = feild;
        this.post = post;
        this.doc = null;
        this.col = col;
        this.valc = valc;
        this.fichier = fichier;
        this.cuser = cuser;
        this.muser = muser;
        this.rang = rang;
        this.status = status;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public void setFeuille(String feuille) {
        this.feuille = feuille;
    }

    public void setFeild(String feild) {
        this.feild = feild;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String etab;
    private String filter;
    private String doc;
    private String feuille;
    private String post;
    private String feild;
    private String col;
    private String valc;
    private String rawres;

    public String getValc() {
        return valc;
    }

    public void setValc(String valc) {
        this.valc = valc;
    }

    private Double val;
    private String cuser;
    private String muser;
    private String fichier;
    private String sens;
    private Date dar;
    private Long rang;
    private Date crtd;
    private Date mdfi;
    private Long dele;
    private int status;

    private String field;
    private Date vald;
    private Double valt;
    private Double valm;

    public Long getDele() {
        return dele;
    }

    public void setRawres(String rawres) {
        this.rawres = rawres;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setVald(Date vald) {
        this.vald = vald;
    }

    public void setValt(Double valt) {
        this.valt = valt;
    }

    public void setValm(Double valm) {
        this.valm = valm;
    }

    public String getRawres() {
        return rawres;
    }

    public String getField() {
        return field;
    }

    public Date getVald() {
        return vald;
    }

    public Double getValt() {
        return valt;
    }

    public Double getValm() {
        return valm;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getRang() {
        return rang;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public void setRang(Long rang) {
        this.rang = rang;
    }

    public Long getId() {
        return id;
    }

    public String getEtab() {
        return etab;
    }

    public String getSens() {
        return sens;
    }

    public String getFilter() {
        return filter;
    }

    public String getDoc() {
        return doc;
    }

    public String getFeuille() {
        return feuille;
    }

    public String getPost() {
        return post;
    }

    public String getFeild() {
        return feild;
    }

    public Double getVal() {
        return val;
    }

    public String getFichier() {
        return fichier;
    }

    public String getCuser() {
        return cuser;
    }

    public String getMuser() {
        return muser;
    }

    public Date getCrtd() {
        return crtd;
    }

    public String col() {
        return col;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setfeild(String feild) {
        this.feild = feild;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public void setfeuille(String feuille) {
        this.feuille = feuille;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public void setCrtd(Date crtd) {
        this.crtd = crtd;
    }

    public void setMdfi(Date mdfi) {
        this.mdfi = mdfi;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public Date getMdfi() {
        return mdfi;
    }

    public Date getDar() {
        return dar;
    }

//    public ReportResult() {
//
//    }
    public ReportResultTmp(Date dar, String etab, String feuille, String feild, String post, String doc, String col,
            String cuser, String muser, String fichier, Double val) {
        super();
        this.dar = dar;
        this.etab = etab;
        this.feuille = feuille;
        this.feild = feild;
        this.post = post;
        this.doc = doc;
        this.col = col;
        this.val = val;
        this.fichier = fichier;
        this.cuser = cuser;
        this.muser = muser;
        this.crtd = crtd;
        this.mdfi = mdfi;
    }
}

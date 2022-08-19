/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;


/**
 *
 * @author fabri
 */


@Entity(name = "rprep")
@Table(name = "rprep")

public class ReportDatasGenerated {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
   // @OrderBy("feuille asc, post asc")
    private Long id;
    private String etab;
//    private String date;
    private int rang;
  //  private Date date;
    private String fichier;
    private String doc;
    private String dar;
    private String feuille;
    private String post;
    private String field;
    private int col;
    private Double valm;
    private Double valt;
    private String valc;
    private Date vald;
   // private String typeval;
    private String cuser;
    private String muser;
    private String crdt;
    private String mdfi;
//    private Date crdt;
//    private Date mdfi;
    private Long dele;
    private Date crtd;
    private String feild;
    private String filter;
    private String sens;

    public Date getVald() {
        return vald;
    }

    public void setVald(Date vald) {
        this.vald = vald;
    }
   
    
    public Long getId() {
        return id;
    }

    public String getDar() {
        return dar;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtab() {
        return etab;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

//    public String getDate() {
//        return date;
//    }

//    public void setDate(String date) {
//        this.date = date;
//    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getFeuille() {
        return feuille;
    }

    public void setFeuille(String feuille) {
        this.feuille = feuille;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }


    public void setDar(String dar) {
        this.dar = dar;
    }

  
    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getMuser() {
        return muser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public String getCrdt() {
        return crdt;
    }

    public void setCrdt(String crdt) {
        this.crdt = crdt;
    }

    public String getMdfi() {
        return mdfi;
    }

    public void setMdfi(String mdfi) {
        this.mdfi = mdfi;
    }

    public Long getDele() {
        return dele;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public Double getValm() {
        return valm;
    }

    public void setValm(Double valm) {
        this.valm = valm;
    }

    public Double getValt() {
        return valt;
    }

    public void setValt(Double valt) {
        this.valt = valt;
    }

    public String getValc() {
        return valc;
    }

    public void setValc(String valc) {
        this.valc = valc;
    }

//    public String getTypeval() {
//        return typeval;
//    }
//
//    public void setTypeval(String typeval) {
//        this.typeval = typeval;
//    }

    public ReportDatasGenerated(String etab, String date, int rang, String fichier, String doc, String dar, String feuille, String post, String field, int col, Double valm, Double valt, String valc, String cuser, String muser, String crdt, String mdfi, Long dele, Date crtd, String feild, String filter, String sens, Date vald) {
        
        this.etab = etab;
//        this.date = date;
        this.rang = rang;
        this.fichier = fichier;
        this.doc = doc;
        this.dar = dar;
        this.feuille = feuille;
        this.post = post;
        this.field = field;
        this.col = col;
        this.valm = valm;
        this.valt = valt;
        this.valc = valc;
      //  this.typeval = typeval;
        this.cuser = cuser;
        this.muser = muser;
        this.crdt = crdt;
        this.mdfi = mdfi;
        this.dele = dele;
        this.crtd = crtd;
        this.feild = feild;
        this.filter = filter;
        this.sens = sens;
        this.vald = vald;
    }
  

	public Date getCrtd() {
		return crtd;
	}

	public void setCrtd(Date crtd) {
		this.crtd = crtd;
	}

	public String getFeild() {
		return feild;
	}

	public void setFeild(String feild) {
		this.feild = feild;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}
    
        public ReportDatasGenerated() {


        }
        
        
    
}


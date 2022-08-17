/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author fabri
 */
@Entity(name = "incau")
@Table(name = "incau")
public class InCautions {

    @Id
        @GeneratedValue

    private Long id;
    private String Cetab; // cetab 
    private String age; // agence 
    private String com; // compte 
    private String cle; // cle 
    private String dev; // devise 
    private String cli; // client 
    private String chap; // chapitre 
    private String nom; // chapitre 
    private String ref; // reference de la quotion 
    private String typ; // type 
    private Date dmep; // date de mise en place 
    private Date ddeb; // data de debut 
    private Date dfin; // date de fin 
    private Double mont; // montant 
    private String eta; // ETAT 
    private Date dlev; // Date levée 
    private Date dcre; // Date creation 
    private Date dmod; // Date modificaation 
    private String uticre; // Utilisateur creation 
    private String utimod; // Utilisteur  modification
    private Long dele; // delete 
    private Date dar; // delete 

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }
   
   
    public Long getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDmep(Date dmep) {
        this.dmep = dmep;
    }

    public void setDdeb(Date ddeb) {
        this.ddeb = ddeb;
    }

    public void setDfin(Date dfin) {
        this.dfin = dfin;
    }

    public void setMont(Double mont) {
        this.mont = mont;
    }

    public void setDlev(Date dlev) {
        this.dlev = dlev;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCetab() {
        return Cetab;
    }

    public void setCetab(String Cetab) {
        this.Cetab = Cetab;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public String getChap() {
        return chap;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getUticre() {
        return uticre;
    }

    public void setUticre(String uticre) {
        this.uticre = uticre;
    }

    public String getUtimod() {
        return utimod;
    }

    public void setUtimod(String utimod) {
        this.utimod = utimod;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getNom() {
        return nom;
    }

    public Date getDmep() {
        return dmep;
    }

    public Date getDdeb() {
        return ddeb;
    }

    public Date getDfin() {
        return dfin;
    }

    public Double getMont() {
        return mont;
    }

    public String getEta() {
        return eta;
    }

    public Date getDlev() {
        return dlev;
    }

    public Long getDele() {
        return dele;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public void setDcre(Date dcre) {
        this.dcre = dcre;
    }

    public void setDmod(Date dmod) {
        this.dmod = dmod;
    }

    public Date getDcre() {
        return dcre;
    }

    public Date getDmod() {
        return dmod;
    }
    public InCautions(){}
    public InCautions(String Cetab, String age, String com, String cle, String dev, String cli, String chap, String ref, String typ, Date dmep, Date ddeb, Date dfin, Double mont, String eta, Date dlev, String dcre, String dmod, String uticre, String utimod, Long dele,Date dar) {
       
        this.Cetab = Cetab;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.ref = ref;
        this.typ = typ;
        this.dmep = dmep;
        this.ddeb = ddeb;
        this.dfin = dfin;
        this.mont = mont;
        this.eta = eta;
        this.dlev = dlev;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
        this.dar = dar;
    }
    
    
    
    
    
    
}

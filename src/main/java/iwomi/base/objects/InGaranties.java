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
@Entity(name = "ingar")
@Table(name = "ingar")
public class InGaranties {

    @Id
    @GeneratedValue
    private Long id;
    private String etab;
    private String age;  // agence      
    private String com;  // compte 
    private String cle; // cle
    private String dev;  // devise 
    private String cli;  // client 
    private String chap; // chapitre 
    private String ref; // reference de la garantie
    private String typ;// type
    private Date dmep; // date de mise en place
    private Date ddeb; // date debut
    private Date dfin; // date de fin
    private Double mon; // montant 
    private Double maf; // Montant affecté  
    private String eng; // reference engagement 
    private String teng; // type engagement 
    private String taff;  // taux affectation
    private String eta;  // Etat 
    private Date dlev; // date levée 
    private String dcre;  // date creation 
    private String dmod;  // date modification 
    private String uticre; // utilisateur creation 
    private String utimod;  // utilisateur modification. 
    private Long dele;  // utilisateur modification. 
    private Date dar;  // utilisateur modification. 
    private String cetab;  // utilisateur modification. 

    private Double valneg;
    private Double valest;
    private Double valg;
    private Double valcpt;
    private Double valacq;

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public void setValneg(Double valneg) {
        this.valneg = valneg;
    }

    public void setValest(Double valest) {
        this.valest = valest;
    }

    public void setValg(Double valg) {
        this.valg = valg;
    }

    public void setValcpt(Double valcpt) {
        this.valcpt = valcpt;
    }

    public void setValacq(Double valacq) {
        this.valacq = valacq;
    }

    public Double getValneg() {
        return valneg;
    }

    public Double getValest() {
        return valest;
    }

    public Double getValg() {
        return valg;
    }

    public Double getValcpt() {
        return valcpt;
    }

    public Double getValacq() {
        return valacq;
    }

    public InGaranties() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setTyp(String typ) {
        this.typ = typ;
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

    public void setMon(Double mon) {
        this.mon = mon;
    }

    public void setMaf(Double maf) {
        this.maf = maf;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setTeng(String teng) {
        this.teng = teng;
    }

    public void setTaff(String taff) {
        this.taff = taff;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public void setDlev(Date dlev) {
        this.dlev = dlev;
    }

    public void setDcre(String dcre) {
        this.dcre = dcre;
    }

    public void setDmod(String dmod) {
        this.dmod = dmod;
    }

    public void setUticre(String uticre) {
        this.uticre = uticre;
    }

    public void setUtimod(String utimod) {
        this.utimod = utimod;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public Long getId() {
        return id;
    }

    public String getEtab() {
        return etab;
    }

    public String getAge() {
        return age;
    }

    public String getCom() {
        return com;
    }

    public String getCle() {
        return cle;
    }

    public String getDev() {
        return dev;
    }

    public String getCli() {
        return cli;
    }

    public String getChap() {
        return chap;
    }

    public String getRef() {
        return ref;
    }

    public String getTyp() {
        return typ;
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

    public Double getMon() {
        return mon;
    }

    public Double getMaf() {
        return maf;
    }

    public String getEng() {
        return eng;
    }

    public String getTeng() {
        return teng;
    }

    public String getTaff() {
        return taff;
    }

    public String getEta() {
        return eta;
    }

    public Date getDlev() {
        return dlev;
    }

    public String getDcre() {
        return dcre;
    }

    public String getDmod() {
        return dmod;
    }

    public String getUticre() {
        return uticre;
    }

    public String getUtimod() {
        return utimod;
    }

    public Long getDele() {
        return dele;
    }

    public Date getDar() {
        return dar;
    }

}

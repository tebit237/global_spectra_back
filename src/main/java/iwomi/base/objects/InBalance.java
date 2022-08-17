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
@Entity(name = "inbal")
@Table(name = "inbal")
public class InBalance {
    
    @Id
    @GeneratedValue

    private Long id ;
    private String cetab ;
    private Date dar ;
    private String age ;
    private String com ;
    private String cle ;
    private String dev ;
    private String cli ;
    private String chap ;
    private String sldd ;
    private String sldcvd ;
    private String cumc ;
    private String cumd ;
    private String sldf ;
    private String sldcvf ;
    private String txb ;
    /*private Date dcre ;
    private Date dmod ;*/
    private String dcre ;
    private String dmod ;
    private String uticre ;
    private String utimod ;
    private Long dele ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
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

    public String getSldd() {
        return sldd;
    }

    public void setSldd(String sldd) {
        this.sldd = sldd;
    }

    public String getSldcvd() {
        return sldcvd;
    }

    public void setSldcvd(String sldcvd) {
        this.sldcvd = sldcvd;
    }

    public String getCumc() {
        return cumc;
    }

    public void setCumc(String cumc) {
        this.cumc = cumc;
    }

    public String getCumd() {
        return cumd;
    }

    public void setCumd(String cumd) {
        this.cumd = cumd;
    }

    public String getSldf() {
        return sldf;
    }

    public void setSldf(String sldf) {
        this.sldf = sldf;
    }

    public String getSldcvf() {
        return sldcvf;
    }

    public void setSldcvf(String sldcvf) {
        this.sldcvf = sldcvf;
    }

    public String getTxb() {
        return txb;
    }

    public void setTxb(String txb) {
        this.txb = txb;
    }

    public String getDcre() {
        return dcre;
    }

    public void setDcre(String dcre) {
        this.dcre = dcre;
    }

    public String getDmod() {
        return dmod;
    }

    public void setDmod(String dmod) {
        this.dmod = dmod;
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

    public Long getDele() {
        return dele;
    }
           
    
    
    public void setDele(Long dele) {
        this.dele = dele;
    }
    
     public InBalance(){}

    public InBalance(String cetab, Date dar, String age, String com, String cle, String dev, String cli, String chap, String sldd, String sldcvd, String cumc, String cumd, String sldf, String sldcvf, String txb, String dcre, String dmod, String uticre, String utimod, Long dele) {
        this.cetab = cetab;
        this.dar = dar;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.cumc = cumc;
        this.cumd = cumd;
        this.sldf = sldf;
        this.sldcvf = sldcvf;
        this.txb = txb;
        this.dcre = dcre;
        this.dmod = dmod;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
    }

    


}

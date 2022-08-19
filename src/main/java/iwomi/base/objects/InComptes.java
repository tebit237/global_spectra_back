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
@Entity(name = "incom")
@Table(name = "incom")
public class InComptes {

    @Id
    @GeneratedValue
    private Long id;
    private String cetab ;  // code etablissement 
    private String age ;   // agence
    private String com ;    // compte 
    private String cle ;   // cle 
    private String dev ;   // devise 
    private String cli ;  // client
    private String chap ;  // chapitre 
    private String lib ;   // libelle compte 
    private String dou ;
    private String dfe ;
    private String cfer ;
    private String shi ;
    private String sde ;
    private String dodb ;
    private String ddm ;
    private String ddc ;
    private String ddd ;
    private String cumc ;
    private String cumd ;
    private String chl1 ;
    private String chl2 ;
    private String chl3 ;
    private String chl4 ;
    private String chl5 ;
    private String chl6 ;
    private String dcre ;
    private Long dele ;
    private Date dar ;
    
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

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public String getDou() {
        return dou;
    }

    public void setDou(String dou) {
        this.dou = dou;
    }

    public String getDfe() {
        return dfe;
    }

    public void setDfe(String dfe) {
        this.dfe = dfe;
    }

    public String getCfer() {
        return cfer;
    }

    public void setCfer(String cfer) {
        this.cfer = cfer;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getSde() {
        return sde;
    }

    public void setSde(String sde) {
        this.sde = sde;
    }

    public String getDodb() {
        return dodb;
    }

    public void setDodb(String dodb) {
        this.dodb = dodb;
    }

    public String getDdm() {
        return ddm;
    }

    public void setDdm(String ddm) {
        this.ddm = ddm;
    }

    public String getDdc() {
        return ddc;
    }

    public void setDdc(String ddc) {
        this.ddc = ddc;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
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

    public String getChl1() {
        return chl1;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public String getChl2() {
        return chl2;
    }

    public void setChl2(String chl2) {
        this.chl2 = chl2;
    }

    public String getChl3() {
        return chl3;
    }

    public void setChl3(String chl3) {
        this.chl3 = chl3;
    }

    public String getChl4() {
        return chl4;
    }

    public void setChl4(String chl4) {
        this.chl4 = chl4;
    }

    public String getChl5() {
        return chl5;
    }

    public void setChl5(String chl5) {
        this.chl5 = chl5;
    }

    public String getChl6() {
        return chl6;
    }

    public void setChl6(String chl6) {
        this.chl6 = chl6;
    }

    public String getDcre() {
        return dcre;
    }

    public void setDcre(String dcre) {
        this.dcre = dcre;
    }

    public Long getDele() {
        return dele;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }
    
    

    public InComptes(String cetab, String age, String com, String cle, String dev, String cli, String chap, String lib, String dou, String dfe, String cfer, String shi, String sde, String dodb, String ddm, String ddc, String ddd, String cumc, String cumd, String chl1, String chl2, String chl3, String chl4, String chl5, String chl6, String dcre, Long dele, Date dar) {
        this.cetab = cetab;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.lib = lib;
        this.dou = dou;
        this.dfe = dfe;
        this.cfer = cfer;
        this.shi = shi;
        this.sde = sde;
        this.dodb = dodb;
        this.ddm = ddm;
        this.ddc = ddc;
        this.ddd = ddd;
        this.cumc = cumc;
        this.cumd = cumd;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
        this.chl4 = chl4;
        this.chl5 = chl5;
        this.chl6 = chl6;
        this.dcre = dcre;
        this.dele = dele;
        this.dar = dar;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tebit roger
 */
@Entity(name = "invg")
@Table(name = "invg")
public class Invgenr {

    @Id
    @GeneratedValue
    private Long id;
    
    private Date dar;
    
    private String cinv;
    private String age;
    private String com;
    private String dev;
    private String cle;
    private String chap;
    private String cha1;
    private String cha2;
    private String nsou;
    private String ncp1;
    private String ncp2;
    private Double maut;
    private Date dou;
    private Date deb;
    private Date fin;
    private Double mond;
    private Double moncv;
    private String nat;
    private String sen;
    private Double tau;
    private Double mob;
    private Double mmob;
    private String issut;
    private Double mon1;
    private Double mon2;
    private Double mon3;
    private Double mon4;
    private Double mon5;
    private Double mon6;
    private String lib1;
    private String lib2;
    private String lib3;
    private String lib4;
    private String lib5;
    private String lib6;
    private String lib7;
    private String lib8;
    private String lib9;
    private String lib10;
    private String lib11;
    private String lib12;
    private String lib13;
    private String lib14;
    private String lib15;
    private String lib16;
    private String lib17;
    private String lib18;
    private String cetab;
    private String cli;
    private String eve;
    private Double tau1;
    private Double tau2;
    private Double tau3;
    private Double tau4;
    private Double tau5;
    private Double tau6;
    private Date dat1;
    private Date dat2;
    private Date dat3;
    private Date dat4;
    private Date dat5;
    private Date dat6;
    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public void setEve(String eve) {
        this.eve = eve;
    }

    public String getCli() {
        return cli;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getCetab() {
        return cetab;
    }

    public String getEve() {
        return eve;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public void setCinv(String cinv) {
        this.cinv = cinv;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public void setCha1(String cha1) {
        this.cha1 = cha1;
    }

    public void setCha2(String cha2) {
        this.cha2 = cha2;
    }

    public void setNsou(String nsou) {
        this.nsou = nsou;
    }

    public void setNcp1(String ncp1) {
        this.ncp1 = ncp1;
    }

    public void setNcp2(String ncp2) {
        this.ncp2 = ncp2;
    }

    public void setMaut(Double maut) {
        this.maut = maut;
    }

    public void setDou(Date dou) {
        this.dou = dou;
    }

    public void setDeb(Date deb) {
        this.deb = deb;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public void setMond(Double mond) {
        this.mond = mond;
    }

    public void setMoncv(Double moncv) {
        this.moncv = moncv;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }

    public void setTau(Double tau) {
        this.tau = tau;
    }

    public void setMob(Double mob) {
        this.mob = mob;
    }

    public void setMmob(Double mmob) {
        this.mmob = mmob;
    }

    public void setIssut(String issut) {
        this.issut = issut;
    }

    public void setMon1(Double mon1) {
        this.mon1 = mon1;
    }

    public void setMon2(Double mon2) {
        this.mon2 = mon2;
    }

    public void setMon3(Double mon3) {
        this.mon3 = mon3;
    }

    public String getLib7() {
        return lib7;
    }

    public String getLib8() {
        return lib8;
    }

    public String getLib9() {
        return lib9;
    }

    public String getLib10() {
        return lib10;
    }

    public String getLib11() {
        return lib11;
    }

    public String getLib12() {
        return lib12;
    }

    public String getLib13() {
        return lib13;
    }

    public String getLib14() {
        return lib14;
    }

    public String getLib15() {
        return lib15;
    }

    public String getLib16() {
        return lib16;
    }

    public String getLib17() {
        return lib17;
    }

    public String getLib18() {
        return lib18;
    }

    public void setLib7(String lib7) {
        this.lib7 = lib7;
    }

    public void setLib8(String lib8) {
        this.lib8 = lib8;
    }

    public void setLib9(String lib9) {
        this.lib9 = lib9;
    }

    public void setLib10(String lib10) {
        this.lib10 = lib10;
    }

    public void setLib11(String lib11) {
        this.lib11 = lib11;
    }

    public void setLib12(String lib12) {
        this.lib12 = lib12;
    }

    public void setLib13(String lib13) {
        this.lib13 = lib13;
    }

    public void setLib14(String lib14) {
        this.lib14 = lib14;
    }

    public void setLib15(String lib15) {
        this.lib15 = lib15;
    }

    public void setLib16(String lib16) {
        this.lib16 = lib16;
    }

    public void setLib17(String lib17) {
        this.lib17 = lib17;
    }

    public void setLib18(String lib18) {
        this.lib18 = lib18;
    }

    public void setMon4(Double mon4) {
        this.mon4 = mon4;
    }

    public void setMon5(Double mon5) {
        this.mon5 = mon5;
    }

    public void setMon6(Double mon6) {
        this.mon6 = mon6;
    }

    public void setLib1(String lib1) {
        this.lib1 = lib1;
    }

    public void setLib2(String lib2) {
        this.lib2 = lib2;
    }

    public void setLib3(String lib3) {
        this.lib3 = lib3;
    }

    public void setLib4(String lib4) {
        this.lib4 = lib4;
    }

    public void setLib5(String lib5) {
        this.lib5 = lib5;
    }

    public void setLib6(String lib6) {
        this.lib6 = lib6;
    }

    public void setTau1(Double tau1) {
        this.tau1 = tau1;
    }

    public void setTau2(Double tau2) {
        this.tau2 = tau2;
    }

    public void setTau3(Double tau3) {
        this.tau3 = tau3;
    }

    public void setTau4(Double tau4) {
        this.tau4 = tau4;
    }

    public void setTau5(Double tau5) {
        this.tau5 = tau5;
    }

    public void setTau6(Double tau6) {
        this.tau6 = tau6;
    }

    public void setDat1(Date dat1) {
        this.dat1 = dat1;
    }

    public void setDat2(Date dat2) {
        this.dat2 = dat2;
    }

    public void setDat3(Date dat3) {
        this.dat3 = dat3;
    }

    public void setDat4(Date dat4) {
        this.dat4 = dat4;
    }

    public void setDat5(Date dat5) {
        this.dat5 = dat5;
    }

    public void setDat6(Date dat6) {
        this.dat6 = dat6;
    }

    public void setDcre(Date dcre) {
        this.dcre = dcre;
    }

    public void setDmod(Date dmod) {
        this.dmod = dmod;
    }

    public void setUticre(String uticre) {
        this.uticre = uticre;
    }

    public void setUtimod(String utimod) {
        this.utimod = utimod;
    }

    public Long getId() {
        return id;
    }

    public Date getDar() {
        return dar;
    }

    public String getCinv() {
        return cinv;
    }

    public String getAge() {
        return age;
    }

    public String getCom() {
        return com;
    }

    public String getDev() {
        return dev;
    }

    public String getCle() {
        return cle;
    }

    public String getChap() {
        return chap;
    }

    public String getCha1() {
        return cha1;
    }

    public String getCha2() {
        return cha2;
    }

    public String getNsou() {
        return nsou;
    }

    public String getNcp1() {
        return ncp1;
    }

    public String getNcp2() {
        return ncp2;
    }

    public Double getMaut() {
        return maut;
    }

    public Date getDou() {
        return dou;
    }

    public Date getDeb() {
        return deb;
    }

    public Date getFin() {
        return fin;
    }

    public Double getMond() {
        return mond;
    }

    public Double getMoncv() {
        return moncv;
    }

    public String getNat() {
        return nat;
    }

    public String getSen() {
        return sen;
    }

    public Double getTau() {
        return tau;
    }

    public Double getMob() {
        return mob;
    }

    public Double getMmob() {
        return mmob;
    }

    public String getIssut() {
        return issut;
    }

    public Double getMon1() {
        return mon1;
    }

    public Double getMon2() {
        return mon2;
    }

    public Double getMon3() {
        return mon3;
    }

    public Double getMon4() {
        return mon4;
    }

    public Double getMon5() {
        return mon5;
    }

    public Double getMon6() {
        return mon6;
    }

    public String getLib1() {
        return lib1;
    }

    public String getLib2() {
        return lib2;
    }

    public String getLib3() {
        return lib3;
    }

    public String getLib4() {
        return lib4;
    }

    public String getLib5() {
        return lib5;
    }

    public String getLib6() {
        return lib6;
    }

    public Double getTau1() {
        return tau1;
    }

    public Double getTau2() {
        return tau2;
    }

    public Double getTau3() {
        return tau3;
    }

    public Double getTau4() {
        return tau4;
    }

    public Double getTau5() {
        return tau5;
    }

    public Double getTau6() {
        return tau6;
    }

    public Date getDat1() {
        return dat1;
    }

    public Date getDat2() {
        return dat2;
    }

    public Date getDat3() {
        return dat3;
    }

    public Date getDat4() {
        return dat4;
    }

    public Date getDat5() {
        return dat5;
    }

    public Date getDat6() {
        return dat6;
    }

    public Date getDcre() {
        return dcre;
    }

    public Date getDmod() {
        return dmod;
    }

    public String getUticre() {
        return uticre;
    }

    public String getUtimod() {
        return utimod;
    }

}

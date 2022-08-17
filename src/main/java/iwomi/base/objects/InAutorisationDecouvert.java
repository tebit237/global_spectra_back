/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author fabri
 */
@Entity(name = "inaut")
@Table(name = "inaut")
public class InAutorisationDecouvert {

    @Id
    @GeneratedValue
    private Long id;
    private String age; //agence
    private String cetab;
    private String chap;
    private String cle;
    private String cli;
    private String com;
    private String dcre;
    private Date ddeb;
    private Long dele;
    private String dev;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dfin;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dmep;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dmod;
    private String eta;
    private Double mont;
    private Double util;
    private String chadr;
    private String chacr;
    private String ref;
    private String typ;
    private String uticre;
    private String utimod;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar;

    public InAutorisationDecouvert() {

    }

    public Long getId() {
        return id;
    }

    public void setChadr(String chadr) {
        this.chadr = chadr;
    }

    public void setChacr(String chacr) {
        this.chacr = chacr;
    }

    public String getChadr() {
        return chadr;
    }

    public String getChacr() {
        return chacr;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getChap() {
        return chap;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getDcre() {
        return dcre;
    }

    public void setDcre(String dcre) {
        this.dcre = dcre;
    }

    public Date getDdeb() {
        return ddeb;
    }

    public void setDdeb(Date ddeb) {
        this.ddeb = ddeb;
    }

    public Long getDele() {
        return dele;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setDfin(Date dfin) {
        this.dfin = dfin;
    }

    public void setDmep(Date dmep) {
        this.dmep = dmep;
    }

    public void setDmod(Date dmod) {
        this.dmod = dmod;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public void setMont(Double mont) {
        this.mont = mont;
    }

    public void setUtil(Double util) {
        this.util = util;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public void setUticre(String uticre) {
        this.uticre = uticre;
    }

    public void setUtimod(String utimod) {
        this.utimod = utimod;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public Date getDfin() {
        return dfin;
    }

    public Date getDmep() {
        return dmep;
    }

    public Date getDmod() {
        return dmod;
    }

    public String getEta() {
        return eta;
    }

    public Double getMont() {
        return mont;
    }

    public Double getUtil() {
        return util;
    }

    public String getRef() {
        return ref;
    }

    public String getTyp() {
        return typ;
    }

    public String getUticre() {
        return uticre;
    }

    public String getUtimod() {
        return utimod;
    }

    public Date getDar() {
        return dar;
    }

    public InAutorisationDecouvert(String age, String cetab, String chap, String cle, String cli, String com, String dcre, Date ddep, Long dele, String dev, Date dfin, Date dmep, Date dmod, String eta, Double mont, String ref, String typ, String uticre, String utimod, Date dar) {

        this.age = age;
        this.cetab = cetab;
        this.chap = chap;
        this.cle = cle;
        this.cli = cli;
        this.com = com;
        this.dcre = dcre;
        this.ddeb = ddep;
        this.dele = dele;
        this.dev = dev;
        this.dfin = dfin;
        this.dmep = dmep;
        this.dmod = dmod;
        this.eta = eta;
        this.mont = mont;
        this.ref = ref;
        this.typ = typ;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dar = dar;

    }

}

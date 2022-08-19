package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INDAT")
@Entity(name = "INDAT")

public class Indat {

    @Id
    @GeneratedValue

    private Long id;
    private String age;
    private String com;
    private String cle;
    private String dev;
    private String chap;
    private String cli;
    private String numsou;
    private String ref;
    private String typ;
    private Date dmep;
    private Date ddeb;
    private Date dfin;
    private Double mont;
    private String nan;
    private String per;
    private Double tau;
    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;

    private String nom;
    private String cetab;
    private Long dele;
    private Date dar;

    public Indat() {

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCha(String cha) {
        this.chap = cha;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public void setNumsou(String numsou) {
        this.numsou = numsou;
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

    public void setMont(Double mont) {
        this.mont = mont;
    }

    public void setNan(String nan) {
        this.nan = nan;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public void setTau(Double tau) {
        this.tau = tau;
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

    public void setCetab(String cetab) {
        this.cetab = cetab;
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

    public String getCha() {
        return chap;
    }

    public String getCli() {
        return cli;
    }

    public String getNumsou() {
        return numsou;
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

    public Double getMont() {
        return mont;
    }

    public String getNan() {
        return nan;
    }

    public String getPer() {
        return per;
    }

    public Double getTau() {
        return tau;
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

    public String getCetab() {
        return cetab;
    }

    public Long getDele() {
        return dele;
    }

    public Date getDar() {
        return dar;
    }


}

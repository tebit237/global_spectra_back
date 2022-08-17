package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INCRE")
@Entity(name = "INCRE")

public class Incredit {

    @Id
    @GeneratedValue

    private Long id;
    private String age;
    private String come;
    private String comc;
    private String dev;
    private String chae;
    private String chac;
    private String cli;
    private String ref;
    private String typ;
    private String nat;
    private Date dmep;
    private Date deb;
    private Date Fin;
    private Double mont;
    private Double moncv;
    private Double tdev;
    private Double tau;
    private Double teg;
    private String ant;
    private String per;
    private Double nbe;
    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;

    private Long dele;
    private String cetab;
    private Date dar;

    public Incredit() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public void setComc(String comc) {
        this.comc = comc;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setChae(String chae) {
        this.chae = chae;
    }

    public void setChac(String chac) {
        this.chac = chac;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public void setDmep(Date dmep) {
        this.dmep = dmep;
    }

    public void setDeb(Date deb) {
        this.deb = deb;
    }

    public void setFin(Date Fin) {
        this.Fin = Fin;
    }

    public void setMont(Double mont) {
        this.mont = mont;
    }

    public void setMoncv(Double moncv) {
        this.moncv = moncv;
    }

    public void setTdev(Double tdev) {
        this.tdev = tdev;
    }

    public void setTau(Double tau) {
        this.tau = tau;
    }

    public void setTeg(Double teg) {
        this.teg = teg;
    }

    public void setAnt(String ant) {
        this.ant = ant;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public void setNbe(Double nbe) {
        this.nbe = nbe;
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

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
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

    public String getCome() {
        return come;
    }

    public String getComc() {
        return comc;
    }

    public String getDev() {
        return dev;
    }

    public String getChae() {
        return chae;
    }

    public String getChac() {
        return chac;
    }

    public String getCli() {
        return cli;
    }

    public String getRef() {
        return ref;
    }

    public String getTyp() {
        return typ;
    }

    public String getNat() {
        return nat;
    }

    public Date getDmep() {
        return dmep;
    }

    public Date getDeb() {
        return deb;
    }

    public Date getFin() {
        return Fin;
    }

    public Double getMont() {
        return mont;
    }

    public Double getMoncv() {
        return moncv;
    }

    public Double getTdev() {
        return tdev;
    }

    public Double getTau() {
        return tau;
    }

    public Double getTeg() {
        return teg;
    }

    public String getAnt() {
        return ant;
    }

    public String getPer() {
        return per;
    }

    public Double getNbe() {
        return nbe;
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

    public Long getDele() {
        return dele;
    }

    public String getCetab() {
        return cetab;
    }

    public Date getDar() {
        return dar;
    }

}

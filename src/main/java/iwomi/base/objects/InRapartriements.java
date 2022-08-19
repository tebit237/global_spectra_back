package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INRAPA")
@Entity(name = "INRAPA")

public class InRapartriements {

    @Id
    @GeneratedValue

    private Long id;
    private String age;
    private String com;
    private String cle;
    private String dev;
    private String cli;
    private String ref;
    private String typ;
    private Date dmep;
    private String pdes;
    private String mot;
    private Double mont;
    private Double moncv;
    private Double tau;
    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;

    private Long dele;
    private String cetab;
    private Date dar;

    public InRapartriements() {

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

    public void setCli(String cli) {
        this.cli = cli;
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

    public void setPdes(String pdes) {
        this.pdes = pdes;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public void setMont(Double mont) {
        this.mont = mont;
    }

    public void setMoncv(Double moncv) {
        this.moncv = moncv;
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

    public String getRef() {
        return ref;
    }

    public String getTyp() {
        return typ;
    }

    public Date getDmep() {
        return dmep;
    }

    public String getPdes() {
        return pdes;
    }

    public String getMot() {
        return mot;
    }

    public Double getMont() {
        return mont;
    }

    public Double getMoncv() {
        return moncv;
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

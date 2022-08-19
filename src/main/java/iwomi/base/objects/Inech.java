package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INECH")
@Entity(name = "INECH")

public class Inech {

    @Id
    @GeneratedValue

    private Long id;
    private String age;
    private String ref;
    private String ech;
    private String chap;
    private String typ;
    private Double num;
    private Date dech;
    private Double mon;
    private Double monp;
    private Double mimp;
    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;

    private Long dele;
    private String cetab;
    private Date dar;

    public Inech() {

    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public String getChap() {
        return chap;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setEch(String ech) {
        this.ech = ech;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public void setDech(Date dech) {
        this.dech = dech;
    }

    public void setMon(Double mon) {
        this.mon = mon;
    }

    public void setMonp(Double monp) {
        this.monp = monp;
    }

    public void setMimp(Double mimp) {
        this.mimp = mimp;
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

    public String getRef() {
        return ref;
    }

    public String getEch() {
        return ech;
    }

    public String getTyp() {
        return typ;
    }

    public Double getNum() {
        return num;
    }

    public Date getDech() {
        return dech;
    }

    public Double getMon() {
        return mon;
    }

    public Double getMonp() {
        return monp;
    }

    public Double getMimp() {
        return mimp;
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

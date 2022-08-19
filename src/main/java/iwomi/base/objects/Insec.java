package iwomi.base.objects;

import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INSEC")
@Entity(name = "INSEC")

public class Insec {

    @Id
    @GeneratedValue
    private Long id;
    private String cbr;
    private String br1;
    private String br2;
    private String br3;
    private String br4;
    private String br5;
    private String br6;
    private String br7;
    private String br8;
    private String br9;
    private String br10;
    private String nbr;
    private String tamt;

    private Date dcre;
    private Date dmod;
    private String uticre;
    private String utimod;

    private Long dele;
    private String cetab;
    private Date dar;

    public Long getDele() {
        return dele;
    }

    public String getCetab() {
        return cetab;
    }

    public Date getDar() {
        return dar;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCbr() {
        return cbr;
    }

    public String getBr1() {
        return br1;
    }

    public String getBr2() {
        return br2;
    }

    public String getBr3() {
        return br3;
    }

    public String getBr4() {
        return br4;
    }

    public String getBr5() {
        return br5;
    }

    public String getBr6() {
        return br6;
    }

    public String getBr7() {
        return br7;
    }

    public String getBr8() {
        return br8;
    }

    public String getBr9() {
        return br9;
    }

    public String getBr10() {
        return br10;
    }

    public String getNbr() {
        return nbr;
    }

    public String getTamt() {
        return tamt;
    }

    public void setCbr(String cbr) {
        this.cbr = cbr;
    }

    public void setBr1(String br1) {
        this.br1 = br1;
    }

    public void setBr2(String br2) {
        this.br2 = br2;
    }

    public void setBr3(String br3) {
        this.br3 = br3;
    }

    public void setBr4(String br4) {
        this.br4 = br4;
    }

    public void setBr5(String br5) {
        this.br5 = br5;
    }

    public void setBr6(String br6) {
        this.br6 = br6;
    }

    public void setBr7(String br7) {
        this.br7 = br7;
    }

    public void setBr8(String br8) {
        this.br8 = br8;
    }

    public void setBr9(String br9) {
        this.br9 = br9;
    }

    public void setBr10(String br10) {
        this.br10 = br10;
    }

    public void setNbr(String nbr) {
        this.nbr = nbr;
    }

    public void setTamt(String tamt) {
        this.tamt = tamt;
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

    public Insec() {
    }

}

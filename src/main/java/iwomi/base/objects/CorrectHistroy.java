package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "rpcohis")
@Entity(name = "rpcohis")

public class CorrectHistroy {

    @Id
    @GeneratedValue
    private Long id;
    private String codcorr;
    private String typecope;
    private Double init;
    private Double fin;
    private Date mdfi;

    public Long getId() {
        return id;
    }

    public CorrectHistroy() {
    }

    public String getCodcorr() {
        return codcorr;
    }

    public String getTypecope() {
        return typecope;
    }

    public Double getInit() {
        return init;
    }

    public Double getFin() {
        return fin;
    }

    public Date getMdfi() {
        return mdfi;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodcorr(String codcorr) {
        this.codcorr = codcorr;
    }

    public void setTypecope(String typecope) {
        this.typecope = typecope;
    }

    public void setInit(Double init) {
        this.init = init;
    }

    public void setFin(Double fin) {
        this.fin = fin;
    }

    public void setMdfi(Date mdfi) {
        this.mdfi = mdfi;
    }
    
}

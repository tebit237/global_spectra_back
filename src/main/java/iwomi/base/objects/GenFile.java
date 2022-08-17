package iwomi.base.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "genfile")
@Entity(name = "genfile")
@IdClass(GenFileId.class)
public class GenFile {

    @Id
    private String fich;
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date datemodif;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date datecreat;
    @Id
    private String cetab;
    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar;

    public GenFile() {

    }

    public GenFile(String fich, Date datemodif, Date datecreat, String cetab, Date dar) {
        this.fich = fich;
        this.datemodif = datemodif;
        this.datecreat = datecreat;
        this.cetab = cetab;
        this.dar = dar;
    }

    public GenFile(String fich, String code, Date datemodif, Date datecreat, String cetab, Date dar) {
        this.fich = fich;
        this.code = code;
        this.datemodif = datemodif;
        this.datecreat = datecreat;
        this.cetab = cetab;
        this.dar = dar;
    }

    public void setFich(String fich) {
        this.fich = fich;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDatemodif(Date datemodif) {
        this.datemodif = datemodif;
    }

    public void setDatecreat(Date datecreat) {
        this.datecreat = datecreat;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public String getFich() {
        return fich;
    }

    public Date getDatemodif() {
        return datemodif;
    }

    public Date getDatecreat() {
        return datecreat;
    }

    public String getCetab() {
        return cetab;
    }

    public Date getDar() {
        return dar;
    }

}

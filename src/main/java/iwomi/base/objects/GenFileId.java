package iwomi.base.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class GenFileId implements Serializable {

    private String fich;
    private String cetab;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar;

    public GenFileId() {

    }

    public void setFich(String fich) {
        this.fich = fich;
    }

    public GenFileId(String fich, String cetab, Date dar) {
        this.fich = fich;
        this.cetab = cetab;
        this.dar = dar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenFileId genFileId = (GenFileId) o;
        return fich.equals(genFileId.fich) && cetab.equals(genFileId.cetab) && dar.equals(genFileId.dar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fich, cetab, dar);
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

    public String getCetab() {
        return cetab;
    }

    public Date getDar() {
        return dar;
    }

}

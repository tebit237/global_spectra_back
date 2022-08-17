package iwomi.base.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportRepIdS implements Serializable {

    private Long rang;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar;
    private String col;
    private String fichier;

    public ReportRepIdS() {

    }

    public void setRang(Long rang) {
        this.rang = rang;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Long getRang() {
        return rang;
    }

    public Date getDar() {
        return dar;
    }

    public String getCol() {
        return col;
    }

    public String getFichier() {
        return fichier;
    }

    public ReportRepIdS(Long rang, Date dar, String col, String fichier) {
        this.rang = rang;
        this.dar = dar;
        this.col = col;
        this.fichier = fichier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportRepIdS reportRepId = (ReportRepIdS) o;
        return dar.equals(reportRepId.dar) && col.equals(reportRepId.col) && fichier.equals(reportRepId.fichier)
                && rang.equals(reportRepId.rang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dar, col, fichier, rang);
    }
}

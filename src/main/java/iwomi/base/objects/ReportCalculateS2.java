package iwomi.base.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rpcalc")
@IdClass(ReportCalculeIdS.class)
public class ReportCalculateS2 implements Serializable {

    private Long id;
    private String etab;
    @Id
    private String fichi;
    @Id
    private String field;
    private String lib;
    @Id
    @Column(nullable = false)
    private String post;
    @Id
    @Column(nullable = true)
    private String col;
    private String calc;
    private String source;
    private String typeval;
    private String divd;
    private String cuser;
    private String descc;
    @Id
    private String tysorce;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date crdt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date mdfi;
    private long dele;
    private String muser;

    public void takefield(ReportCalculateS2 obj) {
        if (obj.getCalc() != null) {
            this.calc = obj.getCalc();
        }
        if (obj.getDivd() != null) {
            this.divd = obj.getDivd();
        }
        if (obj.getSource() != null) {
            this.source = obj.getSource();
        }
        if (obj.getTypeval() != null) {
            this.typeval = obj.getTypeval();
        }
        this.mdfi = new Date();
    }

    public ReportCalculeIdS getIds() {
        ReportCalculeIdS r = new ReportCalculeIdS();
        r.setFichi(this.fichi);
        r.setField(this.field);
        r.setPost(this.post);
        r.setCol(this.col);
        r.setTysorce(this.tysorce);
        return r;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescc() {
        return descc;
    }

    public void setDescc(String descc) {
        this.descc = descc;
    }

    public String getTysorce() {
        return tysorce;
    }

    public void setTysorce(String tysorce) {
        this.tysorce = tysorce;
    }

    public long getDele() {
        return dele;
    }

    public void setDele(long dele) {
        this.dele = dele;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public void setFichi(String fichi) {
        this.fichi = fichi;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTypeval(String typeval) {
        this.typeval = typeval;
    }

    public void setDivd(String divd) {
        this.divd = divd;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public void setCrdt(Date crdt) {
        this.crdt = crdt;
    }

    public void setMdfi(Date mdfi) {
        this.mdfi = mdfi;
    }

    public ReportCalculateS2() {

    }

    public String getEtab() {
        return etab;
    }

    public String getFichi() {
        return fichi;
    }

    public String getField() {
        return field;
    }

    public String getSource() {
        return source;
    }

    public String getTypeval() {
        return typeval;
    }

    public String getLib() {
        return lib;
    }

    public String getPost() {
        return post;
    }

    public String getCol() {
        return col;
    }

    public String getCalc() {
        return calc;
    }

    public String getDivd() {
        return divd;
    }

    public String getCuser() {
        return cuser;
    }

    public String getMuser() {
        return muser;
    }

    public Date getCrdt() {
        return crdt;
    }

    public Date getMdfi() {
        return mdfi;
    }

    public ReportCalculateS2(String etab, String fichi, String field, String lib, String post, String col,
            String calc, String source, String typeval, String divd, String cuser, String descc, String tysorce,
            Date crdt, Date mdfi, long dele, String muser) {
        super();
        this.etab = etab;
        this.fichi = fichi;
        this.field = field;
        this.lib = lib;
        this.post = post;
        this.col = col;
        this.calc = calc;
        this.source = source;
        this.typeval = typeval;
        this.divd = divd;
        this.cuser = cuser;
        this.descc = descc;
        this.tysorce = tysorce;
        this.crdt = crdt;
        this.mdfi = mdfi;
        this.dele = dele;
        this.muser = muser;
    }

}

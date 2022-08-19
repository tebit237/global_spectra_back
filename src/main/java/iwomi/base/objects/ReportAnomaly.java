/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author User
 */
@Entity
@Table(name = "rpanom")
public class ReportAnomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String etab;

    public void setDESCRP(String DESCRP) {
        this.DESCRP = DESCRP;
    }
    private String DESCRP;

    public String getDESCRP() {
        return DESCRP;
    }

    private Double mntg;
    private Double mntd;
    private String cg;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar;

    private String type;

    private String cd;

    private String cuser;

    private String muser;

    private Long ctrid;
    private Date crdt;

    private Date mdfi;

    private String fichier;

    private String sign;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getCtrid() {
        return ctrid;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public void setMntg(Double mntg) {
        this.mntg = mntg;
    }

    public void setMntd(Double mntd) {
        this.mntd = mntd;
    }

    public void setCg(String cg) {
        this.cg = cg;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCd(String cd) {
        this.cd = cd;
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

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public ReportAnomaly() {

    }

    public void setCtrid(Long ctrid) {
        this.ctrid = ctrid;
    }

    public String getEtab() {
        return etab;
    }

    public String getFichier() {
        return fichier;
    }

    public Double getMntg() {
        return mntg;
    }

    public Double getMntd() {
        return mntd;
    }

    public Date getDar() {
        return dar;
    }

    public void setDar() {
        this.dar = dar;
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

    public String getCg() {
        return cg;
    }

    public String getType() {
        return type;
    }

    public String getCd() {
        return cd;
    }

    public String getKey(String e) {
        if (e.equalsIgnoreCase("mntd")) {
            return cd;
        } else if (e.equalsIgnoreCase("mntg")) {
            return cg;
        } else {
            return "";
        }

    }

    public String getSign() {
        return sign;
    }

    public ReportAnomaly(String etab, Date dar, Double mntg, Double mntd,
            String cuser, String muser, String cg, String cd, String type, String fichier, String sign) {
        super();
        this.etab = etab;
        this.dar = dar;
        this.mntg = mntg;
        this.mntd = mntd;
        this.cuser = cuser;
        this.muser = cuser;
        this.cg = cg;
        this.type = type;
        this.cd = cd;
        this.fichier = fichier;
        this.sign = sign;

    }
}

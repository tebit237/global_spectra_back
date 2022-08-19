/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name = "rpctrq")
public class ReportControleQuality {

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String etab;
    private String descp;
    private String qrycot;
    private String rsult;
    private String dele;
    private String lebelle;

    public void setLebelle(String lebelle) {
        this.lebelle = lebelle;
    }

    public String getLebelle() {
        return lebelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtab() {
        return etab;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public String getQrycot() {
        return qrycot;
    }

    public void setQrycot(String qrycot) {
        this.qrycot = qrycot;
    }

    public String getRsult() {
        return rsult;
    }

    public void setRsult(String rsult) {
        this.rsult = rsult;
    }

    public String getDele() {
        return dele;
    }

    public void setDele(String dele) {
        this.dele = dele;
    }

    public Boolean setByColumn1(ReportControleQuality d) {
        if (d.qrycot != null) {
            this.qrycot = d.qrycot;
        }
        if (d.descp != null) {
            this.descp = d.descp;
        }
        if (d.rsult != null) {
            this.rsult = d.rsult;
        }
        if (d.lebelle != null) {
            this.lebelle = d.lebelle;
        }
        return true;
    }

    public Boolean setByColumn(String column, String value) {
        switch (column) {
            case "rsult":
                this.rsult = value;
                break;
            case "qrycot":
                this.qrycot = value;
                break;
            case "descp":
                this.descp = value;
                break;
            default:
                return false;
        }
        return true;

    }
}

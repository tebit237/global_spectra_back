/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author fabri
 */
    @Entity(name = "liveOp")
@Table(name = "liveOp")
public class LiveOperation {
   @Id
   @GeneratedValue
   private Long id;
   String cetab;
   String codeUnique;
   String operations;
   String usid;
   Long statut;     //  statut 1 succes  stauut 2 failed 3 pending  statut 4 succes mais il ya des erreur sur les elements traité
  // Double pcentage;
   Long nbtotal;
   Long nbtraite;
   String details;
   @Temporal(javax.persistence.TemporalType.DATE)
   Date dateDebut;
   @Temporal(javax.persistence.TemporalType.DATE)
   Date dateEnd;

    public LiveOperation() {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public Long getStatut() {
        return statut;
    }

    public void setStatut(Long statut) {
        this.statut = statut;
    }

//    public Double getPcentage() {
//        return pcentage;
//    }
//
//    public void setPcentage(Double pcentage) {
//        this.pcentage = pcentage;
//    }

    public Long getNbtotal() {
        return nbtotal;
    }

    public void setNbtotal(Long nbtotal) {
        this.nbtotal = nbtotal;
    }

    public Long getNbtraite() {
        return nbtraite;
    }

    public void setNbtraite(Long nbtraite) {
        this.nbtraite = nbtraite;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LiveOperation(String cetab, String codeUnique, String operations, String usid, Long statut, Long nbtotal, Long nbtraite, String details, Date dateDebut, Date dateEnd) {
        this.cetab = cetab;
        this.codeUnique = codeUnique;
        this.operations = operations;
        this.usid = usid;
        this.statut = statut;
        this.nbtotal = nbtotal;
        this.nbtraite = nbtraite;
        this.details = details;
        this.dateDebut = dateDebut;
        this.dateEnd = dateEnd;
    }

    
    
    
}

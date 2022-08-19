/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.io.Serializable;
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
@Entity(name = "sliveTrait")
@Table(name = "sliveTrait")
public class LiveTraitementS{
   // @Id
   // @GeneratedValue    
   // private Long id;
    @Id
    String codeLigne;
  //  Long   idope;
    String codeUnique;
    String codefichier;
    String nbtotal;
    String nbtraite;
    String  statut;//3 pending
    String details;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateDebut;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateFin;

    public LiveTraitementS() {
    }

    public String getCodeLigne() {
        return codeLigne;
    }

    public void setCodeLigne(String codeLigne) {
        this.codeLigne = codeLigne;
    }

    public String getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getCodefichier() {
        return codefichier;
    }

    public void setCodefichier(String codefichier) {
        this.codefichier = codefichier;
    }

    public String getNbtotal() {
        return nbtotal;
    }

    public void setNbtotal(String nbtotal) {
        this.nbtotal = nbtotal;
    }

    public String getNbtraite() {
        return nbtraite;
    }

    public void setNbtraite(String nbtraite) {
        this.nbtraite = nbtraite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public LiveTraitementS(String codeLigne, String codeUnique, String codefichier, String nbtotal, String nbtraite, String statut, String details, Date dateDebut, Date dateFin) {
        
        this.codeLigne = codeLigne;
        this.codeUnique = codeUnique;
        this.codefichier = codefichier;
        this.nbtotal = nbtotal;
        this.nbtraite = nbtraite;
        this.statut = statut;
        this.details = details;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    

}

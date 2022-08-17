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
@Entity(name = "sliveTrait1")
@Table(name = "sliveTrait1")
public class LiveTraitementv1S{
   // @Id
    @Id
    String codeLigne;
    String codeUnique;
    String codefichier;
    long nbtotal;
    long nbtraite;
    String  statut;//3 pending
    String details;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateDebut; 
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateFin;

    public LiveTraitementv1S() {
    }

    public String getCodeLigne() {
        return codeLigne;
    }

    public void setNbtotal(long nbtotal) {
        this.nbtotal = nbtotal;
    }

    public void setNbtraite(long nbtraite) {
        this.nbtraite = nbtraite;
    }

    public long getNbtotal() {
        return nbtotal;
    }

    public long getNbtraite() {
        return nbtraite;
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

    public LiveTraitementv1S(String codeLigne, String codeUnique, String codefichier, long nbtotal, long nbtraite, String statut, String details, Date dateDebut) {
        this.codeLigne = codeLigne;
        this.codeUnique = codeUnique;
        this.codefichier = codefichier;
        this.nbtotal = nbtotal;
        this.nbtraite = nbtraite;
        this.statut = statut;
        this.details = details;
        this.dateDebut = dateDebut;
    }

  

    

}

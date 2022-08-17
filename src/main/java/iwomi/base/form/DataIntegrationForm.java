/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.form;

/**
 *
 * @author fabri
 */ 
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fabri
 */
public class DataIntegrationForm {
    // les données json pour la generation des fichiers inventaires contenant les données des tables venant du systeme du client.
   // @JsonFormat(pattern="yyyy-MM-dd")
    private String  date;
    private String periodicity;
    private String type;
    private String cetab;
    private String usid;
    private String codeUnique;
    private String operation;
    
    private List<CodeForm> codeInventaires;
    
    
    public DataIntegrationForm() {
    
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<CodeForm> getCodeInventaires() {
        return codeInventaires;
    }

    public void setCodeInventaires(List<CodeForm> codeInventaires) {
        this.codeInventaires = codeInventaires;
    }

    public DataIntegrationForm(String  date, String periodicity, String type, String cetab, String usid, String codeUnique, String operation, List<CodeForm> codeInventaires) {
        this.date = date;
        this.periodicity = periodicity;
        this.type = type;
        this.cetab = cetab;
        this.usid = usid;
        this.codeUnique = codeUnique;
        this.operation = operation;
        this.codeInventaires = codeInventaires;
    }
    
    
    
    
}

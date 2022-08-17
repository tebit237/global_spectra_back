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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fabri
 */
public class ClientToSystemFormS {
    // les données json pour la generation des fichiers inventaires contenant les données des tables venant du systeme du client.
    private String  date;
    private String periodicity;
    private String cetab;
    private String usid;
    private String codeUnique;
    private String operation;
    private String type;
    private List<CodeForm> codeInventaires;
    
    public ClientToSystemFormS() {

    }

    public String  getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CodeForm> getCodeInventaires() {
        return codeInventaires;
    }

    public void setCodeInventaires(List<CodeForm> codeInventaires) {
        this.codeInventaires = codeInventaires;
    }

    public ClientToSystemFormS(String  date, String periodicity, String cetab, String usid, String codeUnique, String operation, String type, List<CodeForm> codeInventaires) {
        this.date = date;
        this.periodicity = periodicity;
        this.cetab = cetab;
        this.usid = usid;
        this.codeUnique = codeUnique;
        this.operation = operation;
        this.type = type;
        this.codeInventaires = codeInventaires;
    }
    
    
    
    
}

    

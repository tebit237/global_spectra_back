/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fabri
 */
public class GenererFichierFormS {
    
 //   @JsonFormat(pattern="yyyy-MM-dd")
    private String date;
    private String periodicity;
    private String type;
    private List<CodeForm> codeFichier;
    private String usid;
    private String codeUnique;
    private String cetab;
    private String operation;

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

    public List<CodeForm> getCodeFichier() {
        return codeFichier;
    }

    public void setCodeFichier(List<CodeForm> codeFichier) {
        this.codeFichier = codeFichier;
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

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public GenererFichierFormS(String date, String periodicity, String type, List<CodeForm> codeFichier, String usid, String codeUnique, String cetab, String operation) {
        this.date = date;
        this.periodicity = periodicity;
        this.type = type;
        this.codeFichier = codeFichier;
        this.usid = usid;
        this.codeUnique = codeUnique;
        this.cetab = cetab;
        this.operation = operation;
    }
    
   
        
    public GenererFichierFormS() {
        
    }
}

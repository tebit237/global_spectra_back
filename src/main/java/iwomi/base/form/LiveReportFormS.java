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
public class LiveReportFormS {
    
    String codeUnique;
    String codeFichier;
    String operation;
    String cetab;
    String usid;

    public String getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getCodeFichier() {
        return codeFichier;
    }

    public void setCodeFichier(String codeFichier) {
        this.codeFichier = codeFichier;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public LiveReportFormS(String codeUnique, String codeFichier, String operation, String cetab, String usid) {
        
        this.codeUnique = codeUnique;
        this.codeFichier = codeFichier;
        this.operation = operation;
        this.cetab = cetab;
        this.usid = usid;
    
    }
 
     public LiveReportFormS(){
     
     }
     
    
    
       
}

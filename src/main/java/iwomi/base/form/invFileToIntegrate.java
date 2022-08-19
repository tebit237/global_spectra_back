/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.form;

import java.util.List;

/**
 *
 * @author fabri
 */
public class invFileToIntegrate {
    
    String filename;
    String filecode;
    String ispresent;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
     public String getFilecode() {
        return filecode;
    }

    public void setIspresent(String ispresent) {
        this.ispresent = ispresent;
    }
    
    public String getIspresent() {
        return ispresent;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }
    public invFileToIntegrate(String filename,String filecode,String ispresent) {
        this.filename = filename;
        this.filecode = filecode;
        this.ispresent = ispresent;
    }
    
}

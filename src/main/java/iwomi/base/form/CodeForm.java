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
public class CodeForm {
    
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CodeForm(String code) {
        this.code = code;
    }
    
     public CodeForm() {
        
    }
     
    @Override
    public String toString() {
        return "Code [code=" +code +"]";
    }
    
    
}

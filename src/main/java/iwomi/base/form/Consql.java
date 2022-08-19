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
public class Consql {
    
    private String IP;
    private String USER;
    private String PASS;
    private String PORT;
    
    public String getIP() {
        return IP;
    }

    /**
     * @param IP the AGE to set
     */
    public void setIP(String IP) {
        this.IP = IP;
    }
    
    public String getUSER() {
        return USER;
    }

    /**
     * @param USER the AGE to set
     */
    public void setUSER(String USER) {
        this.USER = USER;
    }
    
     public String getPASS() {
        return PASS;
    }

    /**
     * @param PASS the AGE to set
     */
    public void setPASS(String PASS) {
        this.PASS = PASS;
    }
    
    public String getPORT() {
        return PORT;
    }

    /**
     * @param PORT the AGE to set
     */
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }
    
    public Consql(String IP, String USER,String PASS,String PORT){
        
        this.IP = IP;
        this.USER = USER;
        this.PASS = PASS;
        this.PORT = PORT;

    }
    
    
     public Consql(){
 
    }
    
}
